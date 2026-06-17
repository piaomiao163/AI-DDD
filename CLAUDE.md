# CLAUDE.md - AI-DDD 项目指南

## AI 角色

**资深全栈开发工程师** — 精通本项目所有技术栈：Spring Boot、MyBatis-Plus、Spring Security、Activiti 7.1.0、JWT、Redis、DDD架构、Vue3、TypeScript、Element Plus、bpmn-js、JAVA23种设计模式。在编码时遵循项目既有模式和规范，输出生产级代码。

## 项目概述

基于 **领域驱动设计（DDD）** 的后台管理系统，前后端分离架构。后端 Java (Spring Boot)，前端 Vue3 + Element Plus + TypeScript。工作流引擎使用 **Activiti 7.1.0**。

## 项目结构

```
AI-DDD/
├── admin/                    # 后端 (Java/Spring Boot)
│   ├── application/          # 应用层 - 命令处理、DTO、业务事件处理器
│   ├── common/               # 公共模块 - 响应对象、REST模板、工具类、数据权限注解
│   ├── domain/               # 领域层 - 领域模型、领域服务、仓储接口、领域事件
│   ├── infrastructure/       # 基础设施层 - 数据访问、仓储实现、MyBatis映射、技术事件处理器
│   ├── interface/            # 接口层 - 控制器、配置、拦截器、VO
│   ├── sql/                  # SQL脚本
│   └── pom.xml
├── admin-ui/                 # 前端 (Vue3/TypeScript/Vite)
│   ├── src/
│   │   ├── api/              # API接口定义 (process, processInstance, task, post, user)
│   │   ├── bpmn/             # BPMN相关 (activitiModdle.ts)
│   │   ├── layout/           # 布局组件
│   │   ├── router/           # 路由配置
│   │   ├── store/            # Pinia状态管理 (theme, user, permission)
│   │   ├── styles/           # 样式/主题
│   │   ├── utils/            # 工具函数 (auth, request, customTranslate)
│   │   └── views/            # 页面: dashboard, login, process, system, 404
│   └── vite.config.ts        # 代理 /api → localhost:8086
└── CLAUDE.md
```

## 技术栈

### 后端
| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.3.12.RELEASE | 应用框架 |
| MyBatis-Plus | 3.1.2 | ORM |
| Spring Security | 5.3.13.RELEASE | 安全框架 |
| **Activiti** | **7.1.0.M6** | **工作流引擎** |
| MySQL | 8.0.19 | 数据库 |
| Redis | - | 缓存/分布式锁 |
| JWT | 0.9.1 | 认证 |
| Druid | 1.2.2 | 连接池 |
| Lombok | 1.18.24 | 代码简化 |
| Swagger | 2.9.2 | API文档 |

### 前端
- Vue 3 + TypeScript + Vite
- Element Plus (UI组件库)
- Pinia (状态管理)
- Axios (HTTP请求)
- **bpmn-js 17.x** (BPMN流程设计器)
- **bpmn-js-properties-panel 5.x** (属性面板)
- Sass (样式预处理)

## DDD 分层约定

- **domain/model** → 领域模型（充血模型，包含业务行为和状态常量，如 LeaveModel.apply()、LeaveModel.withdraw()）
- **domain/service** → 领域服务（跨聚合编排，不含单聚合内的业务校验逻辑）
- **domain/repository** → 仓储接口 (数据访问抽象)
- **domain/event** → 领域事件基类 + 业务事件
- **domain/event/process** → 流程相关领域事件（ProcessDefinitionPublishedEvent、ProcessInstanceStartedEvent、TaskCompletedEvent 等）
- **infrastructure/dal/event** → 事件发布器实现（SpringDomainEventPublisher）
- **infrastructure/dal/event/handler** → 技术事件处理器（纯日志/审计，@TransactionalEventListener BEFORE_COMMIT）
- **application/event/handler** → 业务事件处理器（含状态变更/数据写入，@TransactionalEventListener BEFORE_COMMIT，通过 Repository 接口操作数据）
- **infrastructure/dal/entity** → DO 数据对象 (对应数据库表)
- **infrastructure/dal/mapper** → MyBatis Mapper
- **infrastructure/dal/repository/impl** → 仓储实现 (含数据权限过滤 + Activiti服务包装)
- **application/command** → 命令接口 + impl
- **application/dto** → 数据传输对象
- **application/event/handler** → 业务事件处理器（ApprovalTaskCreateHandler、LeaveSyncHandler、ProcessInstanceSyncHandler、TaskCompletedHandler）
- **interface/web** → Controller + VO (视图对象)
- **interface/web/config** → Spring配置 (Security, JWT, Activiti, WebMvc 等)

### 领域事件机制

基于 Spring `ApplicationEvent` + `@TransactionalEventListener(phase = BEFORE_COMMIT)` 实现：

```
领域行为触发 → Model.registerEvent() 收集事件 → Service 调用 Model.pullEvents() 拉取 → DomainEventPublisher 发布 → application 层业务 handler / infrastructure 层技术 handler 处理
```

