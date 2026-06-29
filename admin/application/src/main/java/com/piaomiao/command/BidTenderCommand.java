package com.piaomiao.command;

import com.piaomiao.dto.BidTenderDTO;

import java.util.List;

/**
 * 投标书命令接口
 */
public interface BidTenderCommand {

    /**
     * 保存投标书（草稿）
     * @param dto 投标书信息
     * @return 保存后的投标书DTO
     */
    BidTenderDTO save(BidTenderDTO dto);

    /**
     * 提交投标书
     * @param id 投标书ID
     * @return 更新后的投标书DTO
     */
    BidTenderDTO submit(Long id);

    /**
     * 根据ID查询投标书
     * @param id 投标书ID
     * @return 投标书DTO
     */
    BidTenderDTO findById(Long id);

    /**
     * 根据项目ID查询所有投标书
     * @param projectId 项目ID
     * @return 投标书列表
     */
    List<BidTenderDTO> findByProjectId(Long projectId);

    /**
     * 查询我的投标书
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 投标书DTO
     */
    BidTenderDTO findMyTender(Long projectId, Long userId);
}
