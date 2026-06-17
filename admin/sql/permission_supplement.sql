-- =============================================
-- 补充缺失的权限记录
-- 对照后端 Controller @PreAuthorize 中使用的权限编码
-- parent_id: 按钮权限(type=2)挂到对应菜单权限(type=1)下
-- =============================================

-- 1. 用户管理：补充 view/list 按钮
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`, `parent_id`) VALUES
(21, '用户查看权限', 'system:user:view', '用户查看权限', 2, 2, 1),
(22, '用户列表权限', 'system:user:list', '用户列表权限', 2, 2, 1);

-- 2. 角色管理：补充 view/list/assignPermission 按钮
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`, `parent_id`) VALUES
(23, '角色查看权限', 'system:role:view', '角色查看权限', 2, 3, 2),
(24, '角色列表权限', 'system:role:list', '角色列表权限', 2, 3, 2),
(25, '分配权限', 'system:role:assignPermission', '角色分配权限', 2, 3, 2);

-- 3. 权限管理：补充 view/list/add/edit/delete 按钮
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`, `parent_id`) VALUES
(26, '权限查看权限', 'system:permission:view', '权限查看权限', 2, 4, 3),
(27, '权限列表权限', 'system:permission:list', '权限列表权限', 2, 4, 3),
(28, '权限添加权限', 'system:permission:add', '权限添加权限', 2, 4, 3),
(29, '权限编辑权限', 'system:permission:edit', '权限编辑权限', 2, 4, 3),
(30, '权限删除权限', 'system:permission:delete', '权限删除权限', 2, 4, 3);

-- 4. 菜单管理：补充 view/list/add/edit/delete 按钮
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`, `parent_id`) VALUES
(31, '菜单查看权限', 'system:menu:view', '菜单查看权限', 2, 5, 4),
(32, '菜单列表权限', 'system:menu:list', '菜单列表权限', 2, 5, 4),
(33, '菜单添加权限', 'system:menu:add', '菜单添加权限', 2, 5, 4),
(34, '菜单编辑权限', 'system:menu:edit', '菜单编辑权限', 2, 5, 4),
(35, '菜单删除权限', 'system:menu:delete', '菜单删除权限', 2, 5, 4);

-- 5. 部门管理：补充 view/list 按钮
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`, `parent_id`) VALUES
(36, '部门查看权限', 'system:department:view', '部门查看权限', 2, NULL, 11),
(37, '部门列表权限', 'system:department:list', '部门列表权限', 2, NULL, 11);

-- 6. 数据字典：补充按钮级权限
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`, `parent_id`) VALUES
(38, '数据字典列表', 'system:dataDictionary:list', '数据字典列表', 2, NULL, 15),
(39, '数据字典查询', 'system:dataDictionary:query', '数据字典查询', 2, NULL, 15),
(40, '数据字典修改', 'system:dataDictionary:update', '数据字典修改', 2, NULL, 15),
(41, '数据字典删除', 'system:dataDictionary:delete', '数据字典删除', 2, NULL, 15),
(42, '字典项列表', 'system:dataDictionary:item:list', '字典项列表', 2, NULL, 15),
(43, '字典项添加', 'system:dataDictionary:item:add', '字典项添加', 2, NULL, 15),
(44, '字典项修改', 'system:dataDictionary:item:update', '字典项修改', 2, NULL, 15),
(45, '字典项删除', 'system:dataDictionary:item:delete', '字典项删除', 2, NULL, 15);

-- 7. 操作日志：补充 view/list 按钮
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`, `parent_id`) VALUES
(46, '操作日志查看', 'system:operationLog:view', '操作日志查看', 2, NULL, 16),
(47, '操作日志列表', 'system:operationLog:list', '操作日志列表', 2, NULL, 16);

-- 8. 岗位管理：补充 view/list 按钮
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`, `parent_id`) VALUES
(48, '岗位查看权限', 'system:post:view', '岗位查看权限', 2, NULL, 17),
(49, '岗位列表权限', 'system:post:list', '岗位列表权限', 2, NULL, 17);

