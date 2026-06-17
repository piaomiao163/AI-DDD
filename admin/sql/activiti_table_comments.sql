-- =========================================================
-- Activiti 7.1.0 表字段注释 + 表注释
-- 表注释前缀：ACT-
-- =========================================================

-- ==================== ACT_GE_* 通用数据 ====================

-- act_ge_bytearray
ALTER TABLE act_ge_bytearray COMMENT 'ACT-通用字节数组（存储BPMN XML、流程图PNG等二进制资源）';
ALTER TABLE act_ge_bytearray MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_ge_bytearray MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_ge_bytearray MODIFY COLUMN name_ varchar(255) DEFAULT NULL COMMENT '资源名称（如BPMN文件名）';
ALTER TABLE act_ge_bytearray MODIFY COLUMN deployment_id_ varchar(64) DEFAULT NULL COMMENT '所属部署ID（关联act_re_deployment）';
ALTER TABLE act_ge_bytearray MODIFY COLUMN bytes_ longblob COMMENT '二进制内容（BPMN XML、流程图PNG等）';
ALTER TABLE act_ge_bytearray MODIFY COLUMN generated_ tinyint(4) DEFAULT NULL COMMENT '是否自动生成（1=引擎自动生成如流程图，0=用户上传）';

-- act_ge_property
ALTER TABLE act_ge_property COMMENT 'ACT-通用属性（存储引擎schema版本等全局属性）';
ALTER TABLE act_ge_property MODIFY COLUMN name_ varchar(64) NOT NULL COMMENT '属性名';
ALTER TABLE act_ge_property MODIFY COLUMN value_ varchar(300) DEFAULT NULL COMMENT '属性值';
ALTER TABLE act_ge_property MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';

-- ==================== ACT_RE_* 流程定义/部署 ====================

-- act_re_deployment
ALTER TABLE act_re_deployment COMMENT 'ACT-流程部署（每次发布流程定义生成一条部署记录）';
ALTER TABLE act_re_deployment MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_re_deployment MODIFY COLUMN name_ varchar(255) DEFAULT NULL COMMENT '部署名称';
ALTER TABLE act_re_deployment MODIFY COLUMN category_ varchar(255) DEFAULT NULL COMMENT '部署分类';
ALTER TABLE act_re_deployment MODIFY COLUMN key_ varchar(255) DEFAULT NULL COMMENT '部署标识';
ALTER TABLE act_re_deployment MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';
ALTER TABLE act_re_deployment MODIFY COLUMN deploy_time_ datetime(3) DEFAULT NULL COMMENT '部署时间';
ALTER TABLE act_re_deployment MODIFY COLUMN derived_from_ varchar(64) DEFAULT NULL COMMENT '派生来源部署ID';
ALTER TABLE act_re_deployment MODIFY COLUMN derived_from_root_ varchar(64) DEFAULT NULL COMMENT '派生根部署ID';
ALTER TABLE act_re_deployment MODIFY COLUMN parent_deployment_id_ varchar(255) DEFAULT NULL COMMENT '父部署ID';
ALTER TABLE act_re_deployment MODIFY COLUMN engine_version_ varchar(255) DEFAULT NULL COMMENT '引擎版本';

-- act_re_procdef
ALTER TABLE act_re_procdef COMMENT 'ACT-流程定义（存储已部署的流程定义信息，同一key按版本递增）';
ALTER TABLE act_re_procdef MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键（格式：key:version:uniqueId）';
ALTER TABLE act_re_procdef MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_re_procdef MODIFY COLUMN category_ varchar(255) DEFAULT NULL COMMENT '流程分类（BPMN中targetNamespace）';
ALTER TABLE act_re_procdef MODIFY COLUMN name_ varchar(255) DEFAULT NULL COMMENT '流程名称（BPMN中name属性）';
ALTER TABLE act_re_procdef MODIFY COLUMN key_ varchar(255) NOT NULL COMMENT '流程标识（BPMN中id属性，如leave）';
ALTER TABLE act_re_procdef MODIFY COLUMN version_ int(11) NOT NULL COMMENT '流程版本（同一key自动递增）';
ALTER TABLE act_re_procdef MODIFY COLUMN deployment_id_ varchar(64) DEFAULT NULL COMMENT '所属部署ID（关联act_re_deployment）';
ALTER TABLE act_re_procdef MODIFY COLUMN resource_name_ varchar(255) DEFAULT NULL COMMENT 'BPMN资源文件名';
ALTER TABLE act_re_procdef MODIFY COLUMN dgrm_resource_name_ varchar(255) DEFAULT NULL COMMENT '流程图资源文件名';
ALTER TABLE act_re_procdef MODIFY COLUMN description_ varchar(255) DEFAULT NULL COMMENT '流程描述';
ALTER TABLE act_re_procdef MODIFY COLUMN has_start_form_key_ tinyint(4) DEFAULT NULL COMMENT '是否有开始表单Key';
ALTER TABLE act_re_procdef MODIFY COLUMN has_graphical_notation_ tinyint(4) DEFAULT NULL COMMENT '是否有图形信息';
ALTER TABLE act_re_procdef MODIFY COLUMN suspension_state_ int(11) DEFAULT NULL COMMENT '挂起状态（1=激活，0=挂起）';
ALTER TABLE act_re_procdef MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';
ALTER TABLE act_re_procdef MODIFY COLUMN engine_version_ varchar(255) DEFAULT NULL COMMENT '引擎版本';
ALTER TABLE act_re_procdef MODIFY COLUMN derived_from_ varchar(64) DEFAULT NULL COMMENT '派生来源流程定义ID';
ALTER TABLE act_re_procdef MODIFY COLUMN derived_from_root_ varchar(64) DEFAULT NULL COMMENT '派生根流程定义ID';
ALTER TABLE act_re_procdef MODIFY COLUMN derived_version_ int(11) NOT NULL DEFAULT '0' COMMENT '派生版本号';

