package com.piaomiao.web.config;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 自定义权限评估器
 * 超级管理员(ROLE_admin)默认拥有所有权限
 */
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        System.out.println("检查权限: " + permission);
        System.out.println("用户权限列表: " + authentication.getAuthorities());

        // 检查是否是超级管理员
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_admin".equals(authority.getAuthority())) {
                return true;
            }
        }

        // 非超级管理员，检查具体权限
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (permission.toString().equals(authority.getAuthority())) {
                System.out.println("权限匹配成功: " + permission);
                return true;
            }
        }

        System.out.println("权限匹配失败: " + permission);
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return hasPermission(authentication, null, permission);
    }
}
