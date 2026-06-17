-- Activiti迁移：为sys_process_definition添加Activiti关联字段
ALTER TABLE sys_process_definition
    ADD COLUMN deployment_id VARCHAR(64) COMMENT 'Activiti部署ID',
    ADD COLUMN act_process_definition_id VARCHAR(64) COMMENT 'Activiti流程定义ID';
