package com.piaomiao.dto;

import com.piaomiao.base.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 我的请假分页查询DTO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MyLeaveQueryDTO extends BasePageDTO {
    // applicantId由Command层从UserContext获取后单独传入Service
}
