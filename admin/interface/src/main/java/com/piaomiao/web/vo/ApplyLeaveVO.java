package com.piaomiao.web.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 申请请假请求VO
 */
@Data
public class ApplyLeaveVO {
    /**
     * 请假标题（必填）
     */
    private String title;
    /**
     * 请假类型 1年假 2事假 3病假 4婚假 5产假 6丧假 7其他（必填）
     */
    private Integer leaveType;
    /**
     * 开始日期（必填）
     */
    private String startDate;
    /**
     * 结束日期（必填）
     */
    private String endDate;
    /**
     * 请假天数（必填）
     */
    private BigDecimal days;
    /**
     * 请假原因（必填）
     */
    private String reason;
}
