package com.piaomiao.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 */
@Component
public class JwtUtil {

    /**
     * 密钥
     */
    private static String secretKey;

    /**
     * 过期时间（毫秒）
     */
    private static long expireTime;

    /**
     * 设置JWT密钥
     * @param configuredSecretKey 配置的JWT密钥
     */
    @Value("${jwt.secret}")
    public void setSecretKey(String configuredSecretKey) {
        JwtUtil.secretKey = configuredSecretKey;
    }

    /**
     * 设置过期时间
     * @param configuredExpireTime 配置的过期时间（毫秒）
     */
    @Value("${jwt.expire-time:86400000}")
    public void setExpireTime(long configuredExpireTime) {
        JwtUtil.expireTime = configuredExpireTime;
    }

    /**
     * 生成 token
     * @param username 用户名
     * @return token
     */
    public static String generateToken(String username) {
        // 创建 claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        // 创建 token
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * 解析 token
     * @param token token
     * @return claims
     */
    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 从 token 中获取用户名
     * @param token token
     * @return 用户名
     */
    public static String getUsernameFromToken(String token) {
        return parseToken(token).get("username", String.class);
    }

    /**
     * 验证 token 是否过期
     * @param token token
     * @return 是否过期
     */
    public static boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    /**
     * 验证 token 是否有效
     * @param token token
     * @param username 用户名
     * @return 是否有效
     */
    public static boolean validateToken(String token, String username) {
        return getUsernameFromToken(token).equals(username) && !isTokenExpired(token);
    }
}