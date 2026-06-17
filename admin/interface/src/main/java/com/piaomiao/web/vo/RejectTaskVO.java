package com.piaomiao.web.vo;

import lombok.Data;

/**
 * 驳回任务请求VO
 */
@Data
public class RejectTaskVO {
    /**
     * 流程实例ID（必填）
     */
    private String processInstanceId;
    /**
     * 驳回原因（必填）
     */
    private String reason;
}
