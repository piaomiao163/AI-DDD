package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.sys.DataDictionary;

import java.time.LocalDateTime;

/**
 * 数据字典DO
 */
@TableName("sys_data_dictionary")
public class DataDictionaryDO {
    /**
     * 字典ID
     */
    private Long id;
    
    /**
     * 字典名称
     */
    private String name;
    
    /**
     * 字典编码
     */
    private String code;
    
    /**
     * 字典描述
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    /**
     * 转换为模型
     */
    public DataDictionary toModel() {
        DataDictionary model = new DataDictionary();
        model.setId(this.id);
        model.setName(this.name);
        model.setCode(this.code);
        model.setDescription(this.description);
        model.setStatus(this.status);
        model.setCreateTime(this.createTime);
        model.setUpdateTime(this.updateTime);
        model.setDeleted(this.deleted);
        return model;
    }
    
    /**
     * 从模型转换
     */
    public static DataDictionaryDO fromModel(DataDictionary model) {
        DataDictionaryDO doObj = new DataDictionaryDO();
        doObj.setId(model.getId());
        doObj.setName(model.getName());
        doObj.setCode(model.getCode());
        doObj.setDescription(model.getDescription());
        doObj.setStatus(model.getStatus());
        doObj.setCreateTime(model.getCreateTime());
        doObj.setUpdateTime(model.getUpdateTime());
        doObj.setDeleted(model.getDeleted());
        return doObj;
    }
}
