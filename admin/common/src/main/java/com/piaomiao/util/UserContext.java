package com.piaomiao.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 当前登录用户上下文工具类
 * <p>
 * 基于 Spring Security 的 SecurityContextHolder 获取当前登录用户信息，
 * 可在任何层（Controller / Command / Service / Repository）直接使用，无需额外注入。
 * <p>
 * 使用示例:
 * <pre>
 *   Long userId = UserContext.getCurrentUserId();
 *   String username = UserContext.getCurrentUsername();
 *   if (UserContext.isAuthenticated()) { ... }
 * </pre>
 */
public class UserContext {

    private UserContext() {
    }

    /**
     * 判断当前请求是否已认证
     *
     * @return true=已登录
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal());
    }

    /**
     * 获取当前登录用户名
     *
     * @return 用户名，未登录返回 null
     */
    public static String getCurrentUsername() {
        if (!isAuthenticated()) {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * 获取当前登录用户ID
     * <p>
     * 注意：Spring Security 默认的 UserDetails 不包含用户ID，
     * 此方法从 Authentication 的 principal 中提取。
     * 当前 principal 类型为 Spring Security 内置 User 对象，仅包含 username。
     * <p>
     * 如需获取用户ID，请通过 UserService.findByUsername() 查询，
     * 或使用 DataPermissionContext.getCurrentUserId()（仅在 @DataPermission 作用域内可用）。
     *
     * @return 用户ID，未登录或无法获取时返回 null
     */
    public static Long getCurrentUserId() {
        // 优先从 DataPermissionContext 获取（@DataPermission 作用域内）
        Long userId = com.piaomiao.web.interceptor.DataPermissionContext.getCurrentUserId();
        if (userId != null) {
            return userId;
        }
        // DataPermissionContext 未设置时返回 null
        // 调用方需通过 UserService.findByUsername() 查询用户ID
        return null;
    }

    /**
     * 要求当前用户必须已登录，否则抛出 IllegalStateException
     *
     * @return 当前用户名
     */
    public static String requireCurrentUsername() {
        String username = getCurrentUsername();
        if (username == null) {
            throw new IllegalStateException("当前未登录，无法获取用户信息");
        }
        return username;
    }
}
