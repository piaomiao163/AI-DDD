package com.piaomiao.dto;

import com.piaomiao.base.BasePageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 审批任务分页查询DTO
 *
 * @author system
 * @date 2026-06-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ApprovalTaskQueryDTO extends BasePageDTO {

    private Long instanceId;

    private String nodeName;

    private Integer status;
}
