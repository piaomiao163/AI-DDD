-- 岗位管理菜单SQL

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
