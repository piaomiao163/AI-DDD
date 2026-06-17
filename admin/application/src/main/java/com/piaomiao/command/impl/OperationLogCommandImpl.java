package com.piaomiao.command.impl;

import com.piaomiao.command.OperationLogCommand;
import com.piaomiao.dto.OperationLogDTO;
import com.piaomiao.model.OperationLogModel;
import com.piaomiao.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationLogCommandImpl implements OperationLogCommand {

    @Autowired
    private OperationLogService operationLogService;

    @Override
    public OperationLogModel findById(Long id) {
        return operationLogService.findById(id);
    }

    @Override
    public List<OperationLogModel> findAll() {
        return operationLogService.findAll();
    }

    @Override
    public Long save(OperationLogDTO operationLogDTO) {
        OperationLogModel operationLogModel = new OperationLogModel();
        operationLogModel.setModule(operationLogDTO.getModule());
        operationLogModel.setType(operationLogDTO.getType());
        operationLogModel.setDescription(operationLogDTO.getDescription());
        operationLogModel.setRequestMethod(operationLogDTO.getRequestMethod());
        operationLogModel.setRequestUrl(operationLogDTO.getRequestUrl());
        operationLogModel.setRequestParams(operationLogDTO.getRequestParams());
        operationLogModel.setResponseResult(operationLogDTO.getResponseResult());
        operationLogModel.setUserId(operationLogDTO.getUserId());
        operationLogModel.setUsername(operationLogDTO.getUsername());
        operationLogModel.setIpAddress(operationLogDTO.getIpAddress());
        operationLogModel.setLocation(operationLogDTO.getLocation());
        operationLogModel.setStatus(operationLogDTO.getStatus());
        operationLogModel.setErrorMsg(operationLogDTO.getErrorMsg());
        operationLogModel.setExecutionTime(operationLogDTO.getExecutionTime());
        operationLogModel.setOperationTime(operationLogDTO.getOperationTime());
        return operationLogService.save(operationLogModel);
    }

    @Override
    public void delete(Long id) {
        operationLogService.delete(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            operationLogService.delete(id);
        }
    }

    @Override
    public void clear() {
        operationLogService.clear();
    }
}
