package com.piaomiao.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 流程实例领域模型
 */
@Data
public class ProcessInstanceModel {

    // ==================== 状态常量 ====================

    /** 运行中 */
    public static final int STATUS_RUNNING = 0;
    /** 已完成 */
    public static final int STATUS_COMPLETED = 1;
    /** 已终止 */
    public static final int STATUS_TERMINATED = 2;
    /** 已撤回 */
    public static final int STATUS_WITHDRAWN = 3;

    /**
     * 业务表主键
     */
    private Long id;

    /**
     * 实例编号(如PROC202606130001)
     */
    private String instanceNo;

    /**
     * 审批标题
     */
    private String title;

    /**
     * Activiti流程实例ID
     */
    private String processInstanceId;

    /**
     * Activiti流程定义ID
     */
    private String processDefinitionId;

    /**
     * 流程定义Key（业务标识）
     */
    private String processDefinitionKey;

    /**
     * 自定义流程定义表ID
     */
    private Long processDefinitionDbId;

    /**
     * 流程名称
     */
    private String processName;

    /**
     * 业务Key
     */
    private String businessKey;

    /**
     * 业务类型(如: leave/expense/purchase)
     */
    private String businessType;

    /**
     * 业务单据主键ID(如请假单ID)
     */
    private Long businessId;

    /**
     * 发起人ID(Activiti中存的是username)
     */
    private String startUserId;

    /**
     * 发起人ID(sys_user.id)
     */
    private Long startUserDbId;

    /**
     * 发起人姓名(冗余)
     */
    private String startUserName;

    /**
     * 发起人部门ID
     */
    private Long applicantDeptId;

    /**
     * 启动时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 状态：0-运行中，1-已完成，2-已终止，3-已撤回
     */
    private Integer status;

    /**
     * 当前节点名称(冗余)
     */
    private String currentNodeName;

    /**
     * 当前节点ID
     */
    private String currentNodeId;

    /**
     * 当前待审批人列表JSON(如:[{"userId":1,"userName":"张三"}])
     */
    private String currentAssignees;

    /**
     * 总审批节点数
     */
    private Integer totalNodes;

    /**
     * 已完成节点数
     */
    private Integer completedNodes;

    /**
     * 优先级 0普通 1紧急 2特急
     */
    private Integer priority;

    /**
     * 终止/撤回原因
     */
    private String deleteReason;

    /**
     * 流程变量
     */
    private Map<String, Object> variables;

    /**
     * 流程变量JSON快照(持久化用)
     */
    private String variablesJson;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Integer deleted;
}
