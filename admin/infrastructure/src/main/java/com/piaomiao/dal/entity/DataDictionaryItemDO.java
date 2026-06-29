package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.sys.DataDictionaryItem;

import java.time.LocalDateTime;

/**
 * 数据字典项DO
 */
@TableName("sys_data_dictionary_item")
public class DataDictionaryItemDO {
    /**
     * 字典项ID
     */
    private Long id;
    
    /**
     * 字典ID
     */
    private Long dictionaryId;
    
    /**
     * 字典项名称
     */
    private String name;
    
    /**
     * 字典项值
     */
    private String value;
    
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

    public Long getDictionaryId() {
        return dictionaryId;
    }

    public void setDictionaryId(Long dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
     */
    public DataDictionaryItem toModel() {
        DataDictionaryItem model = new DataDictionaryItem();
        model.setId(this.id);
        model.setDictionaryId(this.dictionaryId);
        model.setName(this.name);
        model.setValue(this.value);
        model.setSort(this.sort);
        model.setStatus(this.status);
        model.setCreateTime(this.createTime);
        model.setUpdateTime(this.updateTime);
        model.setDeleted(this.deleted);
        return model;
    }
    
    /**
     * 从模型转换
     */
    public static DataDictionaryItemDO fromModel(DataDictionaryItem model) {
        DataDictionaryItemDO doObj = new DataDictionaryItemDO();
        doObj.setId(model.getId());
        doObj.setDictionaryId(model.getDictionaryId());
        doObj.setName(model.getName());
        doObj.setValue(model.getValue());
        doObj.setSort(model.getSort());
        doObj.setStatus(model.getStatus());
        doObj.setCreateTime(model.getCreateTime());
        doObj.setUpdateTime(model.getUpdateTime());
        doObj.setDeleted(model.getDeleted());
        return doObj;
    }
}
