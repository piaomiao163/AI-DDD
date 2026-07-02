package com.piaomiao.web.vo.bid;

import lombok.Data;

import java.util.List;

/**
 * 专家抽取请求VO
 */
@Data
public class BidExpertSelectVO {

    /**
     * 关联项目ID
     */
    private Long projectId;

    /**
     * 专业方向
     */
    private String specialty;

    /**
     * 需要抽取的数量
     */
    private Integer count;

    /**
     * 排除的专家ID列表
     */
    private List<Long> excludeIds;
}
