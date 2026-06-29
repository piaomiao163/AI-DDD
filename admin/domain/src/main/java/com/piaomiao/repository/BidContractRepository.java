package com.piaomiao.repository;

import com.piaomiao.model.BidContractModel;
import com.piaomiao.query.BidContractPageQuery;
import com.piaomiao.response.PageResult;

/**
 * 合同仓储接口
 */
public interface BidContractRepository {

    /**
     * 保存合同
     * @param model 合同模型
     * @return 保存后的合同模型
     */
    BidContractModel save(BidContractModel model);

    /**
     * 更新合同
     * @param model 合同模型
     * @return 更新后的合同模型
     */
    BidContractModel update(BidContractModel model);

    /**
     * 根据ID查询合同
     * @param id 合同ID
     * @return 合同模型
     */
    BidContractModel findById(Long id);

    /**
     * 分页查询合同
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<BidContractModel> findByPage(BidContractPageQuery query);
}
