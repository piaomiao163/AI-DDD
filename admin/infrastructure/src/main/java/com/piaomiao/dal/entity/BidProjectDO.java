package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.BidProjectModel;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("bid_project")
public class BidProjectDO {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("project_no")
    private String projectNo;

    @TableField("project_name")
    private String projectName;

    @TableField("purchase_type")
    private Integer purchaseType;

    @TableField("bid_method")
    private Integer bidMethod;

    @TableField("budget_amount")
    private BigDecimal budgetAmount;

    @TableField("fund_source")
    private Integer fundSource;

    @TableField("description")
    private String description;

    @TableField("announce_start_time")
    private LocalDateTime announceStartTime;

    @TableField("register_deadline")
    private LocalDateTime registerDeadline;

    @TableField("qa_deadline")
    private LocalDateTime qaDeadline;

    @TableField("bid_deadline")
    private LocalDateTime bidDeadline;

    @TableField("eval_expected_end")
    private LocalDateTime evalExpectedEnd;

    @TableField("eval_method")
    private Integer evalMethod;

    @TableField("expert_count")
    private Integer expertCount;

    @TableField("expert_select_mode")
    private Integer expertSelectMode;

    @TableField("deposit_amount")
    private BigDecimal depositAmount;

    @TableField("announce_content")
    private String announceContent;

    private Integer status;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("update_time")
    private LocalDateTime updateTime;

    @TableField("create_by")
    private String createBy;

    @TableField("update_by")
    private String updateBy;

    @TableField("dept_id")
    private Long deptId;

    @TableField("dept_name")
    private String deptName;

    @TableLogic
    private Integer deleted;

    public BidProjectModel toModel() {
        BidProjectModel model = new BidProjectModel();
        BeanUtils.copyProperties(this, model);
        return model;
    }

    public static BidProjectDO fromModel(BidProjectModel model) {
        BidProjectDO entity = new BidProjectDO();
        BeanUtils.copyProperties(model, entity);
        return entity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getProjectNo() { return projectNo; }
    public void setProjectNo(String projectNo) { this.projectNo = projectNo; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public Integer getPurchaseType() { return purchaseType; }
    public void setPurchaseType(Integer purchaseType) { this.purchaseType = purchaseType; }

    public Integer getBidMethod() { return bidMethod; }
    public void setBidMethod(Integer bidMethod) { this.bidMethod = bidMethod; }

    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }

    public Integer getFundSource() { return fundSource; }
    public void setFundSource(Integer fundSource) { this.fundSource = fundSource; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDateTime getAnnounceStartTime() { return announceStartTime; }
    public void setAnnounceStartTime(LocalDateTime announceStartTime) { this.announceStartTime = announceStartTime; }

    public LocalDateTime getRegisterDeadline() { return registerDeadline; }
    public void setRegisterDeadline(LocalDateTime registerDeadline) { this.registerDeadline = registerDeadline; }

    public LocalDateTime getQaDeadline() { return qaDeadline; }
    public void setQaDeadline(LocalDateTime qaDeadline) { this.qaDeadline = qaDeadline; }

    public LocalDateTime getBidDeadline() { return bidDeadline; }
    public void setBidDeadline(LocalDateTime bidDeadline) { this.bidDeadline = bidDeadline; }

    public LocalDateTime getEvalExpectedEnd() { return evalExpectedEnd; }
    public void setEvalExpectedEnd(LocalDateTime evalExpectedEnd) { this.evalExpectedEnd = evalExpectedEnd; }

    public Integer getEvalMethod() { return evalMethod; }
    public void setEvalMethod(Integer evalMethod) { this.evalMethod = evalMethod; }

    public Integer getExpertCount() { return expertCount; }
    public void setExpertCount(Integer expertCount) { this.expertCount = expertCount; }

    public Integer getExpertSelectMode() { return expertSelectMode; }
    public void setExpertSelectMode(Integer expertSelectMode) { this.expertSelectMode = expertSelectMode; }

    public BigDecimal getDepositAmount() { return depositAmount; }
    public void setDepositAmount(BigDecimal depositAmount) { this.depositAmount = depositAmount; }

    public String getAnnounceContent() { return announceContent; }
    public void setAnnounceContent(String announceContent) { this.announceContent = announceContent; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }

    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
