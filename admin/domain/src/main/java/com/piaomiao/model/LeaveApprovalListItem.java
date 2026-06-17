package com.piaomiao.model;

/**
 * 请假审批列表项（请假单 + 待办任务）
 */
public class LeaveApprovalListItem {

    /**
     * 请假单
     */
    private final LeaveModel leave;

    /**
     * 待办任务
     */
    private final TaskModel task;

    /**
     * 创建请假审批列表项
     *
     * @param leave 请假单
     * @param task 待办任务
     */
    public LeaveApprovalListItem(LeaveModel leave, TaskModel task) {
        this.leave = leave;
        this.task = task;
    }

    public LeaveModel getLeave() {
        return leave;
    }

    public TaskModel getTask() {
        return task;
    }
}
