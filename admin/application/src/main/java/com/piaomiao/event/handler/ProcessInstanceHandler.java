package com.piaomiao.event.handler;

import com.piaomiao.event.process.ProcessInstanceCompletedEvent;
import com.piaomiao.event.process.ProcessInstanceWithdrawnEvent;
import com.piaomiao.model.ProcessInstanceModel;
import com.piaomiao.repository.ProcessInstanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

/**
 * 流程实例状态同步处理器
 * <p>
 * 监听流程完成/撤回事件，同步更新自有表状态。
 * 使用 BEFORE_COMMIT 保证与主事务强一致性。
 */
@Slf4j
@Component
public class ProcessInstanceHandler {

    @Autowired
    private ProcessInstanceRepository processInstanceRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleCompleted(ProcessInstanceCompletedEvent event) {
        String activitiPid = event.getEntity().getProcessInstanceId();
        LocalDateTime endTime = event.getEntity().getEndTime();
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        processInstanceRepository.updateStatusByActId(
                activitiPid, ProcessInstanceModel.STATUS_COMPLETED, endTime, null);
        log.info("同步流程实例完成状态 - activitiPid:{}, endTime:{}", activitiPid, endTime);
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleWithdrawn(ProcessInstanceWithdrawnEvent event) {
        log.info("流程实例撤回事件 - 事件ID:{}, 流程实例ID:{}, 原因:{}",
                event.getEventId(),
                event.getEntity().getProcessInstanceId(),
                event.getReason());
    }
}