- **BaseDomainEvent<T>**：泛型基类，持有事件ID、实体ID、实体类型、发生时间
- **DomainEventPublisher**：domain层接口，infrastructure层通过 `SpringDomainEventPublisher` 实现
- **Model 事件收集**：Model 通过 `registerEvent(BaseDomainEvent<?>)` 收集事件，Service 通过 `pullEvents()` 拉取后用 `DomainEventPublisher` 发布
- **事件处理器**：使用 `@TransactionalEventListener(phase = BEFORE_COMMIT)` 保证与主事务强一致性

**领域事件清单**（关键状态变更，跨聚合副作用）：

| 实体 | 事件 | 触发时机 | 注册位置 | 处理层级 |
|------|------|---------|---------|---------|
| ProcessDefinition | ProcessDefinitionPublishedEvent | 流程发布（状态0→1） | RepositoryImpl | infrastructure（技术） |
| ProcessInstance | ProcessInstanceStartedEvent | 流程实例启动 | RepositoryImpl | infrastructure（技术） |
| ProcessInstance | ProcessInstanceCompletedEvent | 流程实例完成 | RepositoryImpl | application（业务）+ infrastructure（技术） |
| ProcessInstance | ProcessInstanceWithdrawnEvent | 流程实例撤回 | RepositoryImpl | application（业务）+ infrastructure（技术） |
| Task | TaskCompletedEvent | 任务审批通过 | RepositoryImpl | application（业务） |
| Task | TaskRejectedEvent | 任务驳回 | RepositoryImpl | application（业务） |
| Leave | LeaveAppliedEvent | 请假申请提交并启动流程后 | LeaveModel.startApproving() | application（业务） |

**事件处理器分类：**
- **业务事件处理器**（application层）：ApprovalTaskCreateHandler、LeaveSyncHandler、ProcessInstanceSyncHandler、TaskCompletedHandler — 含状态变更/数据写入，通过 Repository 接口操作
- **技术事件处理器**（infrastructure层）：ProcessInstanceStartedHandler、ProcessDefinitionPublishedHandler — 纯日志/审计

**不需要领域事件的实体**：User、Role、Department、Menu、Permission、Post、DataDictionary、OperationLog — 简单CRUD，无跨聚合副作用。

## 核心特性

### 数据权限
- `@DataPermission` 注解控制，类型: DEPARTMENT / ALL / SELF / CUSTOM
- `DataPermissionSqlParser` 拦截SQL自动拼接权限条件
- `DataPermissionContext` (ThreadLocal) 存储当前用户权限

### 认证
- JWT无状态认证 → `JwtAuthenticationFilter` + `JwtUtil`
- Spring Security + `CustomUserDetailsService` + `CustomPermissionEvaluator`
- **获取当前登录用户** → `UserContext` 工具类（`com.piaomiao.util.UserContext`）

#### UserContext 工具类

任何层（Controller / Command / Service / Repository）均可直接使用，无需注入：

```java
// 获取当前用户名（从 SecurityContextHolder，始终可用）
String username = UserContext.getCurrentUsername();

// 获取当前用户ID（优先从 DataPermissionContext，否则返回 null）
Long userId = UserContext.getCurrentUserId();

// 判断是否已登录
if (UserContext.isAuthenticated()) { ... }

// 要求必须登录，否则抛 IllegalStateException
String username = UserContext.requireCurrentUsername();
```

**获取用户ID的两种方式：**
1. `@DataPermission` 作用域内：`UserContext.getCurrentUserId()` 直接返回（由 `DataPermissionAspect` 设置）
2. 非 `@DataPermission` 作用域：需通过 `userService.findByUsername(UserContext.getCurrentUsername()).getId()` 查询

**禁止直接使用 `SecurityContextHolder.getContext().getAuthentication()`**，统一使用 `UserContext`。

### REST模板
- `TemplateRest` / `AbstractTemplateRest` 统一请求处理
- `CallbackRest` 回调接口: check() → execute()
- 支持事务、分布式锁(Redis)自动降级本地锁

### 工作流 (Activiti 7.1.0)

#### 流程定义
- CRUD + 发布(部署到Activiti) + BPMN XML管理
- `sys_process_definition` 自定义表存储流程定义元数据
- 发布时自动部署到Activiti引擎，回写 `deploymentId` 和 `activitiProcessDefinitionId`
- 前端使用 **bpmn-js Modeler** 可视化设计器，属性面板，中文本地化
- XML为流程定义唯一数据源（已废弃 nodes/edges JSON存储方式）

#### 流程实例
- 启动流程：`POST /process-instance/start`
- 查询运行中实例：`GET /process-instance/running`
- 查询历史实例：`GET /process-instance/history`
- 终止实例：`POST /process-instance/{id}/terminate`

#### 任务管理
- 待办任务：`GET /task/todo`
- 已办任务：`GET /task/done`
- 可认领任务：`GET /task/claimable`
- 认领/取消认领：`POST /task/{id}/claim`、`POST /task/{id}/unclaim`
- 审批通过：`POST /task/{id}/complete`（设置 outcome=approved）
- 驳回：`POST /task/{id}/reject`（设置 outcome=rejected）
- 委派：`POST /task/{id}/delegate`

