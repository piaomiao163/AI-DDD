package com.piaomiao.web.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 流程定义VO
 */
@Data
public class ProcessDefinitionVO {
    /**
     * 流程ID
     */
    private Long id;
    
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
     * 流程XML
     */
    private String xml;
    
    /**
     * 流程版本
     */
    private Integer version;
    
    /**
     * 状态：0-草稿，1-已发布
     */
    private Integer status;

    /**
     * 流程分类
     */
    private String category;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 流程节点列表
     */
    private List<ProcessNodeVO> nodes;
    
    /**
     * 流程边列表
     */
    private List<ProcessEdgeVO> edges;

    /**
     * Activiti部署ID
     */
    private String deploymentId;

    /**
     * Activiti流程定义ID
     */
    private String actProcessDefinitionId;
}