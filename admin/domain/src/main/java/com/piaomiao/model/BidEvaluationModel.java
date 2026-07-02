package com.piaomiao.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 评标记录领域模型
 */
public class BidEvaluationModel {

    /** 主键ID */
    private Long id;
    /** 关联招标项目ID */
    private Long projectId;
    /** 关联投标书ID */
    private Long tenderId;
    /** 评标专家ID */
    private Long expertId;
    /** 总分 */
    private BigDecimal totalScore;
    /** 综合评价 */
    private String comment;
    /** 评分明细（JSON） */
    private String items;
    /** 是否已提交 */
    private Boolean submitted;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 最后更新时间 */
    private LocalDateTime updateTime;
    /** 创建人 */
    private String createBy;
    /** 最后更新人 */
    private String updateBy;

    /**
     * 保存草稿
     */
    public void saveDraft() {
        if (this.projectId == null) {
            throw new IllegalArgumentException("项目ID不能为空");
        }
        if (this.tenderId == null) {
            throw new IllegalArgumentException("投标书ID不能为空");
        }
        if (this.submitted == null) {
            this.submitted = false;
        }
    }

    /**
     * 提交评分
     */
    public void submit() {
        if (Boolean.TRUE.equals(this.submitted)) {
            throw new IllegalStateException("评分已提交，不可重复提交");
        }
        if (this.totalScore == null) {
            throw new IllegalArgumentException("总分不能为空");
        }
        this.submitted = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }

    public Long getTenderId() { return tenderId; }
    public void setTenderId(Long tenderId) { this.tenderId = tenderId; }

    public Long getExpertId() { return expertId; }
    public void setExpertId(Long expertId) { this.expertId = expertId; }

    public BigDecimal getTotalScore() { return totalScore; }
    public void setTotalScore(BigDecimal totalScore) { this.totalScore = totalScore; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getItems() { return items; }
    public void setItems(String items) { this.items = items; }

    public Boolean getSubmitted() { return submitted; }
    public void setSubmitted(Boolean submitted) { this.submitted = submitted; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public String getCreateBy() { return createBy; }
    public void setCreateBy(String createBy) { this.createBy = createBy; }

    public String getUpdateBy() { return updateBy; }
    public void setUpdateBy(String updateBy) { this.updateBy = updateBy; }
}
