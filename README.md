<div align="center">

# WebHomeTV

**面向 Android TV / 手机的影视播放器壳子**

自带你的配置源，App 负责导入、管理、同步与播放。

[![Release](https://img.shields.io/github/v/release/motao123/webtv?label=release)](https://github.com/motao123/webtv/releases)
[![Android](https://img.shields.io/badge/Android-7.0%2B-3DDC84)](#下载安装)
[![Android TV](https://img.shields.io/badge/Android%20TV-Leanback-4285F4)](#下载安装)
[![Build](https://img.shields.io/badge/JDK-21-orange)](.github/workflows/ci.yml)

</div>

---

## 目录

- [项目简介](#项目简介)
- [主要特性](#主要特性)
- [下载安装](#下载安装)
- [快速上手](#快速上手)
- [使用示例](#使用示例)
- [技术栈](#技术栈)
- [目录结构](#目录结构)
- [构建与开发](#构建与开发)
- [常见问题](#常见问题)
- [文档](#文档)
- [开源说明与合规](#开源说明与合规)

---

## 项目简介

WebHomeTV 是基于 **FongMi / CatVod** 生态增强维护的 Android 影音播放器壳子，包名 `com.fongmi.android.tv`，同一套代码同时产出**手机端**与**电视端**。

它**不内置内容、不分发 JSON、不维护站源**，只做一件事：让「自带配置」这件事变得更容易——导入更稳、管理更清楚、多设备之间能迁移。

> 一句话：这是一个"让用户自带配置更容易导入、管理和迁移"的播放器壳子。

**它做什么**

- 导入你自己的点播 / 直播 / 壁纸配置（URL、文件、assets、局域网同步）
- 用 WebHome 给每个站点配置自定义网页首页，并开放原生能力给网页调用
- 把配置、历史、收藏、登录态、设置等壳子状态在手机与电视之间一键同步
- 提供局域网管理页与可自托管的公网遥控

**它不做什么**

- 不内置任何影视内容
- 不分发、不托管、不推荐任何 JSON / 站源
- 不提供公共遥控服务器（服务端程序随源码提供，需自行部署）

---

## 主要特性

### 播放与内核

| 能力 | 说明 |
| --- | --- |
| 多内核播放 | EXO / IJK / MPV 三选一，默认 EXO；精简版仅 EXO + IJK |
| 播放控制增强 | 播放中重播、内核热切换、0.1x–5x 倍速微调、章节选择、播放 OSD 与错误阶段提示 |
| 字幕与解码 | 字幕样式高级设置、双字幕、音视频解码偏好、音频直通与 Dolby Vision 输出策略 |
| 播放连续性 | 换源、换集、解析播放后保持进度与倍速等播放状态一致 |
| 预加载 | 按内核配置预加载开关、线程、缓存大小与预加载时长，自动预加载下一集 |
| 弹幕 | 总开关与播放页显示状态一致；关闭后换集、切内核、云搜或外部注入都不会隐式开启；支持密度/样式增强 |
| 直播增强 | 失败自动换源、自定义 EPG 源与频道信息、时移、分组 |
| 协议兼容 | 已接入 Force、JianPian、Thunder、TVBus 等 FongMi 协议扩展 |
| 投屏 | 手机端可投屏；电视端可作 DLNA Renderer 接收投屏 |

### 配置与同步

| 能力 | 说明 |
| --- | --- |
| 配置导入增强 | 导入前预检，失败不覆盖当前配置，支持 URL / 文件 / assets |
| 配置管理 | 查看当前配置、历史配置、来源类型与最后使用时间；删除前提示关联影响 |
| 一键同步 | 局域网同步配置与站源、脚本 / Jar 数据、WebHome、搜索记录、继续观看、收藏、应用设置、登录态 |
| 登录态学习 | 学习 Cookie / 登录态文件路径，供一键同步跨设备迁移网盘登录态；可管理已选路径与候选并预览内容 |
| 最近设备记忆 | 一键同步记住上次设备，下次优先选中 |
| 继续观看 / 收藏 | 强化恢复路径，配置缺失时提供回退 |
| 搜索相关性 | 结果按关键词匹配度过滤与排序 |
| 播放记录同步 | 管理本机写入、远端同步与 Webhook；备份中自动脱敏并禁用上报凭据 |

### WebHome 与扩展

| 能力 | 说明 |
| --- | --- |
| WebHome 首页 | 每个 CSP 站点可配置独立网页首页，支持透明背景 |
| Native SDK | 网页通过 `window.fm` 调用受信任范围内的原生播放、搜索、请求、缓存等能力 |
| 内嵌 VOD 播放 | 网页可把剧集数据直接交给播放器（`player.playVodInline`），无需站点接口即可开播 |
| CSP 预热与兼容 | 配置加载后预初始化 Jar CSP，并隔离插件自带 protobuf，减少首次打开与版本冲突 |
| 扩展开发套件 | `webhome-devkit/` 提供首页 / 扩展示例、起步模板与 AI skills |

### 家庭、安全与管理

| 能力 | 说明 |
| --- | --- |
| 家庭过滤 | 按标签 / 关键词屏蔽不适合客厅展示的内容，并隐藏配置中心里的指定来源项 |
| 网盘检测 | 检测网盘分享链接有效性，WebHome 与本地 HTTP 均可调用（可关闭） |
| 局域网管理与遥控 | 管理页查看播放状态与进度，配对后控制本机或远端设备 |
| 公网遥控（自托管） | HTTPS 中转、限时配对、设备撤销，WebSocket / HTTP 轮询；默认关闭，仅开放搜索、推送与播放控制 |
| 安全加固 | 本地 HTTP 写操作强制 token、DNS rebinding / SSRF / 路径遍历 / XXE / DoS 防护、CORS 白名单 |
| MPV 配置管理 | 管理 mpv.conf、input.conf 与脚本 profile；安全导入、历史回滚，脚本默认禁用 |
| 多语言与界面 | 英文 / 简体中文 / 繁体中文切换，手机端可调界面缩放；内置 27 款设计壁纸 |

### 发布与更新

| 能力 | 说明 |
| --- | --- |
| 自动更新 | 多源更新（GitHub Release / 自建镜像 / CNB），适配不同网络环境 |
| 断点续传 | 下载中断保留进度，同源最多重试 3 次，耗尽后换源并按 HTTP Range 续传 |
| 多重校验 | 下载后依次校验体积 → SHA-256 → 包名 → versionCode/versionName → 签名指纹 |
| 自动安装兜底 | 自动安装失败时导出 APK 到 Downloads，用户手动安装 |
| 崩溃可定位 | 发布构建上传 R8 mapping，线上堆栈可精确反混淆 |

---

## 下载安装

最新版本：**v5.15.0**

- 项目主页（GitHub Pages）：https://motao123.github.io/webtv/
- 下载地址：[GitHub Releases](https://github.com/motao123/webtv/releases)

一次发版共 **10 个安装包**：手机 / 电视 × 完整版 / 精简版 × 三种 CPU 架构。

| 设备 | 完整版 | 精简版 |
| --- | --- | --- |
| Android TV · 不确定架构 | `leanback-universal.apk` | `leanback-lite-universal.apk` |
| Android TV · 新电视盒子 | `leanback-arm64_v8a.apk` | `leanback-lite-arm64_v8a.apk` |
| Android TV · 老盒子 | `leanback-armeabi_v7a.apk` | — |
| Android 手机 · 不确定架构 | `mobile-universal.apk` | `mobile-lite-universal.apk` |
| Android 手机 · 新设备 | `mobile-arm64_v8a.apk` | `mobile-lite-arm64_v8a.apk` |
| Android 手机 · 老设备 | `mobile-armeabi_v7a.apk` | — |

**怎么选**

- **不确定 CPU 架构** → 选对应设备的 `universal`（通用包，体积最大但一定能跑）。
- **存储紧张 / 只要基础播放** → 选 `lite`：去掉 MPV 内核（约 37MB）与 Python 运行时（约 21MB），体积约为完整版一半，保留 EXO/IJK 双内核、QuickJS/JS 源与全部协议能力。
- **需要 Python 类型直播源**（如央视频、央视官网、广东广电官方解析源）→ **必须用完整版**，精简版不含 Python 运行时。

> 精简版与完整版的包名相同，可互相覆盖安装；应用内更新按 `*-lite-*.json` 独立清单分发，不会跨形态"升级"。

---

## 快速上手

### 1. 安装

下载对应 APK 安装即可。Android 7.0（API 24）及以上支持。

### 2. 导入配置

安装后**先导入你自己的配置源**（App 不自带内容）。在设置中填写配置 URL 或选择本地配置文件。

可导入的配置类型：**点播配置**、**直播配置**、**壁纸配置**。

### 3. 开始使用

配置加载完成后即可浏览分类、搜索、收藏与播放。后续常用入口：

```text
设置 → 增强功能      网盘检测、管理页面、WebHome 扩展、登录态学习、一键同步、调试日志
设置 → 版本检查      应用内更新
```

> 首次启动若出现"远程依赖确认"弹窗，是 App 在请求你确认加载配置中的远程 jar——这是安全机制，确认后才会加载站点。请只信任你自己配置来源的内容。

---

## 使用示例

### 例 1：在配置里为站点指定 WebHome 首页

在点播 JSON 的站点条目里加上 `homePage` 字段（别名 `home_page` / `webHome` / `web_home`），该站点就会使用这个网页作为首页。注意它**不是** `ext`——`ext` 是爬虫的扩展参数。

```json
{
  "sites": [
    {
      "key": "mysite",
      "name": "我的站点",
      "type": 3,
      "api": "csp_MySpider",
      "homePage": "https://example.com/home/index.html"
    }
  ]
}
```

其中 `type: 3` 表示该站点是 Spider 类型（`csp_` 开头的 `api`）。

### 例 2：WebHome 网页调用原生能力

网页中通过 `window.fm` 调用 App 原生能力。常用方法：

| 方法 | 说明 |
| --- | --- |
| `fm.req(url, options)` | 原生请求，绕过普通浏览器 CORS 限制 |
| `fm.res(url, options)` | 生成本地资源网关地址 |
| `fm.play(url, title, options)` | 播放直链或 `push://` 地址 |
| `fm.vod(siteKey, vodId, title, pic)` | 打开原生详情 / 播放链路 |
| `fm.search(keyword, { direct })` | 调用原生搜索 |
| `fm.openLive()` / `fm.openKeep()` / `fm.openSetting()` | 打开原生页面 |
| `fm.history()` | 读取最近观看记录 |
| `fm.config()` | 获取当前配置与家庭过滤状态 |
| `fm.site()` | 获取当前站点信息 |
| `fm.cache` | 本地缓存能力 |
| `fm.back()` / `fm.reload()` | 处理返回与刷新 |

示例：把网页上的一集交给 App 播放。

```js
async function playEpisode(vodId, title, pic, episodeUrl) {
  // 方案一：走原生详情 / 播放链路（推荐，能享受换源、历史、弹幕等能力）
  await window.fm.vod('mysite', vodId, title, pic);

  // 方案二：直接播放一个直链
  // await window.fm.play(episodeUrl, title);
}
```

更完整的 API、信任边界与扩展示例见 [`docs/webhome-extension/README.md`](docs/webhome-extension/README.md)。

### 例 3：局域网管理页

从 App 的「**设置 → 增强功能 → 管理页面**」取得访问地址，在同一局域网的手机或电脑浏览器中打开。

- 可做文件管理、一键同步、调试日志与配置管理
- 可查看播放状态、标题与进度，发送播放 / 暂停 / 停止 / 上下集 / 循环 / 重播命令
- 远端设备需**单独配对**，发现设备不等于获得控制权限

> ⚠️ 实际端口由 App 分配，**请使用 App 显示的完整地址**，不要固定假定为 9978。
> 管理地址与配对凭据属于敏感信息，请勿公开分享；App 进程重启后局域网凭据可能失效，需要重新配对。

### 例 4：互联网远程遥控（自托管）

与局域网管理是**两套独立连接方式**：公网遥控由电视**主动连接**你部署的 HTTPS 中转服务，**不需要、也不应把 App 的本地管理端口暴露到公网**。

1. 按 [中转服务部署说明](serverless/webtv-remote-go/README.md) 部署独立服务并配置 HTTPS。
2. 在手机或电视的远程遥控设置中填写服务地址，主动启用连接（**默认关闭**）。
3. 在被控设备生成**限时配对码**，在中转服务网页控制台输入完成绑定。
4. 在网页中选择设备，执行搜索、推送 HTTP/HTTPS 播放地址、播放控制或读取播放状态。
5. 不再使用时**撤销设备授权**；配对码仅可使用一次，控制凭据不要分享给他人。

连接优先使用 WebSocket，并提供 HTTP 轮询回退。本版不开放远程安装 APK、任意文件访问、登录态提取或网络代理，也不包含完整云同步与离线下载。

### 例 5：应用内更新

```text
设置 → 版本检查
```

检查到新版本后下载 APK；下载中断会保留进度并按 HTTP Range 续传（同源最多重试 3 次后换源）；下载完成后交给系统安装器，安装失败则导出到 Downloads 由用户手动安装。

---

## 技术栈

| 分类 | 选型 |
| --- | --- |
| 语言 | Java 21（`sourceCompatibility 21`，启用 core library desugaring） |
| 构建 | Gradle Wrapper + Android Gradle Plugin + Version Catalog（`gradle/libs.versions.toml`） |
| 平台 | `minSdk 24`（Android 7.0）· `targetSdk 37` · `compileSdk 37` |
| 播放内核 | Media3 / ExoPlayer（内置定制版 `1.11.0-alpha01-fongmi`）、IJK、MPV（电视/手机完整版） |
| 扩展运行时 | QuickJS（JavaScript Spider）、Chaquopy（Python Spider，仅完整版）、JAR（Java Spider） |
| 数据与网络 | Room（配置 / 历史库）、OkHttp、NanoHTTPD（本地 HTTP 服务）、JGit（配置仓库）、jUPnP（DLNA） |
| UI | ViewBinding、Material Components、Leanback（电视）、ViewPager2 / RecyclerView、Lottie、Glide |
| 其它 | EventBus、NewPipeExtractor、TensorFlow Lite、desugar_jdk_libs_nio、zxing（手机扫码）、biometric（手机） |
| 附属服务 | Go 编写的自托管公网遥控中转（`serverless/webtv-remote-go`） |

**Gradle 模块**

| 模块 | 作用 |
| --- | --- |
| `:app` | Android 主应用（`main` 共用 + `mobile` / `leanback` 两套 UI） |
| `:catvod` | CatVod 抽象层：Spider 接口、OkHttp、代理工具 |
| `:quickjs` | JavaScript Spider 运行时 |
| `:chaquo` | Python Spider 运行时（仅 `full` 变体参与打包） |

---

## 目录结构

```text
TV/
├── app/                       Android 主应用
│   ├── src/main/              手机 / 电视共用的业务逻辑（Java）
│   │   └── java/com/fongmi/android/tv/
│   │       ├── api/           配置与站源加载（VodConfig 等）
│   │       ├── bean/          数据模型（Site / Config / Vod 等）
│   │       ├── db/            Room 数据库
│   │       ├── playback/      播放状态与续播
│   │       ├── player/        EXO / IJK / MPV 内核封装（最大子包）
│   │       ├── remote/        公网遥控
│   │       ├── server/        本地 HTTP 服务与管理页接口
│   │       ├── setting/       设置项
│   │       ├── ui/            页面与控件
│   │       ├── update/        应用内更新与校验
│   │       ├── utils/         工具类（含依赖信任校验）
│   │       └── web/           WebHome 与原生桥接
│   ├── src/mobile/            手机端 UI 与清单
│   ├── src/leanback/          电视端 UI 与清单
│   ├── src/full/ src/lite/    完整版 / 精简版编译差异
│   ├── schemas/               Room 数据库 schema
│   └── proguard-rules*.pro    混淆规则（含 ViewBinding 保护）
├── catvod/                    CatVod 抽象层与 Spider 接口
├── chaquo/                    Python 运行时（含 requirements.txt）
├── quickjs/                   JavaScript 运行时
├── docs/
│   ├── 应用完整开发文档.md      项目完整说明（结构 / 配置 / Spider / 安全）
│   ├── 代码审计报告.md          安全审计结论
│   ├── webhome-extension/     WebHome 扩展脚本开发指南 + 示例 / 模板
│   └── index.html             GitHub Pages 站点（下载页）
├── webhome-devkit/            WebHome 开发套件（skills / templates / examples）
├── serverless/webtv-remote-go/ 自托管公网遥控中转服务（Go）
├── scripts/                   构建与发布脚本（依赖构建、下载页生成、镜像拉取等）
├── third_party/               本地 Maven 仓库、依赖锁定与补丁源码
├── tools/                     辅助脚本（jar 哈希补丁等）
├── gradle/libs.versions.toml  依赖与版本集中定义
└── .github/workflows/         CI、发版、EPG 同步、Pages 部署
```

---

## 构建与开发

### 环境要求

| 依赖 | 版本 |
| --- | --- |
| JDK | **21**（工具链已在 `gradle/gradle-daemon-jvm.properties` 中声明为 21） |
| Android SDK | `platforms;android-37.0`、`build-tools;37.0.0` |
| Python（宿主机） | **3.10**，仅 Chaquopy 编译 Python 源码时需要 |
| Gradle | 使用仓库内置 `gradlew`，无需单独安装 |

### 常用命令

```bash
# 编译 + 单测（与 CI 一致）
./gradlew :app:compileMobileFullArm64_v8aDebugJavaWithJavac \
          :app:compileLeanbackFullArm64_v8aDebugJavaWithJavac \
          :catvod:compileDebugJavaWithJavac
./gradlew :app:testMobileFullArm64_v8aDebugUnitTest

# 打包（末尾的 Release 可换成 Debug 做本地调试）
./gradlew :app:assembleMobileFullUniversalRelease
./gradlew :app:assembleMobileFullArm64_v8aRelease
./gradlew :app:assembleMobileFullArmeabi_v7aRelease
./gradlew :app:assembleLeanbackFullUniversalRelease
./gradlew :app:assembleLeanbackFullArm64_v8aRelease
./gradlew :app:assembleLeanbackFullArmeabi_v7aRelease
./gradlew :app:assembleMobileLiteUniversalRelease
./gradlew :app:assembleMobileLiteArm64_v8aRelease
./gradlew :app:assembleLeanbackLiteUniversalRelease
./gradlew :app:assembleLeanbackLiteArm64_v8aRelease
```

构建产物位于：

```text
app/build/outputs/apk/<变体名>/release/<包名>.apk
```

### 变体矩阵

变体由三个维度组合：**mode**（`mobile` / `leanback`）× **edition**（`full` / `lite`）× **abi**（`universal` / `arm64_v8a` / `armeabi_v7a`）。

任务名规则：`assemble` + Mode + Edition + Abi + `Release`。

`lite` 不产出 `armeabi_v7a`（老设备不是精简版目标），因此完整版 6 个 + 精简版 4 个 = **10 个包**。

### 签名要求

**Release 构建必须提供签名**，否则构建会在配置阶段直接中止并报错：

```text
Release signing is required for release builds
```

在仓库根目录的 `local.properties` 中配置（该文件不入库）：

```properties
sdk.dir=/path/to/Android/Sdk
storeFile=/absolute/path/to/release.keystore
keyAlias=your_key_alias
storePassword=your_store_password
```

### CI / 发版

| 工作流 | 触发 | 作用 |
| --- | --- | --- |
| `ci.yml` | push / PR 到 main | 编译手机与电视端 + 跑单测（仅 Debug 变体，不依赖签名密钥） |
| `android-release.yml` | 推送 `v*` 标签 / 手动触发 | 构建 10 个 APK 并创建 Release，附带下载页与 R8 mapping |
| `pages.yml` | push 到 main | 部署 GitHub Pages 下载站点 |
| `epg-sync.yml` | 每 4 小时 | 同步 EPG 数据 |
| `remote-relay.yml` | push / PR | 公网遥控中转服务的测试 |

发版流程：改动版本号（`app/build.gradle` 的 `versionCode` / `versionName`）→ 更新 `CHANGELOG.md` → push → 等 CI 通过 → 打 `v<versionName>` 标签 → 等发行构建完成。

---

## 常见问题

**Q：装完是空的，没有影视内容？**
A：这是设计如此。本仓库是**播放器壳子**，不内置内容、不分发 JSON。请在设置中导入你自己的合法配置源。

**Q：全新安装后首页一片空白，而且不再弹确认框？**
A：这是 v5.15.0 修复的已知问题——冷启动时若尚未拿到可用的页面上下文，远程依赖的信任确认会被静默跳过，且失败结果被错误缓存，导致该站点在本进程内永久加载不出来。**请升级到 v5.15.0 或更高版本**。若升级后仍复现，请附上机型、系统版本与日志提交 issue。

**Q：精简版（lite）和完整版有什么区别？**
A：精简版去掉了 MPV 播放内核与 Python 运行时，体积约为完整版一半，保留 EXO/IJK 双内核、QuickJS/JS 源与全部协议能力。**代价是 Python 类型直播源不可用**；需要这类源请装完整版。

**Q：应用内更新一直失败？**
A：更新器已支持断点续传与多源切换（同源最多重试 3 次）。若仍失败，通常是网络对更新源不可达——可在 GitHub Release 直接下载 APK 手动覆盖安装（同签名可覆盖，不丢数据）。

**Q：Release 构建报 `Release signing is required for release builds`？**
A：缺少签名配置。按上文「签名要求」在 `local.properties` 填好 `storeFile` / `keyAlias` / `storePassword`。

**Q：本地跑 Gradle 提示 JDK 版本不对？**
A：本项目要求 **JDK 21**。系统里若是 JDK 8/11/17，请让 `JAVA_HOME` 指向 JDK 21 后再执行 `gradlew`。

**Q：本地单元测试跑不过，但 CI 是绿的？**
A：本地环境容易因测试类未被加入 worker classpath 而报 `ClassNotFoundException`。**以 CI（`ci.yml`）结果为准**，改完代码直接推分支让 CI 验证更可靠。

**Q：局域网管理页打不开 / 端口不是 9978？**
A：端口由 App 动态分配，请以「设置 → 增强功能 → 管理页面」里显示的完整地址为准，并确保手机 / 电脑与设备在同一局域网。

**Q：怎么自己开发 WebHome 首页或注入脚本？**
A：先读 [`docs/webhome-extension/README.md`](docs/webhome-extension/README.md)（自包含的扩展开发指南），再用 [`webhome-devkit/`](webhome-devkit/README.md) 里的模板与示例起步。

---

## 文档

- [应用完整开发文档](docs/应用完整开发文档.md) — 项目结构、配置、Spider、WebHome、本地服务、打包与安全
- [WebHome 扩展脚本开发指南](docs/webhome-extension/README.md) — 面向人工开发者与 AI 助手的自包含指南
- [WebHome 开发套件](webhome-devkit/README.md) — 首页 / 扩展示例、起步模板与 AI skills
- [公网遥控中转服务](serverless/webtv-remote-go/README.md) — 部署与接口契约
- [代码审计报告](docs/代码审计报告.md) — 安全审计结论与已知可接受风险
- [更新日志](CHANGELOG.md) — 逐版本变更说明

开发者建议的阅读顺序：WebHome 首页配置 → Native SDK → 配置导入与管理逻辑 → 本地管理页与同步逻辑。

---

## 开源说明与合规

本仓库只提供技术实现和播放器壳子能力：

- 不内置影视内容
- 不维护站源
- 不分发 JSON
- 不提供内容接口

所有内容来源均应由用户自行配置，并确保合法合规。请遵守所在地区的法律法规，仅将本项目用于合法用途。

已知可接受风险（记录在案）：

- 迅雷网盘签名密钥内嵌于客户端，属客户端签名固有限制，无法真正隐藏。
- 直播 / 媒体源普遍使用明文 HTTP，无法全局禁用；配置源与更新服务器已强制 HTTPS。
- Android `addJavascriptInterface` 无法按 iframe 区分信任来源，跨源 iframe 信任边界为平台限制。

许可协议见 [LICENSE.md](LICENSE.md)。
