package com.piaomiao.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 任务领域模型
 */
@Data
public class TaskModel {
    /**
     * Activiti任务ID
     */
    private String taskId;

    /**
     * 任务定义Key
     */
    private String taskDefinitionKey;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 流程定义ID
     */
    private String processDefinitionId;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 流程定义Key
     */
    private String processDefinitionKey;

    /**
     * 任务处理人
     */
    private String assignee;

    /**
     * 任务拥有者
     */
    private String owner;

    /**
     * 任务创建时间
     */
    private LocalDateTime createTime;

    /**
     * 到期时间
     */
    private LocalDateTime dueDate;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 审批标题(来自流程实例)
     */
    private String title;

    /**
     * 业务类型(来自流程实例,如leave/expense)
     */
    private String businessType;

    /**
     * 业务单据主键ID(来自流程实例)
     */
    private Long businessId;

    /**
     * 流程变量
     */
    private Map<String, Object> processVariables;

    /**
     * 任务本地变量
     */
    private Map<String, Object> taskLocalVariables;

    /**
     * 认领时间
     */
    private LocalDateTime claimTime;

    /**
     * 完成时间
     */
    private LocalDateTime endTime;

    /**
     * 删除原因
     */
    private String deleteReason;

    /**
     * 审批结果：approved / rejected
     */
    private String outcome;
}
