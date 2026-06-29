package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 部门数据访问对象
 */
@TableName("sys_department")
public class DepartmentDO {
    /**
     * 部门ID
     */
    private Long id;

    /**
     * 部门名称
     */
    private String name;

    /**
     * 部门编码
     */
    private String code;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 部门层级
     */
    private Integer level;

    /**
     * 部门排序
     */
    private Integer sort;

    /**
     * 部门状态
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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
     * @return 部门模型
     */
    public com.piaomiao.model.sys.DepartmentModel toModel() {
        com.piaomiao.model.sys.DepartmentModel model = new com.piaomiao.model.sys.DepartmentModel();
        model.setId(id);
        model.setName(name);
        model.setCode(code);
        model.setParentId(parentId);
        model.setLevel(level);
        model.setSort(sort);
        model.setStatus(status);
        model.setCreateTime(createTime);
        model.setUpdateTime(updateTime);
        model.setCreateBy(createBy);
        model.setUpdateBy(updateBy);
        model.setDeleted(deleted);
        return model;
    }

    /**
     * 从模型转换
     * @param model 部门模型
     * @return 部门数据访问对象
     */
    public static DepartmentDO fromModel(com.piaomiao.model.sys.DepartmentModel model) {
        DepartmentDO DO = new DepartmentDO();
        DO.setId(model.getId());
        DO.setName(model.getName());
        DO.setCode(model.getCode());
        DO.setParentId(model.getParentId());
        DO.setLevel(model.getLevel());
        DO.setSort(model.getSort());
        DO.setStatus(model.getStatus());
        DO.setCreateTime(model.getCreateTime());
        DO.setUpdateTime(model.getUpdateTime());
        DO.setCreateBy(model.getCreateBy());
        DO.setUpdateBy(model.getUpdateBy());
        DO.setDeleted(model.getDeleted());
        return DO;
    }
}