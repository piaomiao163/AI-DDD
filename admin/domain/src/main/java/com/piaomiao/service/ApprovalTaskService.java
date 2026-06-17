package com.piaomiao.service;

import com.piaomiao.model.ApprovalTaskModel;
import com.piaomiao.response.PageResult;

import java.util.List;

/**
 * 审批任务领域服务
 *
 * @author system
 * @date 2026-06-14
 */
public interface ApprovalTaskService {

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
    PageResult<ApprovalTaskModel> findByPage(int page, int size, Long instanceId, String nodeName, Integer status);

    /**
     * 保存
     */
    Long save(ApprovalTaskModel model);

    /**
     * 更新
     */
    void update(ApprovalTaskModel model);

    /**
     * 删除
     */
    void delete(Long id);
}
