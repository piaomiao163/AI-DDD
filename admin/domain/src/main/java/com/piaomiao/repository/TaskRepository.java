package com.piaomiao.repository;

import com.piaomiao.model.ProcessInstanceModel;
import com.piaomiao.model.TaskModel;
import com.piaomiao.response.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 任务仓储接口
 * <p>
 * 只负责Activiti API调用和数据查询，不包含业务逻辑。
 */
public interface TaskRepository {

    PageResult<TaskModel> findTodoTasks(String userId, int page, int size);

    PageResult<TaskModel> findDoneTasks(String userId, int page, int size);

    PageResult<TaskModel> findClaimableTasks(String userId, int page, int size);

    TaskModel findById(String taskId);

    void claimTask(String taskId, String userId);

    void unclaimTask(String taskId);

    /**
     * 完成任务（纯Activiti调用，不包含事件发布等业务逻辑）
     *
     * @param taskId    任务ID
     * @param variables 流程变量（outcome等）
     */
    void completeTask(String taskId, Map<String, Object> variables);

    void delegateTask(String taskId, String delegateUserId);

    /**
     * 驳回任务（纯Activiti调用，不包含事件发布等业务逻辑）
     */
    void rejectTask(String taskId, String reason);

    PageResult<TaskModel> findTodoTasksByBusinessType(String userId, String businessType, int page, int size);

    /**
     * 判断流程实例是否仍在运行
     */
    boolean isProcessRunning(String processInstanceId);

    /**
     * 获取流程实例下一个任务列表
     */
    List<TaskModel> findNextTasks(String processInstanceId);

    /**
     * 获取已完成任务数
     */
    long countFinishedTasks(String processInstanceId);

    /**
     * 根据Activiti流程实例ID查询已完成的流程实例信息
     */
    ProcessInstanceModel findCompletedProcessInstance(String processInstanceId);
}
