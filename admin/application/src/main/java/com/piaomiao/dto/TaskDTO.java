package com.piaomiao.dto;

import lombok.Data;

import java.util.Map;

/**
 * 任务DTO
 */
@Data
public class TaskDTO {
    private String taskId;
    private String taskDefinitionKey;
    private String taskName;
    private String processInstanceId;
    private String processDefinitionId;
    private String processName;
    private String processDefinitionKey;
    private String assignee;
    private String owner;
    private String createTime;
    private String dueDate;
    private String description;
    private Integer priority;
    private String claimTime;
    private String endTime;
    private String deleteReason;
    private String outcome;
    private String title;
    private String businessType;
    private Long businessId;
    private Map<String, Object> processVariables;
}
