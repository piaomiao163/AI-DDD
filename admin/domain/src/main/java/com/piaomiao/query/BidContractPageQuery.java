package com.piaomiao.query;

import com.piaomiao.base.BasePageDTO;

/**
 * 合同分页查询条件
 */
public class BidContractPageQuery extends BasePageDTO {

    /**
     * 关联项目ID
     */
    private Long projectId;

    /**
     * 合同状态
     */
    private Integer status;

    public BidContractPageQuery() {}

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
