package com.piaomiao.event.process;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.model.TaskModel;

/**
 * 任务驳回事件
 * <p>
 * 任务被驳回时触发。可用于：通知流程发起人、记录驳回原因。
 */
public class TaskRejectedEvent extends BaseDomainEvent<TaskModel> {

    private final String reason;

    public TaskRejectedEvent(Object source, TaskModel entity, String reason) {
        super(source, entity, null, "Task");
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
