package com.piaomiao.command;

import com.piaomiao.dto.ProcessDefinitionDTO;
import com.piaomiao.dto.ProcessDefinitionQueryDTO;
import com.piaomiao.response.PageResult;

import java.util.List;

/**
 * 流程定义命令接口
 */
public interface ProcessDefinitionCommand {
    /**
     * 保存流程定义
     * @param processDefinitionDTO 流程定义DTO
     * @return 流程定义DTO
     */
    ProcessDefinitionDTO save(ProcessDefinitionDTO processDefinitionDTO);
    
    /**
     * 根据ID查询流程定义
     * @param id 流程定义ID
     * @return 流程定义DTO
     */
    ProcessDefinitionDTO findById(Long id);
    
    /**
     * 根据Key查询流程定义
     * @param key 流程定义Key
     * @return 流程定义DTO
     */
    ProcessDefinitionDTO findByKey(String key);
    
    /**
     * 查询所有流程定义
     * @return 流程定义DTO列表
     */
    List<ProcessDefinitionDTO> findAll();
    
    /**
     * 分页查询流程定义
     * @param page 页码
     * @param size 每页大小
     * @param name 流程名称
     * @param key 流程标识
     * @return 分页结果
     */
    PageResult<ProcessDefinitionDTO> findByPage(ProcessDefinitionQueryDTO queryDTO);
    
    /**
     * 更新流程定义
     * @param processDefinitionDTO 流程定义DTO
     * @return 流程定义DTO
     */
    ProcessDefinitionDTO update(ProcessDefinitionDTO processDefinitionDTO);
    
    /**
     * 删除流程定义
     * @param id 流程定义ID
     */
    void delete(Long id);
    
    /**
     * 发布流程定义
     * @param id 流程定义ID
     * @return 流程定义DTO
     */
    ProcessDefinitionDTO publish(Long id);

    /**
     * 取消发布流程定义
     * @param id 流程定义ID
     */
    void unpublish(Long id);
}