-- 9. 审批任务：全部权限
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`, `parent_id`) VALUES
(50, '审批任务管理', 'system:approval-task:manage', '审批任务管理', 1, NULL, NULL),
(51, '审批任务查看', 'system:approval-task:view', '审批任务查看', 2, NULL, 50),
(52, '审批任务列表', 'system:approval-task:list', '审批任务列表', 2, NULL, 50),
(53, '审批任务添加', 'system:approval-task:add', '审批任务添加', 2, NULL, 50),
(54, '审批任务编辑', 'system:approval-task:edit', '审批任务编辑', 2, NULL, 50),
(55, '审批任务删除', 'system:approval-task:delete', '审批任务删除', 2, NULL, 50);

-- =============================================
-- 补充已有权限的 parent_id（初始数据的 type=2 权限没有 parent_id）
-- =============================================
UPDATE `sys_permission` SET `parent_id` = 1 WHERE `id` IN (5, 6, 7, 21, 22);    -- 用户按钮 → 用户管理
UPDATE `sys_permission` SET `parent_id` = 2 WHERE `id` IN (8, 9, 10, 23, 24, 25); -- 角色按钮 → 角色管理
UPDATE `sys_permission` SET `parent_id` = 3 WHERE `id` IN (26, 27, 28, 29, 30);   -- 权限按钮 → 权限管理
UPDATE `sys_permission` SET `parent_id` = 4 WHERE `id` IN (31, 32, 33, 34, 35);   -- 菜单按钮 → 菜单管理
UPDATE `sys_permission` SET `parent_id` = 11 WHERE `id` IN (12, 13, 14, 36, 37);  -- 部门按钮 → 部门管理
UPDATE `sys_permission` SET `parent_id` = 15 WHERE `id` IN (38, 39, 40, 41, 42, 43, 44, 45); -- 字典按钮 → 字典管理
UPDATE `sys_permission` SET `parent_id` = 16 WHERE `id` IN (46, 47);               -- 日志按钮 → 日志管理
UPDATE `sys_permission` SET `parent_id` = 17 WHERE `id` IN (18, 19, 20, 48, 49);  -- 岗位按钮 → 岗位管理

-- =============================================
-- 将新增权限全部关联给 admin 角色
-- =============================================
INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(1, 21), (1, 22), (1, 23), (1, 24), (1, 25),
(1, 26), (1, 27), (1, 28), (1, 29), (1, 30),
(1, 31), (1, 32), (1, 33), (1, 34), (1, 35),
(1, 36), (1, 37),
(1, 38), (1, 39), (1, 40), (1, 41), (1, 42), (1, 43), (1, 44), (1, 45),
(1, 46), (1, 47),
(1, 48), (1, 49),
(1, 50), (1, 51), (1, 52), (1, 53), (1, 54), (1, 55);

-- =============================================
-- 补充流程、请假、任务权限
-- =============================================
INSERT INTO `sys_permission` (`id`, `name`, `code`, `description`, `type`, `menu_id`, `parent_id`) VALUES
(56, '流程定义管理', 'process:definition:manage', '流程定义管理', 1, NULL, NULL),
(57, '流程定义新增', 'process:definition:add', '流程定义新增', 2, NULL, 56),
(58, '流程定义查看', 'process:definition:view', '流程定义查看', 2, NULL, 56),
(59, '流程定义列表', 'process:definition:list', '流程定义列表', 2, NULL, 56),
(60, '流程定义编辑', 'process:definition:edit', '流程定义编辑', 2, NULL, 56),
(61, '流程定义删除', 'process:definition:delete', '流程定义删除', 2, NULL, 56),
(62, '流程定义发布', 'process:definition:publish', '流程定义发布', 2, NULL, 56),
(63, '流程实例管理', 'process:instance:manage', '流程实例管理', 1, NULL, NULL),
(64, '流程实例发起', 'process:instance:start', '流程实例发起', 2, NULL, 63),
(65, '我的流程实例', 'process:instance:my', '我的流程实例', 2, NULL, 63),
(66, '流程实例列表', 'process:instance:list', '流程实例列表', 2, NULL, 63),
(67, '流程实例查看', 'process:instance:view', '流程实例查看', 2, NULL, 63),
(68, '流程实例终止', 'process:instance:terminate', '流程实例终止', 2, NULL, 63),
(69, '流程实例撤回', 'process:instance:withdraw', '流程实例撤回', 2, NULL, 63),
(70, '流程图查看', 'process:diagram:view', '流程图查看', 2, NULL, 63),
(71, '任务管理', 'task:manage', '任务管理', 1, NULL, NULL),
(72, '待办任务', 'task:todo', '待办任务', 2, NULL, 71),
(73, '已办任务', 'task:done', '已办任务', 2, NULL, 71),
(74, '可认领任务', 'task:claimable', '可认领任务', 2, NULL, 71),
(75, '任务查看', 'task:view', '任务查看', 2, NULL, 71),
(76, '任务认领', 'task:claim', '任务认领', 2, NULL, 71),
(77, '取消认领', 'task:unclaim', '取消认领', 2, NULL, 71),
(78, '任务审批通过', 'task:complete', '任务审批通过', 2, NULL, 71),
(79, '任务委派', 'task:delegate', '任务委派', 2, NULL, 71),
(80, '任务驳回', 'task:reject', '任务驳回', 2, NULL, 71),
(81, '请假管理', 'leave:manage', '请假管理', 1, NULL, NULL),
(82, '请假申请', 'leave:apply', '请假申请', 2, NULL, 81),
(83, '我的请假列表', 'leave:my:list', '我的请假列表', 2, NULL, 81),
(84, '请假列表', 'leave:list', '请假列表', 2, NULL, 81),
(85, '请假查看', 'leave:view', '请假查看', 2, NULL, 81),
(86, '请假撤回', 'leave:withdraw', '请假撤回', 2, NULL, 81),
(87, '请假审批列表', 'leave:approval:list', '请假审批列表', 2, NULL, 81),
(88, '请假审批查看', 'leave:approval:view', '请假审批查看', 2, NULL, 81),
(89, '操作日志删除', 'system:operationLog:delete', '操作日志删除', 2, NULL, 16),
(90, '操作日志清空', 'system:operationLog:clear', '操作日志清空', 2, NULL, 16);

INSERT INTO `sys_role_permission` (`role_id`, `permission_id`) VALUES
(1, 56), (1, 57), (1, 58), (1, 59), (1, 60), (1, 61), (1, 62),
(1, 63), (1, 64), (1, 65), (1, 66), (1, 67), (1, 68), (1, 69), (1, 70),
(1, 71), (1, 72), (1, 73), (1, 74), (1, 75), (1, 76), (1, 77), (1, 78), (1, 79), (1, 80),
(1, 81), (1, 82), (1, 83), (1, 84), (1, 85), (1, 86), (1, 87), (1, 88),
(1, 89), (1, 90);
