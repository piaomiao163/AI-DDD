package com.piaomiao.query;

import com.piaomiao.base.BasePageDTO;

/**
 * 质疑投诉分页查询条件
 */
public class BidComplaintPageQuery extends BasePageDTO {

    /**
     * 关联项目ID
     */
    private Long projectId;

    /**
     * 类型：1质疑 2投诉
     */
    private Integer type;

    /**
     * 状态
     */
    private Integer status;

    public BidComplaintPageQuery() {}

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
