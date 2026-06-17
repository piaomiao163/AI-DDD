package com.piaomiao.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 审批任务DTO
 *
 * @author system
 * @date 2026-06-14
 */
@Data
public class ApprovalTaskDTO {

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
}
