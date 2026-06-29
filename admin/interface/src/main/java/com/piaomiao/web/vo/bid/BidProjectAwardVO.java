package com.piaomiao.web.vo.bid;

import lombok.Data;

/**
 * 招标项目定标请求VO
 */
@Data
public class BidProjectAwardVO {

    /** 招标项目ID */
    private Long projectId;

    /** 中标投标书ID */
    private Long winnerTenderId;
}
