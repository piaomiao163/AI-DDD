package com.piaomiao.web.vo;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.dto.TaskQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务分页查询请求VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskQueryVO extends BasePageVO {
    // userId从UserContext获取，不在VO中暴露

    public static TaskQueryDTO toDTO(TaskQueryVO vo) {
        TaskQueryDTO dto = new TaskQueryDTO();
        dto.setPageNum(vo.getPageNum());
        dto.setPageSize(vo.getPageSize());
        dto.setSortField(vo.getSortField());
        dto.setSortOrder(vo.getSortOrder());
        return dto;
    }
}
