package com.piaomiao.event.process;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.model.ProcessInstanceModel;

/**
 * 流程实例完成事件
 * <p>
 * 流程实例自然完成（最后一个任务审批通过）时触发。可用于：通知发起人、更新统计。
 */
public class ProcessInstanceCompletedEvent extends BaseDomainEvent<ProcessInstanceModel> {

    public ProcessInstanceCompletedEvent(Object source, ProcessInstanceModel entity) {
        super(source, entity, entity.getId(), "ProcessInstance");
    }
}
