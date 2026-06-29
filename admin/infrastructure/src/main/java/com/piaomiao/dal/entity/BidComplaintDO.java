package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.BidComplaintModel;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * 质疑投诉数据对象
 */
@TableName("bid_complaint")
public class BidComplaintDO {

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
     * 项目名称（冗余）
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 类型：1质疑 2投诉
     */
    @TableField("type")
    private Integer type;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 质疑/投诉内容
     */
    @TableField("content")
    private String content;

    /**
     * 状态：0待处理 1处理中 2已回复 3已关闭 4已升级
     */
    @TableField("status")
    private Integer status;

    /**
     * 回复内容
     */
    @TableField("reply_content")
    private String replyContent;

    /**
     * 提交人ID
     */
    @TableField("submitter_id")
    private Long submitterId;

    /**
     * 提交人姓名
     */
    @TableField("submitter_name")
    private String submitterName;

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
    public BidComplaintModel toModel() {
        BidComplaintModel model = new BidComplaintModel();
        BeanUtils.copyProperties(this, model);
        return model;
    }

    /**
     * Model转DO
     * @param model 领域模型
     * @return 数据对象
     */
    public static BidComplaintDO fromModel(BidComplaintModel model) {
        BidComplaintDO entity = new BidComplaintDO();
        BeanUtils.copyProperties(model, entity);
        return entity;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    public Integer getType() { return type; }
    public void setType(Integer type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getReplyContent() { return replyContent; }
    public void setReplyContent(String replyContent) { this.replyContent = replyContent; }
    public Long getSubmitterId() { return submitterId; }
    public void setSubmitterId(Long submitterId) { this.submitterId = submitterId; }
    public String getSubmitterName() { return submitterName; }
    public void setSubmitterName(String submitterName) { this.submitterName = submitterName; }
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
