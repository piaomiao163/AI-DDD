package com.piaomiao.repository;

import com.piaomiao.model.ProcessDefinitionModel;
import com.piaomiao.response.PageResult;

import java.util.List;

/**
 * 流程定义仓库接口
 */
public interface ProcessDefinitionRepository {
    ProcessDefinitionModel save(ProcessDefinitionModel processDefinitionModel);

    ProcessDefinitionModel findById(Long id);

    ProcessDefinitionModel findByKey(String key);

    List<ProcessDefinitionModel> findAll();

    PageResult<ProcessDefinitionModel> findByPage(int page, int size, String name, String key);

    ProcessDefinitionModel update(ProcessDefinitionModel processDefinitionModel);

    void delete(Long id);

    /**
     * 部署流程定义到Activiti引擎，并回写部署信息
     * @param processDefinitionModel 流程定义模型（需含xml字段）
     * @return 更新后的流程定义模型（含deploymentId和actProcessDefinitionId）
     */
    ProcessDefinitionModel deployToEngine(ProcessDefinitionModel processDefinitionModel);

    /**
     * 取消发布：从Activiti引擎删除部署，清除部署信息，状态改回草稿
     * @param processDefinitionModel 流程定义模型
     */
    void undeployFromEngine(ProcessDefinitionModel processDefinitionModel);
}
