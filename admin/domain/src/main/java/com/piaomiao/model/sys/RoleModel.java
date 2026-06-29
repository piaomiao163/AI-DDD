package com.piaomiao.model.sys;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 角色模型
 */
@Data
public class RoleModel {
    /**
     * 角色ID
     */
    private Long id;
    
    /**
     * 角色名称
     */
    private String name;
    
    /**
     * 角色编码
     */
    private String code;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 是否删除
     */
    private Integer deleted;

    /**
     * 关联的权限列表
     */
    private List<PermissionModel> permissions;
    
    /**
     * 关联的菜单列表
     */
    private List<MenuModel> menus;
}