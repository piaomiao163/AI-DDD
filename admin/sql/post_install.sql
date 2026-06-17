-- 创建岗位管理表
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `code` varchar(64) NOT NULL COMMENT '岗位编码',
  `name` varchar(128) NOT NULL COMMENT '岗位名称',
  `sort` int(4) DEFAULT '0' COMMENT '排序',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态（0-禁用，1-启用）',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位表';

-- 插入岗位管理菜单（在系统管理下，菜单管理之后）
INSERT INTO `sys_menu` (`id`, `name`, `path`, `icon`, `parent_id`, `sort`, `status`) VALUES
(6, '岗位管理', '/system/post', 'post', 1, 5, 1);

-- 插入岗位管理权限
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`) VALUES
(11, '岗位管理权限', 'system:post:manage', '岗位管理权限', 1, 6),
(12, '岗位查看权限', 'system:post:view', '岗位查看权限', 2, 6),
(13, '岗位列表权限', 'system:post:list', '岗位列表权限', 2, 6),
(14, '岗位添加权限', 'system:post:add', '岗位添加权限', 2, 6),
(15, '岗位编辑权限', 'system:post:edit', '岗位编辑权限', 2, 6),
(16, '岗位删除权限', 'system:post:delete', '岗位删除权限', 2, 6);

-- 给超级管理员角色分配岗位管理所有权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(1, 11),
(1, 12),
(1, 13),
(1, 14),
(1, 15),
(1, 16);

-- 给超级管理员角色分配岗位管理菜单权限
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES
(1, 6);

-- 给普通用户分配岗位查看和列表权限
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(2, 11),
(2, 12),
(2, 13);

-- 插入初始岗位数据
INSERT INTO `sys_post` (`id`, `code`, `name`, `sort`, `status`, `remark`) VALUES
(1, 'CEO', '首席执行官', 1, 1, '公司最高管理者'),
(2, 'CTO', '首席技术官', 2, 1, '技术部门负责人'),
(3, 'COO', '首席运营官', 3, 1, '运营部门负责人'),
(4, 'CFO', '首席财务官', 4, 1, '财务部门负责人'),
(5, 'HR_DIRECTOR', '人力资源总监', 5, 1, '人力资源部门负责人'),
(6, 'PROJECT_MANAGER', '项目经理', 6, 1, '项目管理'),
(7, 'SENIOR_ENGINEER', '高级工程师', 7, 1, '技术专家'),
(8, 'ENGINEER', '工程师', 8, 1, '开发人员'),
(9, 'ASSISTANT', '助理', 9, 1, '辅助岗位');
