package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.piaomiao.dal.entity.OperationLogDO;
import com.piaomiao.dal.mapper.OperationLogMapper;
import com.piaomiao.model.OperationLogModel;
import com.piaomiao.repository.OperationLogRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志仓储实现
 */
@Repository
public class OperationLogRepositoryImpl implements OperationLogRepository {

    @Autowired
    private OperationLogMapper operationLogMapper;

    public Long save(OperationLogModel operationLogModel) {
        OperationLogDO operationLogDO = new OperationLogDO();
        BeanUtils.copyProperties(operationLogModel, operationLogDO);
        operationLogMapper.insert(operationLogDO);
        return operationLogDO.getId();
    }

    public OperationLogModel findById(Long id) {
        OperationLogDO operationLogDO = operationLogMapper.selectById(id);
        if (operationLogDO == null) {
            return null;
        }
        OperationLogModel operationLogModel = new OperationLogModel();
        BeanUtils.copyProperties(operationLogDO, operationLogModel);
        return operationLogModel;
    }

    public List<OperationLogModel> findAll() {
        List<OperationLogDO> list = operationLogMapper.selectList(null);
        return list.stream().map(doEntity -> {
            OperationLogModel model = new OperationLogModel();
            BeanUtils.copyProperties(doEntity, model);
            return model;
        }).collect(Collectors.toList());
    }

    public List<OperationLogModel> findByCondition(String module, String type, String username) {
        LambdaQueryWrapper<OperationLogDO> wrapper = new LambdaQueryWrapper<>();
        if (module != null && !module.isEmpty()) {
            wrapper.like(OperationLogDO::getModule, module);
        }
        if (type != null && !type.isEmpty()) {
            wrapper.eq(OperationLogDO::getType, type);
        }
        if (username != null && !username.isEmpty()) {
            wrapper.like(OperationLogDO::getUsername, username);
        }
        wrapper.orderByDesc(OperationLogDO::getOperationTime);
        List<OperationLogDO> list = operationLogMapper.selectList(wrapper);
        return list.stream().map(doEntity -> {
            OperationLogModel model = new OperationLogModel();
            BeanUtils.copyProperties(doEntity, model);
            return model;
        }).collect(Collectors.toList());
    }

    public void delete(Long id) {
        operationLogMapper.deleteById(id);
    }

    public void deleteBatch(List<Long> ids) {
        operationLogMapper.deleteBatchIds(ids);
    }

    public void clear() {
        operationLogMapper.delete(null);
    }
}
