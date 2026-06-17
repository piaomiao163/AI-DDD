package com.piaomiao.repository;

import com.piaomiao.model.LeaveModel;
import com.piaomiao.query.LeavePageQuery;
import com.piaomiao.query.MyLeavePageQuery;
import com.piaomiao.response.PageResult;

import java.util.List;

/**
 * 请假单仓储接口
 */
public interface LeaveRepository {

    /**
     * 保存请假单
     *
     * @param model 请假单模型
     * @return 保存后的请假单模型
     */
    LeaveModel save(LeaveModel model);

    /**
     * 更新请假单
     *
     * @param model 请假单模型
     * @return 更新后的请假单模型
     */
    LeaveModel update(LeaveModel model);

    /**
     * 根据ID查询请假单
     *
     * @param id 请假单ID
     * @return 请假单模型
     */
    LeaveModel findById(Long id);

    /**
     * 根据流程实例ID查询请假单
     *
     * @param processInstanceId 流程实例ID
     * @return 请假单模型
     */
    LeaveModel findByProcessInstanceId(Long processInstanceId);

    /**
     * 批量查询请假单
     *
     * @param ids 请假单ID列表
     * @return 请假单列表
     */
    List<LeaveModel> findByIds(List<Long> ids);

    /**
     * 分页查询申请人的请假单
     *
     * @param applicantId 申请人ID
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<LeaveModel> findByApplicantId(Long applicantId, MyLeavePageQuery query);

    /**
     * 管理端分页查询请假单
     *
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<LeaveModel> findByPage(LeavePageQuery query);

}
