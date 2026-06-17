package com.piaomiao.dto;

import com.piaomiao.base.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 我的流程实例分页查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyProcessInstanceQueryDTO extends BasePageDTO {
    // startUserDbId由Command层从UserContext获取后单独传入Service
}
