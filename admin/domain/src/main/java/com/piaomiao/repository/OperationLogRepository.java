package com.piaomiao.repository;

import com.piaomiao.model.OperationLogModel;

import java.util.List;

/**
 * 操作日志仓储接口
 */
public interface OperationLogRepository {
    /**
     * 保存操作日志
     * @param operationLogModel 操作日志模型
     * @return 日志ID
     */
    Long save(OperationLogModel operationLogModel);
    
    /**
     * 根据ID查询操作日志
     * @param id 日志ID
     * @return 操作日志模型
     */
    OperationLogModel findById(Long id);
    
    /**
     * 查询所有操作日志
     * @return 操作日志列表
     */
    List<OperationLogModel> findAll();
    
    /**
     * 根据条件查询操作日志
     * @param module 操作模块
     * @param type 操作类型
     * @param username 操作人
     * @return 操作日志列表
     */
    List<OperationLogModel> findByCondition(String module, String type, String username);
    
    /**
     * 删除操作日志
     * @param id 日志ID
     */
    void delete(Long id);
    
    /**
     * 批量删除操作日志
     * @param ids 日志ID列表
     */
    void deleteBatch(List<Long> ids);
    
    /**
     * 清空操作日志
     */
    void clear();
}
