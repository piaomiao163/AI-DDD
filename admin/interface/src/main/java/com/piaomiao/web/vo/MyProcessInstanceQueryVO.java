package com.piaomiao.web.vo;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.dto.MyProcessInstanceQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 我的流程实例分页查询请求VO（运行中/历史）
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyProcessInstanceQueryVO extends BasePageVO {
    // startUserDbId从UserContext获取，不在VO中暴露

    public static MyProcessInstanceQueryDTO toDTO(MyProcessInstanceQueryVO vo) {
        MyProcessInstanceQueryDTO dto = new MyProcessInstanceQueryDTO();
        dto.setPageNum(vo.getPageNum());
        dto.setPageSize(vo.getPageSize());
        dto.setSortField(vo.getSortField());
        dto.setSortOrder(vo.getSortOrder());
        return dto;
    }
}
