package com.piaomiao.service.impl;

import com.piaomiao.model.ProcessInstanceModel;
import com.piaomiao.repository.ProcessInstanceRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.ProcessInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Map;

@Service
public class ProcessInstanceServiceImpl implements ProcessInstanceService {

    @Autowired
    private ProcessInstanceRepository processInstanceRepository;

    @Override
    public ProcessInstanceModel startProcess(String processDefinitionKey, String businessKey,
                                               String userId, Map<String, Object> variables,
                                               String title, String businessType, Long businessId,
                                               Integer priority) {
        return processInstanceRepository.startProcess(processDefinitionKey, businessKey, userId, variables,
                title, businessType, businessId, priority);
    }

    @Override
    public ProcessInstanceModel findById(Long id) {
        return processInstanceRepository.findById(id);
    }

    @Override
    public ProcessInstanceModel findByActId(String actProcessInstanceId) {
        return processInstanceRepository.findByActProcessInstanceId(actProcessInstanceId);
    }

    @Override
    public PageResult<ProcessInstanceModel> findRunningByUser(Long startUserDbId, int page, int size) {
        return processInstanceRepository.findRunningByUser(startUserDbId, page, size);
    }

    @Override
    public PageResult<ProcessInstanceModel> findHistoryByUser(Long startUserDbId, int page, int size) {
        return processInstanceRepository.findHistoryByUser(startUserDbId, page, size);
    }

    @Override
    public PageResult<ProcessInstanceModel> findByPage(int page, int size, String title, Integer status, String businessType) {
        return processInstanceRepository.findByPage(page, size, title, status, businessType);
    }

    @Override
    public void terminateProcess(Long id, String reason) {
        processInstanceRepository.terminateProcess(id, reason);
    }

    @Override
    public void withdrawProcess(Long id, String reason, Long currentUserId) {
        processInstanceRepository.withdrawProcess(id, reason, currentUserId);
    }

    @Override
    public InputStream generateDiagram(Long id) {
        return processInstanceRepository.generateDiagram(id);
    }
}
