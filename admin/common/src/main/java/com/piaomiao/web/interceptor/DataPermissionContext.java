package com.piaomiao.web.interceptor;

import com.piaomiao.permission.DataPermissionScope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 数据权限上下文
 */
@Component
public class DataPermissionContext {

    /**
     * 线程本地变量，存储当前用户可访问的部门ID列表
     */
    private static final ThreadLocal<List<Long>> DEPARTMENT_IDS = new ThreadLocal<>();

    /**
     * 线程本地变量，存储当前用户ID列表
     */
    private static final ThreadLocal<List<Long>> USER_IDS = new ThreadLocal<>();

    /**
     * 线程本地变量，存储当前用户ID
     */
    private static final ThreadLocal<Long> CURRENT_USER_ID = new ThreadLocal<>();

    /**
     * 线程本地变量，存储当前用户名
     */
    private static final ThreadLocal<String> CURRENT_USERNAME = new ThreadLocal<>();

    /**
     * 线程本地变量，存储是否全部数据权限
     */
    private static final ThreadLocal<Boolean> ALL_DATA = new ThreadLocal<>();

    /**
     * 线程本地变量，存储是否本人数据权限
     */
    private static final ThreadLocal<Boolean> SELF_DATA = new ThreadLocal<>();

    /**
     * 设置数据权限范围
     * @param scope 数据权限范围
     */
    public static void setPermissionScope(DataPermissionScope scope) {
        if (scope == null) {
            return;
        }
        
        DEPARTMENT_IDS.set(scope.getDepartmentIds());
        USER_IDS.set(scope.getUserIds());
        ALL_DATA.set(scope.isAllData());
        SELF_DATA.set(scope.isSelfData());
    }

    /**
     * 设置当前用户可访问的部门ID列表
     * @param departmentIds 部门ID列表
     */
    public static void setDepartmentIds(List<Long> departmentIds) {
        DEPARTMENT_IDS.set(departmentIds);
    }

    /**
     * 获取当前用户可访问的部门ID列表
     * @return 部门ID列表
     */
    public static List<Long> getCurrentUserDepartmentIds() {
        return DEPARTMENT_IDS.get();
    }

    /**
     * 获取当前用户的部门ID
     * @return 部门ID
     */
    public static Long getCurrentDepartmentId() {
        List<Long> departmentIds = DEPARTMENT_IDS.get();
        return departmentIds != null && !departmentIds.isEmpty() ? departmentIds.get(0) : null;
    }

    /**
     * 获取当前用户可访问的用户ID列表
     * @return 用户ID列表
     */
    public static List<Long> getCurrentUserIds() {
        return USER_IDS.get();
    }

    /**
     * 设置当前用户可访问的用户ID列表
     * @param userIds 用户ID列表
     */
    public static void setUserIds(List<Long> userIds) {
        USER_IDS.set(userIds);
    }

    /**
     * 设置当前用户信息
     * @param userId 用户ID
     * @param username 用户名
     */
    public static void setCurrentUser(Long userId, String username) {
        CURRENT_USER_ID.set(userId);
        CURRENT_USERNAME.set(username);
    }

    /**
     * 获取当前用户ID
     * @return 用户ID
     */
    public static Long getCurrentUserId() {
        return CURRENT_USER_ID.get();
    }

    /**
     * 获取当前用户名
     * @return 用户名
     */
    public static String getCurrentUsername() {
        return CURRENT_USERNAME.get();
    }

    /**
     * 是否全部数据权限
     * @return 是否全部数据权限
     */
    public static boolean isAllData() {
        Boolean allData = ALL_DATA.get();
        return allData != null && allData;
    }

    /**
     * 是否本人数据权限
     * @return 是否本人数据权限
     */
    public static boolean isSelfData() {
        Boolean selfData = SELF_DATA.get();
        return selfData != null && selfData;
    }

    /**
     * 清除线程本地变量
     */
    public static void clear() {
        DEPARTMENT_IDS.remove();
        USER_IDS.remove();
        CURRENT_USER_ID.remove();
        CURRENT_USERNAME.remove();
        ALL_DATA.remove();
        SELF_DATA.remove();
    }
}