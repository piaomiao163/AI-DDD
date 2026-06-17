package com.piaomiao.web.vo;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.dto.MyLeaveQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 我的请假分页查询请求VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyLeaveQueryVO extends BasePageVO {
    // applicantId从UserContext获取，不在VO中暴露

    public static MyLeaveQueryDTO toDTO(MyLeaveQueryVO vo) {
        MyLeaveQueryDTO dto = new MyLeaveQueryDTO();
        if (vo == null) {
            return dto;
        }
        dto.setPageNum(vo.getPageNum() != null ? vo.getPageNum() : 1);
        dto.setPageSize(vo.getPageSize() != null ? vo.getPageSize() : 10);
        dto.setSortField(vo.getSortField());
        dto.setSortOrder(vo.getSortOrder());
        return dto;
    }
}