-- act_re_model
ALTER TABLE act_re_model COMMENT 'ACT-流程模型（在线流程设计器保存的模型草稿）';
ALTER TABLE act_re_model MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_re_model MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_re_model MODIFY COLUMN name_ varchar(255) DEFAULT NULL COMMENT '模型名称';
ALTER TABLE act_re_model MODIFY COLUMN category_ varchar(255) DEFAULT NULL COMMENT '模型分类';
ALTER TABLE act_re_model MODIFY COLUMN create_time_ datetime(3) DEFAULT NULL COMMENT '创建时间';
ALTER TABLE act_re_model MODIFY COLUMN last_update_time_ datetime(3) DEFAULT NULL COMMENT '最后更新时间';
ALTER TABLE act_re_model MODIFY COLUMN version_ int(11) DEFAULT NULL COMMENT '模型版本';
ALTER TABLE act_re_model MODIFY COLUMN meta_info_ varchar(4000) DEFAULT NULL COMMENT '模型元信息（JSON）';
ALTER TABLE act_re_model MODIFY COLUMN deployment_id_ varchar(64) DEFAULT NULL COMMENT '部署ID（模型已部署时关联）';
ALTER TABLE act_re_model MODIFY COLUMN editor_source_value_id_ varchar(64) DEFAULT NULL COMMENT '编辑器源数据ID（关联act_ge_bytearray）';
ALTER TABLE act_re_model MODIFY COLUMN editor_source_extra_value_id_ varchar(64) DEFAULT NULL COMMENT '编辑器扩展源数据ID';
ALTER TABLE act_re_model MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';

-- ==================== ACT_RU_* 运行时 ====================

-- act_ru_execution
ALTER TABLE act_ru_execution COMMENT 'ACT-运行时执行实例（流程实例和执行树的运行时状态）';
ALTER TABLE act_ru_execution MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_ru_execution MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_ru_execution MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID（主执行与流程实例ID相同）';
ALTER TABLE act_ru_execution MODIFY COLUMN business_key_ varchar(255) DEFAULT NULL COMMENT '业务键（启动流程时传入的业务标识）';
ALTER TABLE act_ru_execution MODIFY COLUMN parent_id_ varchar(64) DEFAULT NULL COMMENT '父执行ID（子流程/并行网关产生子执行）';
ALTER TABLE act_ru_execution MODIFY COLUMN proc_def_id_ varchar(64) DEFAULT NULL COMMENT '流程定义ID';
ALTER TABLE act_ru_execution MODIFY COLUMN super_exec_ varchar(64) DEFAULT NULL COMMENT '超级执行ID（调用活动产生的关联）';
ALTER TABLE act_ru_execution MODIFY COLUMN root_proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '根流程实例ID';
ALTER TABLE act_ru_execution MODIFY COLUMN act_id_ varchar(255) DEFAULT NULL COMMENT '当前活动ID（BPMN节点ID）';
ALTER TABLE act_ru_execution MODIFY COLUMN is_active_ tinyint(4) DEFAULT NULL COMMENT '是否激活';
ALTER TABLE act_ru_execution MODIFY COLUMN is_concurrent_ tinyint(4) DEFAULT NULL COMMENT '是否并行执行';
ALTER TABLE act_ru_execution MODIFY COLUMN is_scope_ tinyint(4) DEFAULT NULL COMMENT '是否作用域执行';
ALTER TABLE act_ru_execution MODIFY COLUMN is_event_scope_ tinyint(4) DEFAULT NULL COMMENT '是否事件作用域';
ALTER TABLE act_ru_execution MODIFY COLUMN suspension_state_ int(11) DEFAULT NULL COMMENT '挂起状态（1=激活，0=挂起）';
ALTER TABLE act_ru_execution MODIFY COLUMN cached_ent_state_ int(11) DEFAULT NULL COMMENT '缓存实体状态位';
ALTER TABLE act_ru_execution MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';
ALTER TABLE act_ru_execution MODIFY COLUMN name_ varchar(255) DEFAULT NULL COMMENT '执行名称';

