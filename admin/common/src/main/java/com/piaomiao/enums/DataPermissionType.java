package com.piaomiao.enums;

/**
 * 数据权限类型枚举
 */
public enum DataPermissionType {
    /**
     * 部门权限
     */
    DEPARTMENT("department", "部门权限"),
    
    /**
     * 用户权限
     */
    USER("user", "用户权限"),
    
    /**
     * 全部数据权限
     */
    ALL("all", "全部数据权限"),
    
    /**
     * 自定义权限
     */
    CUSTOM("custom", "自定义权限"),
    
    /**
     * 仅本人数据权限
     */
    SELF("self", "本人数据权限");
    
    private final String code;
    private final String description;
    
    DataPermissionType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}