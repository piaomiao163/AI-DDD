package com.piaomiao.web.vo.bid;

import lombok.Data;

/**
 * 招标项目公示请求VO
 */
@Data
public class BidProjectPublicizeVO {

    /** 公示天数，默认7天 */
    private Integer days = 7;
}
