package com.piaomiao.service;

import com.piaomiao.model.BidContractModel;
import com.piaomiao.query.BidContractPageQuery;
import com.piaomiao.response.PageResult;

import java.time.LocalDate;

/**
 * 合同领域服务接口
 */
public interface BidContractService {

    /**
     * 创建合同
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

    /**
     * 签署合同
     * @param id 合同ID
     * @param signDate 签署日期
     * @return 更新后的合同模型
     */
    BidContractModel sign(Long id, LocalDate signDate);

    /**
     * 开始履行
     * @param id 合同ID
     * @return 更新后的合同模型
     */
    BidContractModel execute(Long id);

    /**
     * 完结合同
     * @param id 合同ID
     * @return 更新后的合同模型
     */
    BidContractModel complete(Long id);

    /**
     * 解除合同
     * @param id 合同ID
     * @return 更新后的合同模型
     */
    BidContractModel terminate(Long id);
}
