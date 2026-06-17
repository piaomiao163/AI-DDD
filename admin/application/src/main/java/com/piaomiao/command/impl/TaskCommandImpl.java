package com.piaomiao.command.impl;

import com.piaomiao.command.TaskCommand;
import com.piaomiao.dto.TaskCompleteDTO;
import com.piaomiao.dto.TaskDTO;
import com.piaomiao.dto.TaskQueryDTO;
import com.piaomiao.model.TaskModel;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.TaskAppService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TaskCommandImpl implements TaskCommand {

    @Autowired
    private TaskAppService taskAppService;

    @Override
    public PageResult<TaskDTO> findTodoTasks(String userId, TaskQueryDTO queryDTO) {
        PageResult<TaskModel> result = taskAppService.findTodoTasks(userId, queryDTO.getPageNum(), queryDTO.getPageSize());
        return convertPageResult(result);
    }

    @Override
    public PageResult<TaskDTO> findDoneTasks(String userId, TaskQueryDTO queryDTO) {
        PageResult<TaskModel> result = taskAppService.findDoneTasks(userId, queryDTO.getPageNum(), queryDTO.getPageSize());
        return convertPageResult(result);
    }

    @Override
    public PageResult<TaskDTO> findClaimableTasks(String userId, TaskQueryDTO queryDTO) {
        PageResult<TaskModel> result = taskAppService.findClaimableTasks(userId, queryDTO.getPageNum(), queryDTO.getPageSize());
        return convertPageResult(result);
    }

    @Override
    public TaskDTO findById(String taskId) {
        TaskModel model = taskAppService.findById(taskId);
        return convertToDTO(model);
    }

    @Override
    public void claimTask(String taskId, String userId) {
        taskAppService.claimTask(taskId, userId);
    }

    @Override
    public void unclaimTask(String taskId) {
        taskAppService.unclaimTask(taskId);
    }

    @Override
    public void completeTask(String taskId, TaskCompleteDTO dto) {
        taskAppService.completeTask(taskId, dto.getOutcome(), dto.getComment());
    }

    @Override
    public void delegateTask(String taskId, String delegateUserId) {
        taskAppService.delegateTask(taskId, delegateUserId);
    }

    @Override
    public void rejectTask(String taskId, String processInstanceId, String reason) {
        taskAppService.rejectTask(taskId, processInstanceId, reason);
    }

    private TaskDTO convertToDTO(TaskModel model) {
        if (model == null) return null;
        TaskDTO dto = new TaskDTO();
        BeanUtils.copyProperties(model, dto);
        if (model.getCreateTime() != null) dto.setCreateTime(model.getCreateTime().toString());
        if (model.getDueDate() != null) dto.setDueDate(model.getDueDate().toString());
        if (model.getClaimTime() != null) dto.setClaimTime(model.getClaimTime().toString());
        if (model.getEndTime() != null) dto.setEndTime(model.getEndTime().toString());
        return dto;
    }

    private PageResult<TaskDTO> convertPageResult(PageResult<TaskModel> result) {
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::convertToDTO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }
}
