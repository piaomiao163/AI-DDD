package com.piaomiao.event.process;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.model.LeaveModel;

/**
 * 请假单提交事件
 * <p>
 * 请假单提交时触发（尚未启动流程）。可用于：发送提交通知、记录提交日志。
 * 同步发布，在同一事务内处理。
 */
public class LeaveSubmittedEvent extends BaseDomainEvent<LeaveModel> {

    public LeaveSubmittedEvent(Object source, LeaveModel entity) {
        super(source, entity, entity.getId(), "Leave");
    }
}
