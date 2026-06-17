# DDD 项目技术文档

## 1. 项目概述

本项目是一个基于 **领域驱动设计（DDD）** 的 Java 后端项目，采用分层架构设计，实现了一个完整的业务系统。项目使用 Spring Boot、MyBatis-Plus 等技术栈，支持事务管理、分布式锁、领域事件、数据权限控制等高级特性。

### 1.1 主要功能

- **用户管理**：用户注册、登录、查询、修改、删除
- **部门管理**：部门的创建、查询、修改、删除
- **角色管理**：角色的创建、查询、修改、删除
- **权限管理**：权限的创建、查询、修改、删除
- **数据权限**：基于部门、用户、角色的细粒度数据权限控制
- **JWT 认证**：基于 JWT 的无状态认证

## 2. 项目结构

项目采用典型的 DDD 分层架构，包含以下模块：

```
DDD/
├── application/        # 应用层
├── common/             # 公共模块
├── domain/             # 领域层
├── infrastructure/     # 基础设施层
├── interface/          # 接口层
└── pom.xml             # 项目依赖配置
```

### 2.1 模块说明

| 模块 | 职责 | 主要内容 |
|------|------|----------|
| **application** | 应用层，处理用户请求和业务流程 | 命令处理、DTO 对象 |
| **common** | 公共模块，提供通用功能 | 响应对象、REST 模板、工具类、数据权限注解 |
| **domain** | 领域层，核心业务逻辑 | 领域模型、领域服务、仓储接口、领域事件、数据权限处理 |
| **infrastructure** | 基础设施层，技术实现 | 数据访问、仓储实现、MyBatis 映射 |
| **interface** | 接口层，与外部交互 | 控制器、配置、应用入口、拦截器 |

## 3. 核心功能

### 3.1 领域驱动设计（DDD）

- **领域模型**：定义核心业务实体，如 `UserModel`、`DepartmentModel`、`RoleModel`
- **领域服务**：实现业务逻辑，如 `UserService`、`DepartmentService`、`RoleService`
- **仓储模式**：抽象数据访问，如 `UserRepository`、`DepartmentRepository`、`RoleRepository`
- **领域事件**：实现业务事件发布与处理

### 3.2 数据权限控制

- **基于注解的权限控制**：使用 `@DataPermission` 注解实现细粒度数据权限
- **多种权限类型**：支持部门权限、用户权限、全部数据权限、本人数据权限、自定义权限
- **递归部门查询**：支持包含子部门的数据权限
- **线程安全**：使用 ThreadLocal 存储权限信息
- **灵活配置**：支持通过注解参数灵活配置权限规则

### 3.3 认证与授权

- **JWT 认证**：基于 JWT 的无状态认证
- **密码加密**：使用 Spring Security 的 PasswordEncoder 对密码进行加密
- **登录验证**：验证用户名和密码，生成 JWT token
- **注册功能**：支持新用户注册

### 3.4 事务管理

- 支持声明式事务
- 支持手动事务管理
- 事务与业务逻辑分离

### 3.5 分布式锁

- 基于 Redis 实现分布式锁
- 支持锁超时机制
- 自动降级为本地锁（当 Redis 不可用时）

### 3.6 REST 模板

- 统一的请求处理模板
- 支持参数校验
- 支持事务和分布式锁
- 统一的异常处理

### 3.7 数据访问

- 使用 MyBatis-Plus 实现 ORM
- 支持主键自增
- 支持复杂查询
- 支持数据权限过滤

## 4. 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.3.12.RELEASE | 应用框架 |
| MyBatis-Plus | 3.1.2 | ORM 框架 |
| Spring Security | 5.3.13.RELEASE | 安全框架 |
| Redis | - | 缓存、分布式锁 |
| MySQL | 8.0.19 | 数据库 |
| Lombok | 1.18.10 | 代码简化 |
| Jackson | 2.12.7 | JSON 处理 |
| JWT | 0.9.1 | 认证 |

## 5. 核心类与接口

### 5.1 领域层

- **UserModel**：用户领域实体，包含用户属性和行为
- **DepartmentModel**：部门领域实体，包含部门属性和行为
- **RoleModel**：角色领域实体，包含角色属性和行为
- **UserService**：用户领域服务，实现用户相关业务逻辑
- **DepartmentService**：部门领域服务，实现部门相关业务逻辑
- **RoleService**：角色领域服务，实现角色相关业务逻辑
- **UserRepository**：用户仓储接口，定义用户数据访问方法
- **DepartmentRepository**：部门仓储接口，定义部门数据访问方法
- **RoleRepository**：角色仓储接口，定义角色数据访问方法

### 5.2 基础设施层

