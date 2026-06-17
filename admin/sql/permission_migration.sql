-- 补充缺失的权限记录（部门、数据字典、操作日志、岗位）
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`) VALUES
(11, '部门管理权限', 'system:department:manage', '部门管理权限', 1, NULL),
(12, '部门添加权限', 'system:department:add', '部门添加权限', 2, NULL),
(13, '部门编辑权限', 'system:department:edit', '部门编辑权限', 2, NULL),
(14, '部门删除权限', 'system:department:delete', '部门删除权限', 2, NULL),
(15, '数据字典权限', 'system:dataDictionary:manage', '数据字典权限', 1, NULL),
(16, '操作日志权限', 'system:operationLog:manage', '操作日志权限', 1, NULL),
(17, '岗位管理权限', 'system:post:manage', '岗位管理权限', 1, NULL),
(18, '岗位添加权限', 'system:post:add', '岗位添加权限', 2, NULL),
(19, '岗位编辑权限', 'system:post:edit', '岗位编辑权限', 2, NULL),
(20, '岗位删除权限', 'system:post:delete', '岗位删除权限', 2, NULL);

-- 将新增权限关联给admin角色
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(1, 11),
(1, 12),
(1, 13),
(1, 14),
(1, 15),
(1, 16),
(1, 17),
(1, 18),
(1, 19),
(1, 20);
