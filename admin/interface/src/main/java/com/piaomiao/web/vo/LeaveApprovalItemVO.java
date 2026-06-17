package com.piaomiao.web.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 请假审批列表项响应VO
 */
@Data
public class LeaveApprovalItemVO {
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
    private String taskId;
    private String taskName;
    private String taskCreateTime;
}
