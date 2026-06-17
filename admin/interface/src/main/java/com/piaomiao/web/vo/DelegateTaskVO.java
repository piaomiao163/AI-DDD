package com.piaomiao.web.vo;

import lombok.Data;

/**
 * 委派任务请求VO
 */
@Data
public class DelegateTaskVO {
    /**
     * 目标用户ID（必填）
     */
    private String delegateUserId;
}
