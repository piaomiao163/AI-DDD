package com.piaomiao.command;

import com.piaomiao.dto.BidRegistrationDTO;
import com.piaomiao.query.BidRegistrationPageQuery;
import com.piaomiao.response.PageResult;

import java.util.List;

/**
 * 供应商报名命令接口
 */
public interface BidRegistrationCommand {

    /**
     * 提交报名
     * @param dto 报名信息
     * @return 保存后的报名DTO
     */
    BidRegistrationDTO save(BidRegistrationDTO dto);

    /**
     * 分页查询报名
     * @param query 查询条件
     * @return 分页结果
     */
    PageResult<BidRegistrationDTO> findByPage(BidRegistrationPageQuery query);

    /**
     * 根据项目ID查询报名列表
     * @param projectId 项目ID
     * @return 报名列表
     */
    List<BidRegistrationDTO> findByProjectId(Long projectId);

    /**
     * 查询我的报名
     * @param projectId 项目ID
     * @param userId 用户ID
     * @return 报名DTO
     */
    BidRegistrationDTO findMyRegistration(Long projectId, Long userId);

    /**
     * 审核通过
     * @param id 报名ID
     * @return 更新后的报名DTO
     */
    BidRegistrationDTO approve(Long id);

    /**
     * 审核拒绝
     * @param id 报名ID
     * @param reason 拒绝原因
     * @return 更新后的报名DTO
     */
    BidRegistrationDTO reject(Long id, String reason);
}
