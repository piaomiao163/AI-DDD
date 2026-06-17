package com.piaomiao.web.vo;

import lombok.Data;

/**
 * 更新流程定义请求VO
 */
@Data
public class UpdateProcessVO {
    /**
     * 流程ID
     */
    private Long id;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程描述
     */
    private String description;

    /**
     * 流程分类
     */
    private String category;

    /**
     * BPMN XML
     */
    private String xml;

    /**
     * 转换为DTO
     */
    public com.piaomiao.dto.ProcessDefinitionDTO toDTO() {
        com.piaomiao.dto.ProcessDefinitionDTO dto = new com.piaomiao.dto.ProcessDefinitionDTO();
        dto.setId(this.id);
        dto.setName(this.name);
        dto.setDescription(this.description);
        dto.setCategory(this.category);
        dto.setXml(this.xml);
        return dto;
    }
}
