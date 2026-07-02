package com.piaomiao.dto;

import java.util.List;

/**
 * 供应商报名DTO
 */
public class BidRegistrationDTO {

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
     * 资质材料文件路径列表
     */
    private List<String> qualificationFiles;
    /**
     * 状态
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
    private String createTime;
    /**
     * 更新时间
     */
    private String updateTime;

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
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}
