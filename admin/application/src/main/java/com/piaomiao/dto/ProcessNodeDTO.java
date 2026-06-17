package com.piaomiao.dto;

import lombok.Data;

import java.util.Map;

/**
 * 流程节点DTO
 */
@Data
public class ProcessNodeDTO {
    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点类型：startEvent, userTask, endEvent
     */
    private String type;

    /**
     * 节点配置
     */
    private Map<String, Object> config;

    /**
     * 坐标X
     */
    private Integer x;

    /**
     * 坐标Y
     */
    private Integer y;

    /**
     * 宽度
     */
    private Integer width;

    /**
     * 高度
     */
    private Integer height;
}
