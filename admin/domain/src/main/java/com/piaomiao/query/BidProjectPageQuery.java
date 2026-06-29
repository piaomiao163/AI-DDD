package com.piaomiao.query;

import com.piaomiao.base.BasePageDTO;

public class BidProjectPageQuery extends BasePageDTO {

    private String projectName;
    private Integer purchaseType;
    /** 招标方式：1-公开招标 2-邀请招标 3-竞争性谈判 4-询价 5-单一来源 */
    private Integer bidMethod;
    private Integer status;

    public BidProjectPageQuery() {}

    public BidProjectPageQuery(Integer pageNum, Integer pageSize, String sortField, String sortOrder,
                               String projectName, Integer purchaseType, Integer bidMethod, Integer status) {
        super(pageNum, pageSize, sortField, sortOrder);
        this.projectName = projectName;
        this.purchaseType = purchaseType;
        this.bidMethod = bidMethod;
        this.status = status;
    }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public Integer getPurchaseType() { return purchaseType; }
    public void setPurchaseType(Integer purchaseType) { this.purchaseType = purchaseType; }

    public Integer getBidMethod() { return bidMethod; }
    public void setBidMethod(Integer bidMethod) { this.bidMethod = bidMethod; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
