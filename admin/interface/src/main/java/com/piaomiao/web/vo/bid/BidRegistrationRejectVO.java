package com.piaomiao.web.vo.bid;

import lombok.Data;

/**
 * 供应商报名拒绝请求VO
 */
@Data
public class BidRegistrationRejectVO {

    /**
     * 拒绝原因
     */
    private String reason;
}