#### 流程图
- `GET /process-diagram/{processInstanceId}` 返回带当前节点高亮的PNG流程图
- 使用 Activiti `ProcessDiagramGenerator` 在 RepositoryImpl 中生成
- 前端 `diagram.vue` 直接展示后端返回的PNG图片

#### Activiti 配置
- `ActivitiConfig.java`：实现 `ProcessEngineConfigurationConfigurer`，仅定制 UUID 生成器；由 Activiti Starter 自动配置所有 Service Bean
- `application.yml`：`spring.activiti` 配置段，history-level=audit
- 用户集成：`Authentication.setAuthenticatedUserId(username)` 在 RepositoryImpl 中调用，Service 层不直接注入 Activiti 服务
- Activiti 自动管理约23张表 (`ACT_RE_*`, `ACT_RU_*`, `ACT_HI_*`, `ACT_GE_*`)

#### 前端 Activiti 扩展
- `src/bpmn/activitiModdle.ts`：Activiti 命名空间描述符（assignee, candidateUsers, candidateGroups, delegateExpression 等）
- `src/utils/customTranslate.ts`：bpmn-js 中文翻译字典

## 构建与运行

### 后端
```bash
cd admin
mvn clean install
mvn spring-boot:run -pl interface
```
- 端口: 8086
- 数据库: ddd (MySQL, 自动建表)
- 配置: `admin/interface/src/main/resources/application.yml`
- Activiti 自动创建表，需执行 `admin/infrastructure/src/main/resources/sql/activiti_migration.sql` 为 sys_process_definition 添加新字段

### 前端
```bash
cd admin-ui
npm install
npm run dev
```
- 代理: `/api` → `http://localhost:8086`

## API 端点

### 认证
- `POST /api/login` - 登录
- `POST /api/register` - 注册

### 系统管理
- 用户/部门/角色/菜单/岗位/权限/数据字典/操作日志 等CRUD接口

### 流程定义
- `POST /process/save` - 保存
- `GET /process/findById/{id}` - 查询
- `GET /process/findByPage` - 分页
- `PUT /process/update` - 更新
- `DELETE /process/delete/{id}` - 删除
- `POST /process/publish/{id}` - 发布(部署到Activiti)

### 流程实例
- `POST /process-instance/start` - 启动流程
- `GET /process-instance/running` - 我的运行中实例
- `GET /process-instance/history` - 我的历史实例
- `GET /process-instance/{id}` - 实例详情
- `POST /process-instance/{id}/terminate` - 终止

### 任务
- `GET /task/todo` - 待办
- `GET /task/done` - 已办
- `GET /task/claimable` - 可认领
- `POST /task/{id}/claim` - 认领
- `POST /task/{id}/complete` - 审批
- `POST /task/{id}/reject` - 驳回
- `POST /task/{id}/delegate` - 委派

### 流程图
- `GET /process-diagram/{processInstanceId}` - 带高亮的流程图PNG

---

## 编码规范（强制）

### ⛔ 规则1: Controller 必须使用 TemplateRest + CallbackRest

**所有 Controller 接口必须通过 `TemplateRest` 统一请求处理，禁止直接返回 `Response.success()`。**

正确写法:
```java
@Autowired
private TemplateRest templateRest;

@PostMapping("/save")
public Response<FooVO> save(@RequestBody FooVO vo) {
    return templateRest.request(new CallbackRest<FooVO>() {
        @Override
        public void check() {
            if (vo.getName() == null) throw new IllegalArgumentException("名称不能为空");
        }
        @Override
        public FooVO execute() {
            return fooCommand.save(vo.toDTO());
        }
    }, true); // true=需要事务, false或省略=无事务
}
```

错误写法:
```java
// ❌ 直接返回 Response.success()
@PostMapping("/save")
public Response<FooVO> save(@RequestBody FooDTO dto) {
    FooDTO saved = fooCommand.save(dto);
    return Response.success(convertToVO(saved)); // 禁止!
}
```

### ⛔ 规则2: 请求参数必须用 VO 对象，禁止 Map

**Controller 方法入参禁止使用 `Map<String, Object>` / `Map<String, String>`，必须创建对应的请求 VO。**

正确写法:
```java
@PostMapping("/start")
public Response<ProcessInstanceVO> startProcess(@RequestBody StartProcessVO vo) {
    // vo.getProcessDefinitionKey(), vo.getBusinessKey(), vo.getVariables()
}
```

错误写法:
```java
// ❌ 使用 Map 接收参数
@PostMapping("/start")
public Response<ProcessInstanceVO> startProcess(@RequestBody Map<String, Object> params) {
    String key = (String) params.get("processDefinitionKey"); // 禁止!
}
```

VO 命名规范: 操作 + 实体名，如 `StartProcessVO`, `CompleteTaskVO`, `RejectTaskVO`, `DelegateTaskVO`, `TerminateProcessVO`

### ⛔ 规则2.5: 分页查询必须使用 POST + 对象入参

**所有分页查询接口必须使用 POST 请求，入参通过继承 `BasePageVO` / `BasePageDTO` 的对象接收，禁止使用 GET + @RequestParam 逐个传参。**

