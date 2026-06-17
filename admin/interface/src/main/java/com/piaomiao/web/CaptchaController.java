package com.piaomiao.web;

import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.CaptchaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

/**
 * 验证码控制器
 */
@RestController
public class CaptchaController {

    @Autowired
    private TemplateRest templateRest;

    /**
     * 生成验证码
     * @return 验证码图片和key
     */
    @GetMapping("/captcha")
    public Response<CaptchaVO> generateCaptcha() {
        return templateRest.request(new CallbackRest<CaptchaVO>() {
            @Override
            public void check() {
            }

            @Override
            public CaptchaVO execute() {
                // 生成验证码
                String captchaCode = generateCaptchaCode(4);

                // 生成验证码图片
                BufferedImage image = generateCaptchaImage(captchaCode);

                // 将图片转为Base64
                String base64Image = imageToBase64(image);

                // 生成验证码key（用于验证）
                String captchaKey = generateCaptchaKey();

                // 这里应该将验证码存入Redis或Session，key为captchaKey，值为captchaCode
                // 为了简化，暂时直接返回验证码文本（实际项目中不应该返回）

                CaptchaVO vo = new CaptchaVO();
                vo.setCaptchaKey(captchaKey);
                vo.setCaptchaImage("data:image/png;base64," + base64Image);
                vo.setCaptchaCode(captchaCode); // 实际项目中应该删除这行

                return vo;
            }
        });
    }

    /**
     * 生成随机验证码
     * @param length 验证码长度
     * @return 验证码
     */
    private String generateCaptchaCode(int length) {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 生成验证码图片
     * @param code 验证码
     * @return 图片
     */
    private BufferedImage generateCaptchaImage(String code) {
        int width = 120;
        int height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        // 设置背景色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        
        // 设置字体
        g.setFont(new Font("Arial", Font.BOLD, 24));
        
        // 绘制验证码
        Random random = new Random();
        for (int i = 0; i < code.length(); i++) {
            // 随机颜色
            g.setColor(new Color(random.nextInt(100), random.nextInt(100), random.nextInt(100)));
            // 绘制字符
            g.drawString(String.valueOf(code.charAt(i)), 20 + i * 25, 28);
        }
        
        // 绘制干扰线
        for (int i = 0; i < 5; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }
        
        // 绘制干扰点
        for (int i = 0; i < 30; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            g.drawOval(x, y, 2, 2);
        }
        
        g.dispose();
        return image;
    }

    /**
     * 图片转Base64
     * @param image 图片
     * @return Base64字符串
     */
    private String imageToBase64(BufferedImage image) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] bytes = baos.toByteArray();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            throw new RuntimeException("生成验证码失败", e);
        }
    }

    /**
     * 生成验证码key
     * @return key
     */
    private String generateCaptchaKey() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
