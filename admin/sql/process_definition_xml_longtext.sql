-- 迁移脚本: sys_process_definition.xml 由 TEXT(最大64KB) 扩容为 LONGTEXT
-- 背景: 复杂流程图导出的 BPMN XML(含 BPMNDI 图形布局)容易超过 64KB,
--       旧的 TEXT 列会导致保存报错 "Data too long for column 'xml'"。
ALTER TABLE sys_process_definition MODIFY COLUMN xml LONGTEXT COMMENT '流程XML';
