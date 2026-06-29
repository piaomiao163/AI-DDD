package com.piaomiao.dto.sys;

import lombok.Data;

/**
 * 数据字典项DTO
 */
@Data
public class DataDictionaryItemDTO {
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
}
