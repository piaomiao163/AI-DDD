-- sys_process_definition 迁移脚本
-- 1. 将 `key` 列重命名为 `process_key`（避免 MySQL 保留字 + MyBatis-Plus 兼容）
-- 2. 添加缺失字段

ALTER TABLE sys_process_definition
    CHANGE COLUMN `key` `process_key` varchar(255) NOT NULL COMMENT '流程标识';

ALTER TABLE sys_process_definition
    ADD COLUMN IF NOT EXISTS `category` varchar(255) DEFAULT NULL COMMENT '分类' AFTER `status`,
    ADD COLUMN IF NOT EXISTS `deployment_id` varchar(64) DEFAULT NULL COMMENT 'Activiti部署ID',
    ADD COLUMN IF NOT EXISTS `act_process_definition_id` varchar(64) DEFAULT NULL COMMENT 'Activiti流程定义ID';

-- 重建唯一索引（原 uk_key 引用的是旧列名）
DROP INDEX IF EXISTS `uk_key` ON sys_process_definition;
ALTER TABLE sys_process_definition ADD UNIQUE KEY `uk_process_key` (`process_key`);
