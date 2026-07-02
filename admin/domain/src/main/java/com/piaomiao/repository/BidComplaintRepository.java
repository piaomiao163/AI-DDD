package com.piaomiao.repository;

import com.piaomiao.model.BidComplaintModel;
import com.piaomiao.query.BidComplaintPageQuery;
import com.piaomiao.response.PageResult;

/**
 * 质疑投诉仓储接口
 */
public interface BidComplaintRepository {

    /**
     * 保存质疑投诉
     * @param model 质疑投诉模型
     * @return 保存后的质疑投诉模型
     */
    BidComplaintModel save(BidComplaintModel model);

    /**
     * 更新质疑投诉
     * @param model 质疑投诉模型
     * @return 更新后的质疑投诉模型
     */
    BidComplaintModel update(BidComplaintModel model);

    /**
     * 根据ID查询质疑投诉
     * @param id 质疑投诉ID
     * @return 质疑投诉模型
     */
    BidComplaintModel findById(Long id);

    /**
     * 分页查询质疑投诉
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<BidComplaintModel> findByPage(BidComplaintPageQuery query);
}
