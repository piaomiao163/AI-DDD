package com.piaomiao.service;

import com.piaomiao.model.BidTenderModel;

import java.util.List;

/**
 * 投标书领域服务接口
 */
public interface BidTenderService {

    /**
     * 保存投标书（草稿）
     * @param model 投标书模型
     * @return 保存后的投标书模型
     */
    BidTenderModel save(BidTenderModel model);

    /**
     * 提交投标书
     * @param id 投标书ID
     * @return 更新后的投标书模型
     */
    BidTenderModel submit(Long id);

    /**
     * 根据ID查询投标书
     * @param id 投标书ID
     * @return 投标书模型
     */
    BidTenderModel findById(Long id);

    /**
     * 根据项目ID查询所有投标书
     * @param projectId 项目ID
     * @return 投标书列表
     */
    List<BidTenderModel> findByProjectId(Long projectId);

    /**
     * 查询当前用户在指定项目的投标书
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 投标书模型
     */
    BidTenderModel findByProjectIdAndUserId(Long projectId, Long userId);
}
