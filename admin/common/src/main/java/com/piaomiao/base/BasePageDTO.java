package com.piaomiao.base;

public class BasePageDTO {
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

    public BasePageDTO() {
    }

    public BasePageDTO(Integer pageNum, Integer pageSize, String sortField, String sortOrder) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sortField = sortField;
        this.sortOrder = sortOrder;
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

    @Override
    public String toString() {
        return "BasePageVO{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", sortField='" + sortField + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                '}';
    }
}