层级规范:
- **interface 层**: 分页查询入参 VO 必须继承 `BasePageVO`（`com.piaomiao.base.BasePageVO`），字段: pageNum, pageSize, sortField, sortOrder
- **application 层**: 分页查询入参 DTO 必须继承 `BasePageDTO`（`com.piaomiao.base.BasePageDTO`），字段: pageNum, pageSize, sortField, sortOrder

正确写法:
```java
// interface层 - 请求VO
@Data
public class QueryLeaveVO extends BasePageVO {
    private String title;
    private Integer leaveType;
    private Integer status;
}

// Controller
@PostMapping("/page")
public Response<PageResult<LeaveVO>> findByPage(@RequestBody QueryLeaveVO vo) {
    return templateRest.request(new CallbackRest<PageResult<LeaveVO>>() {
        @Override
        public void check() {}

        @Override
        public PageResult<LeaveVO> execute() {
            QueryLeaveDTO dto = new QueryLeaveDTO();
            BeanUtils.copyProperties(vo, dto);
            PageResult<LeaveDTO> result = leaveCommand.findByPage(dto);
            return convertPageResult(result);
        }
    });
}

// application层 - 查询DTO
@Data
public class QueryLeaveDTO extends BasePageDTO {
    private String title;
    private Integer leaveType;
    private Integer status;
}
```

错误写法:
```java
// ❌ 使用GET + @RequestParam逐个传参
@GetMapping("/page")
public Response<PageResult<LeaveVO>> findByPage(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) Integer status) { ... } // 禁止!
```

### ⛔ 规则3: 新增领域实体必须遵循完整 DDD 流程

```
Model → Repository接口 → DO → Mapper → RepositoryImpl → Service → Command → Controller
```

每层对象职责:
- **Model**: 领域模型（充血模型），包含业务行为、状态常量、校验逻辑（详见规则11）
- **DO**: 数据库映射对象，`@TableName` + `@TableField`，含 `toModel()`/`fromModel()` 转换方法
- **DTO**: 层间传输对象
- **VO**: 分两类 — 请求VO(入参) + 响应VO(出参)
- **Repository接口**: domain层定义，infrastructure层实现
- **Service**: 领域服务，只做跨聚合编排（如持久化、启动流程、发布事件），不含单聚合内的业务校验
- **Command**: 应用层编排，连接 Controller 和 Service，负责 DTO↔Model 转换

#### 对象命名规范

**通用规则：去掉表名前缀的实体名 + 功能描述 + 层级后缀**

表名前缀：`sys_`（系统表）、`biz_`（业务表）。去掉前缀后得到实体名，再组合功能和层级后缀：

| 表名 | 去前缀实体名 | Model | DO | DTO | VO |
|------|------------|-------|----|----|----|
| `biz_leave` | Leave | `LeaveModel` | `LeaveDO` | `LeaveDTO`, `QueryLeaveDTO`, `LeaveApplyDTO` | `LeaveVO`, `ApplyLeaveVO` |
| `sys_process_instance` | ProcessInstance | `ProcessInstanceModel` | `ProcessInstanceDO` | `ProcessInstanceDTO` | `ProcessInstanceVO`, `StartProcessVO` |
| `sys_user` | User | `UserModel` | `UserDO` | `UserDTO` | `UserVO` |
| - | Task | - | - | `TaskCompleteDTO` | `CompleteTaskVO`, `RejectTaskVO` |

- **基础DTO/VO**：`实体名 + 后缀`，如 `LeaveDTO`、`LeaveVO`
- **功能DTO/VO**：`实体名 + 功能动词 + 后缀`，如 `TaskCompleteDTO`、`LeaveApplyDTO`、`ApplyLeaveVO`、`StartProcessVO`、`RejectTaskVO`
- **查询DTO/VO**：`Query + 实体名 + 后缀`，如 `QueryLeaveDTO`、`QueryLeaveVO`、`MyLeaveQueryDTO`

### ⛔ 规则4: Activiti 引擎服务不得泄漏到 domain/interface 层

**`RepositoryService`, `RuntimeService`, `TaskService`, `HistoryService` 只能在 `infrastructure/dal/repository/impl` 中使用。**

- Service 层通过 Repository 接口调用 `deployToEngine()` / `generateDiagram()` 等方法
- Controller 层通过 Command → Service → Repository 链路调用
- `Authentication.setAuthenticatedUserId()` 在 RepositoryImpl 中调用

### ⛔ 规则5: BeanUtils.copyProperties 仅复制同名字段，注意层间转换

- Model ↔ DO: 注意 `nodes`/`edges` (List) 与 `nodesJson`/`edgesJson` (String) 的手动转换
- DTO ↔ Model: 注意 `createTime`/`updateTime` 可能因 DTO 缺失字段而丢失，需手动补充
- DTO ↔ VO: 确保 VO 包含所有需要返回前端的字段（包括时间字段）

### 规则6: 前端新增页面流程

```
1. src/api/xxx.ts  — 定义接口和类型
2. src/views/xxx/index.vue — 页面组件
3. src/router/index.ts — 注册路由
```

### ⛔ 规则8: MySQL 规范

