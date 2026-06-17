-- =====================================================================
-- schema_full.sql
-- 单一、一致、幂等的全量数据库脚本（AI-DDD 后台管理系统）
-- ---------------------------------------------------------------------
-- 权威来源：MyBatis-Plus DO 实体类
--   admin/infrastructure/src/main/java/com/piaomiao/dal/entity/*.java
-- 关联表来源：Mapper XML 中引用的 JOIN/关系表
--   admin/infrastructure/src/main/resources/mapper/*.xml
-- 列类型/注释/种子数据参考：admin/sql/*.sql、admin/infrastructure/.../sql/*.sql
-- ---------------------------------------------------------------------
-- 说明：
--   * 全部使用 CREATE TABLE IF NOT EXISTS，可重复执行。
--   * 种子数据使用 INSERT IGNORE / ON DUPLICATE KEY UPDATE，可重复执行。
--   * 不包含任何 ACT_* (Activiti) 表，这些表由引擎启动时自动创建。
--   * 不包含任何 DROP 语句。
-- =====================================================================

CREATE DATABASE IF NOT EXISTS ddd DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE ddd;

-- =====================================================================
-- 1. 基础系统表
-- =====================================================================

-- 部门表 (DepartmentDO -> sys_department)
CREATE TABLE IF NOT EXISTS `sys_department` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `name` VARCHAR(50) NOT NULL COMMENT '部门名称',
  `code` VARCHAR(50) NOT NULL COMMENT '部门编码',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父部门ID',
  `level` INT DEFAULT 1 COMMENT '部门层级',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` VARCHAR(100) DEFAULT NULL COMMENT '创建人',
  `update_by` VARCHAR(100) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- 用户表 (UserDO -> sys_user)
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码(BCrypt)',
  `nickname` VARCHAR(50) DEFAULT NULL COMMENT '昵称',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `department_id` BIGINT DEFAULT NULL COMMENT '部门ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` VARCHAR(100) DEFAULT NULL COMMENT '创建人',
  `update_by` VARCHAR(100) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_department_id` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 角色表 (RoleDO -> sys_role)
CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` VARCHAR(100) DEFAULT NULL COMMENT '创建人',
  `update_by` VARCHAR(100) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 菜单表 (MenuDO -> sys_menu)
CREATE TABLE IF NOT EXISTS `sys_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
  `path` VARCHAR(200) DEFAULT NULL COMMENT '菜单路径',
  `icon` VARCHAR(50) DEFAULT NULL COMMENT '菜单图标',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';

-- 权限表 (PermissionDO -> sys_permission) 注意: roleId 为 @TableField(exist=false)，非数据库列
CREATE TABLE IF NOT EXISTS `sys_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `name` VARCHAR(50) NOT NULL COMMENT '权限名称',
  `code` VARCHAR(100) NOT NULL COMMENT '权限编码',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `type` INT NOT NULL COMMENT '权限类型：1-菜单权限，2-按钮权限，3-数据权限',
  `menu_id` BIGINT DEFAULT NULL COMMENT '关联的菜单ID',
  `parent_id` BIGINT DEFAULT NULL COMMENT '父级权限ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_menu_id` (`menu_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- 岗位表 (PostDO -> sys_post)
CREATE TABLE IF NOT EXISTS `sys_post` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `code` VARCHAR(64) NOT NULL COMMENT '岗位编码',
  `name` VARCHAR(128) NOT NULL COMMENT '岗位名称',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `remark` VARCHAR(512) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='岗位表';

-- 数据字典表 (DataDictionaryDO -> sys_data_dictionary)
CREATE TABLE IF NOT EXISTS `sys_data_dictionary` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `name` VARCHAR(100) NOT NULL COMMENT '字典名称',
  `code` VARCHAR(100) NOT NULL COMMENT '字典编码',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '字典描述',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据字典表';

-- 数据字典项表 (DataDictionaryItemDO -> sys_data_dictionary_item)
CREATE TABLE IF NOT EXISTS `sys_data_dictionary_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '字典项ID',
  `dictionary_id` BIGINT NOT NULL COMMENT '字典ID',
  `name` VARCHAR(100) NOT NULL COMMENT '字典项名称',
  `value` VARCHAR(255) NOT NULL COMMENT '字典项值',
  `sort` INT DEFAULT 0 COMMENT '排序',
  `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否',
  PRIMARY KEY (`id`),
  KEY `idx_dictionary_id` (`dictionary_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据字典项表';

-- 操作日志表 (OperationLogDO -> sys_operation_log)
CREATE TABLE IF NOT EXISTS `sys_operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块',
  `type` VARCHAR(50) DEFAULT NULL COMMENT '操作类型',
  `description` VARCHAR(255) DEFAULT NULL COMMENT '操作描述',
  `request_method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法',
  `request_url` VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
  `request_params` TEXT DEFAULT NULL COMMENT '请求参数',
  `response_result` TEXT DEFAULT NULL COMMENT '响应结果',
  `user_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
  `username` VARCHAR(50) DEFAULT NULL COMMENT '操作人用户名',
  `ip_address` VARCHAR(50) DEFAULT NULL COMMENT '操作IP地址',
  `location` VARCHAR(100) DEFAULT NULL COMMENT '操作地点',
  `status` TINYINT DEFAULT 1 COMMENT '操作状态（0失败 1成功）',
  `error_msg` TEXT DEFAULT NULL COMMENT '错误消息',
  `execution_time` BIGINT DEFAULT NULL COMMENT '执行时长（毫秒）',
  `operation_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_module` (`module`),
  KEY `idx_type` (`type`),
  KEY `idx_username` (`username`),
  KEY `idx_operation_time` (`operation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- Demo 表 (DemoDO -> demo)
CREATE TABLE IF NOT EXISTS `demo` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(255) DEFAULT NULL COMMENT '名称',
  `age` INT DEFAULT NULL COMMENT '年龄',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='示例表';

-- =====================================================================
-- 2. 关系/关联表 (由 Mapper XML 引用，无对应 DO 或简单 DO)
-- =====================================================================

-- 用户角色关联表 (RoleMapper.xml: sys_user_role)
CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- 角色权限关联表 (RoleMapper.xml / PermissionMapper.xml: sys_role_permission)
CREATE TABLE IF NOT EXISTS `sys_role_permission` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- 角色菜单关联表 (RoleMapper.xml / MenuMapper.xml: sys_role_menu)
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` BIGINT NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`,`menu_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色菜单关联表';

-- 用户部门关联表 (UserDepartmentDO -> sys_user_department)
CREATE TABLE IF NOT EXISTS `sys_user_department` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `department_id` BIGINT NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_department` (`user_id`,`department_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_department_id` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户部门关联表';

-- =====================================================================
-- 3. 工作流业务表
-- =====================================================================

-- 流程定义表 (ProcessDefinitionDO -> sys_process_definition)
CREATE TABLE IF NOT EXISTS `sys_process_definition` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` VARCHAR(255) NOT NULL COMMENT '流程名称',
  `process_key` VARCHAR(255) NOT NULL COMMENT '流程标识',
  `description` TEXT COMMENT '流程描述',
  `xml` LONGTEXT COMMENT '流程XML',
  `version` INT DEFAULT 1 COMMENT '版本',
  `status` INT DEFAULT 0 COMMENT '状态：0-草稿，1-已发布',
  `category` VARCHAR(255) DEFAULT NULL COMMENT '分类',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `nodes_json` TEXT COMMENT '流程节点JSON',
  `edges_json` TEXT COMMENT '流程边JSON',
  `deployment_id` VARCHAR(64) DEFAULT NULL COMMENT 'Activiti部署ID',
  `act_process_definition_id` VARCHAR(64) DEFAULT NULL COMMENT 'Activiti流程定义ID',
  `create_by` VARCHAR(100) DEFAULT NULL COMMENT '创建人',
  `update_by` VARCHAR(100) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_process_key` (`process_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程定义表';

-- 流程实例表 (ProcessInstanceDO -> sys_process_instance)
CREATE TABLE IF NOT EXISTS `sys_process_instance` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `instance_no` VARCHAR(64) DEFAULT NULL COMMENT '实例编号',
  `title` VARCHAR(255) DEFAULT NULL COMMENT '审批标题',
  `act_process_instance_id` VARCHAR(64) DEFAULT NULL COMMENT 'Activiti流程实例ID',
  `process_definition_id` BIGINT DEFAULT NULL COMMENT '关联流程定义ID(sys_process_definition.id)',
  `process_definition_key` VARCHAR(255) DEFAULT NULL COMMENT '流程定义标识(冗余)',
  `process_name` VARCHAR(255) DEFAULT NULL COMMENT '流程名称(冗余)',
  `business_key` VARCHAR(255) DEFAULT NULL COMMENT '业务单据号',
  `business_type` VARCHAR(64) DEFAULT NULL COMMENT '业务类型',
  `business_id` BIGINT DEFAULT NULL COMMENT '业务单据主键ID',
  `start_user_id` BIGINT DEFAULT NULL COMMENT '发起人ID(sys_user.id)',
  `start_user_name` VARCHAR(100) DEFAULT NULL COMMENT '发起人姓名(冗余)',
  `applicant_dept_id` BIGINT DEFAULT NULL COMMENT '申请人部门ID',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态 0运行中 1已完成 2已终止 3已撤回',
  `current_node_name` VARCHAR(255) DEFAULT NULL COMMENT '当前节点名称(冗余)',
  `current_node_id` VARCHAR(255) DEFAULT NULL COMMENT '当前节点ID',
  `current_assignees` TEXT COMMENT '当前待审批人列表JSON',
  `total_nodes` INT DEFAULT NULL COMMENT '总审批节点数',
  `completed_nodes` INT DEFAULT 0 COMMENT '已完成节点数',
  `priority` TINYINT DEFAULT 0 COMMENT '优先级 0普通 1紧急 2特急',
  `delete_reason` VARCHAR(500) DEFAULT NULL COMMENT '终止/撤回原因',
  `variables_json` TEXT COMMENT '流程变量JSON(快照)',
  `start_time` DATETIME DEFAULT NULL COMMENT '启动时间',
  `end_time` DATETIME DEFAULT NULL COMMENT '结束时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` VARCHAR(100) DEFAULT NULL COMMENT '创建人',
  `update_by` VARCHAR(100) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_instance_no` (`instance_no`),
  KEY `idx_process_definition_id` (`process_definition_id`),
  KEY `idx_business_key` (`business_type`,`business_key`),
  KEY `idx_start_user_id` (`start_user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='流程实例表';

-- 审批任务表 (ApprovalTaskDO -> biz_approval_task)
CREATE TABLE IF NOT EXISTS `biz_approval_task` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `instance_id` BIGINT NOT NULL COMMENT '关联实例ID',
  `process_definition_id` BIGINT DEFAULT NULL COMMENT '流程定义ID(冗余)',
  `node_id` VARCHAR(64) NOT NULL COMMENT '节点ID',
  `node_name` VARCHAR(200) DEFAULT NULL COMMENT '节点名称',
  `node_type` VARCHAR(32) NOT NULL COMMENT '节点类型: approve-审批 cc-抄送 notify-通知 condition-条件分支',
  `node_index` INT DEFAULT NULL COMMENT '节点顺序(用于展示)',
  `assignee_id` BIGINT DEFAULT NULL COMMENT '审批人ID',
  `assignee_name` VARCHAR(100) DEFAULT NULL COMMENT '审批人姓名',
  `assignee_type` VARCHAR(32) DEFAULT NULL COMMENT '审批人类型: user-指定人 role-角色 supervisor-上级 dept_leader-部门负责人',
  `candidate_users` TEXT COMMENT '候选审批人列表JSON',
  `approve_mode` VARCHAR(32) DEFAULT NULL COMMENT '审批模式: single-单签 all-会签 any-或签',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-待审批 1-已通过 2-已驳回 3-已转交 4-已超时 5-已撤销',
  `opinion` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
  `attachments` TEXT COMMENT '附件列表JSON',
  `signature` VARCHAR(500) DEFAULT NULL COMMENT '电子签名',
  `receive_time` DATETIME DEFAULT NULL COMMENT '收到任务时间',
  `deadline` DATETIME DEFAULT NULL COMMENT '截止时间',
  `remind_time` DATETIME DEFAULT NULL COMMENT '最后一次提醒时间',
  `complete_time` DATETIME DEFAULT NULL COMMENT '完成时间',
  `remark` VARCHAR(500) DEFAULT NULL COMMENT '备注',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` VARCHAR(100) DEFAULT NULL COMMENT '创建人',
  `update_by` VARCHAR(100) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否',
  PRIMARY KEY (`id`),
  KEY `idx_instance_id` (`instance_id`),
  KEY `idx_assignee_id` (`assignee_id`),
  KEY `idx_status` (`status`),
  KEY `idx_node_id` (`node_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='审批任务表';

-- 请假单表 (LeaveDO -> biz_leave)
CREATE TABLE IF NOT EXISTS `biz_leave` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` VARCHAR(255) NOT NULL COMMENT '请假标题',
  `leave_type` TINYINT NOT NULL COMMENT '请假类型 1年假 2事假 3病假 4婚假 5产假 6丧假 7其他',
  `start_date` DATE NOT NULL COMMENT '开始日期',
  `end_date` DATE NOT NULL COMMENT '结束日期',
  `days` DECIMAL(5,1) NOT NULL COMMENT '请假天数(支持0.5天)',
  `reason` VARCHAR(500) NOT NULL COMMENT '请假原因',
  `applicant_id` BIGINT NOT NULL COMMENT '申请人ID',
  `applicant_name` VARCHAR(100) DEFAULT NULL COMMENT '申请人姓名(冗余)',
  `dept_name` VARCHAR(100) DEFAULT NULL COMMENT '申请人部门(冗余)',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态 0待审批 1审批中 2已通过 3已驳回 4已撤回',
  `process_instance_id` BIGINT DEFAULT NULL COMMENT '关联流程实例ID(sys_process_instance.id)',
  `approve_comment` VARCHAR(500) DEFAULT NULL COMMENT '审批意见',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` VARCHAR(100) DEFAULT NULL COMMENT '创建人',
  `update_by` VARCHAR(100) DEFAULT NULL COMMENT '更新人',
  `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否',
  PRIMARY KEY (`id`),
  KEY `idx_applicant_id` (`applicant_id`),
  KEY `idx_status` (`status`),
  KEY `idx_leave_type` (`leave_type`),
  KEY `idx_start_date` (`start_date`),
  KEY `idx_process_instance_id` (`process_instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='请假单表';

-- =====================================================================
-- 4. 参考/种子数据 (幂等)
--    与 permission.sql 一致的最小可用权限模型 + 岗位数据
-- =====================================================================

-- 部门
INSERT IGNORE INTO `sys_department` (`id`, `name`, `code`, `parent_id`, `level`, `sort`, `status`) VALUES
(1, '总公司', 'company', 0, 1, 1, 1),
(2, '技术部', 'tech', 1, 2, 1, 1),
(3, '市场部', 'market', 1, 2, 2, 1),
(4, '财务部', 'finance', 1, 2, 3, 1);

-- 角色
INSERT IGNORE INTO `sys_role` (`id`, `name`, `code`, `description`, `status`) VALUES
(1, '超级管理员', 'admin', '拥有系统所有权限', 1),
(2, '普通用户', 'user', '拥有基本操作权限', 1);

-- 菜单
INSERT IGNORE INTO `sys_menu` (`id`, `name`, `path`, `icon`, `parent_id`, `sort`, `status`) VALUES
(1, '系统管理', '/system', 'system', 0, 1, 1),
(2, '用户管理', '/system/user', 'user', 1, 1, 1),
(3, '角色管理', '/system/role', 'role', 1, 2, 1),
(4, '权限管理', '/system/permission', 'permission', 1, 3, 1),
(5, '菜单管理', '/system/menu', 'menu', 1, 4, 1),
(6, '岗位管理', '/system/post', 'post', 1, 5, 1);

-- 权限 (基础菜单/按钮权限)
INSERT IGNORE INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`) VALUES
(1, '用户管理权限', 'system:user:manage', '用户管理权限', 1, 2),
(2, '角色管理权限', 'system:role:manage', '角色管理权限', 1, 3),
(3, '权限管理权限', 'system:permission:manage', '权限管理权限', 1, 4),
(4, '菜单管理权限', 'system:menu:manage', '菜单管理权限', 1, 5),
(5, '用户添加权限', 'system:user:add', '用户添加权限', 2, 2),
(6, '用户编辑权限', 'system:user:edit', '用户编辑权限', 2, 2),
(7, '用户删除权限', 'system:user:delete', '用户删除权限', 2, 2),
(8, '角色添加权限', 'system:role:add', '角色添加权限', 2, 3),
(9, '角色编辑权限', 'system:role:edit', '角色编辑权限', 2, 3),
(10, '角色删除权限', 'system:role:delete', '角色删除权限', 2, 3),
(11, '岗位管理权限', 'system:post:manage', '岗位管理权限', 1, 6),
(12, '岗位查看权限', 'system:post:view', '岗位查看权限', 2, 6),
(13, '岗位列表权限', 'system:post:list', '岗位列表权限', 2, 6),
(14, '岗位添加权限', 'system:post:add', '岗位添加权限', 2, 6),
(15, '岗位编辑权限', 'system:post:edit', '岗位编辑权限', 2, 6),
(16, '岗位删除权限', 'system:post:delete', '岗位删除权限', 2, 6);

-- 完整权限码目录 (与所有 @PreAuthorize hasAuthority('...') 注解保持一致)
-- 超级管理员(ROLE_admin)在 CustomUserDetailsService 中被授予 sys_permission 表的全部 code,
-- 因此此表必须包含控制器引用的每个权限码, 否则对应页面/接口返回 403。
-- 按 code 去重(uk_code), 可安全重复执行。
INSERT IGNORE INTO `sys_permission` (`name`, `code`, `description`, `type`) VALUES
('用户查看', 'system:user:view', '用户查看', 2),
('用户列表', 'system:user:list', '用户列表', 2),
('用户按角色查询', 'system:user:byRole', '用户按角色查询', 2),
('角色查看', 'system:role:view', '角色查看', 2),
('角色列表', 'system:role:list', '角色列表', 2),
('角色分配权限', 'system:role:assignPermission', '角色分配权限', 2),
('权限查看', 'system:permission:view', '权限查看', 2),
('权限列表', 'system:permission:list', '权限列表', 2),
('权限添加', 'system:permission:add', '权限添加', 2),
('权限编辑', 'system:permission:edit', '权限编辑', 2),
('权限删除', 'system:permission:delete', '权限删除', 2),
('菜单查看', 'system:menu:view', '菜单查看', 2),
('菜单列表', 'system:menu:list', '菜单列表', 2),
('菜单根节点', 'system:menu:root', '菜单根节点', 2),
('菜单子节点', 'system:menu:children', '菜单子节点', 2),
('菜单添加', 'system:menu:add', '菜单添加', 2),
('菜单编辑', 'system:menu:edit', '菜单编辑', 2),
('菜单删除', 'system:menu:delete', '菜单删除', 2),
('菜单树', 'system:menu:tree', '菜单树', 2),
('部门查看', 'system:department:view', '部门查看', 2),
('部门列表', 'system:department:list', '部门列表', 2),
('部门根节点', 'system:department:root', '部门根节点', 2),
('部门子节点', 'system:department:children', '部门子节点', 2),
('部门添加', 'system:department:add', '部门添加', 2),
('部门编辑', 'system:department:edit', '部门编辑', 2),
('部门删除', 'system:department:delete', '部门删除', 2),
('部门用户', 'system:department:users', '部门用户', 2),
('数据字典列表', 'system:dataDictionary:list', '数据字典列表', 2),
('数据字典查询', 'system:dataDictionary:query', '数据字典查询', 2),
('数据字典添加', 'system:dataDictionary:add', '数据字典添加', 2),
('数据字典更新', 'system:dataDictionary:update', '数据字典更新', 2),
('数据字典删除', 'system:dataDictionary:delete', '数据字典删除', 2),
('字典项列表', 'system:dataDictionary:item:list', '字典项列表', 2),
('字典项添加', 'system:dataDictionary:item:add', '字典项添加', 2),
('字典项更新', 'system:dataDictionary:item:update', '字典项更新', 2),
('字典项删除', 'system:dataDictionary:item:delete', '字典项删除', 2),
('审批任务查看', 'system:approval-task:view', '审批任务查看', 2),
('审批任务列表', 'system:approval-task:list', '审批任务列表', 2),
('审批任务添加', 'system:approval-task:add', '审批任务添加', 2),
('审批任务编辑', 'system:approval-task:edit', '审批任务编辑', 2),
('审批任务删除', 'system:approval-task:delete', '审批任务删除', 2),
('操作日志查看', 'system:operationLog:view', '操作日志查看', 2),
('操作日志列表', 'system:operationLog:list', '操作日志列表', 2),
('操作日志删除', 'system:operationLog:delete', '操作日志删除', 2),
('操作日志清空', 'system:operationLog:clear', '操作日志清空', 2),
('待办任务', 'task:todo', '待办任务', 2),
('已办任务', 'task:done', '已办任务', 2),
('可认领任务', 'task:claimable', '可认领任务', 2),
('任务查看', 'task:view', '任务查看', 2),
('任务认领', 'task:claim', '任务认领', 2),
('任务取消认领', 'task:unclaim', '任务取消认领', 2),
('任务完成', 'task:complete', '任务完成', 2),
('任务委派', 'task:delegate', '任务委派', 2),
('任务驳回', 'task:reject', '任务驳回', 2),
('请假申请', 'leave:apply', '请假申请', 2),
('我的请假', 'leave:my:list', '我的请假', 2),
('请假列表', 'leave:list', '请假列表', 2),
('请假查看', 'leave:view', '请假查看', 2),
('请假撤回', 'leave:withdraw', '请假撤回', 2),
('请假审批列表', 'leave:approval:list', '请假审批列表', 2),
('请假审批查看', 'leave:approval:view', '请假审批查看', 2),
('流程实例启动', 'process:instance:start', '流程实例启动', 2),
('我的流程实例', 'process:instance:my', '我的流程实例', 2),
('流程实例列表', 'process:instance:list', '流程实例列表', 2),
('流程实例查看', 'process:instance:view', '流程实例查看', 2),
('流程实例终止', 'process:instance:terminate', '流程实例终止', 2),
('流程实例撤回', 'process:instance:withdraw', '流程实例撤回', 2),
('流程图查看', 'process:diagram:view', '流程图查看', 2),
('流程定义添加', 'process:definition:add', '流程定义添加', 2),
('流程定义查看', 'process:definition:view', '流程定义查看', 2),
('流程定义列表', 'process:definition:list', '流程定义列表', 2),
('流程定义编辑', 'process:definition:edit', '流程定义编辑', 2),
('流程定义删除', 'process:definition:delete', '流程定义删除', 2),
('流程定义发布', 'process:definition:publish', '流程定义发布', 2);

-- 用户 (密码为明文 123456 的 BCrypt 哈希；ON DUPLICATE 保证重跑时修正密码)
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `email`, `phone`, `status`, `department_id`) VALUES
(1, 'admin', '$2a$10$tp2K0izQ1VxdBpRDOQcmx.o3/lnb9AsYUT9TlzUCeHVF4bZ9G4mZq', '超级管理员', 'admin@example.com', '13800138000', 1, 1),
(2, 'user', '$2a$10$tp2K0izQ1VxdBpRDOQcmx.o3/lnb9AsYUT9TlzUCeHVF4bZ9G4mZq', '普通用户', 'user@example.com', '13800138001', 1, 2)
ON DUPLICATE KEY UPDATE `password` = VALUES(`password`);

-- 用户角色关联
INSERT IGNORE INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

-- 角色权限关联 (admin 拥有全部, user 拥有基础)
-- 超级管理员(role_id=1)动态授予全部权限, 避免遗漏新增权限码
INSERT IGNORE INTO `sys_role_permission` (`role_id`, `permission_id`)
SELECT 1, `id` FROM `sys_permission`;
INSERT IGNORE INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(2, 1), (2, 5), (2, 6), (2, 11), (2, 12), (2, 13);

-- 角色菜单关联
INSERT IGNORE INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6),
(2, 1), (2, 2);

-- 岗位
INSERT IGNORE INTO `sys_post` (`id`, `code`, `name`, `sort`, `status`, `remark`) VALUES
(1, 'CEO', '首席执行官', 1, 1, '公司最高管理者'),
(2, 'CTO', '首席技术官', 2, 1, '技术部门负责人'),
(3, 'COO', '首席运营官', 3, 1, '运营部门负责人'),
(4, 'CFO', '首席财务官', 4, 1, '财务部门负责人'),
(5, 'HR_DIRECTOR', '人力资源总监', 5, 1, '人力资源部门负责人'),
(6, 'PROJECT_MANAGER', '项目经理', 6, 1, '项目管理'),
(7, 'SENIOR_ENGINEER', '高级工程师', 7, 1, '技术专家'),
(8, 'ENGINEER', '工程师', 8, 1, '开发人员'),
(9, 'ASSISTANT', '助理', 9, 1, '辅助岗位');
