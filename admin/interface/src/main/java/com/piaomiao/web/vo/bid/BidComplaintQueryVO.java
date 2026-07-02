package com.piaomiao.web.vo.bid;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.query.BidComplaintPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 质疑投诉分页查询请求VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BidComplaintQueryVO extends BasePageVO {

    /**
     * 关联项目ID
     */
    private Long projectId;

    /**
     * 类型：1质疑 2投诉
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 转换为查询对象
     * @param vo 请求VO
     * @return 查询条件
     */
    public static BidComplaintPageQuery toQuery(BidComplaintQueryVO vo) {
        BidComplaintPageQuery query = new BidComplaintPageQuery();
        if (vo == null) return query;
        query.setPageNum(vo.getPageNum() != null ? vo.getPageNum() : 1);
        query.setPageSize(vo.getPageSize() != null ? vo.getPageSize() : 10);
        query.setSortField(vo.getSortField());
        query.setSortOrder(vo.getSortOrder());
        query.setProjectId(vo.getProjectId());
        query.setType(vo.getType());
        query.setStatus(vo.getStatus());
        return query;
    }
}
