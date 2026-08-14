---
version: alpha
name: Developer Tools
description: 一套面向开发者产品的高对比暗色界面体系，以荧光绿行动色、青色系统反馈、等宽代码片段和克制的终端表面构建设计语言。
sourceUrl: "https://www.uupm.cc/demo/developer-tools"
websiteUrl: "https://www.uupm.cc/demo/developer-tools"
exampleUrl: "https://www.uupm.cc/demo/developer-tools"
language: zh-CN
sourceLanguage: en

colors:
  primary: "#39FF14"
  primary-dark: "#20C80A"
  secondary: "#00D4FF"
  accent: "#A855F7"
  ink: "#E4E4E7"
  body: "#A1A1AA"
  muted: "#A1A1AA"
  canvas: "#0A0A0A"
  surface: "#161616"
  surface-soft: "#0A0A0A"
  border: "#27272A"
  on-primary: "#000000"
  success: "#00D4FF"
  warning: "#F59E0B"
  error: "#EF4444"

typography:
  display-xl:
    fontFamily: "IBM Plex Sans, sans-serif"
    fontSize: 60px
    fontWeight: 700
    lineHeight: 1.12
    letterSpacing: -1.6px
  display-lg:
    fontFamily: "IBM Plex Sans, sans-serif"
    fontSize: 48px
    fontWeight: 700
    lineHeight: 1.18
    letterSpacing: -1px
  display-md:
    fontFamily: "IBM Plex Sans, sans-serif"
    fontSize: 36px
    fontWeight: 600
    lineHeight: 1.24
    letterSpacing: -0.5px
  title-lg:
    fontFamily: "IBM Plex Sans, sans-serif"
    fontSize: 24px
    fontWeight: 600
    lineHeight: 1.32
  title-md:
    fontFamily: "IBM Plex Sans, sans-serif"
    fontSize: 20px
    fontWeight: 600
    lineHeight: 1.4
  body-lg:
    fontFamily: "IBM Plex Sans, sans-serif"
    fontSize: 20px
    fontWeight: 400
    lineHeight: 1.6
  body-md:
    fontFamily: "IBM Plex Sans, sans-serif"
    fontSize: 16px
    fontWeight: 400
    lineHeight: 1.6
  body-sm:
    fontFamily: "IBM Plex Sans, sans-serif"
    fontSize: 14px
    fontWeight: 400
    lineHeight: 1.5
  label:
    fontFamily: "IBM Plex Sans, sans-serif"
    fontSize: 14px
    fontWeight: 600
    lineHeight: 1.4
  data:
    fontFamily: "IBM Plex Mono, monospace"
    fontSize: 14px
    fontWeight: 500
    lineHeight: 1.5

rounded:
  xs: 4px
  sm: 8px
  md: 12px
  lg: 12px
  xl: 12px
  pill: 16px

spacing:
  xxs: 4px
  xs: 8px
  sm: 12px
  md: 16px
  lg: 24px
  xl: 32px
  xxl: 48px
  section: 80px

effects:
  border-subtle: "1px solid #27272A"
  shadow-sm: "0 2px 8px rgba(15, 23, 42, 0.06)"
  shadow-md: "0 12px 32px rgba(15, 23, 42, 0.10)"
  focus-ring: "0 0 0 3px #39FF1433"
  transition-fast: "160ms ease"
  transition-base: "240ms ease"

components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    typography: "{typography.label}"
    rounded: "{rounded.md}"
    padding: 13px 24px
    minHeight: 48px
  button-secondary:
    backgroundColor: transparent
    textColor: "{colors.primary}"
    borderColor: "{colors.primary}"
    borderWidth: 1px
    typography: "{typography.label}"
    rounded: "{rounded.md}"
    padding: 12px 23px
    minHeight: 48px
  content-card:
    backgroundColor: "{colors.surface}"
    borderColor: "{colors.border}"
    rounded: "{rounded.lg}"
    padding: 24px
  input:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.ink}"
    borderColor: "{colors.border}"
    rounded: "{rounded.sm}"
    padding: 12px 14px
    minHeight: 48px
  status-badge:
    backgroundColor: "{colors.surface-soft}"
    textColor: "{colors.primary}"
    rounded: "{rounded.pill}"
    padding: 6px 10px
---

## 概览

