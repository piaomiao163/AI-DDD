package com.piaomiao.service;

import com.piaomiao.model.LeaveApprovalListItem;
import com.piaomiao.model.LeaveModel;
import com.piaomiao.model.TaskModel;
import com.piaomiao.query.LeavePageQuery;
import com.piaomiao.query.MyLeavePageQuery;
import com.piaomiao.response.PageResult;

import java.util.List;

/**
 * 请假单服务接口
 */
public interface LeaveService {

    /**
     * 提交请假申请并启动流程
     * @param model 请假单模型
     * @return 保存后的请假单模型
     */
    LeaveModel apply(LeaveModel model);

    /**
     * 根据ID查询请假单
     * @param id 请假单ID
     * @return 请假单模型
     */
    LeaveModel findById(Long id);

    /**
     * 根据流程实例ID查询请假单
     * @param processInstanceId 流程实例ID
     * @return 请假单模型
     */
    LeaveModel findByProcessInstanceId(Long processInstanceId);

    /**
     * 批量查询请假单
     * @param ids 请假单ID列表
     * @return 请假单列表
     */
    List<LeaveModel> findByIds(List<Long> ids);

    /**
     * 分页查询申请人的请假单
     * @param applicantId 申请人ID
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<LeaveModel> findByApplicantId(Long applicantId, MyLeavePageQuery query);

    /**
     * 管理端分页查询请假单
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<LeaveModel> findByPage(LeavePageQuery query);

    /**
     * 查询当前用户的请假审批列表
     * @param page 页码
     * @param size 每页条数
     * @return 请假单与待办任务组合列表
     */
    PageResult<LeaveApprovalListItem> findApprovalList(int page, int size);

    /**
     * 根据任务ID查询审批详情所需的请假单与任务
     * @param taskId 任务ID
     * @return 任务模型
     */
    TaskModel findApprovalTaskById(String taskId);

    /**
     * 撤回请假单
     * @param id 请假单ID
     * @param reason 撤回原因
     * @param currentUserId 当前用户ID
     */
    void withdraw(Long id, String reason, Long currentUserId);
}