-- act_ru_task
ALTER TABLE act_ru_task COMMENT 'ACT-运行时任务（当前待办的用户任务）';
ALTER TABLE act_ru_task MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键（任务ID）';
ALTER TABLE act_ru_task MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_ru_task MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_ru_task MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_ru_task MODIFY COLUMN proc_def_id_ varchar(64) DEFAULT NULL COMMENT '流程定义ID';
ALTER TABLE act_ru_task MODIFY COLUMN name_ varchar(255) DEFAULT NULL COMMENT '任务名称（BPMN中UserTask的name）';
ALTER TABLE act_ru_task MODIFY COLUMN parent_task_id_ varchar(64) DEFAULT NULL COMMENT '父任务ID';
ALTER TABLE act_ru_task MODIFY COLUMN description_ varchar(4000) DEFAULT NULL COMMENT '任务描述';
ALTER TABLE act_ru_task MODIFY COLUMN task_def_key_ varchar(255) DEFAULT NULL COMMENT '任务定义Key（BPMN中UserTask的id）';
ALTER TABLE act_ru_task MODIFY COLUMN owner_ varchar(255) DEFAULT NULL COMMENT '任务拥有者（委派时的原处理人）';
ALTER TABLE act_ru_task MODIFY COLUMN assignee_ varchar(255) DEFAULT NULL COMMENT '任务处理人（当前负责审批的用户）';
ALTER TABLE act_ru_task MODIFY COLUMN delegation_ varchar(64) DEFAULT NULL COMMENT '委派状态（PENDING=待委派，RESOLVED=已解决）';
ALTER TABLE act_ru_task MODIFY COLUMN priority_ int(11) DEFAULT NULL COMMENT '优先级';
ALTER TABLE act_ru_task MODIFY COLUMN create_time_ datetime(3) DEFAULT NULL COMMENT '任务创建时间';
ALTER TABLE act_ru_task MODIFY COLUMN due_date_ datetime(3) DEFAULT NULL COMMENT '到期时间';
ALTER TABLE act_ru_task MODIFY COLUMN category_ varchar(255) DEFAULT NULL COMMENT '任务分类';
ALTER TABLE act_ru_task MODIFY COLUMN suspension_state_ int(11) DEFAULT NULL COMMENT '挂起状态（1=激活，0=挂起）';
ALTER TABLE act_ru_task MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';
ALTER TABLE act_ru_task MODIFY COLUMN form_key_ varchar(255) DEFAULT NULL COMMENT '表单Key';

-- act_ru_identitylink
ALTER TABLE act_ru_identitylink COMMENT 'ACT-运行时身份关联（任务候选人与流程参与者的运行时关系）';
ALTER TABLE act_ru_identitylink MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_ru_identitylink MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_ru_identitylink MODIFY COLUMN group_id_ varchar(255) DEFAULT NULL COMMENT '候选组ID';
ALTER TABLE act_ru_identitylink MODIFY COLUMN type_ varchar(255) DEFAULT NULL COMMENT '关联类型（candidate=候选人，assignee=处理人，owner=拥有者，participant=参与者）';
ALTER TABLE act_ru_identitylink MODIFY COLUMN user_id_ varchar(255) DEFAULT NULL COMMENT '候选用户ID';
ALTER TABLE act_ru_identitylink MODIFY COLUMN task_id_ varchar(64) DEFAULT NULL COMMENT '关联任务ID';
ALTER TABLE act_ru_identitylink MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '关联流程实例ID';
ALTER TABLE act_ru_identitylink MODIFY COLUMN proc_def_id_ varchar(64) DEFAULT NULL COMMENT '关联流程定义ID';

-- act_ru_variable
ALTER TABLE act_ru_variable COMMENT 'ACT-运行时变量（流程实例和任务的运行时变量）';
ALTER TABLE act_ru_variable MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_ru_variable MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_ru_variable MODIFY COLUMN type_ varchar(255) DEFAULT NULL COMMENT '变量类型（string/integer/boolean/json等）';
ALTER TABLE act_ru_variable MODIFY COLUMN name_ varchar(255) NOT NULL COMMENT '变量名';
ALTER TABLE act_ru_variable MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_ru_variable MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_ru_variable MODIFY COLUMN task_id_ varchar(64) DEFAULT NULL COMMENT '任务ID（任务局部变量时关联）';
ALTER TABLE act_ru_variable MODIFY COLUMN bytearray_id_ varchar(64) DEFAULT NULL COMMENT '字节数组ID（序列化类型变量关联act_ge_bytearray）';
ALTER TABLE act_ru_variable MODIFY COLUMN double_ double DEFAULT NULL COMMENT 'double类型变量值';
ALTER TABLE act_ru_variable MODIFY COLUMN long_ bigint(20) DEFAULT NULL COMMENT 'long类型变量值';
ALTER TABLE act_ru_variable MODIFY COLUMN text_ varchar(4000) DEFAULT NULL COMMENT 'text类型变量值';
ALTER TABLE act_ru_variable MODIFY COLUMN text2_ varchar(4000) DEFAULT NULL COMMENT 'text类型变量值2（超长文本第二段）';

