-- 操作日志表
CREATE TABLE IF NOT EXISTS `sys_operation_log` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
    `module` VARCHAR(50) DEFAULT NULL COMMENT '操作模块',
    `type` VARCHAR(50) DEFAULT NULL COMMENT '操作类型',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '操作描述',
    `request_method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法',
    `request_url` VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
    `request_params` TEXT DEFAULT NULL COMMENT '请求参数',
    `response_result` TEXT DEFAULT NULL COMMENT '响应结果',
    `user_id` BIGINT DEFAULT NULL COMMENT '操作人ID',
    `username` VARCHAR(50) DEFAULT NULL COMMENT '操作人用户名',
    `ip_address` VARCHAR(50) DEFAULT NULL COMMENT '操作IP地址',
    `location` VARCHAR(100) DEFAULT NULL COMMENT '操作地点',
    `status` TINYINT DEFAULT 1 COMMENT '操作状态（0失败 1成功）',
    `error_msg` TEXT DEFAULT NULL COMMENT '错误消息',
    `execution_time` BIGINT DEFAULT NULL COMMENT '执行时长（毫秒）',
    `operation_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (`id`),
    INDEX `idx_module` (`module`),
    INDEX `idx_type` (`type`),
    INDEX `idx_username` (`username`),
    INDEX `idx_operation_time` (`operation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
