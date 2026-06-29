package com.piaomiao.command;

import com.piaomiao.dto.BidContractDTO;
import com.piaomiao.query.BidContractPageQuery;
import com.piaomiao.response.PageResult;

/**
 * 合同命令接口
 */
public interface BidContractCommand {

    /**
     * 创建合同
     * @param dto 合同信息
     * @return 保存后的合同DTO
     */
    BidContractDTO save(BidContractDTO dto);

    /**
     * 更新合同
     * @param dto 合同信息
     * @return 更新后的合同DTO
     */
    BidContractDTO update(BidContractDTO dto);

    /**
     * 根据ID查询合同
     * @param id 合同ID
     * @return 合同DTO
     */
    BidContractDTO findById(Long id);

    /**
     * 分页查询合同
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<BidContractDTO> findByPage(BidContractPageQuery query);

    /**
     * 签署合同
     * @param id 合同ID
     * @param signDate 签署日期
     * @return 更新后的合同DTO
     */
    BidContractDTO sign(Long id, String signDate);

    /**
     * 开始履行
     * @param id 合同ID
     * @return 更新后的合同DTO
     */
    BidContractDTO execute(Long id);

    /**
     * 完结合同
     * @param id 合同ID
     * @return 更新后的合同DTO
     */
    BidContractDTO complete(Long id);

    /**
     * 解除合同
     * @param id 合同ID
     * @return 更新后的合同DTO
     */
    BidContractDTO terminate(Long id);
}
