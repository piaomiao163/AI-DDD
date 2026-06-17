-- 迁移脚本: sys_process_instance 添加缺失字段
-- 对齐 ProcessInstanceDO / ProcessInstanceModel 中已定义但表中缺失的字段

ALTER TABLE sys_process_instance
    ADD COLUMN instance_no        VARCHAR(64)                           COMMENT '实例编号(如PROC202606130001)' AFTER title,
    ADD COLUMN business_id        BIGINT(20)                            COMMENT '业务单据主键ID(如请假单ID)' AFTER business_type,
    ADD COLUMN applicant_dept_id  BIGINT(20)                            COMMENT '发起人部门ID' AFTER start_user_name,
    ADD COLUMN current_node_id    VARCHAR(255)                          COMMENT '当前节点ID' AFTER current_node_name,
    ADD COLUMN current_assignees  TEXT                                  COMMENT '当前待审批人列表JSON(如:[{"userId":1,"userName":"张三"}])' AFTER current_node_id,
    ADD COLUMN total_nodes        INT                                   COMMENT '总审批节点数' AFTER current_assignees,
    ADD COLUMN completed_nodes    INT          NOT NULL DEFAULT 0       COMMENT '已完成节点数' AFTER total_nodes;

-- 添加实例编号唯一索引
ALTER TABLE sys_process_instance
    ADD UNIQUE KEY uk_instance_no (instance_no);
