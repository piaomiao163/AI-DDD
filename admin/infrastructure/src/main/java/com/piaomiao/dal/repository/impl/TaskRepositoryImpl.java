package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.piaomiao.dal.entity.ProcessInstanceDO;
import com.piaomiao.dal.mapper.ProcessInstanceMapper;
import com.piaomiao.model.ProcessInstanceModel;
import com.piaomiao.model.TaskModel;
import com.piaomiao.repository.TaskRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.util.UserContext;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务仓储实现
 * <p>
 * 只负责Activiti API调用和数据查询，不包含业务逻辑（事件发布等由Service层处理）。
 */
@Repository
public class TaskRepositoryImpl implements TaskRepository {

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private ProcessInstanceMapper processInstanceMapper;

    @Override
    public PageResult<TaskModel> findTodoTasks(String userId, int page, int size) {
        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime().desc();
        long total = query.count();
        List<Task> tasks = query.listPage((page - 1) * size, size);
        List<TaskModel> models = tasks.stream()
                .map(this::convertFromTask)
                .collect(Collectors.toList());
        return new PageResult<>(total, models, page, size);
    }

    @Override
    public PageResult<TaskModel> findDoneTasks(String userId, int page, int size) {
        HistoricTaskInstanceQuery query = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .orderByHistoricTaskInstanceEndTime().desc();
        long total = query.count();
        List<HistoricTaskInstance> tasks = query.listPage((page - 1) * size, size);
        List<TaskModel> models = tasks.stream()
                .map(this::convertFromHistoricTask)
                .collect(Collectors.toList());
        return new PageResult<>(total, models, page, size);
    }

    @Override
    public PageResult<TaskModel> findClaimableTasks(String userId, int page, int size) {
        TaskQuery query = taskService.createTaskQuery()
                .taskCandidateUser(userId)
                .orderByTaskCreateTime().desc();
        long total = query.count();
        List<Task> tasks = query.listPage((page - 1) * size, size);
        List<TaskModel> models = tasks.stream()
                .map(this::convertFromTask)
                .collect(Collectors.toList());
        return new PageResult<>(total, models, page, size);
    }