-- act_ru_job
ALTER TABLE act_ru_job COMMENT 'ACT-运行时作业（异步消息和定时器作业）';
ALTER TABLE act_ru_job MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_ru_job MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_ru_job MODIFY COLUMN type_ varchar(255) NOT NULL COMMENT '作业类型（message=消息，timer=定时器）';
ALTER TABLE act_ru_job MODIFY COLUMN lock_exp_time_ datetime(3) DEFAULT NULL COMMENT '锁定过期时间（集群节点抢占执行锁）';
ALTER TABLE act_ru_job MODIFY COLUMN lock_owner_ varchar(255) DEFAULT NULL COMMENT '锁持有者（集群节点标识）';
ALTER TABLE act_ru_job MODIFY COLUMN exclusive_ tinyint(4) DEFAULT '1' COMMENT '是否排他执行';
ALTER TABLE act_ru_job MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_ru_job MODIFY COLUMN process_instance_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_ru_job MODIFY COLUMN process_def_id_ varchar(64) DEFAULT NULL COMMENT '流程定义ID';
ALTER TABLE act_ru_job MODIFY COLUMN exception_message_ varchar(4000) DEFAULT NULL COMMENT '异常消息（执行失败时记录）';
ALTER TABLE act_ru_job MODIFY COLUMN duedate_ datetime(3) DEFAULT NULL COMMENT '到期时间';
ALTER TABLE act_ru_job MODIFY COLUMN repeat_ varchar(255) DEFAULT NULL COMMENT '重复表达式（如R3/PT10M）';
ALTER TABLE act_ru_job MODIFY COLUMN handler_type_ varchar(255) DEFAULT NULL COMMENT '处理器类型';
ALTER TABLE act_ru_job MODIFY COLUMN handler_cfg_ varchar(4000) DEFAULT NULL COMMENT '处理器配置';
ALTER TABLE act_ru_job MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';
ALTER TABLE act_ru_job MODIFY COLUMN retries_ int(11) DEFAULT '3' COMMENT '剩余重试次数';

-- act_ru_timer_job
ALTER TABLE act_ru_timer_job COMMENT 'ACT-运行时定时器作业（定时器触发的延迟作业，独立于普通作业以优化查询）';
ALTER TABLE act_ru_timer_job MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_ru_timer_job MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_ru_timer_job MODIFY COLUMN type_ varchar(255) NOT NULL COMMENT '作业类型（timer）';
ALTER TABLE act_ru_timer_job MODIFY COLUMN lock_exp_time_ datetime(3) DEFAULT NULL COMMENT '锁定过期时间';
ALTER TABLE act_ru_timer_job MODIFY COLUMN lock_owner_ varchar(255) DEFAULT NULL COMMENT '锁持有者';
ALTER TABLE act_ru_timer_job MODIFY COLUMN exclusive_ tinyint(4) DEFAULT '1' COMMENT '是否排他执行';
ALTER TABLE act_ru_timer_job MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_ru_timer_job MODIFY COLUMN process_instance_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_ru_timer_job MODIFY COLUMN process_def_id_ varchar(64) DEFAULT NULL COMMENT '流程定义ID';
ALTER TABLE act_ru_timer_job MODIFY COLUMN exception_message_ varchar(4000) DEFAULT NULL COMMENT '异常消息';
ALTER TABLE act_ru_timer_job MODIFY COLUMN duedate_ datetime(3) DEFAULT NULL COMMENT '到期时间';
ALTER TABLE act_ru_timer_job MODIFY COLUMN repeat_ varchar(255) DEFAULT NULL COMMENT '重复表达式';
ALTER TABLE act_ru_timer_job MODIFY COLUMN handler_type_ varchar(255) DEFAULT NULL COMMENT '处理器类型';
ALTER TABLE act_ru_timer_job MODIFY COLUMN handler_cfg_ varchar(4000) DEFAULT NULL COMMENT '处理器配置';
ALTER TABLE act_ru_timer_job MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';
ALTER TABLE act_ru_timer_job MODIFY COLUMN retries_ int(11) DEFAULT '3' COMMENT '剩余重试次数';

-- act_ru_suspended_job
ALTER TABLE act_ru_suspended_job COMMENT 'ACT-运行时挂起作业（流程挂起时暂停执行的作业）';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN type_ varchar(255) NOT NULL COMMENT '作业类型';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN lock_exp_time_ datetime(3) DEFAULT NULL COMMENT '锁定过期时间';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN lock_owner_ varchar(255) DEFAULT NULL COMMENT '锁持有者';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN exclusive_ tinyint(4) DEFAULT '1' COMMENT '是否排他执行';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN process_instance_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN process_def_id_ varchar(64) DEFAULT NULL COMMENT '流程定义ID';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN exception_message_ varchar(4000) DEFAULT NULL COMMENT '异常消息';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN duedate_ datetime(3) DEFAULT NULL COMMENT '到期时间';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN repeat_ varchar(255) DEFAULT NULL COMMENT '重复表达式';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN handler_type_ varchar(255) DEFAULT NULL COMMENT '处理器类型';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN handler_cfg_ varchar(4000) DEFAULT NULL COMMENT '处理器配置';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';
ALTER TABLE act_ru_suspended_job MODIFY COLUMN retries_ int(11) DEFAULT '3' COMMENT '剩余重试次数';

