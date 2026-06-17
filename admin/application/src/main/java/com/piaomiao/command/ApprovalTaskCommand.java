package com.piaomiao.command;

import com.piaomiao.dto.ApprovalTaskDTO;
import com.piaomiao.dto.ApprovalTaskQueryDTO;
import com.piaomiao.model.ApprovalTaskModel;
import com.piaomiao.response.PageResult;

import java.util.List;

/**
 * 审批任务命令接口
 *
 * @author system
 * @date 2026-06-14
 */
public interface ApprovalTaskCommand {

    /**
     * 根据ID查询
     */
    ApprovalTaskModel findById(Long id);

    /**
     * 根据实例ID查询审批任务列表
     */
    List<ApprovalTaskModel> findByInstanceId(Long instanceId);

    /**
     * 查询待审批任务
     */
    List<ApprovalTaskModel> findPendingByAssigneeId(Long assigneeId);

    /**
     * 分页查询
     */
    PageResult<ApprovalTaskModel> findByPage(ApprovalTaskQueryDTO queryDTO);

    /**
     * 保存
     */
    Long save(ApprovalTaskDTO dto);

    /**
     * 更新
     */
    void update(ApprovalTaskDTO dto);

    /**
     * 删除
     */
    void delete(Long id);
}