#### 建表规范
```sql
CREATE TABLE IF NOT EXISTS sys_example (
    id          BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    name        VARCHAR(100) NOT NULL                COMMENT '名称',
    status      TINYINT      NOT NULL DEFAULT 1      COMMENT '状态 1启用 0禁用',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP                 COMMENT '创建时间',
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    -- 可选字段（按需添加）:
    -- create_by   VARCHAR(100)                         COMMENT '创建人',
    -- update_by   VARCHAR(100)                         COMMENT '更新人',
    -- deleted     TINYINT      NOT NULL DEFAULT 0      COMMENT '是否删除1-是、0-否',
    PRIMARY KEY (id),
    UNIQUE KEY uk_name (name),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='示例表';
```

强制要求:
- **表名**: 需要分析结合业务场景进行区分是属于系统表还是业务表再进行创建，系统应用前缀`sys_` + 小写snake_case，如 `sys_user`、`sys_process_definition`,其他业务表使用具体业务前缀 `biz_` + 小写snake_case,如 `biz_order`, `biz_task` 
- **字段名**: 小写snake_case，如 `parent_id`、`create_time`
- **主键**: `id BIGINT(20) NOT NULL AUTO_INCREMENT`
- **字符集**: `utf8mb4`，排序规则 `utf8mb4_unicode_ci`
- **存储引擎**: `InnoDB`
- **时间字段**: `create_time` + `update_time`，必须带 `DEFAULT CURRENT_TIMESTAMP`；`update_time` 加 `ON UPDATE CURRENT_TIMESTAMP`
- **状态字段**: `TINYINT`，`1`=启用 `0`=禁用，默认 `1`
- **索引命名**: 唯一索引 `uk_` 前缀，普通索引 `idx_` 前缀
- **注释**: 每个字段和表都必须有 `COMMENT`
- **可选审计字段**（根据业务场景判断是否需要）:
  - `create_by VARCHAR(100) COMMENT '创建人'` — 需要记录操作人时添加（如业务单据、审批流程）；纯系统配置表（菜单、数据字典）可不加
  - `update_by VARCHAR(100) COMMENT '更新人'` — 与 `create_by` 同步添加，有 `create_by` 则必有 `update_by`
  - `deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否'` — 需要软删除/回收站功能时添加；物理删除无恢复需求的表（如操作日志、临时表）可不加

#### 软删除（@TableLogic）

DO 实体的 `deleted` 字段加 `@TableLogic` 注解，MyBatis-Plus 自动将 `deleteById()` 转为 `UPDATE SET deleted=1`：
```java
@TableLogic
private Integer deleted;
```

使用 `@TableLogic` 后：
- `mapper.deleteById(id)` → 实际执行 `UPDATE SET deleted=1`
- `mapper.selectList()` → 自动添加 `WHERE deleted=0`
- 无需手动修改查询条件



#### DO 实体规范
```java
@TableName("sys_example")
public class ExampleDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    // 可选字段（按需添加，与建表保持一致）:
    // private String createBy;
    // private String updateBy;
    // @TableLogic
    // private Integer deleted;

    // Getter/Setter（手动编写，不使用 Lombok @Data/@Builder）
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    // ... 其他字段同理

    // Model 转换方法
    public ExampleModel toModel() { ... }
    public static ExampleDO fromModel(ExampleModel model) { ... }
}
```

强制要求:
- **类名**: `{Entity}DO` 后缀，包路径 `com.piaomiao.dal.entity`
- **禁止 Lombok**: DO 不使用 `@Data`/`@Builder`/`@NoArgsConstructor`/`@AllArgsConstructor`，手动编写 getter/setter（避免 `@Builder` 与无参构造器冲突导致 setter 不生成）
- **主键**: 必须显式声明 `@TableId(value = "id", type = IdType.AUTO)`
- **字段映射**: Java 用 camelCase，依赖 `map-underscore-to-camel-case: true` 自动映射；特殊字段用 `@TableField("column_name")` 显式指定
- **非数据库字段**: 用 `@TableField(exist = false)`
- **JSON 字段**: 用 `String` 类型存储，在 `toModel()`/`fromModel()` 中按需转换（不要用 `JsonTypeHandler` 等泛型 TypeHandler，避免类型不兼容）
- **Model 转换**: 每个 DO 必须有 `toModel()` 和 `static fromModel()` 方法
- **审计字段手动填充**: `createTime`/`updateTime`/`createBy`/`updateBy` 不使用 `@TableField(fill = FieldFill.xxx)` 自动填充，由 `AuditFieldUtil.fillForInsert()`/`AuditFieldUtil.fillForUpdate()` 在 RepositoryImpl 中手动调用

### ⛔ 规则9: 前端样式规范

**Vue 组件禁止使用 `<style scoped>`，样式必须抽取到独立文件中。**

目录结构:
```
src/
├── styles/
│   └── css/                    # 样式根目录（支持 .css / .sass / .scss）
│       ├── process/            # 与 views/process/ 对应
│       │   ├── index.css       # 对应 views/process/index.vue
│       │   └── design.css      # 对应 views/process/design.vue
│       ├── system/             # 与 views/system/ 对应
│       │   ├── index.css       # 对应 views/system/index.vue
│       │   └── role.css        # 对应 views/system/role.vue
│       └── login/
│           └── index.css       # 对应 views/login/index.vue
└── views/
    ├── process/
    │   ├── index.vue
    │   └── design.vue
    └── ...
```

