CREATE TABLE IF NOT EXISTS `sys_process_definition` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(255) NOT NULL COMMENT '流程名称',
  `process_key` varchar(255) NOT NULL COMMENT '流程标识',
  `description` text COMMENT '流程描述',
  `xml` text COMMENT '流程XML',
  `version` int(11) DEFAULT '1' COMMENT '版本',
  `status` int(11) DEFAULT '0' COMMENT '状态 0草稿 1已发布',
  `category` varchar(255) DEFAULT NULL COMMENT '分类',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `nodes_json` text COMMENT '流程节点JSON',
  `edges_json` text COMMENT '流程边JSON',
  `deployment_id` varchar(64) DEFAULT NULL COMMENT 'Activiti部署ID',
  `act_process_definition_id` varchar(64) DEFAULT NULL COMMENT 'Activiti流程定义ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_process_key` (`process_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='流程定义';
