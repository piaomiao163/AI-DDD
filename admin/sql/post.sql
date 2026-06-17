-- 岗位管理表
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

-- 初始数据
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
