package com.piaomiao.event.process;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.model.ProcessInstanceModel;

/**
 * 流程实例终止事件
 * <p>
 * 流程实例被手动终止时触发。可用于：通知发起人、记录审计日志。
 */
public class ProcessInstanceTerminatedEvent extends BaseDomainEvent<ProcessInstanceModel> {

    private final String reason;

    public ProcessInstanceTerminatedEvent(Object source, ProcessInstanceModel entity, String reason) {
        super(source, entity, null, "ProcessInstance");
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
