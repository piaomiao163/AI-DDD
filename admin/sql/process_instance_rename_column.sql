-- 迁移脚本: sys_process_instance 字段重命名
-- activiti_process_instance_id → act_process_instance_id（简化命名，与项目 act_ 前缀风格一致）

-- 1. 重命名字段
ALTER TABLE sys_process_instance
    CHANGE COLUMN activiti_process_instance_id act_process_instance_id VARCHAR(64) NOT NULL COMMENT 'Activiti流程实例ID';

-- 2. 重命名索引
ALTER TABLE sys_process_instance
    DROP INDEX uk_activiti_pid,
    ADD UNIQUE KEY uk_act_pid (act_process_instance_id);
