package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 权限实体
 */
@TableName("sys_permission")
public class PermissionDO {
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
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 角色ID（用于批量查询时关联角色，非数据库表字段）
     */
    @TableField(exist = false)
    private Long roleId;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    /**
     * 转换为模型
     * @return 权限模型
     */
    public com.piaomiao.model.sys.PermissionModel toModel() {
        com.piaomiao.model.sys.PermissionModel permissionModel = new com.piaomiao.model.sys.PermissionModel();
        permissionModel.setId(id);
        permissionModel.setName(name);
        permissionModel.setCode(code);
        permissionModel.setDescription(description);
        permissionModel.setType(type);
        permissionModel.setMenuId(menuId);
        permissionModel.setParentId(parentId);
        permissionModel.setRoleId(roleId);
        permissionModel.setCreateTime(createTime);
        permissionModel.setUpdateTime(updateTime);
        return permissionModel;
    }

    /**
     * 从模型转换
     * @param permissionModel 权限模型
     * @return 权限实体
     */
    public static PermissionDO fromModel(com.piaomiao.model.sys.PermissionModel permissionModel) {
        PermissionDO permissionDO = new PermissionDO();
        permissionDO.setId(permissionModel.getId());
        permissionDO.setName(permissionModel.getName());
        permissionDO.setCode(permissionModel.getCode());
        permissionDO.setDescription(permissionModel.getDescription());
        permissionDO.setType(permissionModel.getType());
        permissionDO.setMenuId(permissionModel.getMenuId());
        permissionDO.setParentId(permissionModel.getParentId());
        permissionDO.setRoleId(permissionModel.getRoleId());
        permissionDO.setCreateTime(permissionModel.getCreateTime());
        permissionDO.setUpdateTime(permissionModel.getUpdateTime());
        return permissionDO;
    }
}