package com.piaomiao.service;

import com.piaomiao.model.TaskModel;
import com.piaomiao.response.PageResult;

/**
 * 任务服务接口
 * <p>
 * 负责任务相关的业务逻辑：事件发布、流程状态检测、跨聚合数据更新。
 */
public interface TaskAppService {

    PageResult<TaskModel> findTodoTasks(String userId, int page, int size);

    PageResult<TaskModel> findDoneTasks(String userId, int page, int size);

    PageResult<TaskModel> findClaimableTasks(String userId, int page, int size);

    TaskModel findById(String taskId);

    void claimTask(String taskId, String userId);

    void unclaimTask(String taskId);

    /**
     * 完成任务
     *
     * @param taskId  任务ID
     * @param outcome 审批结果：approved / rejected
     * @param comment 审批意见（可选）
     */
    void completeTask(String taskId, String outcome, String comment);

    void delegateTask(String taskId, String delegateUserId);

    void rejectTask(String taskId, String processInstanceId, String reason);

    PageResult<TaskModel> findTodoTasksByBusinessType(String userId, String businessType, int page, int size);
}
