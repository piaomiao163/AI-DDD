package com.piaomiao.event.handler;

import com.piaomiao.event.process.ProcessInstanceCompletedEvent;
import com.piaomiao.event.process.ProcessInstanceWithdrawnEvent;
import com.piaomiao.event.process.TaskRejectedEvent;
import com.piaomiao.model.LeaveModel;
import com.piaomiao.model.ProcessInstanceModel;
import com.piaomiao.repository.LeaveRepository;
import com.piaomiao.repository.ProcessInstanceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.function.Consumer;

/**
 * 请假单状态同步处理器
 * <p>
 * 监听流程实例完成/撤回/驳回事件，通过领域模型行为同步请假单状态。
 */
@Slf4j
@Component
public class LeaveSyncHandler {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private ProcessInstanceRepository processInstanceRepository;

    /**
     * 流程完成：请假单审批通过
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleCompleted(ProcessInstanceCompletedEvent event) {
        syncLeaveByBusinessKey(event.getEntity().getBusinessType(), event.getEntity().getBusinessKey(),
                LeaveModel::approve, "审批通过");
    }

    /**
     * 流程撤回：同步请假单撤回状态
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleWithdrawn(ProcessInstanceWithdrawnEvent event) {
        String reason = event.getReason();
        syncLeaveByBusinessKey(event.getEntity().getBusinessType(), event.getEntity().getBusinessKey(),
                leave -> leave.markWithdrawn(reason), "已撤回");
    }

    /**
     * 任务驳回：同步请假单驳回状态
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleRejected(TaskRejectedEvent event) {
        String processInstanceId = event.getEntity().getProcessInstanceId();
        if (processInstanceId == null) {
            return;
        }
        ProcessInstanceModel processInstance = processInstanceRepository.findByActProcessInstanceId(processInstanceId);
        if (processInstance == null) {
            return;
        }
        String reason = event.getReason();
        syncLeaveByBusinessKey(processInstance.getBusinessType(), processInstance.getBusinessKey(),
                leave -> leave.reject(reason), "已驳回");
    }

    private void syncLeaveByBusinessKey(String businessType, String businessKey,
                                       Consumer<LeaveModel> action, String actionLabel) {
        if (!"leave".equals(businessType) || businessKey == null) {
            return;
        }
        try {
            Long leaveId = Long.valueOf(businessKey);
            LeaveModel leave = leaveRepository.findById(leaveId);
            if (leave == null) {
                return;
            }
            action.accept(leave);
            leaveRepository.update(leave);
            log.info("请假单{} - leaveId:{}", actionLabel, leaveId);
        } catch (NumberFormatException e) {
            log.warn("businessKey非数字，无法关联请假单: {}", businessKey);
        } catch (IllegalStateException e) {
            log.warn("请假单状态同步跳过 - businessKey:{}, reason:{}", businessKey, e.getMessage());
        }
    }
}
