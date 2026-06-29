package com.piaomiao.permission.impl;

import com.piaomiao.enums.DataPermissionType;
import com.piaomiao.model.sys.UserModel;
import com.piaomiao.permission.DataPermissionHandler;
import com.piaomiao.permission.DataPermissionScope;
import com.piaomiao.service.sys.DepartmentService;
import com.piaomiao.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认数据权限处理器
 */
@Component
public class DefaultDataPermissionHandler implements DataPermissionHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Override
    public DataPermissionScope getPermissionScope(Long userId, String username, 
                                               DataPermissionType permissionType, 
                                               boolean includeChildren, 
                                               String expression) {
        DataPermissionScope scope = new DataPermissionScope();
        
        // 首先检查用户是否有admin角色，如果有，直接赋予全部数据权限
        UserModel user = userService.findById(userId);
        if (user != null && user.getRoles() != null) {
            for (com.piaomiao.model.sys.RoleModel role : user.getRoles()) {
                if ("admin".equals(role.getCode())) {
                    scope.setAllData(true);
                    return scope;
                }
            }
        }
        
        switch (permissionType) {
            case ALL:
                scope.setAllData(true);
                break;
                
            case SELF:
                scope.setSelfData(true);
                break;
                
            case USER:
                List<Long> userIds = new ArrayList<>();
                userIds.add(userId);
                scope.setUserIds(userIds);
                break;
                
            case DEPARTMENT:
            default:
                if (user != null && user.getDepartmentId() != null) {
                    List<Long> departmentIds = new ArrayList<>();
                    if (includeChildren) {
                        List<Long> childDeptIds = departmentService.getAllChildDepartmentIds(user.getDepartmentId());
                        departmentIds.addAll(childDeptIds);
                    } else {
                        departmentIds.add(user.getDepartmentId());
                    }
                    scope.setDepartmentIds(departmentIds);
                }
                break;
                
            case CUSTOM:
                if (expression != null && !expression.isEmpty()) {
                    parseCustomExpression(expression, scope);
                }
                break;
        }
        
        return scope;
    }
    
    /**
     * 解析自定义权限表达式
     * @param expression 权限表达式
     * @param scope 权限范围
     */
    private void parseCustomExpression(String expression, DataPermissionScope scope) {
        String[] parts = expression.split(":");
        if (parts.length != 2) {
            return;
        }
        
        String type = parts[0];
        String values = parts[1];
        
        if ("department".equals(type)) {
            List<Long> departmentIds = new ArrayList<>();
            for (String value : values.split(",")) {
                try {
                    departmentIds.add(Long.parseLong(value.trim()));
                } catch (NumberFormatException e) {
                }
            }
            scope.setDepartmentIds(departmentIds);
        } else if ("user".equals(type)) {
            List<Long> userIds = new ArrayList<>();
            for (String value : values.split(",")) {
                try {
                    userIds.add(Long.parseLong(value.trim()));
                } catch (NumberFormatException e) {
                }
            }
            scope.setUserIds(userIds);
        }
    }
}
