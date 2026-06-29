package com.piaomiao.model.sys;

import com.piaomiao.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 权限模型
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionModel extends BaseModel {
    /**
     * 权限ID
     */
    private Long id;
    
    /**
     * 权限名称
     */
    private String name;
    
    /**
     * 权限编码
     */
    private String code;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 权限类型：1-菜单权限，2-按钮权限，3-数据权限
     */
    private Integer type;
    
    /**
     * 关联的菜单ID
     */
    private Long menuId;
    
    /**
     * 父级权限ID
     */
    private Long parentId;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 子权限列表
     */
    private List<PermissionModel> children;


}