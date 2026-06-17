package com.piaomiao.dal.event.handler;

import com.piaomiao.event.process.ProcessDefinitionPublishedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 流程定义发布事件处理器
 * <p>
 * 使用 BEFORE_COMMIT 保证与主事务强一致性。
 */
@Slf4j
@Component
public class ProcessDefinitionPublishedHandler {

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handle(ProcessDefinitionPublishedEvent event) {
        log.info("流程定义发布事件 - 事件ID:{}, 流程定义ID:{}, 流程标识:{}",
                event.getEventId(),
                event.getEntityId(),
                event.getEntity().getProcessKey());
    }
}
