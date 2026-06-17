package com.piaomiao.service;

import com.piaomiao.model.ProcessDefinitionModel;
import com.piaomiao.response.PageResult;

import java.util.List;

/**
 * 流程定义服务接口
 */
public interface ProcessDefinitionService {
    /**
     * 保存流程定义
     * @param processDefinitionModel 流程定义模型
     * @return 流程定义模型
     */
    ProcessDefinitionModel save(ProcessDefinitionModel processDefinitionModel);
    
    /**
     * 根据ID查询流程定义
     * @param id 流程定义ID
     * @return 流程定义模型
     */
    ProcessDefinitionModel findById(Long id);
    
    /**
     * 根据Key查询流程定义
     * @param key 流程定义Key
     * @return 流程定义模型
     */
    ProcessDefinitionModel findByKey(String key);
    
    /**
     * 查询所有流程定义
     * @return 流程定义列表
     */
    List<ProcessDefinitionModel> findAll();
    
    /**
     * 分页查询流程定义
     * @param page 页码
     * @param size 每页大小
     * @param name 流程名称
     * @param key 流程标识
     * @return 分页结果
     */
    PageResult<ProcessDefinitionModel> findByPage(int page, int size, String name, String key);
    
    /**
     * 更新流程定义
     * @param processDefinitionModel 流程定义模型
     * @return 流程定义模型
     */
    ProcessDefinitionModel update(ProcessDefinitionModel processDefinitionModel);
    
    /**
     * 删除流程定义
     * @param id 流程定义ID
     */
    void delete(Long id);
    
    /**
     * 发布流程定义
     * @param id 流程定义ID
     * @return 流程定义模型
     */
    ProcessDefinitionModel publish(Long id);

    /**
     * 取消发布流程定义
     * @param id 流程定义ID
     */
    void unpublish(Long id);
    
    /**
     * 部署流程定义到Activiti
     * @param processDefinitionModel 流程定义模型
     * @return 部署ID
     */
    String deployToActiviti(ProcessDefinitionModel processDefinitionModel);
}
