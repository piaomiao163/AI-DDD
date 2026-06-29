package com.piaomiao.model;

import java.time.LocalDateTime;

/**
 * 评标专家领域模型
 */
public class BidExpertModel {

    /** 主键ID */
    private Long id;
    /** 姓名 */
    private String name;
    /** 专业方向 */
    private String specialty;
    /** 职称 */
    private String title;
    /** 单位 */
    private String organization;
    /** 联系电话 */
    private String phone;
    /** 邮箱 */
    private String email;
    /** 备注 */
    private String remark;
    /** 是否黑名单 */
    private Boolean blacklisted;
    /** 加入黑名单原因 */
    private String blacklistReason;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 最后更新时间 */
    private LocalDateTime updateTime;
    /** 创建人 */
    private String createBy;
    /** 最后更新人 */
    private String updateBy;

    /**
     * 校验必填字段
     */
    public void validate() {
        if (this.name == null || this.name.isEmpty()) {
            throw new IllegalArgumentException("专家姓名不能为空");
        }
    }

    /**
     * 加入黑名单
     * @param reason 加入原因
     */
    public void blacklist(String reason) {
        if (Boolean.TRUE.equals(this.blacklisted)) {
            throw new IllegalStateException("该专家已在黑名单中");
        }
        this.blacklisted = true;
        this.blacklistReason = reason;
    }

    /**
     * 移出黑名单
     */
    public void unblacklist() {
        if (!Boolean.TRUE.equals(this.blacklisted)) {
            throw new IllegalStateException("该专家不在黑名单中");
        }
        this.blacklisted = false;
        this.blacklistReason = null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getOrganization() { return organization; }
    public void setOrganization(String organization) { this.organization = organization; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Boolean getBlacklisted() { return blacklisted; }
    public void setBlacklisted(Boolean blacklisted) { this.blacklisted = blacklisted; }

    public String getBlacklistReason() { return blacklistReason; }
    public void setBlacklistReason(String blacklistReason) { this.blacklistReason = blacklistReason; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
}