- **UserDO**：用户数据模型，对应数据库表
- **DepartmentDO**：部门数据模型，对应数据库表
- **RoleDO**：角色数据模型，对应数据库表
- **UserMapper**：用户 MyBatis 映射器
- **DepartmentMapper**：部门 MyBatis 映射器
- **RoleMapper**：角色 MyBatis 映射器
- **UserRepositoryImpl**：用户仓储实现，实现用户数据访问逻辑，支持数据权限过滤
- **DepartmentRepositoryImpl**：部门仓储实现，实现部门数据访问逻辑
- **RoleRepositoryImpl**：角色仓储实现，实现角色数据访问逻辑

### 5.3 应用层

- **UserCommand**：用户命令接口
- **UserCommandImpl**：用户命令实现，处理用户相关业务流程
- **UserDTO**：用户数据传输对象，用于层间数据传递

### 5.4 接口层

- **LoginController**：登录控制器，处理用户登录和注册请求
- **UserController**：用户控制器，处理用户相关 HTTP 请求
- **DepartmentController**：部门控制器，处理部门相关 HTTP 请求
- **RoleController**：角色控制器，处理角色相关 HTTP 请求
- **DataPermissionInterceptor**：数据权限拦截器，实现数据权限控制
- **WebMvcConfig**：Web 配置，注册拦截器
- **App**：应用入口，启动 Spring Boot 应用

### 5.5 公共模块

- **Response**：统一响应对象
- **TemplateRest**：REST 模板，处理请求和响应
- **CallbackRest**：回调接口，定义业务逻辑执行方法
- **DataPermission**：数据权限注解，用于配置数据权限规则
- **DataPermissionType**：数据权限类型枚举，定义不同的权限类型
- **DataPermissionHandler**：数据权限处理器接口，定义数据权限处理方法
- **DataPermissionContext**：数据权限上下文，存储当前用户的权限信息
- **JwtUtil**：JWT 工具类，生成和解析 JWT token

## 6. 快速开始

### 6.1 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis（可选，用于分布式锁）

### 6.2 数据库配置

1. 数据库会自动创建（如果不存在）
2. 表结构会自动初始化
3. 初始数据会自动导入

### 6.3 配置文件

修改 `interface/src/main/resources/application.yml`：

```yaml
server:
  port: 8086
  tomcat:
    uri-encoding: UTF-8
spring:
  application:
    name: DDD-Application
  datasource:
    url: jdbc:mysql://localhost:3306/ddd?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&characterSetResults=utf8mb4&connectionCollation=utf8mb4_unicode_ci
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
    type: com.alibaba.druid.pool.DruidDataSource
    schema:
      - classpath:schema.sql
    data:
      - classpath:data.sql
    initialization-mode: always
    continue-on-error: true
  redis:
    host: localhost
    port: 6379
    password:
    timeout: 60000
    database: 0
  thymeleaf:
    prefix: classpath:/templates/
    suffix: .html
    mode: HTML
    encoding: UTF-8
    cache: false
  http:
    encoding:
      charset: UTF-8
      enabled: true
      force: true
  logging:
    level:
      com.piaomiao: debug
      org.springframework: info
      org.springframework.security: debug
mybatis-plus:
  global-config:
    db-config:
      id-type: auto  # 确保使用自增策略
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
    map-underscore-to-camel-case: true
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.piaomiao.dal.entity
```

### 6.4 构建与运行

1. **构建项目**：
   ```bash
   mvn clean install
   ```

2. **启动应用**：
   ```bash
   mvn spring-boot:run -pl interface
   ```

3. **访问接口**：
   - POST: `http://localhost:8086/api/login` - 用户登录
   - POST: `http://localhost:8086/api/register` - 用户注册
   - GET: `http://localhost:8086/api/users` - 获取用户列表（需要数据权限）

## 7. 使用示例

### 7.1 认证相关

#### 7.1.1 用户注册

**请求**：
```http
POST /api/register
Content-Type: application/json

{
  "username": "test",
  "password": "123456",
  "nickname": "测试用户",
  "email": "test@example.com",
  "phone": "13800138000",
  "departmentId": 1
}
```

**响应**：
```json
{
  "success": true,
  "message": "注册成功",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": 1,
    "username": "test",
    "nickname": "测试用户",
    "email": "test@example.com",
    "phone": "13800138000",
    "departmentId": 1,
    "status": 1
  }
}
```

#### 7.1.2 用户登录

**请求**：
```http
POST /api/login
Content-Type: application/json

{
  "username": "test",
  "password": "123456"
}
```

**响应**：
```json
{
  "success": true,
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": 1,
    "username": "test",
    "nickname": "测试用户",
    "email": "test@example.com",
    "phone": "13800138000",
    "departmentId": 1,
    "status": 1
  }
}
```

### 7.2 数据权限使用

#### 7.2.1 部门权限（包含子部门）

```java
@DataPermission(value = DataPermissionType.DEPARTMENT, includeChildren = true)
public List<UserModel> findAll() {
    return userRepository.findAll();
}
```

#### 7.2.2 全部数据权限

```java
@DataPermission(value = DataPermissionType.ALL)
public List<UserModel> findAll() {
    return userRepository.findAll();
}
```