一套面向开发者产品的高对比暗色界面体系，以荧光绿行动色、青色系统反馈、等宽代码片段和克制的终端表面构建设计语言。源页面中的具体业务内容已被抽象为可复用体系，而不是复制成固定营销文案。体系的核心签名是**终端式精确感与单一荧光行动通道**。应让代码示例、命令输出与集成状态承担视觉叙事，不用虚构数据或装饰性内容填满页面。

## 设计原则

- **主任务必须明确。** 每个区块只设置一个使用 `{colors.primary}` 的主行动，其他行动采用描边或文字形式。
- **颜色各司其职。** 主色负责行动，辅助色负责进度与支持信息，强调色只用于少量稀缺信号。
- **证据建立信任。** 优先展示真实内容与状态，不用泛化插画、虚构指标和无意义徽章替代产品证据。
- **密度必须有目的。** 相关控制可以紧凑，页面级区块仍需保持充足间隔和稳定阅读顺序。
- **保护风格签名。** 保留终端式精确感与单一荧光行动通道，不要把模板简化成通用卡片网格。

## 色彩体系

- **主色**（`{colors.primary}` — #39FF14）：主 CTA、选中导航、焦点提示和最强交互信号。
- **深主色**（`{colors.primary-dark}` — #20C80A）：hover 与 pressed，不能作为无关装饰。
- **辅助色**（`{colors.secondary}` — #00D4FF）：支持进度、正向上下文、次级数据或选中元信息。
- **强调色**（`{colors.accent}` — #A855F7）：每个视口最多承担一类稀缺提示。
- **高强调文字**（`{colors.ink}` — #E4E4E7）：标题、重要值和高权重标签。
- **正文**（`{colors.body}` — #A1A1AA）：段落、帮助文字和次要元信息。
- **画布 / 表面**（#0A0A0A / #161616）：建立页面深度，不与内容争夺注意力。
- **边框**（`{colors.border}` — #27272A）：分隔线、字段和扁平卡片边界。

状态不能只靠颜色表达，必须同时配合标签、图标、纹理或稳定位置。警告色和错误色只保留给对应语义。

## 字体体系

使用 **IBM Plex Sans** 承担展示标题，**IBM Plex Sans** 承担导航与正文，**IBM Plex Mono** 只用于结构化数值、标识、时间或代码型数据。

| Token | 字号 | 字重 | 用途 |
| --- | ---: | ---: | --- |
| `{typography.display-xl}` | 60px | 700 | 桌面首屏标题 |
| `{typography.display-lg}` | 48px | 700 | 主要区块标题 |
| `{typography.display-md}` | 36px | 600 | 紧凑首屏或 CTA 标题 |
| `{typography.title-lg}` | 24px | 600 | 功能与分组标题 |
| `{typography.title-md}` | 20px | 600 | 命令面板标题 |
| `{typography.body-lg}` | 20px | 400 | 首屏辅助文案 |
| `{typography.body-md}` | 16px | 400 | 默认正文与控件 |
| `{typography.body-sm}` | 14px | 400 | 说明和元信息 |
| `{typography.label}` | 14px | 600 | 按钮、标签页和筛选 |
| `{typography.data}` | 14px | 500 | 结构化产品证据 |

展示标题尽量控制在 8–12 个英文单词或 12–20 个汉字。日常标签不使用装饰性全大写宽字距；动态数值应使用等宽数字。

## 布局

- 最大内容宽度 1280px，桌面两侧最小 24px。
- 营销首屏采用 5/7 或 6/6 比例，在价值主张和真实产品证据之间平衡。
- 核心内容使用 12 栏网格；命令面板集合在宽屏为 3 栏、平板 2 栏、手机 1 栏。
- 应用型高密度区域内部采用 8px 节奏，嵌套于 16/24/32px 页面节奏中。
- 区块默认间距：桌面 80px、平板 64px、手机 48px。
- 首个有意义的行动应尽早出现，但不要把每个区块都拉成首屏高度。

## 表面与层级

1. **画布：** #0A0A0A，无阴影。
2. **柔和分组区：** 与画布相邻的低对比填充，无抬升。
3. **内容表面：** #161616 加 1px #27272A 边框。
4. **浮动控件：** 只有覆盖内容或需要持续可见时才使用中等阴影。
5. **关键浮层：** 模态框或抽屉使用清晰遮罩与明确关闭入口。

层级用于解释遮挡关系，不用于装饰每张卡片。关闭阴影后，代码面板与集成卡片仍必须清晰可读。

## 形状体系

