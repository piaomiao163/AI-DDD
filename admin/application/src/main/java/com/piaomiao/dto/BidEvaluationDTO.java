package com.piaomiao.dto;

import java.math.BigDecimal;

/**
 * 评标记录DTO
 */
public class BidEvaluationDTO {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 关联招标项目ID
     */
    private Long projectId;
    /**
     * 关联投标书ID
     */
    private Long tenderId;
    /**
     * 评标专家ID
     */
    private Long expertId;
    /**
     * 总分
     */
    private BigDecimal totalScore;
    /**
     * 综合评价
     */
    private String comment;
    /**
     * 评分明细（JSON字符串）
     */
    private String items;
    /**
     * 是否已提交
     */
    private Boolean submitted;
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
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}
