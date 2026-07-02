package com.piaomiao.repository;

import com.piaomiao.model.BidExpertModel;
import com.piaomiao.query.BidExpertPageQuery;
import com.piaomiao.response.PageResult;

/**
 * 评标专家仓储接口
 */
public interface BidExpertRepository {

    /**
     * 保存专家
     * @param model 专家模型
     * @return 保存后的专家模型
     */
    BidExpertModel save(BidExpertModel model);

    /**
     * 更新专家
     * @param model 专家模型
     * @return 更新后的专家模型
     */
    BidExpertModel update(BidExpertModel model);

    /**
     * 根据ID查询专家
     * @param id 专家ID
     * @return 专家模型
     */
    BidExpertModel findById(Long id);

    /**
     * 删除专家
     * @param id 专家ID
     */
    void deleteById(Long id);

    /**
     * 分页查询专家
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<BidExpertModel> findByPage(BidExpertPageQuery query);
}
