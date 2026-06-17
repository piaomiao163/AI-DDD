package com.piaomiao.dto;

import lombok.Data;

/**
 * 完成任务DTO
 * <p>
 * 命名规范：实体名(Task) + 功能(Complete) + 后缀(DTO)
 */
@Data
public class TaskCompleteDTO {
    /**
     * 审批结果：approved / rejected
     */
    private String outcome;

    /**
     * 审批意见（可选）
     */
    private String comment;

    /**
     * 驳回原因（仅驳回时使用）
     */
    private String rejectReason;
}
