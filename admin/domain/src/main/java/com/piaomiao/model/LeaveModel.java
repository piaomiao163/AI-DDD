package com.piaomiao.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.event.process.LeaveAppliedEvent;

/**
 * 请假单领域模型
 */
public class LeaveModel {

    /**
     * 默认请假流程定义Key
     */
    private static final String DEFAULT_PROCESS_DEFINITION_KEY = "leave";

    // ==================== 状态常量 ====================

    /**
     * 状态：待审批
     */
    public static final int STATUS_PENDING = 0;

    /**
     * 状态：审批中
     */
    public static final int STATUS_APPROVING = 1;

    /**
     * 状态：已通过
     */
    public static final int STATUS_APPROVED = 2;

    /**
     * 状态：已驳回
     */
    public static final int STATUS_REJECTED = 3;

    /**
     * 状态：已撤回
     */
    public static final int STATUS_WITHDRAWN = 4;

    // ==================== 字段 ====================

    /**
     * 请假单ID
     */
    private Long id;

    /**
     * 请假标题
     */
    private String title;

    /**
     * 请假类型
     */
    private Integer leaveType;

    /**
     * 开始日期
     */
    private LocalDate startDate;

    /**
     * 结束日期
     */
    private LocalDate endDate;

    /**
     * 请假天数
     */
    private BigDecimal days;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 申请人ID
     */
    private Long applicantId;

    /**
     * 申请人姓名
     */
    private String applicantName;

    /**
     * 申请人部门名称
     */
    private String deptName;

    /**
     * 请假单状态
     */
    private Integer status;

    /**
     * 关联流程实例表ID
     */
    private Long processInstanceId;

    /**
     * 流程定义Key
     */
    private String processDefinitionKey;

    /**
     * 审批意见或撤回原因
     */
    private String approveComment;

    /**
     * 当前流程节点名称
     */
    private String currentNodeName;

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
    private Integer deleted;

    /**
     * 待发布领域事件
     */
    private final List<BaseDomainEvent<?>> events = new ArrayList<>();

    /**
     * 注册领域事件（由领域行为内部调用）
     */
    protected void registerEvent(BaseDomainEvent<?> event) {
        this.events.add(event);
    }

    /**
     * 拉取并清空领域事件（由 Service 层调用，拉取后发布）
     */
    public List<BaseDomainEvent<?>> pullEvents() {
        List<BaseDomainEvent<?>> pulled = new ArrayList<>(events);
        events.clear();
        return pulled;
    }

    // ==================== 领域行为 ====================

    /**
     * 提交请假申请
     * 设置初始状态为待审批，校验必填字段
     */
    public void submit() {
        if (isBlank(title)) {
            throw new IllegalArgumentException("请假标题不能为空");
        }
        if (leaveType == null) {
            throw new IllegalArgumentException("请假类型不能为空");
        }
        if (startDate == null) {
            throw new IllegalArgumentException("开始日期不能为空");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("结束日期不能为空");
        }
        if (days == null || days.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("请假天数必须大于0");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }
        if (isBlank(reason)) {
            throw new IllegalArgumentException("请假原因不能为空");
        }
        this.status = STATUS_PENDING;
    }

    /**
     * 撤回请假申请
     *
     * @param currentUserId 当前用户ID
     * @param reason        撤回原因
     */
    public void withdraw(Long currentUserId, String reason) {
        if (status != STATUS_PENDING && status != STATUS_APPROVING) {
            throw new IllegalStateException("只有待审批或审批中的请假单才能撤回");
        }
        if (!Objects.equals(applicantId, currentUserId)) {
            throw new IllegalStateException("只有申请人才能撤回");
        }
        markWithdrawn(reason);
    }

    /**
     * 审批通过
     */
    public void approve() {
        if (status != STATUS_PENDING && status != STATUS_APPROVING) {
            throw new IllegalStateException("只有待审批或审批中的请假单才能标记为通过");
        }
        this.status = STATUS_APPROVED;
    }

    /**
     * 审批驳回
     *
     * @param comment 驳回意见
     */
    public void reject(String comment) {
        if (status != STATUS_PENDING && status != STATUS_APPROVING) {
            throw new IllegalStateException("只有待审批或审批中的请假单才能驳回");
        }
        this.status = STATUS_REJECTED;
        this.approveComment = comment;
    }

    /**
     * 标记为已撤回
     *
     * @param reason 撤回原因
     */
    public void markWithdrawn(String reason) {
        this.status = STATUS_WITHDRAWN;
        if (!isBlank(reason)) {
            this.approveComment = reason;
        }
    }

    /**
     * 标记为审批中，并注册请假申请领域事件
     * 流程实例启动后调用
     *
     * @param processInstanceId   流程实例表ID
     * @param processDefinitionId 流程定义表ID
     * @param currentAssignees    当前审批人JSON
     * @param currentNodeName     当前节点名称
     * @param currentNodeId       当前节点ID
     */
    public void startApproving(Long processInstanceId, Long processDefinitionId,
                               String currentAssignees, String currentNodeName, String currentNodeId) {
        this.processInstanceId = processInstanceId;
        this.status = STATUS_APPROVING;
        registerEvent(new LeaveAppliedEvent(this, this,
                processInstanceId, processDefinitionId,
                currentAssignees, currentNodeName, currentNodeId));
    }

    /**
     * 获取流程变量
     *
     * @return 流程变量Map
     */
    public Map<String, Object> getProcessVariables() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("leaveType", leaveType);
        variables.put("days", days);
        variables.put("reason", reason);
        // 审批人变量（与BPMN中 activiti:assignee 表达式对应，值必须是用户名）
        variables.put("assigneeCurrentUser", createBy);
        variables.put("assigneeLeader", "admin");
        variables.put("assigneePM", "admin");
        variables.put("assigneeHR", "admin");
        variables.put("assigneeHRBP", "admin");
        return variables;
    }

    /**
     * 获取流程标题
     *
     * @return 流程标题
     */
    public String getProcessTitle() {
        return applicantName + "的请假申请";
    }

    /**
     * 获取流程定义Key，默认为leave
     *
     * @return 流程定义Key
     */
    public String resolveProcessDefinitionKey() {
        return isBlank(processDefinitionKey) ? DEFAULT_PROCESS_DEFINITION_KEY : processDefinitionKey;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    // ==================== Getter/Setter ====================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getLeaveType() { return leaveType; }
    public void setLeaveType(Integer leaveType) { this.leaveType = leaveType; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getDays() { return days; }
    public void setDays(BigDecimal days) { this.days = days; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Long getProcessInstanceId() { return processInstanceId; }
    public void setProcessInstanceId(Long processInstanceId) { this.processInstanceId = processInstanceId; }

    public String getProcessDefinitionKey() { return processDefinitionKey; }
    public void setProcessDefinitionKey(String processDefinitionKey) { this.processDefinitionKey = processDefinitionKey; }

    public String getApproveComment() { return approveComment; }
    public void setApproveComment(String approveComment) { this.approveComment = approveComment; }

    public String getCurrentNodeName() { return currentNodeName; }
    public void setCurrentNodeName(String currentNodeName) { this.currentNodeName = currentNodeName; }

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
}
