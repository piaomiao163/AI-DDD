package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.BidEvaluationModel;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 评标记录数据对象
 */
@TableName("bid_evaluation")
public class BidEvaluationDO {

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
     * 关联投标书ID
     */
    @TableField("tender_id")
    private Long tenderId;

    /**
     * 评标专家ID
     */
    @TableField("expert_id")
    private Long expertId;

    /**
     * 总分
     */
    @TableField("total_score")
    private BigDecimal totalScore;

    /**
     * 综合评价
     */
    @TableField("comment")
    private String comment;

    /**
     * 评分明细（JSON）
     */
    @TableField("items")
    private String items;

    /**
     * 是否已提交：0否 1是
     */
    @TableField("submitted")
    private Integer submitted;

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
    public BidEvaluationModel toModel() {
        BidEvaluationModel model = new BidEvaluationModel();
        BeanUtils.copyProperties(this, model);
        model.setSubmitted(this.submitted != null && this.submitted == 1);
        return model;
    }

    /**
     * Model转DO
     * @param model 领域模型
     * @return 数据对象
     */
    public static BidEvaluationDO fromModel(BidEvaluationModel model) {
        BidEvaluationDO entity = new BidEvaluationDO();
        BeanUtils.copyProperties(model, entity);
        entity.setSubmitted(Boolean.TRUE.equals(model.getSubmitted()) ? 1 : 0);
        return entity;
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
    public Integer getSubmitted() { return submitted; }
    public void setSubmitted(Integer submitted) { this.submitted = submitted; }
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
