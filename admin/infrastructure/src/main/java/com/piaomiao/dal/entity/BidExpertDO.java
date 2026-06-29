package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.BidExpertModel;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * 评标专家数据对象
 */
@TableName("bid_expert")
public class BidExpertDO {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 专业方向
     */
    @TableField("specialty")
    private String specialty;

    /**
     * 职称
     */
    @TableField("title")
    private String title;

    /**
     * 单位
     */
    @TableField("organization")
    private String organization;

    /**
     * 联系电话
     */
    @TableField("phone")
    private String phone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 是否黑名单：0否 1是
     */
    @TableField("blacklisted")
    private Integer blacklisted;

    /**
     * 加入黑名单原因
     */
    @TableField("blacklist_reason")
    private String blacklistReason;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 创建人
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 最后更新人
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * DO转Model
     * @return 领域模型
     */
    public BidExpertModel toModel() {
        BidExpertModel model = new BidExpertModel();
        BeanUtils.copyProperties(this, model);
        model.setBlacklisted(this.blacklisted != null && this.blacklisted == 1);
        return model;
    }

    /**
     * Model转DO
     * @param model 领域模型
     * @return 数据对象
     */
    public static BidExpertDO fromModel(BidExpertModel model) {
        BidExpertDO entity = new BidExpertDO();
        BeanUtils.copyProperties(model, entity);
        entity.setBlacklisted(Boolean.TRUE.equals(model.getBlacklisted()) ? 1 : 0);
        return entity;
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
    public Integer getBlacklisted() { return blacklisted; }
    public void setBlacklisted(Integer blacklisted) { this.blacklisted = blacklisted; }
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
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
