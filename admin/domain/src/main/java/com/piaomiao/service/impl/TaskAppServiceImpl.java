package com.piaomiao.service.impl;

import com.piaomiao.event.DomainEventPublisher;
import com.piaomiao.event.process.ProcessInstanceCompletedEvent;
import com.piaomiao.event.process.TaskCompletedEvent;
import com.piaomiao.event.process.TaskRejectedEvent;
import com.piaomiao.model.ProcessInstanceModel;
import com.piaomiao.model.TaskModel;
import com.piaomiao.repository.ProcessInstanceRepository;
import com.piaomiao.repository.TaskRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.TaskAppService;
import com.piaomiao.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 任务服务实现
 * <p>
 * 包含任务完成的业务逻辑：事件发布、流程状态检测、跨聚合数据更新。
 * Repository 只做纯 Activiti API 调用。
 */
@Service
public class TaskAppServiceImpl implements TaskAppService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private ProcessInstanceRepository processInstanceRepository;

    @Override
    public PageResult<TaskModel> findTodoTasks(String userId, int page, int size) {
        return taskRepository.findTodoTasks(userId, page, size);
    }

    @Override
    public PageResult<TaskModel> findDoneTasks(String userId, int page, int size) {
        return taskRepository.findDoneTasks(userId, page, size);
    }

    @Override
    public PageResult<TaskModel> findClaimableTasks(String userId, int page, int size) {
        return taskRepository.findClaimableTasks(userId, page, size);
    }

    @Override
    public TaskModel findById(String taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public void claimTask(String taskId, String userId) {
        taskRepository.claimTask(taskId, userId);
    }

    @Override
    public void unclaimTask(String taskId) {
        TaskModel model = taskRepository.findById(taskId);
        validateAssignedToCurrentUser(model);
        taskRepository.unclaimTask(taskId);
    }

    @Override
    public void completeTask(String taskId, String outcome, String comment) {
        // 1. 校验参数
        if (outcome == null || outcome.isEmpty()) {
            throw new IllegalArgumentException("任务完成时必须指定outcome(approved/rejected)");
        }

        // 2. 获取任务信息（complete后Activiti会删除运行时任务，需先获取）
        TaskModel model = taskRepository.findById(taskId);
        validateAssignedToCurrentUser(model);

        // 3. 组装流程变量，调用Activiti完成任务
        Map<String, Object> variables = new HashMap<>();
        variables.put("outcome", outcome);
        if (comment != null) {
            variables.put("comment", comment);
        }
        taskRepository.completeTask(taskId, variables);

        // 4. 发布任务完成事件
        if (model != null) {
            domainEventPublisher.publish(new TaskCompletedEvent(this, model, outcome));

            // 5. 检测流程是否已完成
            String procInstId = model.getProcessInstanceId();
            if (!taskRepository.isProcessRunning(procInstId)) {
                // 流程已结束
                ProcessInstanceModel completedModel = taskRepository.findCompletedProcessInstance(procInstId);
                if (completedModel != null) {
                    domainEventPublisher.publish(new ProcessInstanceCompletedEvent(this, completedModel));
                }
            } else {
                // 流程仍在运行，更新当前节点信息
                updateCurrentNodeInfo(procInstId);
            }
        }
    }

    @Override
    public void delegateTask(String taskId, String delegateUserId) {
        TaskModel model = taskRepository.findById(taskId);
        validateAssignedToCurrentUser(model);
        taskRepository.delegateTask(taskId, delegateUserId);
    }

    @Override
    public void rejectTask(String taskId, String processInstanceId, String reason) {
        // 1. 获取任务信息（reject后Activiti会删除运行时任务，需先获取）
        TaskModel model = taskRepository.findById(taskId);
        validateAssignedToCurrentUser(model);

        // 2. 调用Activiti驳回
        taskRepository.rejectTask(taskId, reason);

        // 3. 发布任务驳回事件
        if (model != null) {
            domainEventPublisher.publish(new TaskRejectedEvent(this, model, reason));
        }
    }

    @Override
    public PageResult<TaskModel> findTodoTasksByBusinessType(String userId, String businessType, int page, int size) {
        return taskRepository.findTodoTasksByBusinessType(userId, businessType, page, size);
    }

    /**
     * 更新流程实例的当前节点信息
     */
    private void updateCurrentNodeInfo(String procInstId) {
        List<TaskModel> nextTasks = taskRepository.findNextTasks(procInstId);
        if (nextTasks.isEmpty()) return;

        TaskModel nextTask = nextTasks.get(0);
        long finishedCount = taskRepository.countFinishedTasks(procInstId);

        // 构建当前审批人列表JSON
        List<Map<String, Object>> assigneeList = new ArrayList<>();
        for (TaskModel t : nextTasks) {
            Map<String, Object> assignee = new HashMap<>();
            assignee.put("userId", t.getAssignee());
            assigneeList.add(assignee);
        }
        String assigneesJson = null;
        try {
            assigneesJson = new com.fasterxml.jackson.databind.ObjectMapper()
                    .writeValueAsString(assigneeList);
        } catch (Exception e) { /* ignore */ }

        processInstanceRepository.updateCurrentNodeInfo(
                procInstId, nextTask.getTaskName(), nextTask.getTaskDefinitionKey(),
                assigneesJson, (int) finishedCount);
    }

    private void validateAssignedToCurrentUser(TaskModel task) {
        if (task == null || task.getTaskId() == null) {
            throw new IllegalArgumentException("任务不存在");
        }
        String currentUsername = UserContext.requireCurrentUsername();
        if (!Objects.equals(task.getAssignee(), currentUsername)) {
            throw new IllegalStateException("当前用户不是任务处理人，不能操作该任务");
        }
    }
}
