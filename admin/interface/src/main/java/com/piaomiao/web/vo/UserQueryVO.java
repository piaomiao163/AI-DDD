package com.piaomiao.web.vo;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.dto.UserQueryDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryVO extends BasePageVO {
    /**
     * 用户名
     */
    private String username;

    /**
     * 状态
     */
    private Integer status;

    public static UserQueryDTO toDTO(UserQueryVO vo) {
        UserQueryDTO dto = new UserQueryDTO();
        dto.setUsername(vo.getUsername());
        dto.setStatus(vo.getStatus());
        dto.setPageNum(vo.getPageNum());
        dto.setPageSize(vo.getPageSize());
        dto.setSortField(vo.getSortField());
        dto.setSortOrder(vo.getSortOrder());
        return dto;
    }
}