-- act_ru_deadletter_job
ALTER TABLE act_ru_deadletter_job COMMENT 'ACT-运行时死信作业（重试耗尽后无法执行的作业）';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN type_ varchar(255) NOT NULL COMMENT '作业类型';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN exclusive_ tinyint(4) DEFAULT '1' COMMENT '是否排他执行';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN process_instance_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN process_def_id_ varchar(64) DEFAULT NULL COMMENT '流程定义ID';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN exception_message_ varchar(4000) DEFAULT NULL COMMENT '异常消息';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN duedate_ datetime(3) DEFAULT NULL COMMENT '到期时间';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN repeat_ varchar(255) DEFAULT NULL COMMENT '重复表达式';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN handler_type_ varchar(255) DEFAULT NULL COMMENT '处理器类型';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN handler_cfg_ varchar(4000) DEFAULT NULL COMMENT '处理器配置';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';
ALTER TABLE act_ru_deadletter_job MODIFY COLUMN retries_ int(11) DEFAULT '3' COMMENT '剩余重试次数';

-- act_ru_event_subscr
ALTER TABLE act_ru_event_subscr COMMENT 'ACT-运行时事件订阅（消息/信号/补偿事件的运行时订阅）';
ALTER TABLE act_ru_event_subscr MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_ru_event_subscr MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_ru_event_subscr MODIFY COLUMN event_type_ varchar(255) NOT NULL COMMENT '事件类型（message/signal/compensate等）';
ALTER TABLE act_ru_event_subscr MODIFY COLUMN event_name_ varchar(255) DEFAULT NULL COMMENT '事件名称';
ALTER TABLE act_ru_event_subscr MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_ru_event_subscr MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_ru_event_subscr MODIFY COLUMN activity_id_ varchar(64) DEFAULT NULL COMMENT '活动ID';
ALTER TABLE act_ru_event_subscr MODIFY COLUMN configuration_ varchar(255) DEFAULT NULL COMMENT '配置信息';
ALTER TABLE act_ru_event_subscr MODIFY COLUMN created_ datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间';
ALTER TABLE act_ru_event_subscr MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';

-- act_ru_integration
ALTER TABLE act_ru_integration COMMENT 'ACT-运行时集成（流程与外部系统的集成数据）';
ALTER TABLE act_ru_integration MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_ru_integration MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_ru_integration MODIFY COLUMN process_instance_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_ru_integration MODIFY COLUMN proc_def_id_ varchar(64) DEFAULT NULL COMMENT '流程定义ID';
ALTER TABLE act_ru_integration MODIFY COLUMN flow_node_id_ varchar(64) DEFAULT NULL COMMENT '流程节点ID';
ALTER TABLE act_ru_integration MODIFY COLUMN created_date_ datetime(3) DEFAULT NULL COMMENT '创建时间';

-- ==================== ACT_HI_* 历史 ====================

-- act_hi_procinst
ALTER TABLE act_hi_procinst COMMENT 'ACT-历史流程实例（已结束的流程实例历史记录）';
ALTER TABLE act_hi_procinst MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_hi_procinst MODIFY COLUMN proc_inst_id_ varchar(64) NOT NULL COMMENT '流程实例ID';
ALTER TABLE act_hi_procinst MODIFY COLUMN business_key_ varchar(255) DEFAULT NULL COMMENT '业务键';
ALTER TABLE act_hi_procinst MODIFY COLUMN proc_def_id_ varchar(64) DEFAULT NULL COMMENT '流程定义ID';
ALTER TABLE act_hi_procinst MODIFY COLUMN start_time_ datetime(3) DEFAULT NULL COMMENT '启动时间';
ALTER TABLE act_hi_procinst MODIFY COLUMN end_time_ datetime(3) DEFAULT NULL COMMENT '结束时间';
ALTER TABLE act_hi_procinst MODIFY COLUMN duration_ bigint(20) DEFAULT NULL COMMENT '持续时间（毫秒）';
ALTER TABLE act_hi_procinst MODIFY COLUMN start_user_id_ varchar(255) DEFAULT NULL COMMENT '启动人ID';
ALTER TABLE act_hi_procinst MODIFY COLUMN start_act_id_ varchar(255) DEFAULT NULL COMMENT '起始活动ID';
ALTER TABLE act_hi_procinst MODIFY COLUMN end_act_id_ varchar(255) DEFAULT NULL COMMENT '结束活动ID';
ALTER TABLE act_hi_procinst MODIFY COLUMN super_process_instance_id_ varchar(64) DEFAULT NULL COMMENT '父流程实例ID';
ALTER TABLE act_hi_procinst MODIFY COLUMN delete_reason_ varchar(4000) DEFAULT NULL COMMENT '删除原因';
ALTER TABLE act_hi_procinst MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';
ALTER TABLE act_hi_procinst MODIFY COLUMN name_ varchar(255) DEFAULT NULL COMMENT '流程实例名称';

