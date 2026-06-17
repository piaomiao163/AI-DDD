package com.piaomiao.web.vo;

import lombok.Data;

import java.util.Map;

/**
 * 流程实例VO
 */
@Data
public class ProcessInstanceVO {
    private Long id;
    private String instanceNo;
    private String title;
    private String processInstanceId;
    private String processDefinitionKey;
    private Long processDefinitionDbId;
    private String processName;
    private String businessKey;
    private String businessType;
    private Long businessId;
    private String startUserId;
    private Long startUserDbId;
    private String startUserName;
    private Long applicantDeptId;
    private String startTime;
    private String endTime;
    private Integer status;
    private String currentNodeName;
    private String currentNodeId;
    private String currentAssignees;
    private Integer totalNodes;
    private Integer completedNodes;
    private Integer priority;
    private String deleteReason;
    private Map<String, Object> variables;
    private String createTime;
    private String updateTime;
}
