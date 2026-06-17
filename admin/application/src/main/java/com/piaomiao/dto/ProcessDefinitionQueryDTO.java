package com.piaomiao.dto;

import com.piaomiao.base.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 流程定义分页查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessDefinitionQueryDTO extends BasePageDTO {
    private String name;
    private String key;
}
