# Admin UI - 后台管理系统

## 项目介绍

基于 Vue3 + Element Plus + TypeScript + Vite 的后台管理系统，支持主题切换，风格类似若依。

## 技术栈

- Vue 3
- TypeScript
- Vite
- Element Plus
- Vue Router
- Pinia
- Axios
- Sass

## 项目结构

```
admin-ui/
├── src/
│   ├── api/              # API 接口
│   │   └── login.ts     # 登录相关接口
│   ├── layout/           # 布局组件
│   │   └── index.vue    # 主布局
│   ├── router/           # 路由配置
│   │   └── index.ts
│   ├── store/            # 状态管理
│   │   ├── theme.ts       # 主题状态
│   │   └── user.ts        # 用户状态
│   ├── styles/           # 样式文件
│   │   ├── theme/       # 主题样式
│   │   ├── index.scss
│   │   └── variables.scss
│   ├── utils/            # 工具函数
│   │   ├── auth.ts        # 认证相关
│   │   ├── request.ts    # 请求封装
│   │   └── theme.ts       # 主题相关
│   ├── views/           # 页面组件
│   │   ├── dashboard/     # 首页
│   │   ├── login/      # 登录页
│   │   └── system/      # 系统管理
│   ├── App.vue
│   ├── main.ts
│   └── vite-env.d.ts
├── index.html
├── package.json
├── tsconfig.json
├── tsconfig.node.json
└── vite.config.ts
```

## 功能特性

1. 用户登录/退出
2. 主题切换（默认/暗黑/蓝色）
3. 响应式侧边栏
4. 用户管理
5. 角色管理
6. 菜单管理

## 快速开始

### 安装依赖

```bash
npm install
```

### 启动开发服务器

```bash
npm run dev
```

### 构建生产版本

```bash
npm run build
```

### 预览生产版本

```bash
npm run preview
```

## 代理配置

开发环境下，API 请求会通过 Vite 代理转发到后端服务，代理规则在 `vite.config.ts` 中配置：

```typescript
proxy: {
  '/api': {
    target: 'http://localhost:8086',
    changeOrigin: true,
    rewrite: (path) => path.replace(/^\/api/, '')
  }
}
```

## 主题切换

系统支持三种主题：
- 默认主题（浅色）
- 暗黑主题深色）
- 蓝色主题蓝色风格）

## 与后端联调

确保后端服务运行在 `http://localhost:8086`，然后通过 `/api` 前缀的请求会被代理到后端。

## License

MIT
