package com.piaomiao.web.vo;

import lombok.Data;

import java.util.Map;

/**
 * 启动流程请求VO
 */
@Data
public class StartProcessVO {
    /**
     * 流程定义Key（必填）
     */
    private String processDefinitionKey;
    /**
     * 审批标题（必填）
     */
    private String title;
    /**
     * 业务Key（可选）
     */
    private String businessKey;
    /**
     * 业务类型（可选）
     */
    private String businessType;
    /**
     * 业务单据主键ID（可选，如请假单ID）
     */
    private Long businessId;
    /**
     * 优先级 0普通 1紧急 2特急（可选，默认0）
     */
    private Integer priority;
    /**
     * 流程变量（可选）
     */
    private Map<String, Object> variables;
}
