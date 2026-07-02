package com.piaomiao.dto;

import java.math.BigDecimal;

/**
 * 投标书DTO
 */
public class BidTenderDTO {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 关联招标项目ID
     */
    private Long projectId;
    /**
     * 关联报名ID
     */
    private Long registrationId;
    /**
     * 投标人名称
     */
    private String vendorName;
    /**
     * 总报价
     */
    private BigDecimal totalPrice;
    /**
     * 是否含税
     */
    private Boolean includeTax;
    /**
     * 技术标文件路径
     */
    private String techFilePath;
    /**
     * 商务标文件路径
     */
    private String commercialFilePath;
    /**
     * 报价文件路径
     */
    private String priceFilePath;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 投标用户ID
     */
    private Long userId;
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
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public Long getRegistrationId() { return registrationId; }
    public void setRegistrationId(Long registrationId) { this.registrationId = registrationId; }
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }
    public Boolean getIncludeTax() { return includeTax; }
    public void setIncludeTax(Boolean includeTax) { this.includeTax = includeTax; }
    public String getTechFilePath() { return techFilePath; }
    public void setTechFilePath(String techFilePath) { this.techFilePath = techFilePath; }
    public String getCommercialFilePath() { return commercialFilePath; }
    public void setCommercialFilePath(String commercialFilePath) { this.commercialFilePath = commercialFilePath; }
    public String getPriceFilePath() { return priceFilePath; }
    public void setPriceFilePath(String priceFilePath) { this.priceFilePath = priceFilePath; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}
