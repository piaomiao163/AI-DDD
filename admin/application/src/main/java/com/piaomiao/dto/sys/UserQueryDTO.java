package com.piaomiao.dto.sys;

import com.piaomiao.base.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDTO extends BasePageDTO {
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 状态
     */
    private Integer status;
}
