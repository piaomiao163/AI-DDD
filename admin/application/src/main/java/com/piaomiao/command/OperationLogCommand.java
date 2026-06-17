package com.piaomiao.command;

import com.piaomiao.dto.OperationLogDTO;
import com.piaomiao.model.OperationLogModel;

import java.util.List;

public interface OperationLogCommand {
    OperationLogModel findById(Long id);
    List<OperationLogModel> findAll();
    Long save(OperationLogDTO operationLogDTO);
    void delete(Long id);
    void deleteBatch(List<Long> ids);
    void clear();
}
