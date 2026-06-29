package com.piaomiao.web.vo.bid;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.query.BidProjectPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 招标项目分页查询请求VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BidProjectQueryVO extends BasePageVO {

    /** 项目名称（模糊查询） */
    private String projectName;

    /** 采购类型：1-货物 2-服务 3-工程 */
    private Integer purchaseType;

    /** 招标方式：1-公开招标 2-邀请招标 3-竞争性谈判 4-询价 5-单一来源 */
    private Integer bidMethod;

    /** 项目状态 */
    private Integer status;

    public static BidProjectPageQuery toQuery(BidProjectQueryVO vo) {
        BidProjectPageQuery query = new BidProjectPageQuery();
        if (vo == null) return query;
        query.setPageNum(vo.getPageNum() != null ? vo.getPageNum() : 1);
        query.setPageSize(vo.getPageSize() != null ? vo.getPageSize() : 10);
        query.setSortField(vo.getSortField());
        query.setSortOrder(vo.getSortOrder());
        query.setProjectName(vo.getProjectName());
        query.setPurchaseType(vo.getPurchaseType());
        query.setBidMethod(vo.getBidMethod());
        query.setStatus(vo.getStatus());
        return query;
    }
}
