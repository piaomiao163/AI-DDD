package com.piaomiao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BidProjectModel {

    /** 状态：草稿 */
    public static final int STATUS_DRAFT = 0;
    /** 状态：待审核 */
    public static final int STATUS_PENDING_REVIEW = 1;
    /** 状态：已发布 */
    public static final int STATUS_PUBLISHED = 2;
    /** 状态：报名中 */
    public static final int STATUS_REGISTRATION = 3;
    /** 状态：已截标 */
    public static final int STATUS_CLOSED = 4;
    /** 状态：开标中 */
    public static final int STATUS_BIDDING_OPEN = 5;
    /** 状态：评标中 */
    public static final int STATUS_EVALUATION = 6;
    /** 状态：公示中 */
    public static final int STATUS_PUBLICIZING = 7;
    /** 状态：已定标 */
    public static final int STATUS_AWARDED = 8;
    /** 状态：已归档 */
    public static final int STATUS_ARCHIVED = 9;
    /** 状态：废标 */
    public static final int STATUS_CANCELLED = 10;

    /** 主键ID */
    private Long id;
    /** 项目名称 */
    private String projectName;
    /** 采购类型：1-货物 2-服务 3-工程 */
    private Integer purchaseType;
    /** 招标方式：1-公开招标 2-邀请招标 3-竞争性谈判 4-询价 5-单一来源 */
    private Integer bidMethod;
    /** 预算金额（万元） */
    private BigDecimal budgetAmount;
    /** 资金来源：1-财政资金 2-自有资金 3-其他 */
    private Integer fundSource;
    /** 项目描述 */
    private String description;
    /** 公告开始时间 */
    private LocalDateTime announceStartTime;
    /** 报名截止时间 */
    private LocalDateTime registerDeadline;
    /** 答疑截止时间 */
    private LocalDateTime qaDeadline;
    /** 投标截止时间 */
    private LocalDateTime bidDeadline;
    /** 评标预计完成时间 */
    private LocalDateTime evalExpectedEnd;
    /** 评标方法：1-综合评分法 2-最低价法 3-两阶段评标 */
    private Integer evalMethod;
    /** 评标专家人数 */
    private Integer expertCount;
    /** 专家选取方式：1-随机抽取 2-指定 */
    private Integer expertSelectMode;
    /** 投标保证金（万元） */
    private BigDecimal depositAmount;
    /** 招标公告内容 */
    private String announceContent;
    /** 项目状态：0-草稿 1-待审核 2-已发布 3-报名中 4-已截标 5-开标中 6-评标中 7-公示中 8-已定标 9-已归档 10-废标 */
    private Integer status;
    /** 所属部门ID */
    private Long deptId;
    /** 所属部门名称 */
    private String deptName;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 最后更新时间 */
    private LocalDateTime updateTime;
    /** 创建人 */
    private String createBy;
    /** 最后更新人 */
    private String updateBy;

    /**
     * 提交审核：草稿 → 待审核
     * 要求项目名称不能为空
     */
    public void submit() {
        if (this.status == null || this.status != STATUS_DRAFT) {
            throw new IllegalStateException("只有草稿状态的项目才能提交审核");
        }
        if (this.projectName == null || this.projectName.isEmpty()) {
            throw new IllegalArgumentException("项目名称不能为空");
        }
        this.status = STATUS_PENDING_REVIEW;
    }

    /**
     * 发布项目：待审核 → 报名中
     */
    public void publish() {
        if (this.status == null || this.status != STATUS_PENDING_REVIEW) {
            throw new IllegalStateException("只有待审核状态的项目才能发布");
        }
        this.status = STATUS_REGISTRATION;
    }

    /**
     * 开标：报名中 → 开标中
     */
    public void openBidding() {
        if (this.status == null || this.status != STATUS_REGISTRATION) {
            throw new IllegalStateException("只有报名中状态的项目才能触发开标");
        }
        this.status = STATUS_BIDDING_OPEN;
    }

    /**
     * 开始评标：开标中 → 评标中
     */
    public void startEvaluation() {
        if (this.status == null || this.status != STATUS_BIDDING_OPEN) {
            throw new IllegalStateException("只有开标中状态的项目才能开始评标");
        }
        this.status = STATUS_EVALUATION;
    }

    /**
     * 发起公示：评标中 → 公示中
     */
    public void publicize() {
        if (this.status == null || this.status != STATUS_EVALUATION) {
            throw new IllegalStateException("只有评标中状态的项目才能发起公示");
        }
        this.status = STATUS_PUBLICIZING;
    }

    /**
     * 定标：公示中 → 已定标
     */
    public void award() {
        if (this.status == null || this.status != STATUS_PUBLICIZING) {
            throw new IllegalStateException("只有公示中状态的项目才能定标");
        }
        this.status = STATUS_AWARDED;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Long getDeptId() { return deptId; }
    public void setDeptId(Long deptId) { this.deptId = deptId; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
}
