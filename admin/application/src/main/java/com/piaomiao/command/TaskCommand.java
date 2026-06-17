package com.piaomiao.command;

import com.piaomiao.dto.TaskCompleteDTO;
import com.piaomiao.dto.TaskDTO;
import com.piaomiao.dto.TaskQueryDTO;
import com.piaomiao.response.PageResult;

/**
 * 任务命令接口
 */
public interface TaskCommand {
    PageResult<TaskDTO> findTodoTasks(String userId, TaskQueryDTO queryDTO);

    PageResult<TaskDTO> findDoneTasks(String userId, TaskQueryDTO queryDTO);

    PageResult<TaskDTO> findClaimableTasks(String userId, TaskQueryDTO queryDTO);

    TaskDTO findById(String taskId);

    void claimTask(String taskId, String userId);

    void unclaimTask(String taskId);

    void completeTask(String taskId, TaskCompleteDTO dto);

    void delegateTask(String taskId, String delegateUserId);

    void rejectTask(String taskId, String processInstanceId, String reason);
}
