package com.piaomiao.service.impl;

import com.piaomiao.model.OperationLogModel;
import com.piaomiao.service.OperationLogService;
import com.piaomiao.repository.OperationLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志服务实现
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperationLogRepository operationLogRepository;

    @Override
    public Long save(OperationLogModel operationLogModel) {
        return operationLogRepository.save(operationLogModel);
    }

    @Override
    public OperationLogModel findById(Long id) {
        return operationLogRepository.findById(id);
    }

    @Override
    public List<OperationLogModel> findAll() {
        return operationLogRepository.findAll();
    }

    @Override
    public List<OperationLogModel> findByCondition(String module, String type, String username) {
        return operationLogRepository.findByCondition(module, type, username);
    }

    @Override
    public void delete(Long id) {
        operationLogRepository.delete(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        operationLogRepository.deleteBatch(ids);
    }

    @Override
    public void clear() {
        operationLogRepository.clear();
    }
}
