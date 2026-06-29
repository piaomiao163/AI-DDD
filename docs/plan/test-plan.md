# AI-DDD 全模块自动化测试计划

> 版本：v1.0 | 日期：2026-06-29 | 作者：AI 辅助生成

本文档是 AI-DDD（BidFlow 招投标管理系统）的完整自动化测试计划，覆盖后端 JUnit 单元测试和前端 Playwright E2E 测试。

---

## 一、测试范围

| 模块 | 后端单元测试 | 后端集成测试 | 前端 E2E |
|------|------------|------------|---------|
| 系统管理（登录/用户/角色/权限） | 待补充 | 待补充 | ✅ login.spec.ts |
| 请假模块（Leave） | ✅ LeaveModelTest | 待补充 | ✅ leave.spec.ts |
| 流程定义（Process） | 待补充 | 待补充 | 待补充 |
| 流程实例 / 任务审批 | 待补充 | 待补充 | 待补充 |
| **招标项目（BidProject）** | ✅ BidProjectModelTest | ✅ BidProjectControllerTest | ✅ bid.spec.ts |

---

## 二、测试文件清单

### 后端（JUnit 5 + Mockito）

| 文件 | 层级 | 用例数 |
|------|------|-------|
| `admin/domain/src/test/…/model/BidProjectModelTest.java` | Model | 16 |
| `admin/domain/src/test/…/model/LeaveModelTest.java` | Model | 12 |
| `admin/domain/src/test/…/service/BidProjectServiceTest.java` | Service | 8 |
| `admin/application/src/test/…/command/impl/BidProjectCommandImplTest.java` | Command | 9 |
| `admin/interface/src/test/…/web/bid/BidProjectControllerTest.java` | Controller | 7 |

### 前端（Playwright）

| 文件 | 覆盖场景 |
|------|---------|
| `admin-ui/e2e/login.spec.ts` | 正确登录 / 错误密码 / 空用户名 |
| `admin-ui/e2e/bid.spec.ts` | 列表加载 / 搜索 / 新建 / 详情 / 提交审核 |
| `admin-ui/e2e/leave.spec.ts` | 列表 / 申请请假 / 撤回 |

---

## 三、运行命令

### 后端单元测试

```powershell
# 全量后端测试
$env:JAVA_HOME="D:\Program Files\Java\jdk1.8.0_144"
mvn test -pl domain,application,interface

# 仅测试 Bid 模块
mvn test -pl domain,application,interface -Dtest="BidProject*"

# 仅测试 Leave 模块
mvn test -pl domain -Dtest="LeaveModelTest"
```

### 前端 E2E 测试

```bash
# 1. 安装依赖（首次执行）
cd admin-ui
npm install -D @playwright/test
npx playwright install chromium

# 2. 确保服务已启动
#    后端: http://localhost:8086
#    前端: http://localhost:3000 (npm run dev)

# 3. 运行全量 E2E
npx playwright test

# 4. 交互式调试
npx playwright test --ui

# 5. 仅运行 bid 模块测试
npx playwright test e2e/bid.spec.ts
```

---

## 四、BidProjectModel 关键测试用例

| 用例 | 前置状态 | 操作 | 预期 |
|------|---------|------|------|
| 正常提交 | STATUS_DRAFT(0) | submit() | STATUS_PENDING_REVIEW(1) |
| 非草稿不可提交 | STATUS_PENDING_REVIEW | submit() | IllegalStateException |
| 项目名为空不可提交 | - | submit() | IllegalArgumentException |
| 正常发布 | STATUS_PENDING_REVIEW | publish() | STATUS_REGISTRATION(3) |
| 正常开标 | STATUS_REGISTRATION | openBidding() | STATUS_BIDDING_OPEN(5) |
| 正常评标 | STATUS_BIDDING_OPEN | startEvaluation() | STATUS_EVALUATION(6) |
| 正常公示 | STATUS_EVALUATION | publicize() | STATUS_PUBLICIZING(7) |
| 正常定标 | STATUS_PUBLICIZING | award() | STATUS_AWARDED(8) |
| 全流程链路 | STATUS_DRAFT | 全流程 | STATUS_AWARDED |

---

## 五、依赖配置

### 后端（已配置于 `admin/pom.xml`）

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>3.9.0</version>
    <scope>test</scope>
</dependency>
```

### 前端（需执行安装）

```bash
npm install -D @playwright/test
npx playwright install chromium
```

---

## 六、完成状态

- [x] 后端测试文件创建完成（5 个文件，52 个用例）
- [x] 前端 E2E 测试文件创建完成（3 个文件）
- [x] Playwright 配置文件 `playwright.config.ts` 创建完成
- [x] 父级 `pom.xml` 添加测试依赖
- [x] `interface/pom.xml` 添加 `spring-security-test`
- [x] `CLAUDE.md` 更新规则14（任务前置工作流）+ 规则15（测试规范）
- [x] Playwright `@playwright/test` npm 包安装完成，Chromium 浏览器下载完成
- [x] 后端单元测试全部通过（domain + application，47 用例，0 Failures）
- [x] 前端 E2E 全量通过（11/11，login 3 + bid 5 + leave 3，系统 Chrome 运行）
  - 修复点：登录按钮选择器 `.login-button`，验证码等待自动填充，el-date-picker 用 `fill+Enter`，`user` 账号密码哈希损坏改用 `admin` 账号
