package com.piaomiao.dal.event.handler;

import com.piaomiao.event.process.ProcessInstanceCompletedEvent;
import com.piaomiao.event.process.ProcessInstanceStartedEvent;
import com.piaomiao.event.process.ProcessInstanceTerminatedEvent;
import com.piaomiao.event.process.ProcessInstanceWithdrawnEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 流程实例事件处理器
 * <p>
 * 使用 BEFORE_COMMIT 保证与主事务强一致性。
 */
@Slf4j
@Component
public class ProcessInstanceStartedHandler {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleStarted(ProcessInstanceStartedEvent event) {
        log.info("流程实例启动事件 - 事件ID:{}, 流程定义ID:{}, 发起人:{}",
                event.getEventId(),
                event.getEntity().getProcessDefinitionId(),
                event.getEntity().getStartUserId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleTerminated(ProcessInstanceTerminatedEvent event) {
        log.info("流程实例终止事件 - 事件ID:{}, 流程定义ID:{}, 原因:{}",
                event.getEventId(),
                event.getEntity().getProcessDefinitionId(),
                event.getReason());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleCompleted(ProcessInstanceCompletedEvent event) {
        log.info("流程实例完成事件 - 事件ID:{}, 流程定义ID:{}, Activiti实例ID:{}",
                event.getEventId(),
                event.getEntity().getProcessDefinitionId(),
                event.getEntity().getProcessInstanceId());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleWithdrawn(ProcessInstanceWithdrawnEvent event) {
        log.info("流程实例撤回事件 - 事件ID:{}, 流程定义ID:{}, 原因:{}",
                event.getEventId(),
                event.getEntity().getProcessDefinitionId(),
                event.getReason());
    }
}
