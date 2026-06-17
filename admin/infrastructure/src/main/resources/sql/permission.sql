-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS ddd DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用ddd数据库
USE ddd;

-- 创建部门表
CREATE TABLE `sys_department` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `name` VARCHAR(50) NOT NULL COMMENT '部门名称',
  `code` VARCHAR(50) NOT NULL COMMENT '部门编码',
  `parent_id` BIGINT(20) DEFAULT '0' COMMENT '父部门ID',
  `level` INT(11) DEFAULT '1' COMMENT '部门层级',
  `sort` INT(11) DEFAULT '0' COMMENT '排序',
  `status` INT(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 创建用户表
CREATE TABLE `sys_user` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `password` VARCHAR(100) NOT NULL COMMENT '密码',
  `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `status` INT(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `department_id` BIGINT(20) DEFAULT NULL COMMENT '部门ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_department_id` (`department_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 创建角色表
CREATE TABLE `sys_role` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `name` VARCHAR(50) NOT NULL COMMENT '角色名称',
  `code` VARCHAR(50) NOT NULL COMMENT '角色编码',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `status` INT(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 创建权限表
CREATE TABLE `sys_permission` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `name` VARCHAR(50) NOT NULL COMMENT '权限名称',
  `code` VARCHAR(50) NOT NULL COMMENT '权限编码',
  `description` VARCHAR(200) DEFAULT NULL COMMENT '描述',
  `type` INT(1) NOT NULL COMMENT '权限类型：1-菜单权限，2-按钮权限，3-数据权限',
  `menu_id` BIGINT(20) DEFAULT NULL COMMENT '关联的菜单ID',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 创建菜单表
CREATE TABLE `sys_menu` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
  `path` VARCHAR(200) DEFAULT NULL COMMENT '菜单路径',
  `icon` VARCHAR(50) DEFAULT NULL COMMENT '菜单图标',
  `parent_id` BIGINT(20) DEFAULT '0' COMMENT '父菜单ID',
  `sort` INT(11) DEFAULT '0' COMMENT '排序',
  `status` INT(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 创建用户角色关联表
CREATE TABLE `sys_user_role` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT(20) NOT NULL COMMENT '用户ID',
  `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`,`role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 创建角色权限关联表
CREATE TABLE `sys_role_permission` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
  `permission_id` BIGINT(20) NOT NULL COMMENT '权限ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 创建角色菜单关联表
CREATE TABLE `sys_role_menu` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` BIGINT(20) NOT NULL COMMENT '角色ID',
  `menu_id` BIGINT(20) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_menu` (`role_id`,`menu_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 插入默认数据
-- 插入默认角色
INSERT INTO `sys_role` (`id`, `name`, `code`, `description`, `status`) VALUES
(1, '超级管理员', 'admin', '拥有系统所有权限', 1),
(2, '普通用户', 'user', '拥有基本操作权限', 1);

-- 插入默认菜单
INSERT INTO `sys_menu` (`id`, `name`, `path`, `icon`, `parent_id`, `sort`, `status`) VALUES
(1, '系统管理', '/system', 'system', 0, 1, 1),
(2, '用户管理', '/system/user', 'user', 1, 1, 1),
(3, '角色管理', '/system/role', 'role', 1, 2, 1),
(4, '权限管理', '/system/permission', 'permission', 1, 3, 1),
(5, '菜单管理', '/system/menu', 'menu', 1, 4, 1);

-- 插入默认权限
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`) VALUES
(1, '用户管理权限', 'system:user:manage', '用户管理权限', 1, 2),
(2, '角色管理权限', 'system:role:manage', '角色管理权限', 1, 3),
(3, '权限管理权限', 'system:permission:manage', '权限管理权限', 1, 4),
(4, '菜单管理权限', 'system:menu:manage', '菜单管理权限', 1, 5),
(5, '用户添加权限', 'system:user:add', '用户添加权限', 2, 2),
(6, '用户编辑权限', 'system:user:edit', '用户编辑权限', 2, 2),
(7, '用户删除权限', 'system:user:delete', '用户删除权限', 2, 2),
(8, '角色添加权限', 'system:role:add', '角色添加权限', 2, 3),
(9, '角色编辑权限', 'system:role:edit', '角色编辑权限', 2, 3),
(10, '角色删除权限', 'system:role:delete', '角色删除权限', 2, 3);

-- 插入默认部门
INSERT INTO `sys_department` (`id`, `name`, `code`, `parent_id`, `level`, `sort`, `status`) VALUES
(1, '总公司', 'company', 0, 1, 1, 1),
(2, '技术部', 'tech', 1, 2, 1, 1),
(3, '市场部', 'market', 1, 2, 2, 1),
(4, '财务部', 'finance', 1, 2, 3, 1);

-- 插入默认用户
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `email`, `phone`, `status`, `department_id`) VALUES
(1, 'admin', '123456', '超级管理员', 'admin@example.com', '13800138000', 1, 1),
(2, 'user', '123456', '普通用户', 'user@example.com', '13800138001', 1, 2);

-- 插入用户角色关联
INSERT INTO `sys_user_role` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

-- 插入角色权限关联
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(1, 7),
(1, 8),
(1, 9),
(1, 10),
(2, 1),
(2, 5),
(2, 6);

-- 插入角色菜单关联
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(2, 1),
(2, 2);