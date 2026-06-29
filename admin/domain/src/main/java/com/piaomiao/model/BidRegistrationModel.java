package com.piaomiao.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 供应商报名领域模型
 */
public class BidRegistrationModel {

    /** 状态：待审核 */
    public static final int STATUS_PENDING = 0;
    /** 状态：已通过 */
    public static final int STATUS_APPROVED = 1;
    /** 状态：已拒绝 */
    public static final int STATUS_REJECTED = 2;

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 关联招标项目ID
     */
    private Long projectId;
    /**
     * 供应商名称
     */
    private String vendorName;
    /**
     * 联系人姓名
     */
    private String contactName;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 资质材料文件路径列表（JSON）
     */
    private List<String> qualificationFiles;
    /**
     * 状态：0待审核 1已通过 2已拒绝
     */
    private Integer status;
    /**
     * 拒绝原因
     */
    private String rejectReason;
    /**
     * 报名用户ID
     */
    private Long userId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最后更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 最后更新人
     */
    private String updateBy;

    /**
     * 提交报名：校验必填字段，设置为待审核状态
     */
    public void submit() {
        if (this.vendorName == null || this.vendorName.isEmpty()) {
            throw new IllegalArgumentException("供应商名称不能为空");
        }
        if (this.projectId == null) {
            throw new IllegalArgumentException("项目ID不能为空");
        }
        this.status = STATUS_PENDING;
    }

    /**
     * 审核通过：待审核 → 已通过
     */
    public void approve() {
        if (this.status == null || this.status != STATUS_PENDING) {
            throw new IllegalStateException("只有待审核状态的报名才能通过");
        }
        this.status = STATUS_APPROVED;
    }

    /**
     * 审核拒绝：待审核 → 已拒绝
     * @param reason 拒绝原因
     */
    public void reject(String reason) {
        if (this.status == null || this.status != STATUS_PENDING) {
            throw new IllegalStateException("只有待审核状态的报名才能拒绝");
        }
        this.status = STATUS_REJECTED;
        this.rejectReason = reason;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getVendorName() { return vendorName; }
    public void setVendorName(String vendorName) { this.vendorName = vendorName; }
    public String getContactName() { return contactName; }
    public void setContactName(String contactName) { this.contactName = contactName; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<String> getQualificationFiles() { return qualificationFiles; }
    public void setQualificationFiles(List<String> qualificationFiles) { this.qualificationFiles = qualificationFiles; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
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
