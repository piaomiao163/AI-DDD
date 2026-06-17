package com.piaomiao.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 请假审批详情DTO（供抽屉组件使用）
 */
@Data
public class LeaveApprovalDetailDTO {
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
    private String approveComment;
    private String leaveCreateTime;
    private String leaveUpdateTime;
    // 任务字段
    private String taskId;
    private String taskName;
    private String actProcessInstanceId;
}
