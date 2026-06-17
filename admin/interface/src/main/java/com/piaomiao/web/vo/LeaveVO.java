package com.piaomiao.web.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 请假单响应VO
 */
@Data
public class LeaveVO {
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
    private String currentNodeName;
    private String createTime;
    private String updateTime;
}
