package com.piaomiao.web.vo;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.dto.ProcessInstanceQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程实例管理端分页查询请求VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessInstanceQueryVO extends BasePageVO {
    private String title;
    private Integer status;
    private String businessType;

    public static ProcessInstanceQueryDTO toDTO(ProcessInstanceQueryVO vo) {
        ProcessInstanceQueryDTO dto = new ProcessInstanceQueryDTO();
        dto.setPageNum(vo.getPageNum());
        dto.setPageSize(vo.getPageSize());
        dto.setSortField(vo.getSortField());
        dto.setSortOrder(vo.getSortOrder());
        dto.setTitle(vo.getTitle());
        dto.setStatus(vo.getStatus());
        dto.setBusinessType(vo.getBusinessType());
        return dto;
    }
}
