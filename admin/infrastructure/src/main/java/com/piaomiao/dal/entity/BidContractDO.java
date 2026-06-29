package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.BidContractModel;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同数据对象
 */
@TableName("bid_contract")
public class BidContractDO {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 合同名称
     */
    @TableField("contract_name")
    private String contractName;

    /**
     * 关联招标项目ID
     */
    @TableField("project_id")
    private Long projectId;

    /**
     * 项目名称（冗余）
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 甲方
     */
    @TableField("party_a")
    private String partyA;

    /**
     * 乙方
     */
    @TableField("party_b")
    private String partyB;

    /**
     * 合同金额（万元）
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 合同开始日期
     */
    @TableField("start_date")
    private LocalDate startDate;

    /**
     * 合同结束日期
     */
    @TableField("end_date")
    private LocalDate endDate;

    /**
     * 合同正文
     */
    @TableField("content")
    private String content;

    /**
     * 状态：0草稿 1待签署 2已签署 3履行中 4已完结 5已解除
     */
    @TableField("status")
    private Integer status;

    /**
     * 签署日期
     */
    @TableField("sign_date")
    private LocalDate signDate;

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
    public BidContractModel toModel() {
        BidContractModel model = new BidContractModel();
        BeanUtils.copyProperties(this, model);
        return model;
    }

    /**
     * Model转DO
     * @param model 领域模型
     * @return 数据对象
     */
    public static BidContractDO fromModel(BidContractModel model) {
        BidContractDO entity = new BidContractDO();
        BeanUtils.copyProperties(model, entity);
        return entity;
    }

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
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDate getSignDate() { return signDate; }
    public void setSignDate(LocalDate signDate) { this.signDate = signDate; }
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
