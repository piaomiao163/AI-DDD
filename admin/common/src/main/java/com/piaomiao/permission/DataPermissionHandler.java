package com.piaomiao.permission;

import com.piaomiao.enums.DataPermissionType;


/**
 * 数据权限处理器接口
 */
public interface DataPermissionHandler {

    /**
     * 获取当前用户的数据权限范围
     * @param userId 用户ID
     * @param username 用户名
     * @param permissionType 权限类型
     * @param includeChildren 是否包含子部门
     * @param expression 自定义权限表达式
     * @return 数据权限范围
     */
    DataPermissionScope getPermissionScope(Long userId, String username, 
                                        DataPermissionType permissionType, 
                                        boolean includeChildren, 
                                        String expression);
}