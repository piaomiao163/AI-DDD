package com.piaomiao.model;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 数据字典模型
 */
@Data
public class DataDictionary {
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
    private Integer deleted;
}