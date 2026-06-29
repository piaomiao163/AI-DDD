package com.piaomiao.dto;

import java.math.BigDecimal;

/**
 * 合同DTO
 */
public class BidContractDTO {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 关联招标项目ID
     */
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 甲方
     */
    private String partyA;
    /**
     * 乙方
     */
    private String partyB;
    /**
     * 合同金额
     */
    private BigDecimal amount;
    /**
     * 合同开始日期
     */
    private String startDate;
    /**
     * 合同结束日期
     */
    private String endDate;
    /**
     * 合同正文
     */
    private String content;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 签署日期
     */
    private String signDate;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getContractName() { return contractName; }
    public void setContractName(String contractName) { this.contractName = contractName; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public String getPartyA() { return partyA; }
    public void setPartyA(String partyA) { this.partyA = partyA; }
    public String getPartyB() { return partyB; }
    public void setPartyB(String partyB) { this.partyB = partyB; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getSignDate() { return signDate; }
    public void setSignDate(String signDate) { this.signDate = signDate; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}
