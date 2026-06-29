package com.piaomiao.repository;

import com.piaomiao.model.BidRegistrationModel;
import com.piaomiao.query.BidRegistrationPageQuery;
import com.piaomiao.response.PageResult;

import java.util.List;

/**
 * 供应商报名仓储接口
 */
public interface BidRegistrationRepository {

    /**
     * 保存报名
     * @param model 报名模型
     * @return 保存后的报名模型
     */
    BidRegistrationModel save(BidRegistrationModel model);

    /**
     * 更新报名
     * @param model 报名模型
     * @return 更新后的报名模型
     */
    BidRegistrationModel update(BidRegistrationModel model);

    /**
     * 根据ID查询报名
     * @param id 报名ID
     * @return 报名模型
     */
    BidRegistrationModel findById(Long id);

    /**
     * 根据项目ID查询所有报名列表
     * @param projectId 项目ID
     * @return 报名列表
     */
    List<BidRegistrationModel> findByProjectId(Long projectId);

    /**
     * 查询当前用户在指定项目的报名
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 报名模型
     */
    BidRegistrationModel findByProjectIdAndUserId(Long projectId, Long userId);

    /**
     * 分页查询报名
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<BidRegistrationModel> findByPage(BidRegistrationPageQuery query);
}
