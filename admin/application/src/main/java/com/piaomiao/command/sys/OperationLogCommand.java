package com.piaomiao.command.sys;

import com.piaomiao.dto.sys.OperationLogDTO;
import com.piaomiao.model.sys.OperationLogModel;

import java.util.List;

public interface OperationLogCommand {
    OperationLogModel findById(Long id);
    List<OperationLogModel> findAll();
    Long save(OperationLogDTO operationLogDTO);
    void delete(Long id);
    void deleteBatch(List<Long> ids);
    void clear();
}
