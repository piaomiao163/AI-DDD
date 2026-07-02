package com.piaomiao.web.vo.bid;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.query.BidContractPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同分页查询请求VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BidContractQueryVO extends BasePageVO {

    /**
     * 关联项目ID
     */
    private Long projectId;

    /**
     * 合同状态
     */
    private Integer status;

    /**
     * 转换为查询对象
     * @param vo 请求VO
     * @return 查询条件
     */
    public static BidContractPageQuery toQuery(BidContractQueryVO vo) {
        BidContractPageQuery query = new BidContractPageQuery();
        if (vo == null) return query;
        query.setPageNum(vo.getPageNum() != null ? vo.getPageNum() : 1);
        query.setPageSize(vo.getPageSize() != null ? vo.getPageSize() : 10);
        query.setSortField(vo.getSortField());
        query.setSortOrder(vo.getSortOrder());
        query.setProjectId(vo.getProjectId());
        query.setStatus(vo.getStatus());
        return query;
    }
}
