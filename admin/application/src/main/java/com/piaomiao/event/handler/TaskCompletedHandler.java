package com.piaomiao.event.handler;

import com.piaomiao.event.process.TaskCompletedEvent;
import com.piaomiao.event.process.TaskRejectedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 任务事件处理器
 * <p>
 * 使用 BEFORE_COMMIT 保证与主事务强一致性。
 */
@Slf4j
@Component
public class TaskCompletedHandler {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleCompleted(TaskCompletedEvent event) {
        log.info("任务完成事件 - 事件ID:{}, 任务ID:{}, 结果:{}",
                event.getEventId(),
                event.getEntity().getTaskId(),
                event.getOutcome());
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleRejected(TaskRejectedEvent event) {
        log.info("任务驳回事件 - 事件ID:{}, 任务ID:{}, 原因:{}",
                event.getEventId(),
                event.getEntity().getTaskId(),
                event.getReason());
    }
}
