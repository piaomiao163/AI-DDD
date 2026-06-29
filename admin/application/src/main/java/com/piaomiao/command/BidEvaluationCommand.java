package com.piaomiao.command;

import com.piaomiao.dto.BidEvaluationDTO;

import java.util.List;

/**
 * 评标记录命令接口
 */
public interface BidEvaluationCommand {

    /**
     * 保存评标记录（草稿）
     * @param dto 评标信息
     * @return 保存后的评标DTO
     */
    BidEvaluationDTO save(BidEvaluationDTO dto);

    /**
     * 提交评标记录
     * @param id 评标记录ID
     * @return 更新后的评标DTO
     */
    BidEvaluationDTO submit(Long id);

    /**
     * 根据项目ID查询所有评标记录
     * @param projectId 项目ID
     * @return 评标记录列表
     */
    List<BidEvaluationDTO> findByProjectId(Long projectId);

    /**
     * 查询我的评标记录
     * @param tenderId 投标书ID
     * @param expertId 专家ID
     * @return 评标DTO
     */
    BidEvaluationDTO findMyEvaluation(Long tenderId, Long expertId);
}
