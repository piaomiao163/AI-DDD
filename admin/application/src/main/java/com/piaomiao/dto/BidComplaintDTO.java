package com.piaomiao.dto;

/**
 * 质疑投诉DTO
 */
public class BidComplaintDTO {

    /**
     * 主键ID
     */
    private Long id;
    /**
     * 关联招标项目ID
     */
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 类型：1质疑 2投诉
     */
    private Integer type;
    /**
     * 标题
     */
    private String title;
    /**
     * 质疑/投诉内容
     */
    private String content;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 回复内容
     */
    private String replyContent;
    /**
     * 提交人ID
     */
    private Long submitterId;
    /**
     * 提交人姓名
     */
    private String submitterName;
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
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }
}