- 4px：细小标记、紧凑数据单元和微型控件。
- 8px：输入框、标签页与紧凑操作。
- 12px：标准卡片、面板和主按钮。
- Pill：仅用于徽章、筛选、状态标签和头像。

不要把段落、大容器和所有按钮都做成胶囊；形状必须强化组件功能。

## 组件规范

### 按钮

主按钮高度至少 48px，使用简洁动词，每个决策组只出现一次。次按钮使用描边或中性表面。破坏性操作绝不继承品牌主色。

### 命令面板

先展示影响决策的核心内容，再展示元信息，最后提供一个明确行动。整卡可点击时仍需保留独立控件语义，并保持可预测的焦点顺序。

### 代码面板

该组件用于承载代码示例、命令输出与集成状态。同组数值与标签必须对齐；空、加载、错误和过期状态保持相同占位，避免布局跳动。

### 集成卡片

把它视为高价值辅助模块，而不是装饰卡片。按需展示来源、状态或下一步行动；不能改变决策的字段应删除。

### 表单与筛选

标签始终显示在控件上方，占位符只提供示例，不能替代标签。多个字段出错时同时提供字段内提示和汇总。触控目标至少 44×44px。

## 交互状态

- **Hover：** 160ms 内微调填充、边框或层级，不移动周围布局。
- **Focus：** 显示清楚的 3px 焦点环，与相邻颜色对比至少 3:1。
- **Pressed：** 使用 `{colors.primary-dark}` 并降低层级。
- **Selected：** 颜色之外增加勾选、边框或实心标签等稳定标记。
- **Loading：** 保持组件尺寸，并说明正在执行的动作。
- **Empty：** 解释内容为空的原因，并提供一个有效恢复行动。
- **Error：** 说明失败原因、保留已输入内容，并保持重试入口可见。
- **Disabled：** 仅用于真正不可用的行动，原因不明显时必须解释。

## 动效

控件反馈使用 160ms，表面过渡使用 240ms。页面入场最多使用一组 320–480ms 的编排，位移控制在 16–24px。必须支持 `prefers-reduced-motion`，避免循环装饰，并让关键数值立即具备可读终态。

## 响应式行为

| 视口 | 行为 |
| --- | --- |
| ≥1280px | 完整 12 栏结构、持续导航、多栏证据展示 |
| 768–1279px | 两栏集合、减少外边距，侧栏转为行内区域 |
| <768px | 单栏阅读顺序、折叠导航、主行动全宽 |
| <420px | 16px 边距、禁止横向滚动，元信息换到主内容下方 |

媒体和产品证据使用有意图的比例与裁切。固定栏不能遮挡末尾内容、键盘焦点或设备安全区。

## 无障碍

- 正文与背景满足 WCAG AA 4.5:1，大字和关键图形满足 3:1。
- 每个交互控件都有可访问名称和可见键盘焦点。
- 标题层级遵循文档顺序，导航、主内容与页脚使用清晰 landmark。
- 承担产品证据的图片提供具体替代文字；装饰资源使用空 alt。
- 状态变化应被辅助技术播报但不抢夺焦点；错误与字段正确关联。
- 颜色、动效和声音都不能成为唯一的信息载体。

## 应当与不应

### 应当

- 保留终端式精确感与单一荧光行动通道。
- 使用真实代码示例、命令输出与集成状态作为主要视觉证据。
- 每个区块只设一个主行动，并明确提供恢复路径。
- 在营销页和应用界面之间维持同一 token 层级。

### 不应

- 把源页面品牌名、主张、评价或虚构指标复制到新产品。
- 给每个表面都添加渐变、徽章、图标和阴影。
- 让强调色承担无关语义，或装饰普通标签。
- 把关键操作藏在仅 hover 可见的区域或低对比图片上。

## QA 检查清单

- [ ] 主色、辅助色、强调色和语义色职责清楚且互不冲突。
- [ ] IBM Plex Sans、IBM Plex Sans 和 IBM Plex Mono 加载正常，并具有稳定 fallback。
- [ ] 首屏、命令面板、代码面板和集成卡片共享同一间距与形状体系。
- [ ] Hover、focus、pressed、loading、empty、error、disabled 和 selected 状态完整。
- [ ] 已检查桌面、平板、手机、200% 缩放、纯键盘和减少动效模式。
- [ ] 无横向溢出，固定控件不遮挡内容。
- [ ] 图片与产品证据含义准确、来源合规，并具有恰当替代文字。
- [ ] 最终结果具有该设计体系识别度，不是通用圆角卡片主题。
