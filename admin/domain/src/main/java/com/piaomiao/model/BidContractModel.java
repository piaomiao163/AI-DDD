package com.piaomiao.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 合同领域模型
 */
public class BidContractModel {

    /** 状态：草稿 */
    public static final int STATUS_DRAFT = 0;
    /** 状态：待签署 */
    public static final int STATUS_PENDING_SIGN = 1;
    /** 状态：已签署 */
    public static final int STATUS_SIGNED = 2;
    /** 状态：履行中 */
    public static final int STATUS_EXECUTING = 3;
    /** 状态：已完结 */
    public static final int STATUS_COMPLETED = 4;
    /** 状态：已解除 */
    public static final int STATUS_TERMINATED = 5;

    /** 主键ID */
    private Long id;
    /** 合同名称 */
    private String contractName;
    /** 关联招标项目ID */
    private Long projectId;
    /** 项目名称（冗余） */
    private String projectName;
    /** 甲方 */
    private String partyA;
    /** 乙方 */
    private String partyB;
    /** 合同金额（万元） */
    private BigDecimal amount;
    /** 合同开始日期 */
    private LocalDate startDate;
    /** 合同结束日期 */
    private LocalDate endDate;
    /** 合同正文 */
    private String content;
    /** 状态 */
    private Integer status;
    /** 签署日期 */
    private LocalDate signDate;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 最后更新时间 */
    private LocalDateTime updateTime;
    /** 创建人 */
    private String createBy;
    /** 最后更新人 */
    private String updateBy;

    /**
     * 创建合同：设置为草稿并提交待签署
     */
    public void create() {
        if (this.contractName == null || this.contractName.isEmpty()) {
            throw new IllegalArgumentException("合同名称不能为空");
        }
        this.status = STATUS_PENDING_SIGN;
    }

    /**
     * 签署合同：待签署 → 已签署
     * @param signDate 签署日期
     */
    public void sign(LocalDate signDate) {
        if (this.status == null || this.status != STATUS_PENDING_SIGN) {
            throw new IllegalStateException("只有待签署状态的合同才能签署");
        }
        this.signDate = signDate;
        this.status = STATUS_SIGNED;
    }

    /**
     * 开始履行：已签署 → 履行中
     */
    public void execute() {
        if (this.status == null || this.status != STATUS_SIGNED) {
            throw new IllegalStateException("只有已签署状态的合同才能开始履行");
        }
        this.status = STATUS_EXECUTING;
    }

    /**
     * 完结合同：履行中 → 已完结
     */
    public void complete() {
        if (this.status == null || this.status != STATUS_EXECUTING) {
            throw new IllegalStateException("只有履行中状态的合同才能完结");
        }
        this.status = STATUS_COMPLETED;
    }

    /**
     * 解除合同
     */
    public void terminate() {
        if (this.status == null || this.status == STATUS_DRAFT
                || this.status == STATUS_COMPLETED || this.status == STATUS_TERMINATED) {
            throw new IllegalStateException("当前状态不允许解除合同");
        }
        this.status = STATUS_TERMINATED;
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
}
