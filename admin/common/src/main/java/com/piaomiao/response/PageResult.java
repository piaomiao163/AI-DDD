package com.piaomiao.response;

import lombok.Data;

import java.util.List;

/**
 * 分页结果
 */
@Data
public class PageResult<T> {
    /**
     * 总条数
     */
    private Long total;
    
    /**
     * 当前页数据
     */
    private List<T> list;
    
    /**
     * 页码
     */
    private Integer pageNum;
    
    /**
     * 每页条数
     */
    private Integer pageSize;
    
    public PageResult() {
    }
    
    public PageResult(Long total, List<T> list, Integer pageNum, Integer pageSize) {
        this.total = total;
        this.list = list;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }
    
    /**
     * 创建分页结果
     */
    public static <T> PageResult<T> of(Long total, List<T> list, Integer pageNum, Integer pageSize) {
        return new PageResult<>(total, list, pageNum, pageSize);
    }
}
