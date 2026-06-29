package com.piaomiao.model;

import java.time.LocalDateTime;

/**
 * 质疑投诉领域模型
 */
public class BidComplaintModel {

    /** 状态：待处理 */
    public static final int STATUS_PENDING = 0;
    /** 状态：处理中 */
    public static final int STATUS_PROCESSING = 1;
    /** 状态：已回复 */
    public static final int STATUS_REPLIED = 2;
    /** 状态：已关闭 */
    public static final int STATUS_CLOSED = 3;
    /** 状态：已升级 */
    public static final int STATUS_ESCALATED = 4;

    /** 类型：质疑 */
    public static final int TYPE_QUESTION = 1;
    /** 类型：投诉 */
    public static final int TYPE_COMPLAINT = 2;

    /** 主键ID */
    private Long id;
    /** 关联招标项目ID */
    private Long projectId;
    /** 项目名称（冗余） */
    private String projectName;
    /** 类型：1质疑 2投诉 */
    private Integer type;
    /** 标题 */
    private String title;
    /** 质疑/投诉内容 */
    private String content;
    /** 状态 */
    private Integer status;
    /** 回复内容 */
    private String replyContent;
    /** 提交人ID */
    private Long submitterId;
    /** 提交人姓名 */
    private String submitterName;
    /** 创建时间 */
    private LocalDateTime createTime;
    /** 最后更新时间 */
    private LocalDateTime updateTime;
    /** 创建人 */
    private String createBy;
    /** 最后更新人 */
    private String updateBy;

    /**
     * 提交质疑/投诉
     */
    public void submit() {
        if (this.title == null || this.title.isEmpty()) {
            throw new IllegalArgumentException("标题不能为空");
        }
        if (this.content == null || this.content.isEmpty()) {
            throw new IllegalArgumentException("内容不能为空");
        }
        if (this.projectId == null) {
            throw new IllegalArgumentException("项目ID不能为空");
        }
        this.status = STATUS_PENDING;
    }

    /**
     * 回复质疑
     * @param replyContent 回复内容
     */
    public void reply(String replyContent) {
        if (this.status == null || (this.status != STATUS_PENDING && this.status != STATUS_PROCESSING)) {
            throw new IllegalStateException("只有待处理或处理中的质疑才能回复");
        }
        if (replyContent == null || replyContent.isEmpty()) {
            throw new IllegalArgumentException("回复内容不能为空");
        }
        this.replyContent = replyContent;
        this.status = STATUS_REPLIED;
    }

    /**
     * 关闭质疑
     */
    public void close() {
        if (this.status != null && this.status == STATUS_CLOSED) {
            throw new IllegalStateException("该质疑已关闭");
        }
        this.status = STATUS_CLOSED;
    }

    /**
     * 升级为投诉：质疑 → 投诉
     */
    public void escalate() {
        if (this.type == null || this.type != TYPE_QUESTION) {
            throw new IllegalStateException("只有质疑类型才能升级为投诉");
        }
        this.type = TYPE_COMPLAINT;
        this.status = STATUS_ESCALATED;
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
}
