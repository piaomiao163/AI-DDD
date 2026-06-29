package com.piaomiao.service;

import com.piaomiao.model.BidComplaintModel;
import com.piaomiao.query.BidComplaintPageQuery;
import com.piaomiao.response.PageResult;

/**
 * 质疑投诉领域服务接口
 */
public interface BidComplaintService {

    /**
     * 提交质疑投诉
     * @param model 质疑投诉模型
     * @return 保存后的质疑投诉模型
     */
    BidComplaintModel save(BidComplaintModel model);

    /**
     * 分页查询质疑投诉
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<BidComplaintModel> findByPage(BidComplaintPageQuery query);

    /**
     * 回复质疑
     * @param id 质疑ID
     * @param replyContent 回复内容
     * @return 更新后的质疑投诉模型
     */
    BidComplaintModel reply(Long id, String replyContent);

    /**
     * 关闭质疑
     * @param id 质疑ID
     * @return 更新后的质疑投诉模型
     */
    BidComplaintModel close(Long id);

    /**
     * 升级为投诉
     * @param id 质疑ID
     * @return 更新后的质疑投诉模型
     */
    BidComplaintModel escalate(Long id);
}
