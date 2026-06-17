package com.piaomiao.web.vo;

import lombok.Data;

/**
 * 登录结果VO
 */
@Data
public class LoginResultVO {
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

    /**
     * 用户昵称
     */
    private String nickname;
}
