package com.piaomiao.dto.sys;

import lombok.Data;

/**
 * 数据字典DTO
 */
@Data
public class DataDictionaryDTO {
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
}
