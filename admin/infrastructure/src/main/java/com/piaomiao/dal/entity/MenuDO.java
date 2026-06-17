package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 菜单实体
 */
@TableName("sys_menu")
public class MenuDO {
    /**
     * 菜单ID
     */
    private Long id;
    
    /**
     * 菜单名称
     */
    private String name;
    
    /**
     * 菜单路径
     */
    private String path;
    
    /**
     * 菜单图标
     */
    private String icon;
    
    /**
     * 父菜单ID
     */
    private Long parentId;
    
    /**
     * 排序
     */
    private Integer sort;
    
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    /**
     * 转换为模型
     * @return 菜单模型
     */
    public com.piaomiao.model.MenuModel toModel() {
        com.piaomiao.model.MenuModel menuModel = new com.piaomiao.model.MenuModel();
        menuModel.setId(id);
        menuModel.setName(name);
        menuModel.setPath(path);
        menuModel.setIcon(icon);
        menuModel.setParentId(parentId);
        menuModel.setSort(sort);
        menuModel.setStatus(status);
        menuModel.setCreateTime(createTime);
        menuModel.setUpdateTime(updateTime);
        menuModel.setDeleted(deleted);
        return menuModel;
    }

    /**
     * 从模型转换
     * @param menuModel 菜单模型
     * @return 菜单实体
     */
    public static MenuDO fromModel(com.piaomiao.model.MenuModel menuModel) {
        MenuDO menuDO = new MenuDO();
        menuDO.setId(menuModel.getId());
        menuDO.setName(menuModel.getName());
        menuDO.setPath(menuModel.getPath());
        menuDO.setIcon(menuModel.getIcon());
        menuDO.setParentId(menuModel.getParentId());
        menuDO.setSort(menuModel.getSort());
        menuDO.setStatus(menuModel.getStatus());
        menuDO.setCreateTime(menuModel.getCreateTime());
        menuDO.setUpdateTime(menuModel.getUpdateTime());
        menuDO.setDeleted(menuModel.getDeleted());
        return menuDO;
    }
}