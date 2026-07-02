package com.piaomiao.command;

import com.piaomiao.dto.BidExpertDTO;
import com.piaomiao.query.BidExpertPageQuery;
import com.piaomiao.response.PageResult;

/**
 * 评标专家命令接口
 */
public interface BidExpertCommand {

    /**
     * 保存专家
     * @param dto 专家信息
     * @return 保存后的专家DTO
     */
    BidExpertDTO save(BidExpertDTO dto);

    /**
     * 更新专家
     * @param dto 专家信息
     * @return 更新后的专家DTO
     */
    BidExpertDTO update(BidExpertDTO dto);

    /**
     * 根据ID查询专家
     * @param id 专家ID
     * @return 专家DTO
     */
    BidExpertDTO findById(Long id);

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
    PageResult<BidExpertDTO> findByPage(BidExpertPageQuery query);

    /**
     * 加入黑名单
     * @param id 专家ID
     * @param reason 原因
     * @return 更新后的专家DTO
     */
    BidExpertDTO blacklist(Long id, String reason);

    /**
     * 移出黑名单
     * @param id 专家ID
     * @return 更新后的专家DTO
     */
    BidExpertDTO unblacklist(Long id);
}
