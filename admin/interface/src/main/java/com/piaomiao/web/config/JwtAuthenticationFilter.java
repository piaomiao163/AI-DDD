package com.piaomiao.web.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.piaomiao.constants.SysConstants;
import com.piaomiao.response.Response;
import com.piaomiao.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 认证过滤器
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private CustomUserDetailsService userDetailsService;

    private ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 放行特定路径
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/login") || requestURI.equals("/captcha") || requestURI.equals("/register") || requestURI.startsWith("/demo")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 从请求头中获取 token
        String token = request.getHeader(SysConstants.Authorization);

        if (token != null && token.startsWith("Bearer ")) {
            // 提取 token
            token = token.substring(7);

            try {
                // 解析 token，获取用户名
                String username = JwtUtil.getUsernameFromToken(token);
                logger.debug("从Token中解析出的用户名：{}", username);

                // 如果用户名不为空
                if (username != null) {
                    // 获取用户详情
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    logger.debug("加载的用户详情：{}", userDetails.getUsername());

                    // 验证 token
                    if (JwtUtil.validateToken(token, userDetails.getUsername())) {
                        // 创建认证对象
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        // 设置认证信息
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("用户 {} 认证成功", username);
                    } else {
                        logger.warn("Token 验证失败，用户：{}", username);
                        sendErrorResponse(response, 401, "Token 验证失败，请重新登录");
                        return;
                    }
                }
            } catch (io.jsonwebtoken.ExpiredJwtException e) {
                logger.error("Token 已过期：{}", e.getMessage());
                sendErrorResponse(response, 401, "Token 已过期，请重新登录");
                return;
            } catch (io.jsonwebtoken.SignatureException e) {
                logger.error("Token 签名无效：{}", e.getMessage());
                sendErrorResponse(response, 401, "Token 签名无效，请重新登录");
                return;
            } catch (io.jsonwebtoken.MalformedJwtException e) {
                logger.error("Token 格式错误：{}", e.getMessage());
                sendErrorResponse(response, 401, "Token 格式错误，请重新登录");
                return;
            } catch (io.jsonwebtoken.UnsupportedJwtException e) {
                logger.error("Token 不支持：{}", e.getMessage());
                sendErrorResponse(response, 401, "Token 不支持，请重新登录");
                return;
            } catch (Exception e) {
                logger.error("Token 解析异常：{}", e.getMessage(), e);
                sendErrorResponse(response, 401, "Token 无效，请重新登录");
                return;
            }
        }

        // 继续过滤链
        filterChain.doFilter(request, response);
    }

    /**
     * 发送错误响应
     * @param response HTTP 响应
     * @param code 错误码
     * @param message 错误消息
     */
    private void sendErrorResponse(HttpServletResponse response, int code, String message) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> errorData = new HashMap<>();
        errorData.put("code", code);
        errorData.put("success", false);
        errorData.put("message", message);

        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(errorData));
        writer.flush();
    }
}
