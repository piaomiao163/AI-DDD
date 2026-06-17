package com.piaomiao.command;

import com.piaomiao.dto.MyProcessInstanceQueryDTO;
import com.piaomiao.dto.ProcessInstanceDTO;
import com.piaomiao.dto.ProcessInstanceQueryDTO;
import com.piaomiao.response.PageResult;

import java.util.Map;

/**
 * 流程实例命令接口
 */
public interface ProcessInstanceCommand {
    ProcessInstanceDTO startProcess(String processDefinitionKey, String businessKey,
                                     String userId, Map<String, Object> variables,
                                     String title, String businessType, Long businessId,
                                     Integer priority);

    ProcessInstanceDTO findById(Long id);

    PageResult<ProcessInstanceDTO> findRunningByUser(Long startUserDbId, MyProcessInstanceQueryDTO queryDTO);

    PageResult<ProcessInstanceDTO> findHistoryByUser(Long startUserDbId, MyProcessInstanceQueryDTO queryDTO);

    PageResult<ProcessInstanceDTO> findByPage(ProcessInstanceQueryDTO queryDTO);

    void terminateProcess(Long id, String reason);

    void withdrawProcess(Long id, String reason, Long currentUserId);
}
