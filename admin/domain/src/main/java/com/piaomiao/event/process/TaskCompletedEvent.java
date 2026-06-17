package com.piaomiao.event.process;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.model.TaskModel;

/**
 * 任务完成事件
 * <p>
 * 任务审批通过时触发。可用于：通知下一环节审批人、更新流程状态。
 */
public class TaskCompletedEvent extends BaseDomainEvent<TaskModel> {

    private final String outcome;

    public TaskCompletedEvent(Object source, TaskModel entity, String outcome) {
        super(source, entity, null, "Task");
        this.outcome = outcome;
    }

    public String getOutcome() {
        return outcome;
    }
}
