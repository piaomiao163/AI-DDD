package com.piaomiao.query;

import com.piaomiao.base.BasePageDTO;

/**
 * 请假管理端分页查询条件
 */
public class LeavePageQuery extends BasePageDTO {
    private String title;
    private Integer leaveType;
    private Integer status;

    public LeavePageQuery() {}

    public LeavePageQuery(Integer pageNum, Integer pageSize, String sortField, String sortOrder,
                          String title, Integer leaveType, Integer status) {
        super(pageNum, pageSize, sortField, sortOrder);
        this.title = title;
        this.leaveType = leaveType;
        this.status = status;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getLeaveType() { return leaveType; }
    public void setLeaveType(Integer leaveType) { this.leaveType = leaveType; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
