package com.piaomiao.service.impl;

import com.piaomiao.model.ApprovalTaskModel;
import com.piaomiao.repository.ApprovalTaskRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.ApprovalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批任务领域服务实现
 *
 * @author system
 * @date 2026-06-14
 */
@Service
public class ApprovalTaskServiceImpl implements ApprovalTaskService {

    @Autowired
    private ApprovalTaskRepository approvalTaskRepository;

    @Override
    public ApprovalTaskModel findById(Long id) {
        return approvalTaskRepository.findById(id);
    }

    @Override
    public List<ApprovalTaskModel> findByInstanceId(Long instanceId) {
        return approvalTaskRepository.findByInstanceId(instanceId);
    }

    @Override
    public List<ApprovalTaskModel> findPendingByAssigneeId(Long assigneeId) {
        return approvalTaskRepository.findPendingByAssigneeId(assigneeId);
    }

    @Override
    public PageResult<ApprovalTaskModel> findByPage(int page, int size, Long instanceId, String nodeName, Integer status) {
        return approvalTaskRepository.findByPage(page, size, instanceId, nodeName, status);
    }

    @Override
    public Long save(ApprovalTaskModel model) {
        return approvalTaskRepository.save(model);
    }

    @Override
    public void update(ApprovalTaskModel model) {
        approvalTaskRepository.update(model);
    }

    @Override
    public void delete(Long id) {
        approvalTaskRepository.delete(id);
    }
}
