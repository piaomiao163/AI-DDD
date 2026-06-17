package com.piaomiao.web.aspect;

import com.piaomiao.annotation.DataPermission;
import com.piaomiao.enums.DataPermissionType;
import com.piaomiao.model.UserModel;
import com.piaomiao.permission.DataPermissionHandler;
import com.piaomiao.permission.DataPermissionScope;
import com.piaomiao.service.UserService;
import com.piaomiao.web.interceptor.DataPermissionContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 数据权限切面
 */
@Aspect
@Component
public class DataPermissionAspect {

    @Autowired
    private UserService userService;

    @Autowired
    private DataPermissionHandler dataPermissionHandler;

    @Around("@annotation(com.piaomiao.annotation.DataPermission)")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        // 获取当前用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return point.proceed();
        }

        // 获取用户权限信息
        String username = authentication.getName();
        UserModel user = userService.findByUsername(username);
        if (user == null) {
            return point.proceed();
        }

        // 存储当前用户信息
        DataPermissionContext.setCurrentUser(user.getId(), username);

        // 获取方法签名
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataPermission dataPermissionAnnotation = signature.getMethod().getAnnotation(DataPermission.class);

        if (dataPermissionAnnotation != null && dataPermissionAnnotation.enabled()) {
            // 获取数据权限范围
            DataPermissionScope scope = dataPermissionHandler.getPermissionScope(
                user.getId(), 
                username, 
                dataPermissionAnnotation.value(), 
                dataPermissionAnnotation.includeChildren(), 
                dataPermissionAnnotation.expression()
            );

            // 设置数据权限范围
            DataPermissionContext.setPermissionScope(scope);

            // 打印用户信息和权限范围
            System.out.println("DataPermissionAspect: User " + username + " (ID: " + user.getId() + ") permission scope: " + 
                dataPermissionAnnotation.value() + ", includeChildren: " + dataPermissionAnnotation.includeChildren());
            System.out.println("DataPermissionAspect: All data: " + scope.isAllData() + ", Self data: " + scope.isSelfData());
            System.out.println("DataPermissionAspect: Department IDs: " + scope.getDepartmentIds() + ", User IDs: " + scope.getUserIds());
        }

        try {
            // 执行方法
            return point.proceed();
        } finally {
            // 清除线程本地变量
            DataPermissionContext.clear();
        }
    }
}