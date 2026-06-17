package com.piaomiao.web.vo;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.dto.ApprovalTaskQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批任务分页查询请求VO
 *
 * @author system
 * @date 2026-06-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApprovalTaskQueryVO extends BasePageVO {

    private Long instanceId;

    private String nodeName;

    private Integer status;

    public static ApprovalTaskQueryDTO toDTO(ApprovalTaskQueryVO vo) {
        ApprovalTaskQueryDTO dto = new ApprovalTaskQueryDTO();
        dto.setPageNum(vo.getPageNum());
        dto.setPageSize(vo.getPageSize());
        dto.setSortField(vo.getSortField());
        dto.setSortOrder(vo.getSortOrder());
        dto.setInstanceId(vo.getInstanceId());
        dto.setNodeName(vo.getNodeName());
        dto.setStatus(vo.getStatus());
        return dto;
    }
}
