# 第0步补充：创建 UniApp 用户端（campus-market-uniapp）

> 目标：用 HBuilderX 创建 UniApp 项目（Vue3 + TypeScript），配好 5 个底部 tab，封装统一请求工具，跑通 H5。
> 完成后你就有了"能跑的移动端骨架"，下一课用它写注册/登录页面。

---

## 1. 安装 HBuilderX（macOS）

1. 打开 DCloud 官网：https://www.dcloud.io/hbuilderx.html
2. 点右上角 **Download / 下载**，选 macOS 版（`.dmg` 文件）
3. 打开 dmg，把 `HBuilderX` 图标拖进「应用程序」文件夹
4. 首次打开：如果系统提示"已损坏/无法验证开发者"，到「系统设置 → 隐私与安全性 → 仍要打开」
   （如果提示已损坏：终端执行 `sudo xattr -dr com.apple.quarantine /Applications/HBuilderX.app` 后重开）

> 备选方案（不想装 HBuilderX）：用命令行创建，效果相同：
> ```bash
> npx degit dcloudio/uni-preset-vue#vite-ts campus-market-uniapp
> cd campus-market-uniapp && npm install && npm run dev:h5
> ```
> 但本课按 HBuilderX 路线讲（对新手更省事）。

---

## 2. 新建项目（关键步骤）

**① 打开 HBuilderX**，首次会弹"选择工作空间"——工作空间就是你存放项目的目录。选 **桌面**（`~/Desktop`）即可，因为项目本身要放进 `CampusMarket` 文件夹。

**② 新建项目**：菜单 `文件 → 新建 → 项目`（快捷键 `Ctrl+N`）

**③ 填写项目信息**（对照下表）：

| 字段 | 填什么 |
| --- | --- |
| 项目名称 | `campus-market-uniapp` |
| 存放位置 | 选到 `~/Desktop/CampusMarket` 这个文件夹下（点旁边的文件夹图标选择） |
| 项目类型 | 选 **uni-app**（第一项） |
| 模板 | 选 **Vue3 + TypeScript** 模板（列表里有"默认模板（Vue3）"等选项，认准 TypeScript） |

> 关键：**存放位置必须选 CampusMarket 文件夹**，这样项目才会变成 `CampusMarket/campus-market-uniapp`，和 Git 仓库结构对上。
> 如果 CampusMarket 下已存在同名空文件夹，选到它即可（HBuilderX 会提示目录已存在，确认用这个目录）。

**④ 点「创建」**，等右侧进度条完成。首次创建可能下载编译插件，耐心等。

---

## 3. 认识生成的项目结构

创建完成后左侧资源管理器应该是这样（Vue3 + TS 模板）：

```
campus-market-uniapp/
├── src/                        # 源代码都在这
│   ├── pages/                  # 页面目录
│   │   └── index/              # 自带一个首页
│   │       └── index.vue
│   ├── static/                 # 静态资源（图片、tabBar 图标）
│   ├── App.vue                 # 应用入口（生命周期、全局样式）
│   ├── main.ts                 # JS 入口（创建应用）
│   ├── manifest.json           # 应用配置：appid、各平台参数
│   ├── pages.json              # 【最重要】页面路由 + tabBar 配置
│   └── uni.scss                # 全局样式变量
├── index.html                  # H5 的入口 html
├── package.json
├── tsconfig.json
└── vite.config.ts
```

---

## 4. 创建 5 个页面

我们需要 5 个 tab 页：首页、分类、发布、消息、我的。`index` 已经有了，再建 4 个：

**① 在 `src/pages` 下新建 4 个目录 + 文件**（右键 `pages` → 新建 → 目录/文件）：

```
src/pages/
├── index/index.vue        # 已有，首页
├── category/category.vue  # 分类
├── publish/publish.vue    # 发布
├── message/message.vue    # 消息
└── user/user.vue          # 我的
```

**② 每个新页面先写最小内容**（以 category 为例，其余 3 个同理，改标题文字即可）：

```vue
<template>
  <view class="page">
    <text>分类页</text>
  </view>
</template>

<script setup lang="ts">
// 页面逻辑后面写
</script>

<style scoped>
.page {
  padding: 40rpx;
  text-align: center;
}
</style>
```

> `rpx` 是 uni-app 的响应式单位：屏幕宽度固定为 750rpx，一套代码适配所有平台，不用写 px。

---

