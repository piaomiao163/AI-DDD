package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piaomiao.model.ProcessInstanceModel;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 流程实例数据对象
 */
@Data
@TableName("sys_process_instance")
public class ProcessInstanceDO {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 实例编号
     */
    @TableField("instance_no")
    private String instanceNo;

    /**
     * 审批标题
     */
    private String title;

    /**
     * Activiti流程实例ID
     */
    @TableField("act_process_instance_id")
    private String actProcessInstanceId;

    /**
     * 关联流程定义ID(sys_process_definition.id)
     */
    @TableField("process_definition_id")
    private Long processDefinitionId;

    /**
     * 流程定义标识(冗余)
     */
    @TableField("process_definition_key")
    private String processDefinitionKey;

    /**
     * 流程名称(冗余)
     */
    @TableField("process_name")
    private String processName;

    /**
     * 业务单据号
     */
    @TableField("business_key")
    private String businessKey;

    /**
     * 业务类型
     */
    @TableField("business_type")
    private String businessType;

    /**
     * 业务单据主键ID
     */
    @TableField("business_id")
    private Long businessId;

    /**
     * 发起人ID(sys_user.id)
     */
    @TableField("start_user_id")
    private Long startUserId;

    /**
     * 发起人姓名(冗余)
     */
    @TableField("start_user_name")
    private String startUserName;

    /**
     * 申请人部门ID
     */
    @TableField("applicant_dept_id")
    private Long applicantDeptId;

    /**
     * 状态 0运行中 1已完成 2已终止 3已撤回
     */
    private Integer status;

    /**
     * 当前节点名称(冗余)
     */
    @TableField("current_node_name")
    private String currentNodeName;

    /**
     * 当前节点ID
     */
    @TableField("current_node_id")
    private String currentNodeId;

    /**
     * 当前待审批人列表JSON
     */
    @TableField("current_assignees")
    private String currentAssignees;

    /**
     * 总审批节点数
     */
    @TableField("total_nodes")
    private Integer totalNodes;

    /**
     * 已完成节点数
     */
    @TableField("completed_nodes")
    private Integer completedNodes;

    /**
     * 优先级 0普通 1紧急 2特急
     */
    private Integer priority;

    /**
     * 终止/撤回原因
     */
    @TableField("delete_reason")
    private String deleteReason;

    /**
     * 流程变量JSON(快照)
     */
    @TableField("variables_json")
    private String variablesJson;

    /**
     * 启动时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

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

    /**
     * DO转Model
     */
    public ProcessInstanceModel toModel() {
        ProcessInstanceModel model = new ProcessInstanceModel();
        BeanUtils.copyProperties(this, model);
        // 手动映射字段名不同的属性
        model.setId(this.id);
        model.setProcessInstanceId(this.actProcessInstanceId);
        model.setProcessDefinitionDbId(this.processDefinitionId);
        model.setStartUserDbId(this.startUserId);
        model.setStartUserName(this.startUserName);
        // 反序列化variablesJson → variables
        if (this.variablesJson != null && !this.variablesJson.isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> variables = mapper.readValue(this.variablesJson,
                        new TypeReference<Map<String, Object>>() {});
                model.setVariables(variables);
            } catch (JsonProcessingException e) {
                // JSON解析失败则忽略
            }
        }
        return model;
    }

    /**
     * Model转DO
     */
    public static ProcessInstanceDO fromModel(ProcessInstanceModel model) {
        ProcessInstanceDO DO = new ProcessInstanceDO();
        BeanUtils.copyProperties(model, DO);
        // 手动映射字段名不同的属性
        DO.setId(model.getId());
        DO.setActProcessInstanceId(model.getProcessInstanceId());
        DO.setProcessDefinitionId(model.getProcessDefinitionDbId());
        DO.setStartUserId(model.getStartUserDbId());
        DO.setStartUserName(model.getStartUserName());
        // 序列化variables → variablesJson
        if (model.getVariables() != null && !model.getVariables().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                DO.setVariablesJson(mapper.writeValueAsString(model.getVariables()));
            } catch (JsonProcessingException e) {
                DO.setVariablesJson(null);
            }
        } else if (model.getVariablesJson() != null) {
            DO.setVariablesJson(model.getVariablesJson());
        }
        return DO;
    }
}
