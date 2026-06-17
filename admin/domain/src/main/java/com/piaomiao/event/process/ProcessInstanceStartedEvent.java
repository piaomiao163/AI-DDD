package com.piaomiao.event.process;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.model.ProcessInstanceModel;

/**
 * 流程实例启动事件
 * <p>
 * 新流程实例创建时触发。可用于：通知审批人、更新待办统计。
 */
public class ProcessInstanceStartedEvent extends BaseDomainEvent<ProcessInstanceModel> {

    public ProcessInstanceStartedEvent(Object source, ProcessInstanceModel entity) {
        super(source, entity, null, "ProcessInstance");
    }
}
