package com.piaomiao.annotation;

import com.piaomiao.enums.DataPermissionType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 数据权限注解
 * 用于标记需要进行数据权限控制的方法
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPermission {
    /**
     * 权限类型
     */
    DataPermissionType value() default DataPermissionType.DEPARTMENT;

    /**
     * 是否启用数据权限控制
     */
    boolean enabled() default true;

    /**
     * 是否包含子部门
     */
    boolean includeChildren() default true;

    /**
     * 自定义权限表达式
     */
    String expression() default "";
}