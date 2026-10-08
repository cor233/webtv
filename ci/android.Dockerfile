# CNB 侧编译门禁的构建环境。
#
# 上游 GitHub Actions 用 android-actions/setup-android@v3 现装 SDK；CNB 上没有对应的
# 官方 Android 镜像（cnbcool 命名空间下不存在 android 镜像），因此这里自建一个：
# 装 JDK 21 + Chaquopy 需要的宿主 Python 3.10 + 项目要求的 SDK 平台与 Build Tools。
# 显式指定 jammy：Ubuntu 22.04 自带 python3.10，满足 Chaquopy v17 的 buildPython。
FROM eclipse-temurin:21-jdk-jammy

ARG ANDROID_COMPILE_SDK=37
ARG ANDROID_BUILD_TOOLS=37.0.0
ARG ANDROID_CMDLINE_TOOLS=13114758
ENV ANDROID_HOME=/opt/android-sdk
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH="${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools:${PATH}"

# Python 3.10 是 Chaquopy v17 的 buildPython：GitHub 侧曾用 deadsnakes PPA，其签名密钥
# 会挂几小时；这里改用发行版自带的 python3.10 并软链出 python3.10 命令。
RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        ca-certificates curl unzip git python3.10 python3.10-venv \
    && rm -rf /var/lib/apt/lists/* \
    && ln -sf "$(command -v python3.10)" /usr/local/bin/python3.10

# serverless/webtv-remote-go 的 go test 也在同一条门禁里跑，镜像需自带 Go。
COPY --from=golang:1.22-bookworm /usr/local/go /usr/local/go
ENV PATH="/usr/local/go/bin:${PATH}" GOFLAGS=-mod=mod

RUN mkdir -p "${ANDROID_HOME}/cmdline-tools" \
    && curl -fsSL -o /tmp/cmdline-tools.zip \
        "https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_CMDLINE_TOOLS}_latest.zip" \
    && unzip -q /tmp/cmdline-tools.zip -d "${ANDROID_HOME}/cmdline-tools" \
    && mv "${ANDROID_HOME}/cmdline-tools/cmdline-tools" "${ANDROID_HOME}/cmdline-tools/latest" \
    && rm /tmp/cmdline-tools.zip

RUN yes | sdkmanager --licenses > /dev/null \
    && sdkmanager --install \
        "platform-tools" \
        "platforms;android-${ANDROID_COMPILE_SDK}" \
        "build-tools;${ANDROID_BUILD_TOOLS}" > /dev/null \
    && sdkmanager --list_installed
