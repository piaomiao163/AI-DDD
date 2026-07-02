package com.piaomiao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 投标书领域模型
 */
public class BidTenderModel {

    /** 状态：草稿 */
    public static final int STATUS_DRAFT = 0;
    /** 状态：已提交 */
    public static final int STATUS_SUBMITTED = 1;
    /** 状态：已开标 */
    public static final int STATUS_OPENED = 2;
    /** 状态：资格审查中 */
    public static final int STATUS_REVIEWING = 3;
    /** 状态：通过 */
    public static final int STATUS_PASSED = 4;
    /** 状态：淘汰 */
    public static final int STATUS_ELIMINATED = 5;

    /** 主键ID */
    private Long id;
    /** 关联招标项目ID */
    private Long projectId;
    /** 关联报名ID */
    private Long registrationId;
    /** 投标人名称 */
    private String vendorName;
    /** 总报价（万元） */
    private BigDecimal totalPrice;
    /** 是否含税 */
    private Boolean includeTax;
    /** 技术标文件路径 */
    private String techFilePath;
    /** 商务标文件路径 */
    private String commercialFilePath;
    /** 报价文件路径 */
    private String priceFilePath;
    /** 备注 */
    private String remark;
    /** 状态 */
    private Integer status;
    /** 投标用户ID */
    private Long userId;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 最后更新时间 */
    private LocalDateTime updateTime;
    /** 创建人 */
    private String createBy;
    /** 最后更新人 */
    private String updateBy;

    /**
     * 保存草稿
     */
    public void saveDraft() {
        if (this.projectId == null) {
            throw new IllegalArgumentException("项目ID不能为空");
        }
        if (this.status == null) {
            this.status = STATUS_DRAFT;
        }
    }

    /**
     * 提交投标：草稿 → 已提交
     */
    public void submit() {
        if (this.status == null || this.status != STATUS_DRAFT) {
            throw new IllegalStateException("只有草稿状态的投标书才能提交");
        }
        if (this.totalPrice == null || this.totalPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("总报价必须大于0");
        }
        this.status = STATUS_SUBMITTED;
    }

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

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
}
