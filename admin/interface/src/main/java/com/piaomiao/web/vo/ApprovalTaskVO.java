package com.piaomiao.web.vo;

import com.piaomiao.dto.ApprovalTaskDTO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 审批任务请求VO
 *
 * @author system
 * @date 2026-06-14
 */
@Data
public class ApprovalTaskVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long instanceId;

    private Long processDefinitionId;

    private String nodeId;

    private String nodeName;

    private String nodeType;

    private Integer nodeIndex;

    private Long assigneeId;

    private String assigneeName;

    private String assigneeType;

    private String approveMode;

    private Integer status;

    private String opinion;

    private String attachments;

    private String signature;

    private LocalDateTime receiveTime;

    private LocalDateTime deadline;

    private LocalDateTime remindTime;

    private LocalDateTime completeTime;

    private String remark;

    public static ApprovalTaskDTO toDTO(ApprovalTaskVO vo) {
        ApprovalTaskDTO dto = new ApprovalTaskDTO();
        dto.setId(vo.getId());
        dto.setInstanceId(vo.getInstanceId());
        dto.setProcessDefinitionId(vo.getProcessDefinitionId());
        dto.setNodeId(vo.getNodeId());
        dto.setNodeName(vo.getNodeName());
        dto.setNodeType(vo.getNodeType());
        dto.setNodeIndex(vo.getNodeIndex());
        dto.setAssigneeId(vo.getAssigneeId());
        dto.setAssigneeName(vo.getAssigneeName());
        dto.setAssigneeType(vo.getAssigneeType());
        dto.setApproveMode(vo.getApproveMode());
        dto.setStatus(vo.getStatus());
        dto.setOpinion(vo.getOpinion());
        dto.setAttachments(vo.getAttachments());
        dto.setSignature(vo.getSignature());
        dto.setReceiveTime(vo.getReceiveTime());
        dto.setDeadline(vo.getDeadline());
        dto.setRemindTime(vo.getRemindTime());
        dto.setCompleteTime(vo.getCompleteTime());
        dto.setRemark(vo.getRemark());
        return dto;
    }
}
