package com.piaomiao.dal.util;

import com.piaomiao.util.UserContext;

import java.time.LocalDateTime;

/**
 * 审计字段手动填充工具
 */
public class AuditFieldUtil {

    public static void fillForInsert(Object entity) {
        setField(entity, "setCreateTime", LocalDateTime.now());
        setField(entity, "setUpdateTime", LocalDateTime.now());
        setField(entity, "setCreateBy", getCurrentUsername());
        setField(entity, "setUpdateBy", getCurrentUsername());
    }

    public static void fillForUpdate(Object entity) {
        setField(entity, "setUpdateTime", LocalDateTime.now());
        setField(entity, "setUpdateBy", getCurrentUsername());
    }

    private static void setField(Object entity, String setter, Object value) {
        try {
            entity.getClass().getMethod(setter, value.getClass()).invoke(entity, value);
        } catch (Exception e) {
            // 字段不存在则跳过
        }
    }

    private static String getCurrentUsername() {
        try {
            String username = UserContext.getCurrentUsername();
            return username != null ? username : "system";
        } catch (Exception e) {
            return "system";
        }
    }
}
