package com.piaomiao.web.vo;

import lombok.Data;

/**
 * 操作结果VO（用于登出、修改密码等简单响应）
 */
@Data
public class OperationResultVO {
    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 消息
     */
    private String message;

    public static OperationResultVO success(String message) {
        OperationResultVO vo = new OperationResultVO();
        vo.setSuccess(true);
        vo.setMessage(message);
        return vo;
    }
}
