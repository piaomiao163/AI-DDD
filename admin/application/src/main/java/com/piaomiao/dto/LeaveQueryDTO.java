package com.piaomiao.dto;

import com.piaomiao.base.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 请假管理端分页查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LeaveQueryDTO extends BasePageDTO {
    private String title;
    private Integer leaveType;
    private Integer status;
}