-- act_hi_actinst
ALTER TABLE act_hi_actinst COMMENT 'ACT-历史活动实例（每个流程节点执行的历史记录）';
ALTER TABLE act_hi_actinst MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_hi_actinst MODIFY COLUMN proc_def_id_ varchar(64) DEFAULT NULL COMMENT '流程定义ID';
ALTER TABLE act_hi_actinst MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_hi_actinst MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_hi_actinst MODIFY COLUMN act_id_ varchar(255) DEFAULT NULL COMMENT '活动ID（BPMN节点ID）';
ALTER TABLE act_hi_actinst MODIFY COLUMN task_id_ varchar(64) DEFAULT NULL COMMENT '关联任务ID（UserTask类型时）';
ALTER TABLE act_hi_actinst MODIFY COLUMN call_proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '调用子流程实例ID';
ALTER TABLE act_hi_actinst MODIFY COLUMN act_name_ varchar(255) DEFAULT NULL COMMENT '活动名称';
ALTER TABLE act_hi_actinst MODIFY COLUMN act_type_ varchar(255) DEFAULT NULL COMMENT '活动类型（startEvent/userTask/exclusiveGateway/endEvent等）';
ALTER TABLE act_hi_actinst MODIFY COLUMN assignee_ varchar(255) DEFAULT NULL COMMENT '处理人';
ALTER TABLE act_hi_actinst MODIFY COLUMN start_time_ datetime(3) NOT NULL COMMENT '活动开始时间';
ALTER TABLE act_hi_actinst MODIFY COLUMN end_time_ datetime(3) DEFAULT NULL COMMENT '活动结束时间';
ALTER TABLE act_hi_actinst MODIFY COLUMN duration_ bigint(20) DEFAULT NULL COMMENT '持续时间（毫秒）';
ALTER TABLE act_hi_actinst MODIFY COLUMN delete_reason_ varchar(4000) DEFAULT NULL COMMENT '删除原因';
ALTER TABLE act_hi_actinst MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';

-- act_hi_taskinst
ALTER TABLE act_hi_taskinst COMMENT 'ACT-历史任务实例（已完成或已删除的用户任务历史）';
ALTER TABLE act_hi_taskinst MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键（任务ID）';
ALTER TABLE act_hi_taskinst MODIFY COLUMN proc_def_id_ varchar(64) DEFAULT NULL COMMENT '流程定义ID';
ALTER TABLE act_hi_taskinst MODIFY COLUMN task_def_key_ varchar(255) DEFAULT NULL COMMENT '任务定义Key';
ALTER TABLE act_hi_taskinst MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_hi_taskinst MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_hi_taskinst MODIFY COLUMN name_ varchar(255) DEFAULT NULL COMMENT '任务名称';
ALTER TABLE act_hi_taskinst MODIFY COLUMN parent_task_id_ varchar(64) DEFAULT NULL COMMENT '父任务ID';
ALTER TABLE act_hi_taskinst MODIFY COLUMN description_ varchar(4000) DEFAULT NULL COMMENT '任务描述';
ALTER TABLE act_hi_taskinst MODIFY COLUMN owner_ varchar(255) DEFAULT NULL COMMENT '任务拥有者';
ALTER TABLE act_hi_taskinst MODIFY COLUMN assignee_ varchar(255) DEFAULT NULL COMMENT '任务处理人';
ALTER TABLE act_hi_taskinst MODIFY COLUMN start_time_ datetime(3) NOT NULL COMMENT '任务开始时间';
ALTER TABLE act_hi_taskinst MODIFY COLUMN claim_time_ datetime(3) DEFAULT NULL COMMENT '认领时间';
ALTER TABLE act_hi_taskinst MODIFY COLUMN end_time_ datetime(3) DEFAULT NULL COMMENT '任务结束时间';
ALTER TABLE act_hi_taskinst MODIFY COLUMN duration_ bigint(20) DEFAULT NULL COMMENT '持续时间（毫秒）';
ALTER TABLE act_hi_taskinst MODIFY COLUMN delete_reason_ varchar(4000) DEFAULT NULL COMMENT '删除原因（completed/deleted等）';
ALTER TABLE act_hi_taskinst MODIFY COLUMN priority_ int(11) DEFAULT NULL COMMENT '优先级';
ALTER TABLE act_hi_taskinst MODIFY COLUMN due_date_ datetime(3) DEFAULT NULL COMMENT '到期时间';
ALTER TABLE act_hi_taskinst MODIFY COLUMN form_key_ varchar(255) DEFAULT NULL COMMENT '表单Key';
ALTER TABLE act_hi_taskinst MODIFY COLUMN category_ varchar(255) DEFAULT NULL COMMENT '任务分类';
ALTER TABLE act_hi_taskinst MODIFY COLUMN tenant_id_ varchar(255) DEFAULT '' COMMENT '租户ID';
ALTER TABLE act_hi_taskinst MODIFY COLUMN last_updated_time_ datetime(3) DEFAULT NULL COMMENT '最后更新时间';