    @Override
    public TaskModel findById(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task != null) {
            return convertFromTask(task);
        }
        HistoricTaskInstance historicTask = historyService.createHistoricTaskInstanceQuery()
                .taskId(taskId).singleResult();
        return convertFromHistoricTask(historicTask);
    }

    @Override
    public void claimTask(String taskId, String userId) {
        org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(userId);
        taskService.claim(taskId, userId);
    }

    @Override
    public void unclaimTask(String taskId) {
        setAuthenticatedUser();
        taskService.unclaim(taskId);
    }

    @Override
    public void completeTask(String taskId, Map<String, Object> variables) {
        setAuthenticatedUser();
        taskService.complete(taskId, variables);
    }

    @Override
    public void delegateTask(String taskId, String delegateUserId) {
        setAuthenticatedUser();
        taskService.delegateTask(taskId, delegateUserId);
    }

    @Override
    public void rejectTask(String taskId, String reason) {
        setAuthenticatedUser();
        Map<String, Object> variables = new java.util.HashMap<>();
        variables.put("outcome", "rejected");
        variables.put("rejectReason", reason);
        taskService.complete(taskId, variables);
    }

    @Override
    public PageResult<TaskModel> findTodoTasksByBusinessType(String userId, String businessType, int page, int size) {
        LambdaQueryWrapper<ProcessInstanceDO> piWrapper = new LambdaQueryWrapper<>();
        piWrapper.eq(ProcessInstanceDO::getBusinessType, businessType)
                 .eq(ProcessInstanceDO::getStatus, 0)
                 .select(ProcessInstanceDO::getActProcessInstanceId);
        List<ProcessInstanceDO> piList = processInstanceMapper.selectList(piWrapper);
        if (piList.isEmpty()) {
            return new PageResult<TaskModel>(0L, new ArrayList<>(), page, size);
        }
        List<String> actProcessInstanceIds = piList.stream()
                .map(ProcessInstanceDO::getActProcessInstanceId)
                .collect(Collectors.toList());

        TaskQuery query = taskService.createTaskQuery()
                .taskAssignee(userId)
                .processInstanceIdIn(actProcessInstanceIds)
                .orderByTaskCreateTime().desc();
        long total = query.count();
        List<Task> tasks = query.listPage((page - 1) * size, size);
        List<TaskModel> models = tasks.stream()
                .map(this::convertFromTask)
                .collect(Collectors.toList());
        return new PageResult<>(total, models, page, size);
    }

    @Override
    public boolean isProcessRunning(String processInstanceId) {
        return runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult() != null;
    }

    @Override
    public List<TaskModel> findNextTasks(String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId).list();
        return tasks.stream().map(this::convertFromTask).collect(Collectors.toList());
    }

    @Override
    public long countFinishedTasks(String processInstanceId) {
        return historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .count();
    }

    @Override
    public ProcessInstanceModel findCompletedProcessInstance(String processInstanceId) {
        HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();
        if (hpi == null || hpi.getEndTime() == null) {
            return null;
        }
        // 从自有表补全businessType等业务字段
        ProcessInstanceDO piDO = processInstanceMapper.selectOne(
                new LambdaQueryWrapper<ProcessInstanceDO>()
                        .eq(ProcessInstanceDO::getActProcessInstanceId, processInstanceId));

        ProcessInstanceModel model = new ProcessInstanceModel();
        model.setProcessInstanceId(hpi.getId());
        model.setProcessDefinitionId(hpi.getProcessDefinitionId());
        model.setProcessDefinitionKey(hpi.getProcessDefinitionKey());
        model.setProcessName(hpi.getName());
        model.setBusinessKey(hpi.getBusinessKey());
        model.setStartUserId(hpi.getStartUserId());
        model.setStatus(ProcessInstanceModel.STATUS_COMPLETED);
        if (piDO != null) {
            model.setBusinessType(piDO.getBusinessType());
            model.setBusinessId(piDO.getBusinessId());
            model.setId(piDO.getId());
        }
        if (hpi.getStartTime() != null) {
            model.setStartTime(toLocalDateTime(hpi.getStartTime()));
        }
        if (hpi.getEndTime() != null) {
            model.setEndTime(toLocalDateTime(hpi.getEndTime()));
        }
        return model;
    }

    // ==================== 私有方法 ====================

    private TaskModel convertFromTask(Task task) {
        if (task == null) return null;
        TaskModel model = new TaskModel();
        model.setTaskId(task.getId());
        model.setTaskDefinitionKey(task.getTaskDefinitionKey());
        model.setTaskName(task.getName());
        model.setProcessInstanceId(task.getProcessInstanceId());
        model.setProcessDefinitionId(task.getProcessDefinitionId());
        model.setAssignee(task.getAssignee());
        model.setOwner(task.getOwner());
        model.setPriority(task.getPriority());
        model.setDescription(task.getDescription());
        if (task.getCreateTime() != null) {
            model.setCreateTime(toLocalDateTime(task.getCreateTime()));
        }
        if (task.getDueDate() != null) {
            model.setDueDate(toLocalDateTime(task.getDueDate()));
        }
        enrichWithProcessInstanceInfo(model, task.getProcessInstanceId());
        return model;
    }

    private TaskModel convertFromHistoricTask(HistoricTaskInstance task) {
        if (task == null) return null;
        TaskModel model = new TaskModel();
        model.setTaskId(task.getId());
        model.setTaskDefinitionKey(task.getTaskDefinitionKey());
        model.setTaskName(task.getName());
        model.setProcessInstanceId(task.getProcessInstanceId());
        model.setProcessDefinitionId(task.getProcessDefinitionId());
        model.setAssignee(task.getAssignee());
        model.setOwner(task.getOwner());
        model.setPriority(task.getPriority());
        model.setDescription(task.getDescription());
        model.setDeleteReason(task.getDeleteReason());
        if (task.getCreateTime() != null) {
            model.setCreateTime(toLocalDateTime(task.getCreateTime()));
        }
        if (task.getEndTime() != null) {
            model.setEndTime(toLocalDateTime(task.getEndTime()));
        }
        if (task.getClaimTime() != null) {
            model.setClaimTime(toLocalDateTime(task.getClaimTime()));
        }
        enrichWithProcessInstanceInfo(model, task.getProcessInstanceId());
        return model;
    }

    private void enrichWithProcessInstanceInfo(TaskModel model, String actProcessInstanceId) {
        if (actProcessInstanceId == null) return;
        LambdaQueryWrapper<ProcessInstanceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessInstanceDO::getActProcessInstanceId, actProcessInstanceId)
               .select(ProcessInstanceDO::getTitle, ProcessInstanceDO::getBusinessType,
                       ProcessInstanceDO::getBusinessId, ProcessInstanceDO::getProcessDefinitionKey,
                       ProcessInstanceDO::getProcessName);
        ProcessInstanceDO pi = processInstanceMapper.selectOne(wrapper);
        if (pi != null) {
            model.setTitle(pi.getTitle());
            model.setBusinessType(pi.getBusinessType());
            model.setBusinessId(pi.getBusinessId());
            model.setProcessDefinitionKey(pi.getProcessDefinitionKey());
            model.setProcessName(pi.getProcessName());
        }
    }

    private LocalDateTime toLocalDateTime(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private void setAuthenticatedUser() {
        if (UserContext.isAuthenticated()) {
            org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(UserContext.requireCurrentUsername());
        }
    }
}