规则:
- **文件夹对应**: `styles/css/` 下的文件夹名与 `views/` 下的文件夹名一致
- **文件名对应**: CSS 文件名与同名目录下的 `.vue` 文件名一致（不含 `.vue` 后缀）
- **引入方式**: 在 `.vue` 的 `<script>` 中 `import '@/styles/css/xxx/yyy.css'`
- **禁止 `<style>`**: Vue 文件中不得包含 `<style>` 或 `<style scoped>` 块

### ⛔ 规则10: 事务与锁

- 写操作传 `true` 启用事务: `templateRest.request(callback, true)`
- 需要分布式锁: `templateRest.requestWithLock(callback, lockKey, timeout)`
- 读操作用无参: `templateRest.request(callback)`

### ⛔ 规则11: 领域模型必须是充血模型，业务行为归属 Model

**领域模型（Model）不仅是数据载体，必须封装自身的业务行为。Service 只做跨聚合编排，不含单聚合内的业务校验逻辑。**

#### 职责划分原则

| 职责 | 归属 | 说明 |
|------|------|------|
| 字段校验、状态变更、业务规则 | **Model** | 单聚合内部逻辑，如：必填校验、状态前置条件检查、状态常量 |
| 跨聚合协调、持久化、外部副作用 | **Service** | 如：保存到数据库、启动流程实例、发布领域事件 |
| DTO↔Model 转换 | **Command** | 应用层负责 DTO 和 Model 之间的映射 |

#### 正确写法 — Model 封装行为

```java
// Model — 领域行为
public class LeaveModel {
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_APPROVING = 1;
    public static final int STATUS_WITHDRAWN = 4;

    /**
     * 提交请假：校验必填字段，设置初始状态
     */
    public void apply() {
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("请假标题不能为空");
        }
        if (days == null || days.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("请假天数必须大于0");
        }
        this.status = STATUS_PENDING;
    }

    /**
     * 撤回：校验状态和操作人权限
     */
    public void withdraw(Long currentUserId) {
        if (status != STATUS_PENDING && status != STATUS_APPROVING) {
            throw new IllegalStateException("只有待审批或审批中的请假单才能撤回");
        }
        if (!applicantId.equals(currentUserId)) {
            throw new IllegalStateException("只有申请人才能撤回");
        }
        this.status = STATUS_WITHDRAWN;
    }

    /**
     * 流程启动后：回写流程实例ID，更新状态为审批中
     */
    public void startApproving(Long processInstanceId) {
        this.processInstanceId = processInstanceId;
        this.status = STATUS_APPROVING;
    }

    /** 获取流程变量 */
    public Map<String, Object> getProcessVariables() { ... }

    /** 获取流程标题 */
    public String getProcessTitle() { ... }

    /** 解析流程定义Key（带默认值） */
    public String resolveProcessDefinitionKey() { ... }
}
```

```java
// Service — 只做跨聚合编排，不含业务校验
@Override
public LeaveModel apply(LeaveModel model) {
    // 1. 领域行为
    model.apply();

    // 2. 持久化
    LeaveModel saved = leaveRepository.save(model);

    // 3. 跨聚合：启动流程
    ProcessInstanceModel pi = processInstanceRepository.startProcess(
        saved.resolveProcessDefinitionKey(), ...);

    // 4. 跨聚合：回写流程实例ID
    saved.startApproving(pi.getId(), pi.getProcessDefinitionDbId(),
        pi.getCurrentAssignees(), pi.getCurrentNodeName(), pi.getCurrentNodeId());
    leaveRepository.updateProcessInstanceId(saved.getId(), pi.getId());
    leaveRepository.updateStatusById(saved.getId(), LeaveModel.STATUS_APPROVING, null);

    // 5. 发布领域事件（驱动审批任务创建等）
    for (BaseDomainEvent<?> event : saved.pullEvents()) {
        domainEventPublisher.publish(event);
    }

    return saved;
}

@Override
public void withdraw(Long id, String reason, Long currentUserId) {
    LeaveModel model = leaveRepository.findById(id);
    if (model == null) throw new RuntimeException("请假单不存在");

    // 1. 领域行为
    model.withdraw(currentUserId);

    // 2. 跨聚合：撤回流程实例
    if (model.getProcessInstanceId() != null) {
        processInstanceRepository.withdrawProcess(model.getProcessInstanceId(), reason, currentUserId);
    }

    // 3. 持久化状态变更
    leaveRepository.updateStatusById(id, LeaveModel.STATUS_WITHDRAWN, reason);
}
```

#### 错误写法 — 贫血模型 + Service 承担所有逻辑

