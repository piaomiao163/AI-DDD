package com.piaomiao.dto;

import java.math.BigDecimal;

public class BidProjectDTO {

    private Long id;
    private String projectName;
    private Integer purchaseType;
    private Integer bidMethod;
    private BigDecimal budgetAmount;
    private Integer fundSource;
    private String description;
    private String announceStartTime;
    private String registerDeadline;
    private String qaDeadline;
    private String bidDeadline;
    private String evalExpectedEnd;
    private Integer evalMethod;
    private Integer expertCount;
    private Integer expertSelectMode;
    private BigDecimal depositAmount;
    private String announceContent;
    private Integer status;
    private Long deptId;
    private String deptName;
    private String createTime;
    private String updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public Integer getPurchaseType() { return purchaseType; }
    public void setPurchaseType(Integer purchaseType) { this.purchaseType = purchaseType; }

    public Integer getBidMethod() { return bidMethod; }
    public void setBidMethod(Integer bidMethod) { this.bidMethod = bidMethod; }

    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }

    public Integer getFundSource() { return fundSource; }
    public void setFundSource(Integer fundSource) { this.fundSource = fundSource; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAnnounceStartTime() { return announceStartTime; }
    public void setAnnounceStartTime(String announceStartTime) { this.announceStartTime = announceStartTime; }

    public String getRegisterDeadline() { return registerDeadline; }
    public void setRegisterDeadline(String registerDeadline) { this.registerDeadline = registerDeadline; }

    public String getQaDeadline() { return qaDeadline; }
    public void setQaDeadline(String qaDeadline) { this.qaDeadline = qaDeadline; }

    public String getBidDeadline() { return bidDeadline; }
    public void setBidDeadline(String bidDeadline) { this.bidDeadline = bidDeadline; }

    public String getEvalExpectedEnd() { return evalExpectedEnd; }
    public void setEvalExpectedEnd(String evalExpectedEnd) { this.evalExpectedEnd = evalExpectedEnd; }

    public Integer getEvalMethod() { return evalMethod; }
    public void setEvalMethod(Integer evalMethod) { this.evalMethod = evalMethod; }

    public Integer getExpertCount() { return expertCount; }
    public void setExpertCount(Integer expertCount) { this.expertCount = expertCount; }

    public Integer getExpertSelectMode() { return expertSelectMode; }
    public void setExpertSelectMode(Integer expertSelectMode) { this.expertSelectMode = expertSelectMode; }

    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }

    public String getAnnounceContent() { return announceContent; }
    public void setAnnounceContent(String announceContent) { this.announceContent = announceContent; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}
