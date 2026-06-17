package com.piaomiao.model;

import com.piaomiao.base.BaseModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 流程定义模型
 * 注意: id, createTime, updateTime 继承自 BaseModel，无需重复声明
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProcessDefinitionModel extends BaseModel {
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
     * 流程节点列表
     */
    private List<ProcessNodeModel> nodes;

    /**
     * 流程边列表
     */
    private List<ProcessEdgeModel> edges;

    /**
     * Activiti部署ID
     */
    private String deploymentId;

    /**
     * Activiti流程定义ID
     */
    private String actProcessDefinitionId;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;
    
    /**
     * 是否删除
     */
    private Integer deleted;
}
