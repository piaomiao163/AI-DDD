-- =====================================================
-- 审计字段 + 软删除迁移脚本
-- 执行前请备份数据库
-- =====================================================

-- sys_user: 添加 create_by, update_by, deleted
ALTER TABLE sys_user ADD COLUMN create_by VARCHAR(100) DEFAULT NULL COMMENT '创建人' AFTER update_time;
ALTER TABLE sys_user ADD COLUMN update_by VARCHAR(100) DEFAULT NULL COMMENT '更新人' AFTER create_by;
ALTER TABLE sys_user ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否' AFTER update_by;

-- sys_role: 添加 create_by, update_by, deleted
ALTER TABLE sys_role ADD COLUMN create_by VARCHAR(100) DEFAULT NULL COMMENT '创建人' AFTER update_time;
ALTER TABLE sys_role ADD COLUMN update_by VARCHAR(100) DEFAULT NULL COMMENT '更新人' AFTER create_by;
ALTER TABLE sys_role ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否' AFTER update_by;

-- sys_department: 添加 create_time, update_time, create_by, update_by, deleted
ALTER TABLE sys_department ADD COLUMN create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间' AFTER sort;
ALTER TABLE sys_department ADD COLUMN update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER create_time;
ALTER TABLE sys_department ADD COLUMN create_by VARCHAR(100) DEFAULT NULL COMMENT '创建人' AFTER update_time;
ALTER TABLE sys_department ADD COLUMN update_by VARCHAR(100) DEFAULT NULL COMMENT '更新人' AFTER create_by;
ALTER TABLE sys_department ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否' AFTER update_by;

-- sys_menu: 添加 deleted
ALTER TABLE sys_menu ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否' AFTER update_time;

-- sys_data_dictionary: 添加 deleted
ALTER TABLE sys_data_dictionary ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否' AFTER update_time;

-- sys_data_dictionary_item: 添加 deleted
ALTER TABLE sys_data_dictionary_item ADD COLUMN deleted TINYINT NOT NULL DEFAULT 0 COMMENT '是否删除1-是、0-否' AFTER update_time;

-- sys_process_definition: 添加 create_by, update_by (deleted 已存在)
ALTER TABLE sys_process_definition ADD COLUMN create_by VARCHAR(100) DEFAULT NULL COMMENT '创建人' AFTER update_time;
ALTER TABLE sys_process_definition ADD COLUMN update_by VARCHAR(100) DEFAULT NULL COMMENT '更新人' AFTER create_by;
