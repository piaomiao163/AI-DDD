package com.piaomiao.web.vo;

import lombok.Data;

/**
 * 保存流程定义请求VO
 */
@Data
public class SaveProcessVO {
    /**
     * 流程名称
     */
    private String name;

    /**
     * 流程标识
     */
    private String processKey;

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
        dto.setName(this.name);
        dto.setProcessKey(this.processKey);
        dto.setDescription(this.description);
        dto.setCategory(this.category);
        dto.setXml(this.xml);
        return dto;
    }
}
