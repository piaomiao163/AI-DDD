package com.piaomiao.model.sys;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 数据字典项模型
 */
@Data
public class DataDictionaryItem {
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
    private Integer deleted;
}