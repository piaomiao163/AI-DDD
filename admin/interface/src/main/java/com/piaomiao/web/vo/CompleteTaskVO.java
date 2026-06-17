package com.piaomiao.web.vo;

import lombok.Data;

/**
 * 完成任务请求VO
 * <p>
 * 命名规范：操作(Complete) + 实体名(Task) + VO
 */
@Data
public class CompleteTaskVO {
    /**
     * 审批结果：approved / rejected
     */
    private String outcome;

    /**
     * 审批意见（可选）
     */
    private String comment;
}