#### 7.2.3 本人数据权限

```java
@DataPermission(value = DataPermissionType.SELF)
public List<UserModel> findAll() {
    return userRepository.findAll();
}
```

#### 7.2.4 自定义权限

```java
@DataPermission(value = DataPermissionType.CUSTOM, expression = "department:1,2,3")
public List<UserModel> findAll() {
    return userRepository.findAll();
}
```

### 7.3 基本操作

#### 7.3.1 调用无事务的请求

```java
@Autowired
private TemplateRest templateRest;

public Response<String> doWithoutTransaction() {
    return templateRest.request(new CallbackRest<String>() {
        @Override
        public void check() {
            // 参数校验
        }
        
        @Override
        public String execute() {
            // 业务逻辑
            return "操作成功";
        }
    });
}
```

#### 7.3.2 调用带事务的请求

```java
@Autowired
private TemplateRest templateRest;

public Response<String> doWithTransaction() {
    return templateRest.request(new CallbackRest<String>() {
        @Override
        public void check() {
            // 参数校验
        }
        
        @Override
        public String execute() {
            // 业务逻辑
            return "操作成功";
        }
    }, true); // 第二个参数为 true，表示使用事务
}
```

#### 7.3.3 调用带分布式锁的请求

```java
@Autowired
private TemplateRest templateRest;

public Response<String> doWithLock(String key) {
    String lockKey = "demo:lock:" + key;
    long lockTimeout = 30; // 锁超时时间（秒）
    
    return templateRest.requestWithLock(new CallbackRest<String>() {
        @Override
        public void check() {
            // 参数校验
        }
        
        @Override
        public String execute() {
            // 业务逻辑
            return "操作成功";
        }
    }, lockKey, lockTimeout);
}
```

## 8. 项目扩展

### 8.1 添加新的领域实体

1. 在 `domain/model` 中创建领域实体类
2. 在 `domain/repository` 中创建仓储接口
3. 在 `infrastructure/dal/entity` 中创建数据模型
4. 在 `infrastructure/dal/mapper` 中创建映射器
5. 在 `infrastructure/repository/impl` 中实现仓储接口，添加数据权限过滤逻辑
6. 在 `domain/service` 中实现业务逻辑
7. 在 `application/command` 中添加命令处理
8. 在 `interface/web` 中添加控制器，并使用 `@DataPermission` 注解配置权限

### 8.2 添加新的数据权限类型

1. 在 `common/enums/DataPermissionType.java` 中添加新的权限类型
2. 在 `domain/permission/impl/DefaultDataPermissionHandler.java` 中实现新的权限处理逻辑
3. 在控制器方法上使用新的权限类型

### 8.3 配置 Redis

在 `application.yml` 中添加 Redis 配置：

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password:
    database: 0
```

## 9. 常见问题与解决方案

### 9.1 依赖冲突

**问题**：依赖版本冲突导致编译失败  
**解决方案**：在 `pom.xml` 中统一管理依赖版本

### 9.2 数据库连接失败

**问题**：无法连接到数据库  
**解决方案**：检查数据库配置和网络连接

### 9.3 数据乱码

**问题**：数据库存储的中文数据显示为乱码  
**解决方案**：确保数据库和表使用 `utf8mb4` 字符集，数据库连接字符串添加编码参数

### 9.4 数据权限不生效

**问题**：数据权限控制不生效  
**解决方案**：
- 确保在控制器方法上添加了 `@DataPermission` 注解
- 确保仓储实现类中添加了数据权限过滤逻辑
- 检查用户是否有正确的部门信息

### 9.5 分布式锁失效

**问题**：分布式锁不生效  
**解决方案**：确保 Redis 配置正确，检查锁键是否唯一

### 9.6 事务不回滚

**问题**：异常发生时事务不回滚  
**解决方案**：确保异常被正确抛出，检查事务配置

### 9.7 JWT 认证失败

**问题**：JWT token 认证失败  
**解决方案**：检查 token 是否过期，确保密钥一致

## 10. 总结

本项目是一个基于 DDD 设计的 Spring Boot 应用，具有以下特点：

- **清晰的分层架构**：遵循 DDD 设计原则，各层职责明确
- **强大的功能支持**：事务管理、分布式锁、领域事件、数据权限控制等
- **灵活的扩展性**：易于添加新的业务功能
- **统一的请求处理**：使用 REST 模板统一处理请求和响应
- **完善的异常处理**：统一捕获和处理异常
- **安全的认证机制**：基于 JWT 的无状态认证
- **细粒度的数据权限**：基于部门、用户、角色的灵活数据权限控制

通过本项目，您可以了解如何在实际项目中应用 DDD 设计原则，构建一个可维护、可扩展的业务系统，同时实现细粒度的数据权限控制。

---

**注意**：本文档基于项目的当前状态生成，随着项目的发展，某些细节可能会发生变化。
