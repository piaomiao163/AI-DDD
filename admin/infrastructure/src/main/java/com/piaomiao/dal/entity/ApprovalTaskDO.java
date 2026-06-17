package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.piaomiao.model.ApprovalTaskModel;

import java.time.LocalDateTime;

/**
 * 审批任务表 DO
 *
 * @author system
 * @date 2026-06-14
 */
@TableName("biz_approval_task")
public class ApprovalTaskDO {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联实例ID
     */
    @TableField("instance_id")
    private Long instanceId;

    /**
     * 流程定义ID(冗余)
     */
    @TableField("process_definition_id")
    private Long processDefinitionId;

    /**
     * 节点ID(对应nodes_json中的节点id)
     */
    @TableField("node_id")
    private String nodeId;

    /**
     * 节点名称
     */
    @TableField("node_name")
    private String nodeName;

    /**
     * 节点类型: approve-审批 cc-抄送 notify-通知 condition-条件分支
     */
    @TableField("node_type")
    private String nodeType;

    /**
     * 节点顺序(用于展示)
     */
    @TableField("node_index")
    private Integer nodeIndex;

    /**
     * 审批人ID
     */
    @TableField("assignee_id")
    private Long assigneeId;

    /**
     * 审批人姓名
     */
    @TableField("assignee_name")
    private String assigneeName;

    /**
     * 审批人类型: user-指定人 role-角色 supervisor-上级 dept_leader-部门负责人
     */
    @TableField("assignee_type")
    private String assigneeType;

    /**
     * 候选审批人列表JSON
     */
    @TableField("candidate_users")
    private String candidateUsers;

    /**
     * 审批模式: single-单签 all-会签 any-或签
     */
    @TableField("approve_mode")
    private String approveMode;

    /**
     * 状态: 0-待审批 1-已通过 2-已驳回 3-已转交 4-已超时 5-已撤销
     */
    @TableField("status")
    private Integer status;

    /**
     * 审批意见
     */
    @TableField("opinion")
    private String opinion;

    /**
     * 附件列表JSON
     */
    @TableField("attachments")
    private String attachments;

    /**
     * 电子签名(如果需要)
     */
    @TableField("signature")
    private String signature;

    /**
     * 收到任务时间
     */
    @TableField("receive_time")
    private LocalDateTime receiveTime;

    /**
     * 截止时间(超时处理)
     */
    @TableField("deadline")
    private LocalDateTime deadline;

    /**
     * 最后一次提醒时间
     */
    @TableField("remind_time")
    private LocalDateTime remindTime;

    /**
     * 完成时间
     */
    @TableField("complete_time")
    private LocalDateTime completeTime;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 更新人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 是否删除1-是、0-否
     */
    @TableLogic
    private Integer deleted;

    // ==================== 状态常量 ====================

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_APPROVED = 1;
    public static final int STATUS_REJECTED = 2;
    public static final int STATUS_TRANSFERRED = 3;
    public static final int STATUS_TIMEOUT = 4;
    public static final int STATUS_CANCELED = 5;

    // ==================== 节点类型常量 ====================

    public static final String NODE_TYPE_APPROVE = "approve";
    public static final String NODE_TYPE_CC = "cc";
    public static final String NODE_TYPE_NOTIFY = "notify";
    public static final String NODE_TYPE_CONDITION = "condition";

    // ==================== 审批人类型常量 ====================

    public static final String ASSIGNEE_TYPE_USER = "user";
    public static final String ASSIGNEE_TYPE_ROLE = "role";
    public static final String ASSIGNEE_TYPE_SUPERVISOR = "supervisor";
    public static final String ASSIGNEE_TYPE_DEPT_LEADER = "dept_leader";

    // ==================== 审批模式常量 ====================

    public static final String APPROVE_MODE_SINGLE = "single";
    public static final String APPROVE_MODE_ALL = "all";
    public static final String APPROVE_MODE_ANY = "any";

    // ==================== Getter/Setter ====================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getInstanceId() { return instanceId; }
    public void setInstanceId(Long instanceId) { this.instanceId = instanceId; }

    public Long getProcessDefinitionId() { return processDefinitionId; }
    public void setProcessDefinitionId(Long processDefinitionId) { this.processDefinitionId = processDefinitionId; }

    public String getNodeId() { return nodeId; }
    public void setNodeId(String nodeId) { this.nodeId = nodeId; }

    public String getNodeName() { return nodeName; }
    public void setNodeName(String nodeName) { this.nodeName = nodeName; }

