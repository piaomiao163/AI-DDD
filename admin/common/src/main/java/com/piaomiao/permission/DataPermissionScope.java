package com.piaomiao.permission;

import java.util.List;

/**
 * 数据权限范围
 */
public class DataPermissionScope {
    /**
     * 部门ID列表
     */
    private List<Long> departmentIds;
    
    /**
     * 用户ID列表
     */
    private List<Long> userIds;
    
    /**
     * 是否全部数据权限
     */
    private boolean allData;
    
    /**
     * 是否本人数据权限
     */
    private boolean selfData;
    
    public DataPermissionScope() {
    }
    
    public DataPermissionScope(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }
    
    public DataPermissionScope(List<Long> departmentIds, List<Long> userIds) {
        this.departmentIds = departmentIds;
        this.userIds = userIds;
    }
    
    public List<Long> getDepartmentIds() {
        return departmentIds;
    }
    
    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }
    
    public List<Long> getUserIds() {
        return userIds;
    }
    
    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }
    
    public boolean isAllData() {
        return allData;
    }
    
    public void setAllData(boolean allData) {
        this.allData = allData;
    }
    
    public boolean isSelfData() {
        return selfData;
    }
    
    public void setSelfData(boolean selfData) {
        this.selfData = selfData;
    }
}