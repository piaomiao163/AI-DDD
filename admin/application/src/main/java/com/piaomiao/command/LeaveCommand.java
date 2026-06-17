package com.piaomiao.command;

import com.piaomiao.dto.LeaveApprovalDetailDTO;
import com.piaomiao.dto.LeaveApprovalItemDTO;
import com.piaomiao.dto.LeaveDTO;
import com.piaomiao.dto.LeaveQueryDTO;
import com.piaomiao.dto.MyLeaveQueryDTO;
import com.piaomiao.response.PageResult;

/**
 * 请假单命令接口
 */
public interface LeaveCommand {

    /**
     * 提交请假申请
     *
     * @param dto 请假单DTO
     * @return 保存后的请假单DTO
     */
    LeaveDTO apply(LeaveDTO dto);

    /**
     * 根据ID查询请假单
     *
     * @param id 请假单ID
     * @return 请假单DTO
     */
    LeaveDTO findById(Long id);

    /**
     * 查询当前用户的请假审批列表
     *
     * @param page 页码
     * @param size 每页条数
     * @return 请假审批分页列表
     */
    PageResult<LeaveApprovalItemDTO> findApprovalList(int page, int size);

    /**
     * 根据任务ID查询请假审批详情
     *
     * @param taskId 任务ID
     * @return 请假审批详情
     */
    LeaveApprovalDetailDTO findApprovalDetailByTaskId(String taskId);

    /**
     * 查询申请人的请假单
     *
     * @param applicantId 申请人ID
     * @param queryDTO 查询条件
     * @return 请假单分页列表
     */
    PageResult<LeaveDTO> findByApplicantId(Long applicantId, MyLeaveQueryDTO queryDTO);

    /**
     * 管理端分页查询请假单
     *
     * @param queryDTO 查询条件
     * @return 请假单分页列表
     */
    PageResult<LeaveDTO> findByPage(LeaveQueryDTO queryDTO);

    /**
     * 撤回请假单
     *
     * @param id 请假单ID
     * @param reason 撤回原因
     * @param currentUserId 当前用户ID
     */
    void withdraw(Long id, String reason, Long currentUserId);
}
