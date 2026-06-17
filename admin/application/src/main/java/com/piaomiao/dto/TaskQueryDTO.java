package com.piaomiao.dto;

import com.piaomiao.base.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 任务分页查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TaskQueryDTO extends BasePageDTO {
    // userId由Command层从UserContext获取后单独传入Service
}
