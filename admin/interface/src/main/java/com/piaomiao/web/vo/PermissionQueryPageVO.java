package com.piaomiao.web.vo;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.dto.sys.PermissionQueryPageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionQueryPageVO extends BasePageVO {

    private String name;


    public static PermissionQueryPageDTO toDTO(PermissionQueryPageVO vo) {
        PermissionQueryPageDTO dto = new PermissionQueryPageDTO();
        dto.setPageNum(vo.getPageNum());
        dto.setPageSize(vo.getPageSize());
        dto.setSortField(vo.getSortField());
        dto.setSortOrder(vo.getSortOrder());
        dto.setName(vo.getName());
        return dto;
    }
}
