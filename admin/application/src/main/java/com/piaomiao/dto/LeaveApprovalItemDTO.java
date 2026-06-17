package com.piaomiao.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 请假审批列表项DTO
 */
@Data
public class LeaveApprovalItemDTO {
    // 请假单字段
    private Long leaveId;
    private String title;
    private Integer leaveType;
    private String startDate;
    private String endDate;
    private BigDecimal days;
    private String reason;
    private String applicantName;
    private String deptName;
    private Integer status;
    private String currentNodeName;
    private Long processInstanceId;
    private String leaveCreateTime;
    // 任务字段
    private String taskId;
    private String taskName;
    private String taskCreateTime;
}
