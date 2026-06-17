package com.piaomiao.command.impl;

import com.piaomiao.command.ProcessInstanceCommand;
import com.piaomiao.dto.MyProcessInstanceQueryDTO;
import com.piaomiao.dto.ProcessInstanceDTO;
import com.piaomiao.dto.ProcessInstanceQueryDTO;
import com.piaomiao.model.ProcessInstanceModel;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.ProcessInstanceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProcessInstanceCommandImpl implements ProcessInstanceCommand {

    @Autowired
    private ProcessInstanceService processInstanceService;

    /**
     * 启动流程实例
     * @param processDefinitionKey 流程定义key
     * @param businessKey 业务key
     * @param userId 发起人ID
     * @param variables 流程变量
     * @param title 流程标题
     * @param businessType 业务类型
     * @param priority 优先级
     * @return 流程实例DTO
     */
    @Override
    public ProcessInstanceDTO startProcess(String processDefinitionKey, String businessKey,
                                            String userId, Map<String, Object> variables,
                                            String title, String businessType, Long businessId,
                                            Integer priority) {
        ProcessInstanceModel model = processInstanceService.startProcess(
                processDefinitionKey, businessKey, userId, variables, title, businessType, businessId, priority);
        return convertToDTO(model);
    }

    @Override
    public ProcessInstanceDTO findById(Long id) {
        ProcessInstanceModel model = processInstanceService.findById(id);
        return convertToDTO(model);
    }

    @Override
    public PageResult<ProcessInstanceDTO> findRunningByUser(Long startUserDbId, MyProcessInstanceQueryDTO queryDTO) {
        PageResult<ProcessInstanceModel> result = processInstanceService.findRunningByUser(startUserDbId, queryDTO.getPageNum(), queryDTO.getPageSize());
        return convertPageResult(result);
    }

    @Override
    public PageResult<ProcessInstanceDTO> findHistoryByUser(Long startUserDbId, MyProcessInstanceQueryDTO queryDTO) {
        PageResult<ProcessInstanceModel> result = processInstanceService.findHistoryByUser(startUserDbId, queryDTO.getPageNum(), queryDTO.getPageSize());
        return convertPageResult(result);
    }

    @Override
    public PageResult<ProcessInstanceDTO> findByPage(ProcessInstanceQueryDTO queryDTO) {
        PageResult<ProcessInstanceModel> result = processInstanceService.findByPage(
                queryDTO.getPageNum(), queryDTO.getPageSize(),
                queryDTO.getTitle(), queryDTO.getStatus(), queryDTO.getBusinessType());
        return convertPageResult(result);
    }

    @Override
    public void terminateProcess(Long id, String reason) {
        processInstanceService.terminateProcess(id, reason);
    }

    @Override
    public void withdrawProcess(Long id, String reason, Long currentUserId) {
        processInstanceService.withdrawProcess(id, reason, currentUserId);
    }

    private ProcessInstanceDTO convertToDTO(ProcessInstanceModel model) {
        if (model == null) return null;
        ProcessInstanceDTO dto = new ProcessInstanceDTO();
        BeanUtils.copyProperties(model, dto);
        // 手动映射字段名不同的属性
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setBusinessType(model.getBusinessType());
        dto.setStartUserDbId(model.getStartUserDbId());
        dto.setStartUserName(model.getStartUserName());
        dto.setCurrentNodeName(model.getCurrentNodeName());
        dto.setPriority(model.getPriority());
        dto.setProcessInstanceId(model.getProcessInstanceId());
        dto.setProcessDefinitionDbId(model.getProcessDefinitionDbId());
        // LocalDateTime → String
        if (model.getStartTime() != null) dto.setStartTime(model.getStartTime().toString());
        if (model.getEndTime() != null) dto.setEndTime(model.getEndTime().toString());
        if (model.getCreateTime() != null) dto.setCreateTime(model.getCreateTime().toString());
        if (model.getUpdateTime() != null) dto.setUpdateTime(model.getUpdateTime().toString());
        return dto;
    }

    private PageResult<ProcessInstanceDTO> convertPageResult(PageResult<ProcessInstanceModel> result) {
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::convertToDTO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }
}
