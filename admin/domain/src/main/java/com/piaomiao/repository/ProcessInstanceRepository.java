package com.piaomiao.repository;

import com.piaomiao.model.ProcessInstanceModel;
import com.piaomiao.response.PageResult;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 流程实例仓储接口
 */
public interface ProcessInstanceRepository {

    /**
     * 启动流程实例（双写Activiti + 自有表）
     */
    ProcessInstanceModel startProcess(String processDefinitionKey, String businessKey,
                                       String userId, Map<String, Object> variables,
                                       String title, String businessType, Long businessId,
                                       Integer priority);

    /**
     * 根据业务表ID查询
     */
    ProcessInstanceModel findById(Long id);

    /**
     * 根据Activiti流程实例ID查询
     */
    ProcessInstanceModel findByActProcessInstanceId(String actProcessInstanceId);

    /**
     * 根据业务表ID批量查询
     * @param ids 流程实例ID列表
     * @return 流程实例模型列表
     */
    List<ProcessInstanceModel> findByIds(List<Long> ids);

    /**
     * 查询用户的运行中实例
     */
    PageResult<ProcessInstanceModel> findRunningByUser(Long startUserDbId, int page, int size);

    /**
     * 查询用户的历史实例
     */
    PageResult<ProcessInstanceModel> findHistoryByUser(Long startUserDbId, int page, int size);

    /**
     * 管理端分页查询
     */
    PageResult<ProcessInstanceModel> findByPage(int page, int size, String title, Integer status, String businessType);

    /**
     * 终止流程实例
     */
    void terminateProcess(Long id, String reason);

    /**
     * 撤回流程实例
     */
    void withdrawProcess(Long id, String reason, Long currentUserId);

    /**
     * 插入业务表记录
     */
    ProcessInstanceModel save(ProcessInstanceModel model);

    /**
     * 更新业务表记录
     */
    ProcessInstanceModel update(ProcessInstanceModel model);

    /**
     * 按Activiti ID更新状态（事件处理器调用）
     */
    void updateStatusByActId(String actProcessInstanceId, Integer status,
                              LocalDateTime endTime, String deleteReason);

    /**
     * 更新流程实例当前节点信息（任务完成后调用）
     */
    void updateCurrentNodeInfo(String actProcessInstanceId, String currentNodeName,
                               String currentNodeId, String currentAssignees, Integer completedNodes);

    /**
     * 生成带当前节点高亮的流程图
     * @param id 业务表ID
     * @return PNG图片输入流
     */
    InputStream generateDiagram(Long id);
}
