package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.BidRegistrationModel;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 供应商报名数据对象
 */
@TableName("bid_registration")
public class BidRegistrationDO {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 关联招标项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 供应商名称
     */
    @TableField("vendor_name")
    private String vendorName;

    /**
     * 联系人姓名
     */
    @TableField("contact_name")
    private String contactName;

    /**
     * 联系电话
     */
    @TableField("contact_phone")
    private String contactPhone;

    /**
     * 邮箱
     */
    @TableField("email")
    private String email;

    /**
     * 资质材料文件路径（JSON字符串）
     */
    @TableField("qualification_files")
    private String qualificationFiles;

    /**
     * 状态：0待审核 1已通过 2已拒绝
     */
    @TableField("status")
    private Integer status;

    /**
     * 拒绝原因
     */
    @TableField("reject_reason")
    private String rejectReason;

    /**
     * 报名用户ID
     */
    @TableField("user_id")
    private Long userId;

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
    public BidRegistrationModel toModel() {
        BidRegistrationModel model = new BidRegistrationModel();
        BeanUtils.copyProperties(this, model);
        if (this.qualificationFiles != null && !this.qualificationFiles.isEmpty()) {
            model.setQualificationFiles(Arrays.asList(this.qualificationFiles.split(",")));
        } else {
            model.setQualificationFiles(Collections.emptyList());
        }
        return model;
    }

    /**
     * Model转DO
     * @param model 领域模型
     * @return 数据对象
     */
    public static BidRegistrationDO fromModel(BidRegistrationModel model) {
        BidRegistrationDO entity = new BidRegistrationDO();
        BeanUtils.copyProperties(model, entity);
        if (model.getQualificationFiles() != null && !model.getQualificationFiles().isEmpty()) {
            entity.setQualificationFiles(String.join(",", model.getQualificationFiles()));
        }
        return entity;
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
    public String getQualificationFiles() { return qualificationFiles; }
    public void setQualificationFiles(String qualificationFiles) { this.qualificationFiles = qualificationFiles; }
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
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
