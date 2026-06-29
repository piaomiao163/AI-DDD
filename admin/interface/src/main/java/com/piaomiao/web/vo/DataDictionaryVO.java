package com.piaomiao.web.vo;

import com.piaomiao.model.sys.DataDictionary;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 数据字典VO
 */
@Data
public class DataDictionaryVO {
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
     * 转换为DTO
     */
    public static com.piaomiao.dto.sys.DataDictionaryDTO toDTO(DataDictionaryVO vo) {
        com.piaomiao.dto.sys.DataDictionaryDTO dto = new com.piaomiao.dto.sys.DataDictionaryDTO();
        dto.setId(vo.getId());
        dto.setName(vo.getName());
        dto.setCode(vo.getCode());
        dto.setDescription(vo.getDescription());
        dto.setStatus(vo.getStatus());
        return dto;
    }
    
    /**
     * 从模型转换
     */
    public static DataDictionaryVO fromModel(DataDictionary model) {
        DataDictionaryVO vo = new DataDictionaryVO();
        vo.setId(model.getId());
        vo.setName(model.getName());
        vo.setCode(model.getCode());
        vo.setDescription(model.getDescription());
        vo.setStatus(model.getStatus());
        vo.setCreateTime(model.getCreateTime());
        vo.setUpdateTime(model.getUpdateTime());
        return vo;
    }
}