-- act_hi_identitylink
ALTER TABLE act_hi_identitylink COMMENT 'ACT-历史身份关联（任务候选人与流程参与者的历史记录）';
ALTER TABLE act_hi_identitylink MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_hi_identitylink MODIFY COLUMN group_id_ varchar(255) DEFAULT NULL COMMENT '候选组ID';
ALTER TABLE act_hi_identitylink MODIFY COLUMN type_ varchar(255) DEFAULT NULL COMMENT '关联类型（candidate/assignee/owner/participant）';
ALTER TABLE act_hi_identitylink MODIFY COLUMN user_id_ varchar(255) DEFAULT NULL COMMENT '候选用户ID';
ALTER TABLE act_hi_identitylink MODIFY COLUMN task_id_ varchar(64) DEFAULT NULL COMMENT '关联任务ID';
ALTER TABLE act_hi_identitylink MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '关联流程实例ID';

-- act_hi_varinst
ALTER TABLE act_hi_varinst COMMENT 'ACT-历史变量实例（流程和任务变量的变更历史）';
ALTER TABLE act_hi_varinst MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_hi_varinst MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_hi_varinst MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_hi_varinst MODIFY COLUMN task_id_ varchar(64) DEFAULT NULL COMMENT '任务ID';
ALTER TABLE act_hi_varinst MODIFY COLUMN name_ varchar(255) NOT NULL COMMENT '变量名';
ALTER TABLE act_hi_varinst MODIFY COLUMN var_type_ varchar(255) DEFAULT NULL COMMENT '变量类型';
ALTER TABLE act_hi_varinst MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_hi_varinst MODIFY COLUMN bytearray_id_ varchar(64) DEFAULT NULL COMMENT '字节数组ID';
ALTER TABLE act_hi_varinst MODIFY COLUMN double_ double DEFAULT NULL COMMENT 'double类型值';
ALTER TABLE act_hi_varinst MODIFY COLUMN long_ bigint(20) DEFAULT NULL COMMENT 'long类型值';
ALTER TABLE act_hi_varinst MODIFY COLUMN text_ varchar(4000) DEFAULT NULL COMMENT 'text类型值';
ALTER TABLE act_hi_varinst MODIFY COLUMN text2_ varchar(4000) DEFAULT NULL COMMENT 'text类型值2';
ALTER TABLE act_hi_varinst MODIFY COLUMN create_time_ datetime(3) DEFAULT NULL COMMENT '创建时间';
ALTER TABLE act_hi_varinst MODIFY COLUMN last_updated_time_ datetime(3) DEFAULT NULL COMMENT '最后更新时间';

-- act_hi_detail
ALTER TABLE act_hi_detail COMMENT 'ACT-历史明细（变量更新和表单属性的详细变更记录）';
ALTER TABLE act_hi_detail MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_hi_detail MODIFY COLUMN type_ varchar(255) NOT NULL COMMENT '明细类型（VariableUpdate/FormProperty）';
ALTER TABLE act_hi_detail MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_hi_detail MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_hi_detail MODIFY COLUMN task_id_ varchar(64) DEFAULT NULL COMMENT '任务ID';
ALTER TABLE act_hi_detail MODIFY COLUMN act_inst_id_ varchar(64) DEFAULT NULL COMMENT '活动实例ID';
ALTER TABLE act_hi_detail MODIFY COLUMN name_ varchar(255) NOT NULL COMMENT '变量/属性名';
ALTER TABLE act_hi_detail MODIFY COLUMN var_type_ varchar(255) DEFAULT NULL COMMENT '变量类型';
ALTER TABLE act_hi_detail MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_hi_detail MODIFY COLUMN time_ datetime(3) NOT NULL COMMENT '变更时间';
ALTER TABLE act_hi_detail MODIFY COLUMN bytearray_id_ varchar(64) DEFAULT NULL COMMENT '字节数组ID';
ALTER TABLE act_hi_detail MODIFY COLUMN double_ double DEFAULT NULL COMMENT 'double类型值';
ALTER TABLE act_hi_detail MODIFY COLUMN long_ bigint(20) DEFAULT NULL COMMENT 'long类型值';
ALTER TABLE act_hi_detail MODIFY COLUMN text_ varchar(4000) DEFAULT NULL COMMENT 'text类型值';
ALTER TABLE act_hi_detail MODIFY COLUMN text2_ varchar(4000) DEFAULT NULL COMMENT 'text类型值2';

