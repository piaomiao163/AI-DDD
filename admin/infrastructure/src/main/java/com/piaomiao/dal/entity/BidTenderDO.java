package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.BidTenderModel;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 投标书数据对象
 */
@TableName("bid_tender")
public class BidTenderDO {

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
     * 关联报名ID
     */
    @TableField("registration_id")
    private Long registrationId;

    /**
     * 投标人名称
     */
    @TableField("vendor_name")
    private String vendorName;

    /**
     * 总报价（万元）
     */
    @TableField("total_price")
    private BigDecimal totalPrice;

    /**
     * 是否含税：1是 0否
     */
    @TableField("include_tax")
    private Integer includeTax;

    /**
     * 技术标文件路径
     */
    @TableField("tech_file_path")
    private String techFilePath;

    /**
     * 商务标文件路径
     */
    @TableField("commercial_file_path")
    private String commercialFilePath;

    /**
     * 报价文件路径
     */
    @TableField("price_file_path")
    private String priceFilePath;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 投标用户ID
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
    public BidTenderModel toModel() {
        BidTenderModel model = new BidTenderModel();
        BeanUtils.copyProperties(this, model);
        model.setIncludeTax(this.includeTax != null && this.includeTax == 1);
        return model;
    }

    /**
     * Model转DO
     * @param model 领域模型
     * @return 数据对象
     */
    public static BidTenderDO fromModel(BidTenderModel model) {
        BidTenderDO entity = new BidTenderDO();
        BeanUtils.copyProperties(model, entity);
        entity.setIncludeTax(Boolean.TRUE.equals(model.getIncludeTax()) ? 1 : 0);
        return entity;
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
    public Integer getIncludeTax() { return includeTax; }
    public void setIncludeTax(Integer includeTax) { this.includeTax = includeTax; }
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
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
