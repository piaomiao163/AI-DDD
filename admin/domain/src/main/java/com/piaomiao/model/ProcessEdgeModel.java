package com.piaomiao.model;

import lombok.Data;

import java.util.Map;

/**
 * 流程边模型
 */
@Data
public class ProcessEdgeModel {
    /**
     * 边ID
     */
    private String id;
    
    /**
     * 源节点ID
     */
    private String source;
    
    /**
     * 目标节点ID
     */
    private String target;
    
    /**
     * 边配置
     */
    private Map<String, Object> config;
    
    /**
     * 条件表达式
     */
    private String condition;
}
