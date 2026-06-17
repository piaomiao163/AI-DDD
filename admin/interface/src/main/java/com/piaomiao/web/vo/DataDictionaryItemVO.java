package com.piaomiao.web.vo;

import com.piaomiao.model.DataDictionaryItem;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 数据字典项VO
 */
@Data
public class DataDictionaryItemVO {
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
     * 转换为DTO
     */
    public static com.piaomiao.dto.DataDictionaryItemDTO toDTO(DataDictionaryItemVO vo) {
        com.piaomiao.dto.DataDictionaryItemDTO dto = new com.piaomiao.dto.DataDictionaryItemDTO();
        dto.setId(vo.getId());
        dto.setDictionaryId(vo.getDictionaryId());
        dto.setName(vo.getName());
        dto.setValue(vo.getValue());
        dto.setSort(vo.getSort());
        dto.setStatus(vo.getStatus());
        return dto;
    }
    
    /**
     * 从模型转换
     */
    public static DataDictionaryItemVO fromModel(DataDictionaryItem model) {
        DataDictionaryItemVO vo = new DataDictionaryItemVO();
        vo.setId(model.getId());
        vo.setDictionaryId(model.getDictionaryId());
        vo.setName(model.getName());
        vo.setValue(model.getValue());
        vo.setSort(model.getSort());
        vo.setStatus(model.getStatus());
        vo.setCreateTime(model.getCreateTime());
        vo.setUpdateTime(model.getUpdateTime());
        return vo;
    }
}