package com.piaomiao.web.vo;

import lombok.Data;

/**
 * 终止流程实例请求VO
 */
@Data
public class TerminateProcessVO {
    /**
     * 终止原因（可选）
     */
    private String reason;
}
