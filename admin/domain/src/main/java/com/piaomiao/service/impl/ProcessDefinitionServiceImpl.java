package com.piaomiao.service.impl;

import com.piaomiao.model.ProcessDefinitionModel;
import com.piaomiao.repository.ProcessDefinitionRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.ProcessDefinitionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 流程定义服务实现类
 */
@Service
@Slf4j
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService {

    @Autowired
    private ProcessDefinitionRepository processDefinitionRepository;

    @Override
    public ProcessDefinitionModel save(ProcessDefinitionModel processDefinitionModel) {
        if (processDefinitionModel.getProcessKey() == null || processDefinitionModel.getProcessKey().isEmpty()) {
            throw new RuntimeException("流程标识不能为空");
        }
        ProcessDefinitionModel existingModel = processDefinitionRepository.findByKey(processDefinitionModel.getProcessKey());
        if (existingModel != null) {
            throw new RuntimeException("流程标识已存在");
        }
        processDefinitionModel.setCreateTime(LocalDateTime.now());
        processDefinitionModel.setUpdateTime(LocalDateTime.now());
        processDefinitionModel.setStatus(0);
        processDefinitionModel.setVersion(1);
        return processDefinitionRepository.save(processDefinitionModel);
    }

    @Override
    public ProcessDefinitionModel findById(Long id) {
        return processDefinitionRepository.findById(id);
    }

    @Override
    public ProcessDefinitionModel findByKey(String key) {
        return processDefinitionRepository.findByKey(key);
    }

    @Override
    public List<ProcessDefinitionModel> findAll() {
        return processDefinitionRepository.findAll();
    }

    @Override
    public PageResult<ProcessDefinitionModel> findByPage(int page, int size, String name, String key) {
        return processDefinitionRepository.findByPage(page, size, name, key);
    }

    @Override
    public ProcessDefinitionModel update(ProcessDefinitionModel processDefinitionModel) {
        ProcessDefinitionModel model = processDefinitionRepository.findById(processDefinitionModel.getId());
        if (model == null) {
            throw new RuntimeException("流程定义不存在");
        }
        processDefinitionModel.setVersion(model.getVersion() + 1);
        processDefinitionModel.setUpdateTime(LocalDateTime.now());
        return processDefinitionRepository.update(processDefinitionModel);
    }

    @Override
    public void delete(Long id) {
        processDefinitionRepository.delete(id);
    }

    @Override
    public ProcessDefinitionModel publish(Long id) {
        ProcessDefinitionModel processDefinitionModel = processDefinitionRepository.findById(id);
        if (processDefinitionModel == null) {
            throw new RuntimeException("流程定义不存在");
        }
        if (processDefinitionModel.getXml() == null || processDefinitionModel.getXml().isEmpty()) {
            throw new RuntimeException("流程XML不能为空，请先设计流程");
        }

        // 更新状态
        processDefinitionModel.setStatus(1);
        processDefinitionModel.setUpdateTime(LocalDateTime.now());

        // 通过Repository部署到Activiti引擎（内部会回写deploymentId等）
        return processDefinitionRepository.deployToEngine(processDefinitionModel);
    }

    @Override
    public void unpublish(Long id) {
        ProcessDefinitionModel processDefinitionModel = processDefinitionRepository.findById(id);
        if (processDefinitionModel == null) {
            throw new RuntimeException("流程定义不存在");
        }
        if (processDefinitionModel.getStatus() != 1) {
            throw new RuntimeException("流程定义未发布，无法取消发布");
        }

        // 从Activiti引擎删除部署，状态改回草稿
        processDefinitionRepository.undeployFromEngine(processDefinitionModel);
    }

    @Override
    public String deployToActiviti(ProcessDefinitionModel processDefinitionModel) {
        ProcessDefinitionModel deployed = processDefinitionRepository.deployToEngine(processDefinitionModel);
        return deployed.getDeploymentId();
    }
}
