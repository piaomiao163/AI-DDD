package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piaomiao.dal.entity.ProcessDefinitionDO;
import com.piaomiao.dal.mapper.ProcessDefinitionMapper;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.event.DomainEventPublisher;
import com.piaomiao.event.process.ProcessDefinitionPublishedEvent;
import com.piaomiao.model.ProcessDefinitionModel;
import com.piaomiao.model.ProcessEdgeModel;
import com.piaomiao.model.ProcessNodeModel;
import com.piaomiao.repository.ProcessDefinitionRepository;
import com.piaomiao.response.PageResult;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程定义仓库实现类
 */
@Repository
public class ProcessDefinitionRepositoryImpl implements ProcessDefinitionRepository {

    @Autowired
    private ProcessDefinitionMapper processDefinitionMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Override
    public ProcessDefinitionModel save(ProcessDefinitionModel processDefinitionModel) {
        ProcessDefinitionDO processDefinitionDO = new ProcessDefinitionDO();
        BeanUtils.copyProperties(processDefinitionModel, processDefinitionDO);
        convertNodesEdgesToJson(processDefinitionModel, processDefinitionDO);
        AuditFieldUtil.fillForInsert(processDefinitionDO);
        processDefinitionMapper.insert(processDefinitionDO);
        return findById(processDefinitionDO.getId());
    }

    @Override
    public ProcessDefinitionModel findById(Long id) {
        ProcessDefinitionDO processDefinitionDO = processDefinitionMapper.selectById(id);
        return convertToModel(processDefinitionDO);
    }

    @Override
    public ProcessDefinitionModel findByKey(String key) {
        QueryWrapper<ProcessDefinitionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("process_key", key);
        ProcessDefinitionDO processDefinitionDO = processDefinitionMapper.selectOne(queryWrapper);
        return convertToModel(processDefinitionDO);
    }

    @Override
    public List<ProcessDefinitionModel> findAll() {
        return processDefinitionMapper.selectList(null).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<ProcessDefinitionModel> findByPage(int page, int size, String name, String key) {
        QueryWrapper<ProcessDefinitionDO> queryWrapper = new QueryWrapper<>();
        if (name != null && !name.isEmpty()) {
            queryWrapper.like("name", name);
        }
        if (key != null && !key.isEmpty()) {
            queryWrapper.like("process_key", key);
        }
        com.baomidou.mybatisplus.core.metadata.IPage<ProcessDefinitionDO> pageResult = processDefinitionMapper.selectPage(
                new Page<>(page, size), queryWrapper);
        List<ProcessDefinitionModel> models = pageResult.getRecords().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
        return new PageResult<>(pageResult.getTotal(), models, page, size);
    }

    @Override
    public ProcessDefinitionModel update(ProcessDefinitionModel processDefinitionModel) {
        ProcessDefinitionDO processDefinitionDO = new ProcessDefinitionDO();
        BeanUtils.copyProperties(processDefinitionModel, processDefinitionDO);
        convertNodesEdgesToJson(processDefinitionModel, processDefinitionDO);
        AuditFieldUtil.fillForUpdate(processDefinitionDO);
        processDefinitionMapper.updateById(processDefinitionDO);
        return findById(processDefinitionDO.getId());
    }

    @Override
    public void delete(Long id) {
        processDefinitionMapper.deleteById(id);
    }

    @Override
    public ProcessDefinitionModel deployToEngine(ProcessDefinitionModel processDefinitionModel) {
        // 部署到Activiti
        Deployment deployment = repositoryService.createDeployment()
                .name(processDefinitionModel.getName())
                .key(processDefinitionModel.getProcessKey())
                .addString(processDefinitionModel.getProcessKey() + ".bpmn20.xml", processDefinitionModel.getXml())
                .deploy();

        // 查询Activiti流程定义ID
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId()).singleResult();

        // 回写部署信息
        processDefinitionModel.setDeploymentId(deployment.getId());
        if (pd != null) {
            processDefinitionModel.setActProcessDefinitionId(pd.getId());
        }
        ProcessDefinitionModel updated = update(processDefinitionModel);

        // 发布流程定义发布事件
        domainEventPublisher.publish(new ProcessDefinitionPublishedEvent(this, updated));

        return updated;
    }

    @Override
    public void undeployFromEngine(ProcessDefinitionModel processDefinitionModel) {
        // 从Activiti引擎删除部署
        String deploymentId = processDefinitionModel.getDeploymentId();
        if (deploymentId != null && !deploymentId.isEmpty()) {
            repositoryService.deleteDeployment(deploymentId, true);
        }

        // 清除部署信息，状态改回草稿
        processDefinitionModel.setStatus(0);
        processDefinitionModel.setDeploymentId(null);
        processDefinitionModel.setActProcessDefinitionId(null);
        update(processDefinitionModel);
    }

    /**
     * 将节点和边转换为JSON字符串
     */
    private void convertNodesEdgesToJson(ProcessDefinitionModel model, ProcessDefinitionDO DO) {
        try {
            if (model.getNodes() != null) {
                DO.setNodesJson(objectMapper.writeValueAsString(model.getNodes()));
            }
            if (model.getEdges() != null) {
                DO.setEdgesJson(objectMapper.writeValueAsString(model.getEdges()));
            }
        } catch (Exception e) {
            throw new RuntimeException("转换节点和边为JSON失败", e);
        }
    }

    /**
     * 将DO转换为Model
     */
    private ProcessDefinitionModel convertToModel(ProcessDefinitionDO DO) {
        if (DO == null) {
            return null;
        }
        ProcessDefinitionModel model = new ProcessDefinitionModel();
        BeanUtils.copyProperties(DO, model);
        try {
            if (DO.getNodesJson() != null) {
                model.setNodes(objectMapper.readValue(DO.getNodesJson(),
                        new TypeReference<List<ProcessNodeModel>>() {}));
            }
            if (DO.getEdgesJson() != null) {
                model.setEdges(objectMapper.readValue(DO.getEdgesJson(),
                        new TypeReference<List<ProcessEdgeModel>>() {}));
            }
        } catch (Exception e) {
            throw new RuntimeException("转换JSON为节点和边失败", e);
        }
        return model;
    }
}
