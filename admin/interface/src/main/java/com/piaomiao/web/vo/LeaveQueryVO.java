package com.piaomiao.web.vo;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.dto.LeaveQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请假管理端分页查询请求VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LeaveQueryVO extends BasePageVO {
    private String title;
    private Integer leaveType;
    private Integer status;

    public static LeaveQueryDTO toDTO(LeaveQueryVO vo) {
        LeaveQueryDTO dto = new LeaveQueryDTO();
        if (vo == null) {
            return dto;
        }
        dto.setPageNum(vo.getPageNum() != null ? vo.getPageNum() : 1);
        dto.setPageSize(vo.getPageSize() != null ? vo.getPageSize() : 10);
        dto.setSortField(vo.getSortField());
        dto.setSortOrder(vo.getSortOrder());
        dto.setTitle(vo.getTitle());
        dto.setLeaveType(vo.getLeaveType());
        dto.setStatus(vo.getStatus());
        return dto;
    }
}
