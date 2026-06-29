package com.piaomiao.web.vo.bid;

import com.piaomiao.base.BasePageVO;
import com.piaomiao.query.BidExpertPageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评标专家分页查询请求VO
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BidExpertQueryVO extends BasePageVO {

    /**
     * 专家姓名（模糊查询）
     */
    private String name;

    /**
     * 专业方向
     */
    private String specialty;

    /**
     * 转换为查询对象
     * @param vo 请求VO
     * @return 查询条件
     */
    public static BidExpertPageQuery toQuery(BidExpertQueryVO vo) {
        BidExpertPageQuery query = new BidExpertPageQuery();
        if (vo == null) return query;
        query.setPageNum(vo.getPageNum() != null ? vo.getPageNum() : 1);
        query.setPageSize(vo.getPageSize() != null ? vo.getPageSize() : 10);
        query.setSortField(vo.getSortField());
        query.setSortOrder(vo.getSortOrder());
        query.setName(vo.getName());
        query.setSpecialty(vo.getSpecialty());
        return query;
    }
}