```java
// ❌ Model 只是数据载体，没有行为
@Data
public class LeaveModel {
    private Integer status; // 没有业务方法
}

// ❌ Service 包含了本应属于 Model 的校验和状态变更逻辑
@Service
public class LeaveServiceImpl {
    public LeaveModel apply(LeaveModel model) {
        if (model.getTitle() == null) throw ...;  // 校验应在 Model
        model.setStatus(0);                        // 状态变更应在 Model
        // ...
    }
    public void withdraw(Long id, ..., Long currentUserId) {
        if (model.getStatus() != 0 && model.getStatus() != 1) throw ...;  // 校验应在 Model
        if (!model.getApplicantId().equals(currentUserId)) throw ...;      // 校验应在 Model
        model.setStatus(4);  // 状态变更应在 Model
        // ...
    }
}
```

#### Model 行为设计要点

1. **状态常量定义在 Model 上** — 用 `public static final` 常量，如 `LeaveModel.STATUS_PENDING`，而非散落在 Service 中用魔法数字
2. **校验方法抛异常** — `IllegalArgumentException`（参数不合法）、`IllegalStateException`（状态不满足前置条件），不要返回布尔值
3. **setter 仅用于数据映射** — `toModel()`/`fromModel()` 场景，业务代码中不允许直接调用 setter 改变状态
4. **跨聚合副作用不属于 Model** — 如启动流程实例、发送通知等，由 Service 协调
5. **Model 不依赖 Spring** — 不注入 Repository、不注入其他 Service，保持纯粹的领域逻辑

### ⛔ 规则12: 领域事件发布规范

**领域事件由 Model 收集、Service 同步发布、infrastructure 层在同一事务内处理。禁止 AFTER_COMMIT 异步处理，必须使用 BEFORE_COMMIT 保证强一致性。**

#### 事件发布流程

```
1. Model 领域行为中调用 registerEvent(new XxxEvent(...))  — 收集事件
2. Service 调用 model.pullEvents()                         — 拉取事件
3. Service 用 domainEventPublisher.publish(event)          — 同步发布
4. infrastructure 层 handler @TransactionalEventListener(BEFORE_COMMIT) — 同一事务内处理
```

#### Model 侧 — 事件收集

```java
public class LeaveModel {
    // 事件收集列表（泛型基类，不依赖具体事件类型）
    private final List<BaseDomainEvent<?>> events = new ArrayList<>();

    // 注册事件（protected，仅领域行为内部调用）
    protected void registerEvent(BaseDomainEvent<?> event) {
        this.events.add(event);
    }

    //拉取并清空事件（public，Service 层调用）
    public List<BaseDomainEvent<?>> pullEvents() {
        List<BaseDomainEvent<?>> pulled = new ArrayList<>(events);
        events.clear();
        return pulled;
    }

    // 领域行为中注册事件
    public void startApproving(Long processInstanceId, ...) {
        this.processInstanceId = processInstanceId;
        this.status = STATUS_APPROVING;
        registerEvent(new LeaveAppliedEvent(this, this, ...));
    }
}
```

#### Service 侧 — 同步事件发布

```java
@Service
public class LeaveServiceImpl {
    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public LeaveModel apply(LeaveModel model) {
        model.apply();
        LeaveModel saved = leaveRepository.save(model);
        ProcessInstanceModel pi = processInstanceRepository.startProcess(...);

        // 领域行为（内部注册事件）
        saved.startApproving(pi.getId(), ...);
        leaveRepository.updateProcessInstanceId(saved.getId(), pi.getId());
        leaveRepository.updateStatusById(saved.getId(), LeaveModel.STATUS_APPROVING, null);

        // 同步发布领域事件（BEFORE_COMMIT，同一事务内强一致性）
        for (BaseDomainEvent<?> event : saved.pullEvents()) {
            domainEventPublisher.publish(event);
        }

        return saved;
    }
}
```

#### infrastructure 侧 — 事件处理（同一事务）

```java
@Slf4j
@Component
public class ApprovalTaskCreateHandler {

    @Autowired
    private ApprovalTaskMapper approvalTaskMapper;

    // BEFORE_COMMIT：与主事务同一事务，保证强一致性
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleLeaveApplied(LeaveAppliedEvent event) {
        ApprovalTaskDO taskDO = new ApprovalTaskDO();
        taskDO.setInstanceId(event.getProcessInstanceId());
        // ... 设置字段
        AuditFieldUtil.fillForInsert(taskDO);
        approvalTaskMapper.insert(taskDO);
        log.info("审批任务已创建 - leaveId:{}, taskId:{}", event.getEntityId(), taskDO.getId());
    }
}
```

#### 事件定义规范

```java
// 事件类继承 BaseDomainEvent<关联的Model类型>
public class LeaveAppliedEvent extends BaseDomainEvent<LeaveModel> {
    // 携带处理事件所需的额外数据（不要传 Model 本身以外的整个聚合）
    private final Long processInstanceId;
    private final String currentAssignees;
    // ...

    public LeaveAppliedEvent(Object source, LeaveModel entity, ...) {
        super(source, entity, entity.getId(), "Leave");  // 实体类型用简短名称
        this.processInstanceId = processInstanceId;
        // ...
    }
    // getter 方法（事件不可变，不提供 setter）
}
```

#### 设计要点

