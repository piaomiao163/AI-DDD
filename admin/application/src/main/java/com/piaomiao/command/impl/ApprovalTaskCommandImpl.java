package com.piaomiao.command.impl;

import com.piaomiao.command.ApprovalTaskCommand;
import com.piaomiao.dto.ApprovalTaskDTO;
import com.piaomiao.dto.ApprovalTaskQueryDTO;
import com.piaomiao.model.ApprovalTaskModel;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.ApprovalTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批任务命令实现
 *
 * @author system
 * @date 2026-06-14
 */
@Service
public class ApprovalTaskCommandImpl implements ApprovalTaskCommand {

    @Autowired
    private ApprovalTaskService approvalTaskService;

    @Override
    public ApprovalTaskModel findById(Long id) {
        return approvalTaskService.findById(id);
    }

    @Override
    public List<ApprovalTaskModel> findByInstanceId(Long instanceId) {
        return approvalTaskService.findByInstanceId(instanceId);
    }

    @Override
    public List<ApprovalTaskModel> findPendingByAssigneeId(Long assigneeId) {
        return approvalTaskService.findPendingByAssigneeId(assigneeId);
    }

    @Override
    public PageResult<ApprovalTaskModel> findByPage(ApprovalTaskQueryDTO queryDTO) {
        return approvalTaskService.findByPage(
                queryDTO.getPageNum(),
                queryDTO.getPageSize(),
                queryDTO.getInstanceId(),
                queryDTO.getNodeName(),
                queryDTO.getStatus()
        );
    }

    @Override
    public Long save(ApprovalTaskDTO dto) {
        ApprovalTaskModel model = new ApprovalTaskModel();
        model.setInstanceId(dto.getInstanceId());
        model.setProcessDefinitionId(dto.getProcessDefinitionId());
        model.setNodeId(dto.getNodeId());
        model.setNodeName(dto.getNodeName());
        model.setNodeType(dto.getNodeType());
        model.setNodeIndex(dto.getNodeIndex());
        model.setAssigneeId(dto.getAssigneeId());
        model.setAssigneeName(dto.getAssigneeName());
        model.setAssigneeType(dto.getAssigneeType());
        model.setApproveMode(dto.getApproveMode());
        model.setStatus(dto.getStatus());
        model.setOpinion(dto.getOpinion());
        model.setAttachments(dto.getAttachments());
        model.setSignature(dto.getSignature());
        model.setReceiveTime(dto.getReceiveTime());
        model.setDeadline(dto.getDeadline());
        model.setRemindTime(dto.getRemindTime());
        model.setCompleteTime(dto.getCompleteTime());
        model.setRemark(dto.getRemark());
        return approvalTaskService.save(model);
    }

    @Override
    public void update(ApprovalTaskDTO dto) {
        ApprovalTaskModel model = new ApprovalTaskModel();
        model.setId(dto.getId());
        model.setInstanceId(dto.getInstanceId());
        model.setProcessDefinitionId(dto.getProcessDefinitionId());
        model.setNodeId(dto.getNodeId());
        model.setNodeName(dto.getNodeName());
        model.setNodeType(dto.getNodeType());
        model.setNodeIndex(dto.getNodeIndex());
        model.setAssigneeId(dto.getAssigneeId());
        model.setAssigneeName(dto.getAssigneeName());
        model.setAssigneeType(dto.getAssigneeType());
        model.setApproveMode(dto.getApproveMode());
        model.setStatus(dto.getStatus());
        model.setOpinion(dto.getOpinion());
        model.setAttachments(dto.getAttachments());
        model.setSignature(dto.getSignature());
        model.setReceiveTime(dto.getReceiveTime());
        model.setDeadline(dto.getDeadline());
        model.setRemindTime(dto.getRemindTime());
        model.setCompleteTime(dto.getCompleteTime());
        model.setRemark(dto.getRemark());
        approvalTaskService.update(model);
    }

    @Override
    public void delete(Long id) {
        approvalTaskService.delete(id);
    }
}
