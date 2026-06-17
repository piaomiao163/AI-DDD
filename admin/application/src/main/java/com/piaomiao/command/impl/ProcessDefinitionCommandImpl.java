package com.piaomiao.command.impl;

import com.piaomiao.command.ProcessDefinitionCommand;
import com.piaomiao.dto.ProcessDefinitionDTO;
import com.piaomiao.dto.ProcessDefinitionQueryDTO;
import com.piaomiao.dto.ProcessEdgeDTO;
import com.piaomiao.dto.ProcessNodeDTO;
import com.piaomiao.model.ProcessDefinitionModel;
import com.piaomiao.model.ProcessEdgeModel;
import com.piaomiao.model.ProcessNodeModel;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.ProcessDefinitionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 流程定义命令实现类
 */
@Component
public class ProcessDefinitionCommandImpl implements ProcessDefinitionCommand {
    
    @Autowired
    private ProcessDefinitionService processDefinitionService;
    
    @Override
    public ProcessDefinitionDTO save(ProcessDefinitionDTO processDefinitionDTO) {
        ProcessDefinitionModel processDefinitionModel = convertToModel(processDefinitionDTO);
        ProcessDefinitionModel savedModel = processDefinitionService.save(processDefinitionModel);
        return convertToDTO(savedModel);
    }
    
    @Override
    public ProcessDefinitionDTO findById(Long id) {
        ProcessDefinitionModel processDefinitionModel = processDefinitionService.findById(id);
        return convertToDTO(processDefinitionModel);
    }
    
    @Override
    public ProcessDefinitionDTO findByKey(String key) {
        ProcessDefinitionModel processDefinitionModel = processDefinitionService.findByKey(key);
        return convertToDTO(processDefinitionModel);
    }
    
    @Override
    public List<ProcessDefinitionDTO> findAll() {
        List<ProcessDefinitionModel> processDefinitionModelList = processDefinitionService.findAll();
        return processDefinitionModelList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public PageResult<ProcessDefinitionDTO> findByPage(ProcessDefinitionQueryDTO queryDTO) {
        PageResult<ProcessDefinitionModel> pageResult = processDefinitionService.findByPage(
                queryDTO.getPageNum(), queryDTO.getPageSize(), queryDTO.getName(), queryDTO.getKey());
        List<ProcessDefinitionDTO> processDefinitionDTOList = pageResult.getList().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new PageResult<>(pageResult.getTotal(), processDefinitionDTOList,
                pageResult.getPageNum(), pageResult.getPageSize());
    }
    
    @Override
    public ProcessDefinitionDTO update(ProcessDefinitionDTO processDefinitionDTO) {
        ProcessDefinitionModel processDefinitionModel = convertToModel(processDefinitionDTO);
        ProcessDefinitionModel updatedModel = processDefinitionService.update(processDefinitionModel);
        return convertToDTO(updatedModel);
    }
    
    @Override
    public void delete(Long id) {
        processDefinitionService.delete(id);
    }
    
    @Override
    public ProcessDefinitionDTO publish(Long id) {
        ProcessDefinitionModel processDefinitionModel = processDefinitionService.publish(id);
        return convertToDTO(processDefinitionModel);
    }

    @Override
    public void unpublish(Long id) {
        processDefinitionService.unpublish(id);
    }
    
    /**
     * 将DTO转换为Model
     */
    private ProcessDefinitionModel convertToModel(ProcessDefinitionDTO processDefinitionDTO) {
        ProcessDefinitionModel processDefinitionModel = new ProcessDefinitionModel();
        BeanUtils.copyProperties(processDefinitionDTO, processDefinitionModel);
        
        // 转换节点和边
        if (processDefinitionDTO.getNodes() != null) {
            List<ProcessNodeModel> nodes = processDefinitionDTO.getNodes().stream()
                    .map(nodeDTO -> {
                        ProcessNodeModel nodeModel = new ProcessNodeModel();
                        BeanUtils.copyProperties(nodeDTO, nodeModel);
                        return nodeModel;
                    })
                    .collect(Collectors.toList());
            processDefinitionModel.setNodes(nodes);
        }
        
        if (processDefinitionDTO.getEdges() != null) {
            List<ProcessEdgeModel> edges = processDefinitionDTO.getEdges().stream()
                    .map(edgeDTO -> {
                        ProcessEdgeModel edgeModel = new ProcessEdgeModel();
                        BeanUtils.copyProperties(edgeDTO, edgeModel);
                        return edgeModel;
                    })
                    .collect(Collectors.toList());
            processDefinitionModel.setEdges(edges);
        }
        
        return processDefinitionModel;
    }
    
    /**
     * 将Model转换为DTO
     */
    private ProcessDefinitionDTO convertToDTO(ProcessDefinitionModel processDefinitionModel) {
        if (processDefinitionModel == null) {
            return null;
        }
        
        ProcessDefinitionDTO processDefinitionDTO = new ProcessDefinitionDTO();
        BeanUtils.copyProperties(processDefinitionModel, processDefinitionDTO);
        
        // 转换节点和边
        if (processDefinitionModel.getNodes() != null) {
            List<ProcessNodeDTO> nodes = processDefinitionModel.getNodes().stream()
                    .map(nodeModel -> {
                        ProcessNodeDTO nodeDTO = new ProcessNodeDTO();
                        BeanUtils.copyProperties(nodeModel, nodeDTO);
                        return nodeDTO;
                    })
                    .collect(Collectors.toList());
            processDefinitionDTO.setNodes(nodes);
        }
        
        if (processDefinitionModel.getEdges() != null) {
            List<ProcessEdgeDTO> edges = processDefinitionModel.getEdges().stream()
                    .map(edgeModel -> {
                        ProcessEdgeDTO edgeDTO = new ProcessEdgeDTO();
                        BeanUtils.copyProperties(edgeModel, edgeDTO);
                        return edgeDTO;
                    })
                    .collect(Collectors.toList());
            processDefinitionDTO.setEdges(edges);
        }
        
        return processDefinitionDTO;
    }
}
