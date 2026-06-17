package com.piaomiao.event.process;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.model.LeaveModel;

/**
 * 请假申请事件
 * <p>
 * 请假单提交并启动流程后触发。可用于：创建审批任务、通知审批人。
 * 同步发布，在同一事务内处理。
 */
public class LeaveAppliedEvent extends BaseDomainEvent<LeaveModel> {

    /**
     * 自有流程实例表ID
     */
    private final Long processInstanceId;

    /**
     * 流程定义表ID
     */
    private final Long processDefinitionId;

    /**
     * 当前审批人列表JSON
     */
    private final String currentAssignees;

    /**
     * 当前节点名称
     */
    private final String currentNodeName;

    /**
     * 当前节点ID
     */
    private final String currentNodeId;

    public LeaveAppliedEvent(Object source, LeaveModel entity,
                             Long processInstanceId, Long processDefinitionId,
                             String currentAssignees, String currentNodeName, String currentNodeId) {
        super(source, entity, entity.getId(), "Leave");
        this.processInstanceId = processInstanceId;
        this.processDefinitionId = processDefinitionId;
        this.currentAssignees = currentAssignees;
        this.currentNodeName = currentNodeName;
        this.currentNodeId = currentNodeId;
    }

    public Long getProcessInstanceId() { return processInstanceId; }
    public Long getProcessDefinitionId() { return processDefinitionId; }
    public String getCurrentAssignees() { return currentAssignees; }
    public String getCurrentNodeName() { return currentNodeName; }
    public String getCurrentNodeId() { return currentNodeId; }
}
