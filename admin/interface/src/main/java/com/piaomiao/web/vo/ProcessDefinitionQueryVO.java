package com.piaomiao.web.vo;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.dto.ProcessDefinitionQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程定义分页查询请求VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessDefinitionQueryVO extends BasePageVO {
    private String name;
    private String processKey;

    public static ProcessDefinitionQueryDTO toDTO(ProcessDefinitionQueryVO vo) {
        ProcessDefinitionQueryDTO dto = new ProcessDefinitionQueryDTO();
        dto.setPageNum(vo.getPageNum());
        dto.setPageSize(vo.getPageSize());
        dto.setSortField(vo.getSortField());
        dto.setSortOrder(vo.getSortOrder());
        dto.setName(vo.getName());
        dto.setKey(vo.getProcessKey());
        return dto;
    }
}