    public String getNodeType() { return nodeType; }
    public void setNodeType(String nodeType) { this.nodeType = nodeType; }

    public Integer getNodeIndex() { return nodeIndex; }
    public void setNodeIndex(Integer nodeIndex) { this.nodeIndex = nodeIndex; }

    public Long getAssigneeId() { return assigneeId; }
    public void setAssigneeId(Long assigneeId) { this.assigneeId = assigneeId; }

    public String getAssigneeName() { return assigneeName; }
    public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; }

    public String getAssigneeType() { return assigneeType; }
    public void setAssigneeType(String assigneeType) { this.assigneeType = assigneeType; }

    public String getCandidateUsers() { return candidateUsers; }
    public void setCandidateUsers(String candidateUsers) { this.candidateUsers = candidateUsers; }

    public String getApproveMode() { return approveMode; }
    public void setApproveMode(String approveMode) { this.approveMode = approveMode; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getOpinion() { return opinion; }
    public void setOpinion(String opinion) { this.opinion = opinion; }

    public String getAttachments() { return attachments; }
    public void setAttachments(String attachments) { this.attachments = attachments; }

    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }

    public LocalDateTime getReceiveTime() { return receiveTime; }
    public void setReceiveTime(LocalDateTime receiveTime) { this.receiveTime = receiveTime; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public LocalDateTime getRemindTime() { return remindTime; }
    public void setRemindTime(LocalDateTime remindTime) { this.remindTime = remindTime; }

    public LocalDateTime getCompleteTime() { return completeTime; }
    public void setCompleteTime(LocalDateTime completeTime) { this.completeTime = completeTime; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }

    // ==================== 模型转换 ====================

    /**
     * 转换为领域模型
     */
    public ApprovalTaskModel toModel() {
        ApprovalTaskModel model = new ApprovalTaskModel();
        model.setId(id);
        model.setInstanceId(instanceId);
        model.setProcessDefinitionId(processDefinitionId);
        model.setNodeId(nodeId);
        model.setNodeName(nodeName);
        model.setNodeType(nodeType);
        model.setNodeIndex(nodeIndex);
        model.setAssigneeId(assigneeId);
        model.setAssigneeName(assigneeName);
        model.setAssigneeType(assigneeType);
        model.setCandidateUsersJson(candidateUsers);
        model.setApproveMode(approveMode);
        model.setStatus(status);
        model.setOpinion(opinion);
        model.setAttachments(attachments);
        model.setSignature(signature);
        model.setReceiveTime(receiveTime);
        model.setDeadline(deadline);
        model.setRemindTime(remindTime);
        model.setCompleteTime(completeTime);
        model.setRemark(remark);
        model.setCreateTime(createTime);
        model.setUpdateTime(updateTime);
        model.setCreateBy(createBy);
        model.setUpdateBy(updateBy);
        return model;
    }

    /**
     * 从领域模型转换
     */
    public static ApprovalTaskDO fromModel(ApprovalTaskModel model) {
        ApprovalTaskDO DO = new ApprovalTaskDO();
        DO.setId(model.getId());
        DO.setInstanceId(model.getInstanceId());
        DO.setProcessDefinitionId(model.getProcessDefinitionId());
        DO.setNodeId(model.getNodeId());
        DO.setNodeName(model.getNodeName());
        DO.setNodeType(model.getNodeType());
        DO.setNodeIndex(model.getNodeIndex());
        DO.setAssigneeId(model.getAssigneeId());
        DO.setAssigneeName(model.getAssigneeName());
        DO.setAssigneeType(model.getAssigneeType());
        DO.setCandidateUsers(model.getCandidateUsersJson());
        DO.setApproveMode(model.getApproveMode());
        DO.setStatus(model.getStatus());
        DO.setOpinion(model.getOpinion());
        DO.setAttachments(model.getAttachments());
        DO.setSignature(model.getSignature());
        DO.setReceiveTime(model.getReceiveTime());
        DO.setDeadline(model.getDeadline());
        DO.setRemindTime(model.getRemindTime());
        DO.setCompleteTime(model.getCompleteTime());
        DO.setRemark(model.getRemark());
        DO.setCreateTime(model.getCreateTime());
        DO.setUpdateTime(model.getUpdateTime());
        DO.setCreateBy(model.getCreateBy());
        DO.setUpdateBy(model.getUpdateBy());
        return DO;
    }
}
