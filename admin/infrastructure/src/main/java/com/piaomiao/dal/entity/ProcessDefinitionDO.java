package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 流程定义数据传输对象
 */
@Data
@TableName("sys_process_definition")
public class ProcessDefinitionDO {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
     * 流程节点JSON
     */
    @TableField("nodes_json")
    private String nodesJson;
    
    /**
     * 流程边JSON
     */
    @TableField("edges_json")
    private String edgesJson;

    /**
     * Activiti部署ID
     */
    private String deploymentId;

    /**
     * Activiti流程定义ID
     */
    @TableField("act_process_definition_id")
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
    @TableLogic
    private Integer deleted;
}
