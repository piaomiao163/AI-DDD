package com.piaomiao.command;

import com.piaomiao.dto.BidComplaintDTO;
import com.piaomiao.query.BidComplaintPageQuery;
import com.piaomiao.response.PageResult;

/**
 * 质疑投诉命令接口
 */
public interface BidComplaintCommand {

    /**
     * 提交质疑投诉
     * @param dto 质疑投诉信息
     * @return 保存后的质疑投诉DTO
     */
    BidComplaintDTO save(BidComplaintDTO dto);

    /**
     * 分页查询质疑投诉
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<BidComplaintDTO> findByPage(BidComplaintPageQuery query);

    /**
     * 回复质疑
     * @param id 质疑ID
     * @param replyContent 回复内容
     * @return 更新后的质疑投诉DTO
     */
    BidComplaintDTO reply(Long id, String replyContent);

    /**
     * 关闭质疑
     * @param id 质疑ID
     * @return 更新后的质疑投诉DTO
     */
    BidComplaintDTO close(Long id);

    /**
     * 升级为投诉
     * @param id 质疑ID
     * @return 更新后的质疑投诉DTO
     */
    BidComplaintDTO escalate(Long id);
}
