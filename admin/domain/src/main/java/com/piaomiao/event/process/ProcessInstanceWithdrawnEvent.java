package com.piaomiao.event.process;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.model.ProcessInstanceModel;

/**
 * 流程实例撤回事件
 * <p>
 * 发起人撤回流程实例时触发。可用于：通知审批人、记录审计日志。
 */
public class ProcessInstanceWithdrawnEvent extends BaseDomainEvent<ProcessInstanceModel> {

    private final String reason;

    public ProcessInstanceWithdrawnEvent(Object source, ProcessInstanceModel entity, String reason) {
        super(source, entity, entity.getId(), "ProcessInstance");
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
