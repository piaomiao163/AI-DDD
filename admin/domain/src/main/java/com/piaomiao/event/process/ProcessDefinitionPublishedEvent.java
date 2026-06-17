package com.piaomiao.event.process;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.model.ProcessDefinitionModel;

/**
 * 流程定义发布事件
 * <p>
 * 流程定义状态从草稿(0)变更为已发布(1)时触发。
 * 可用于：记录审计日志、通知相关方、更新仪表盘统计。
 */
public class ProcessDefinitionPublishedEvent extends BaseDomainEvent<ProcessDefinitionModel> {

    public ProcessDefinitionPublishedEvent(Object source, ProcessDefinitionModel entity) {
        super(source, entity, entity.getId(), "ProcessDefinition");
    }
}
