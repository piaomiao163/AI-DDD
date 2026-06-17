package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 角色实体
 */
@TableName("sys_role")
public class RoleDO {
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
    @TableLogic
    private Integer deleted;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    /**
     * 转换为模型
     * @return 角色模型
     */
    public com.piaomiao.model.RoleModel toModel() {
        com.piaomiao.model.RoleModel roleModel = new com.piaomiao.model.RoleModel();
        roleModel.setId(id);
        roleModel.setName(name);
        roleModel.setCode(code);
        roleModel.setDescription(description);
        roleModel.setStatus(status);
        roleModel.setCreateTime(createTime);
        roleModel.setUpdateTime(updateTime);
        roleModel.setCreateBy(createBy);
        roleModel.setUpdateBy(updateBy);
        roleModel.setDeleted(deleted);
        roleModel.setPermissions(new java.util.ArrayList<>());
        roleModel.setMenus(new java.util.ArrayList<>());
        return roleModel;
    }

    /**
     * 从模型转换
     * @param roleModel 角色模型
     * @return 角色实体
     */
    public static RoleDO fromModel(com.piaomiao.model.RoleModel roleModel) {
        RoleDO roleDO = new RoleDO();
        roleDO.setId(roleModel.getId());
        roleDO.setName(roleModel.getName());
        roleDO.setCode(roleModel.getCode());
        roleDO.setDescription(roleModel.getDescription());
        roleDO.setStatus(roleModel.getStatus());
        roleDO.setCreateTime(roleModel.getCreateTime());
        roleDO.setUpdateTime(roleModel.getUpdateTime());
        roleDO.setCreateBy(roleModel.getCreateBy());
        roleDO.setUpdateBy(roleModel.getUpdateBy());
        roleDO.setDeleted(roleModel.getDeleted());
        return roleDO;
    }
}