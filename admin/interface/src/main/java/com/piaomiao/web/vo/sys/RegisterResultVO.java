package com.piaomiao.web.vo.sys;

import lombok.Data;

/**
 * 注册结果VO
 */
@Data
public class RegisterResultVO {
    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 消息
     */
    private String message;

    /**
     * JWT令牌
     */
    private String token;

    /**
     * 用户名
     */
    private String username;
}
