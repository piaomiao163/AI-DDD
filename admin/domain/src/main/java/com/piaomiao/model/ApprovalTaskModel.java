package com.piaomiao.model;

import java.time.LocalDateTime;

/**
 * 审批任务领域模型
 *
 * @author system
 * @date 2026-06-14
 */
public class ApprovalTaskModel {

    // ==================== 状态常量 ====================

    /** 待审批 */
    public static final int STATUS_PENDING = 0;
    /** 已通过 */
    public static final int STATUS_APPROVED = 1;
    /** 已驳回 */
    public static final int STATUS_REJECTED = 2;
    /** 已转交 */
    public static final int STATUS_TRANSFERRED = 3;
    /** 已超时 */
    public static final int STATUS_TIMEOUT = 4;
    /** 已撤销 */
    public static final int STATUS_CANCELED = 5;

    // ==================== 节点类型常量 ====================

    /** 审批 */
    public static final String NODE_TYPE_APPROVE = "approve";
    /** 抄送 */
    public static final String NODE_TYPE_CC = "cc";
    /** 通知 */
    public static final String NODE_TYPE_NOTIFY = "notify";
    /** 条件分支 */
    public static final String NODE_TYPE_CONDITION = "condition";

    // ==================== 审批人类型常量 ====================

    /** 指定人 */
    public static final String ASSIGNEE_TYPE_USER = "user";
    /** 角色 */
    public static final String ASSIGNEE_TYPE_ROLE = "role";
    /** 上级 */
    public static final String ASSIGNEE_TYPE_SUPERVISOR = "supervisor";
    /** 部门负责人 */
    public static final String ASSIGNEE_TYPE_DEPT_LEADER = "dept_leader";

    // ==================== 审批模式常量 ====================

    /** 单签 */
    public static final String APPROVE_MODE_SINGLE = "single";
    /** 会签 */
    public static final String APPROVE_MODE_ALL = "all";
    /** 或签 */
    public static final String APPROVE_MODE_ANY = "any";

    private Long id;
    private Long instanceId;
    private Long processDefinitionId;
    private String nodeId;
    private String nodeName;
    private String nodeType;
    private Integer nodeIndex;
    private Long assigneeId;
    private String assigneeName;
    private String assigneeType;
    private String candidateUsersJson;
    private String approveMode;
    private Integer status;
    private String opinion;
    private String attachments;
    private String signature;
    private LocalDateTime receiveTime;
    private LocalDateTime deadline;
    private LocalDateTime remindTime;
    private LocalDateTime completeTime;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String createBy;
    private String updateBy;

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

    public String getCandidateUsersJson() { return candidateUsersJson; }
    public void setCandidateUsersJson(String candidateUsersJson) { this.candidateUsersJson = candidateUsersJson; }

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
}
