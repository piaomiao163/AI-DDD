package com.piaomiao.service;

import com.piaomiao.model.ProcessInstanceModel;
import com.piaomiao.response.PageResult;

import java.io.InputStream;
import java.util.Map;

/**
 * 流程实例服务接口
 */
public interface ProcessInstanceService {

    ProcessInstanceModel startProcess(String processDefinitionKey, String businessKey,
                                       String userId, Map<String, Object> variables,
                                       String title, String businessType, Long businessId,
                                       Integer priority);

    ProcessInstanceModel findById(Long id);

    ProcessInstanceModel findByActId(String actProcessInstanceId);

    PageResult<ProcessInstanceModel> findRunningByUser(Long startUserDbId, int page, int size);

    PageResult<ProcessInstanceModel> findHistoryByUser(Long startUserDbId, int page, int size);

    PageResult<ProcessInstanceModel> findByPage(int page, int size, String title, Integer status, String businessType);

    void terminateProcess(Long id, String reason);

    void withdrawProcess(Long id, String reason, Long currentUserId);

    InputStream generateDiagram(Long id);
}