-- act_hi_comment
ALTER TABLE act_hi_comment COMMENT 'ACT-历史评论（审批意见和流程事件评论）';
ALTER TABLE act_hi_comment MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_hi_comment MODIFY COLUMN type_ varchar(255) DEFAULT NULL COMMENT '评论类型（comment/event）';
ALTER TABLE act_hi_comment MODIFY COLUMN time_ datetime(3) NOT NULL COMMENT '评论时间';
ALTER TABLE act_hi_comment MODIFY COLUMN task_id_ varchar(64) DEFAULT NULL COMMENT '关联任务ID';
ALTER TABLE act_hi_comment MODIFY COLUMN user_id_ varchar(255) DEFAULT NULL COMMENT '评论人ID';
ALTER TABLE act_hi_comment MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '关联流程实例ID';
ALTER TABLE act_hi_comment MODIFY COLUMN action_ varchar(255) DEFAULT NULL COMMENT '操作类型';
ALTER TABLE act_hi_comment MODIFY COLUMN message_ varchar(4000) DEFAULT NULL COMMENT '评论消息';
ALTER TABLE act_hi_comment MODIFY COLUMN full_msg_ longblob COMMENT '完整消息（二进制）';

-- act_hi_attachment
ALTER TABLE act_hi_attachment COMMENT 'ACT-历史附件（流程实例和任务的附件记录）';
ALTER TABLE act_hi_attachment MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_hi_attachment MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_hi_attachment MODIFY COLUMN user_id_ varchar(255) DEFAULT NULL COMMENT '上传用户ID';
ALTER TABLE act_hi_attachment MODIFY COLUMN name_ varchar(255) DEFAULT NULL COMMENT '附件名称';
ALTER TABLE act_hi_attachment MODIFY COLUMN description_ varchar(4000) DEFAULT NULL COMMENT '附件描述';
ALTER TABLE act_hi_attachment MODIFY COLUMN type_ varchar(255) DEFAULT NULL COMMENT '附件类型';
ALTER TABLE act_hi_attachment MODIFY COLUMN task_id_ varchar(64) DEFAULT NULL COMMENT '关联任务ID';
ALTER TABLE act_hi_attachment MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '关联流程实例ID';
ALTER TABLE act_hi_attachment MODIFY COLUMN url_ varchar(4000) DEFAULT NULL COMMENT '附件URL';
ALTER TABLE act_hi_attachment MODIFY COLUMN content_id_ varchar(64) DEFAULT NULL COMMENT '附件内容ID（关联act_ge_bytearray）';
ALTER TABLE act_hi_attachment MODIFY COLUMN time_ datetime(3) DEFAULT NULL COMMENT '上传时间';

-- ==================== 其他 ====================

-- act_evt_log
ALTER TABLE act_evt_log COMMENT 'ACT-事件日志（引擎事件的可选日志记录）';
ALTER TABLE act_evt_log MODIFY COLUMN id_ int(11) NOT NULL AUTO_INCREMENT COMMENT '主键';
ALTER TABLE act_evt_log MODIFY COLUMN type_ varchar(255) DEFAULT NULL COMMENT '事件类型';
ALTER TABLE act_evt_log MODIFY COLUMN proc_def_id_ varchar(64) DEFAULT NULL COMMENT '流程定义ID';
ALTER TABLE act_evt_log MODIFY COLUMN proc_inst_id_ varchar(64) DEFAULT NULL COMMENT '流程实例ID';
ALTER TABLE act_evt_log MODIFY COLUMN execution_id_ varchar(64) DEFAULT NULL COMMENT '执行实例ID';
ALTER TABLE act_evt_log MODIFY COLUMN task_id_ varchar(64) DEFAULT NULL COMMENT '任务ID';
ALTER TABLE act_evt_log MODIFY COLUMN time_stamp_ datetime(3) NOT NULL COMMENT '事件时间戳';
ALTER TABLE act_evt_log MODIFY COLUMN user_id_ varchar(255) DEFAULT NULL COMMENT '操作用户ID';
ALTER TABLE act_evt_log MODIFY COLUMN data_ longblob COMMENT '事件数据（二进制）';
ALTER TABLE act_evt_log MODIFY COLUMN lock_owner_ varchar(255) DEFAULT NULL COMMENT '锁持有者';
ALTER TABLE act_evt_log MODIFY COLUMN lock_time_ datetime(3) DEFAULT NULL COMMENT '锁定时间';
ALTER TABLE act_evt_log MODIFY COLUMN is_processed_ tinyint(4) DEFAULT '0' COMMENT '是否已处理（0=未处理，1=已处理）';

-- act_procdef_info
ALTER TABLE act_procdef_info COMMENT 'ACT-流程定义信息（流程定义的JSON元数据快照）';
ALTER TABLE act_procdef_info MODIFY COLUMN id_ varchar(64) NOT NULL COMMENT '主键';
ALTER TABLE act_procdef_info MODIFY COLUMN proc_def_id_ varchar(64) NOT NULL COMMENT '流程定义ID';
ALTER TABLE act_procdef_info MODIFY COLUMN rev_ int(11) DEFAULT NULL COMMENT '版本号';
ALTER TABLE act_procdef_info MODIFY COLUMN info_json_id_ varchar(64) DEFAULT NULL COMMENT '元数据JSON ID（关联act_ge_bytearray）';