1. **事件在领域行为中注册** — 事件与业务操作原子绑定，不会遗漏。不在 Service 中直接 `new XxxEvent()`
2. **pullEvents() 清空列表** — 防止同一事件被重复发布
3. **事件不可变** — 所有字段为 `final`，只提供 getter，不提供 setter
4. **事件处理器用 BEFORE_COMMIT** — 与主事务同一事务，请假单和审批任务要么同时成功要么同时回滚，保证强一致性
5. **禁止 AFTER_COMMIT** — 事务提交后异步执行会导致数据不一致（主事务回滚但副作用已执行）
6. **events 列表用 `BaseDomainEvent<?>` 泛型** — Model 不依赖具体事件类型，可注册任意事件
7. **registerEvent 用 protected** — 仅 Model 内部的领域行为可调用，外部（Service/Command）不能直接注册

### ⛔ 规则13: 代码注释规范

**每个类、接口、枚举必须有注释；每个字段必须有注释；每个public方法必须有Javadoc。**

#### Java 注释规范

| 层级 | 类注释 | 字段注释 | 方法注释 |
|------|--------|---------|---------|
| **Model** | `/** 请假单领域模型 */` | `/** 请假标题 */` | 领域行为方法必须有完整Javadoc（含 `@param` `@return`） |
| **DO** | `/** 请假单数据对象 */` | `/** 请假标题 */` | `toModel()`/`fromModel()` 必须有Javadoc |
| **DTO** | `/** 请假单DTO */` | `/** 请假标题 */` | N/A（数据载体无方法） |
| **VO** | `/** 请假单响应VO */` | `/** 请假标题 */` | `toDTO()` 必须有Javadoc |
| **Service接口** | `/** 请假单服务接口 */` | N/A | 每个方法必须有Javadoc（含 `@param` `@return`） |
| **Service实现** | 不强制（同接口） | N/A | `@Override` 方法可不重复Javadoc；非覆盖方法必须有 |
| **Command接口** | `/** 请假单命令接口 */` | N/A | 每个方法必须有Javadoc（含 `@param` `@return`） |
| **Command实现** | 不强制（同接口） | N/A | 同Service实现 |
| **Controller** | `/** 请假单控制器 */` | N/A | 每个接口方法必须有Javadoc（含 `@param` `@return`） |
| **Repository接口** | `/** 请假单仓储接口 */` | N/A | 每个方法必须有Javadoc（含 `@param` `@return`） |
| **RepositoryImpl** | 不强制（同接口） | N/A | `@Override` 方法可不重复；私有方法按需 |
| **Event** | `/** 请假申请事件 */` | `/** 流程实例ID */` | 构造方法必须有Javadoc |
| **EventHandler** | `/** 请假单状态同步处理器 */` | N/A | 每个handler方法必须有Javadoc |
| **Config** | `/** Activiti配置 */` | N/A | 按需 |
| **工具类** | `/** 用户上下文工具 */` | `/** ... */` | 每个public方法必须有Javadoc |

#### 注释风格

```java
// ✅ 正确：多行Javadoc（所有字段、类、方法统一使用）
/**
 * 请假标题
 */
private String title;

// ✅ 正确：方法Javadoc
/**
 * 提交请假申请
 * @param model 请假单模型
 * @return 保存后的请假单模型
 */
public LeaveModel apply(LeaveModel model) { ... }

// ❌ 错误：无注释
private String title;

// ❌ 错误：单行注释（字段禁止使用）
// 请假标题
private String title;

// ❌ 错误：行尾注释（字段禁止使用）
private String title; // 请假标题
```

#### 例外情况

- **getter/setter**：不需要Javadoc（Lombok `@Data` 自动生成，手写的也不要求）
- **`@Override` 方法**：可以不重复写Javadoc（接口/父类已有）
- **私有辅助方法**：简单转换方法（如 `convertToDTO`）可只写一行 `/** DO转DTO */`，不加 `@param`/`@return`
- **常量字段**：Model 中的 `public static final` 状态常量必须有Javadoc说明含义

#### TypeScript/Vue 注释规范

```typescript
// 接口类型：每个字段必须有注释
export interface LeaveApprovalDetail {
  leaveId: number          // 请假单ID
  title: string            // 请假标题
  taskId: string           // 任务ID
  actProcessInstanceId: string  // Activiti流程实例ID
}

// API函数：必须有Javadoc
/**
 * 根据任务ID获取请假审批详情
 * @param taskId 任务ID
 */
export function getLeaveApprovalDetailByTaskId(taskId: string) { ... }

// Vue组件：script setup 中关键变量和函数按需注释
const detail = ref<LeaveApprovalDetail | null>(null)  // 审批详情
```

#### SQL 注释规范

- **建表**：每个字段和表必须有 `COMMENT`，表注释前缀按表类型区分：
  - 系统表：`sys_` 前缀，如 `COMMENT='系统用户表'`
  - 业务表：`biz_` 前缀，如 `COMMENT='请假单表'`
  - Activiti引擎表：`ACT-` 前缀，如 `COMMENT='ACT-运行时任务（当前待办的用户任务）'`
- **字段COMMENT**：必须写中文，包含业务含义和枚举值说明（如 `COMMENT '状态 0运行中 1已完成 2已终止 3已撤回'`）
