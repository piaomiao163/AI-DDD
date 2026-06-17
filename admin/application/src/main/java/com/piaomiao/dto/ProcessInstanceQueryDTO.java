package com.piaomiao.dto;

import com.piaomiao.base.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程实例管理端分页查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessInstanceQueryDTO extends BasePageDTO {
    private String title;
    private Integer status;
    private String businessType;
}
