package com.piaomiao.web.vo;

import lombok.Data;

/**
 * 撤回流程请求VO
 */
@Data
public class WithdrawProcessVO {
    /**
     * 撤回原因（可选）
     */
    private String reason;
}
