package com.piaomiao.query;

import com.piaomiao.base.BasePageDTO;

/**
 * 供应商报名分页查询条件
 */
public class BidRegistrationPageQuery extends BasePageDTO {

    /**
     * 报名状态
     */
    private Integer status;

    /**
     * 关联项目ID
     */
    private Long projectId;

    public BidRegistrationPageQuery() {}

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
}
