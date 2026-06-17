package com.piaomiao.base;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BaseModel implements Serializable {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页数量
     * 默认 10
     */
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方向
     */
    private String sortOrder;

    public BaseModel() {
    }

    public BaseModel(Long id, LocalDateTime createTime, LocalDateTime updateTime,Integer pageNum, Integer pageSize, String sortField, String sortOrder) {
        this.id = id;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
