package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.sys.PostModel;

import java.time.LocalDateTime;

/**
 * 岗位数据访问对象
 */
@TableName("sys_post")
public class PostDO {
    /**
     * 岗位ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 岗位编码
     */
    private String code;

    /**
     * 岗位名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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

    /**
     * 转换为模型
     * @return 岗位模型
     */
    public PostModel toModel() {
        PostModel model = new PostModel();
        model.setId(id);
        model.setCode(code);
        model.setName(name);
        model.setSort(sort);
        model.setStatus(status);
        model.setRemark(remark);
        model.setCreateTime(createTime);
        model.setUpdateTime(updateTime);
        return model;
    }

    /**
     * 从模型转换
     * @param model 岗位模型
     * @return 岗位数据访问对象
     */
    public static PostDO fromModel(PostModel model) {
        PostDO DO = new PostDO();
        DO.setId(model.getId());
        DO.setCode(model.getCode());
        DO.setName(model.getName());
        DO.setSort(model.getSort());
        DO.setStatus(model.getStatus());
        DO.setRemark(model.getRemark());
        DO.setCreateTime(model.getCreateTime());
        DO.setUpdateTime(model.getUpdateTime());
        return DO;
    }
}
