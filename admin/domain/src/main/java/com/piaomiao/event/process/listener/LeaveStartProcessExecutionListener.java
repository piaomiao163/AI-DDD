package com.piaomiao.event.process.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class LeaveStartProcessExecutionListener implements ExecutionListener {

    @Override
    public void notify(DelegateExecution execution) {
        log.info("流程[{}] 节点[{}] 触发事件: {}", 
            execution.getProcessInstanceId(),
            execution.getProcessInstanceBusinessKey(), 
            execution.getEventName());
    }
}