## 5. 配置 pages.json（底部 5 个 tab）

打开 `src/pages.json`，**整体替换**成下面内容：

```json
{
  "pages": [
    { "path": "pages/index/index", "style": { "navigationBarTitleText": "首页" } },
    { "path": "pages/category/category", "style": { "navigationBarTitleText": "分类" } },
    { "path": "pages/publish/publish", "style": { "navigationBarTitleText": "发布" } },
    { "path": "pages/message/message", "style": { "navigationBarTitleText": "消息" } },
    { "path": "pages/user/user", "style": { "navigationBarTitleText": "我的" } }
  ],
  "tabBar": {
    "color": "#999999",
    "selectedColor": "#3B82F6",
    "backgroundColor": "#ffffff",
    "list": [
      { "pagePath": "pages/index/index", "text": "首页" },
      { "pagePath": "pages/category/category", "text": "分类" },
      { "pagePath": "pages/publish/publish", "text": "发布" },
      { "pagePath": "pages/message/message", "text": "消息" },
      { "pagePath": "pages/user/user", "text": "我的" }
    ]
  }
}
```

**每个字段的含义**（面试/简历都能聊）：

| 配置 | 含义 |
| --- | --- |
| `pages` 数组 | 项目所有页面的路由表，**第一项是启动页**（现在是首页） |
| `navigationBarTitleText` | 顶部导航栏标题 |
| `tabBar.color` | 未选中时 tab 文字颜色（灰色） |
| `tabBar.selectedColor` | 选中时 tab 文字颜色（蓝色） |
| `tabBar.list` | tab 列表，**2~5 项**，`pagePath` 必须在 pages 里注册过 |

> 提示：`list` 里没配 `iconPath`，所以 tab 只有文字没有图标——先这样跑通，后面做 UI 时再加图标。

---

## 6. 封装统一的 request.ts

**为什么封装？** 以后每个接口调用都统一走一个函数：baseURL 只写一处、错误处理只写一处、加 token 只改一处。不封装的话每个页面各写各的，改一处要改十个地方。

**① 新建文件**：`src/utils/request.ts`

**② 内容**：

```typescript
const BASE_URL = 'http://localhost:8080'

export function request<T>(options: {
  url: string
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
  data?: Record<string, any>
}): Promise<T> {
  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + options.url,
      method: options.method || 'GET',
      data: options.data,
      header: { 'Content-Type': 'application/json' },
      success: (res) => {
        if (res.statusCode === 200) {
          resolve(res.data as T)
        } else {
          reject(new Error(`请求失败: ${res.statusCode}`))
        }
      },
      fail: (err) => reject(err),
    })
  })
}
```

**③ 用起来什么样**（下一课会真的用）：

```typescript
import { request } from '@/utils/request'

// 调用登录接口
const res = await request<{ code: number; message: string; data: any }>({
  url: '/api/user/login',
  method: 'POST',
  data: { username: 'test001', password: '123456' },
})
```

---

## 7. 运行到浏览器（H5）

**① 菜单**：`运行 → 运行到浏览器 → Chrome`（或 Safari）

- 首次运行会提示"安装 H5 编译器插件"，点确定自动下载
- 稍等编译，HBuilderX 会自动打开浏览器

**② 浏览器地址**：通常是 `http://localhost:5173`（HBuilderX 用 Vite 开发服务器，端口可能变，看控制台提示）

---

## 8. 验收标准

| 检查项 | 预期 |
| --- | --- |
| 浏览器打开 | 看到首页内容 |
| 底部 tab | 5 个 tab：首页/分类/发布/消息/我的 |
| 切换 | 点每个 tab 能切到对应页面，标题栏文字跟着变（首页/分类/…） |
| 项目位置 | 确认在 `~/Desktop/CampusMarket/campus-market-uniapp`（不在别处） |

全部通过 → 用户端骨架完成，可以进"注册/登录页面"对接后端。

---

## 9. 常见坑

1. **tab 切不动**：pages.json 里 `pagePath` 拼写和实际文件路径不一致（大小写、目录名）。
2. **改完 pages.json 没生效**：保存后重新运行一次（有时需要重启 dev server）。
3. **端口被占**：HBuilderX 控制台会报错，换个端口即可（运行菜单里有"运行设置"）。
4. **HBuilderX 打开项目后空白**：确认打开了 `campus-market-uniapp` 目录本身，而不是 `src` 或父目录。
