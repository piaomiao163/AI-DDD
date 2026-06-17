package com.piaomiao.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 请假单DTO
 */
@Data
public class LeaveDTO {
    private Long id;
    private String title;
    private Integer leaveType;
    private String startDate;
    private String endDate;
    private BigDecimal days;
    private String reason;
    private Long applicantId;
    private String applicantName;
    private String deptName;
    private Integer status;
    private Long processInstanceId;
    private String approveComment;
    private String processDefinitionKey;
    private String currentNodeName;
    private String createBy;
    private String createTime;
    private String updateTime;
}
