package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.LeaveModel;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 请假单数据对象
 */
@TableName("biz_leave")
public class LeaveDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;

    @TableField("leave_type")
    private Integer leaveType;

    @TableField("start_date")
    private LocalDate startDate;

    @TableField("end_date")
    private LocalDate endDate;

    private BigDecimal days;

    private String reason;

    @TableField("applicant_id")
    private Long applicantId;

    @TableField("applicant_name")
    private String applicantName;

    @TableField("dept_name")
    private String deptName;

    private Integer status;

    @TableField("process_instance_id")
    private Long processInstanceId;

    @TableField("approve_comment")
    private String approveComment;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String createBy;

    private String updateBy;

    @TableLogic
    private Integer deleted;

    public LeaveModel toModel() {
        LeaveModel model = new LeaveModel();
        BeanUtils.copyProperties(this, model);
        return model;
    }

    public static LeaveDO fromModel(LeaveModel model) {
        LeaveDO leaveDO = new LeaveDO();
        BeanUtils.copyProperties(model, leaveDO);
        return leaveDO;
    }

    // ==================== Getter/Setter ====================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getLeaveType() { return leaveType; }
    public void setLeaveType(Integer leaveType) { this.leaveType = leaveType; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public BigDecimal getDays() { return days; }
    public void setDays(BigDecimal days) { this.days = days; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public Long getApplicantId() { return applicantId; }
    public void setApplicantId(Long applicantId) { this.applicantId = applicantId; }

    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public Long getProcessInstanceId() { return processInstanceId; }
    public void setProcessInstanceId(Long processInstanceId) { this.processInstanceId = processInstanceId; }

    public String getApproveComment() { return approveComment; }
    public void setApproveComment(String approveComment) { this.approveComment = approveComment; }

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
