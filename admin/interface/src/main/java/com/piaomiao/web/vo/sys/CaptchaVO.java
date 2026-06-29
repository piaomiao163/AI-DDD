package com.piaomiao.web.vo.sys;

import lombok.Data;

/**
 * 验证码VO
 */
@Data
public class CaptchaVO {
    /**
     * 验证码key（用于验证时传回）
     */
    private String captchaKey;

    /**
     * 验证码图片（Base64编码，含data:image/png;base64,前缀）
     */
    private String captchaImage;

    /**
     * 验证码文本（仅开发环境返回，生产环境应移除）
     */
    private String captchaCode;
}
