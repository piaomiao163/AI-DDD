package com.piaomiao.repository;

import com.piaomiao.model.BidEvaluationModel;

import java.util.List;

/**
 * 评标记录仓储接口
 */
public interface BidEvaluationRepository {

    /**
     * 保存评标记录
     * @param model 评标记录模型
     * @return 保存后的评标记录模型
     */
    BidEvaluationModel save(BidEvaluationModel model);

    /**
     * 更新评标记录
     * @param model 评标记录模型
     * @return 更新后的评标记录模型
     */
    BidEvaluationModel update(BidEvaluationModel model);

    /**
     * 根据ID查询评标记录
     * @param id 评标记录ID
     * @return 评标记录模型
     */
    BidEvaluationModel findById(Long id);

    /**
     * 根据项目ID查询所有评标记录
     * @param projectId 项目ID
     * @return 评标记录列表
     */
    List<BidEvaluationModel> findByProjectId(Long projectId);

    /**
     * 查询指定投标书和专家的评标记录
     * @param tenderId 投标书ID
     * @param expertId 专家ID
     * @return 评标记录模型
     */
    BidEvaluationModel findByTenderIdAndExpertId(Long tenderId, Long expertId);
}
