package com.piaomiao.command.impl;

import com.piaomiao.command.LeaveCommand;
import com.piaomiao.dto.LeaveApprovalDetailDTO;
import com.piaomiao.dto.LeaveApprovalItemDTO;
import com.piaomiao.dto.LeaveDTO;
import com.piaomiao.dto.LeaveQueryDTO;
import com.piaomiao.dto.MyLeaveQueryDTO;
import com.piaomiao.model.LeaveApprovalListItem;
import com.piaomiao.model.LeaveModel;
import com.piaomiao.model.TaskModel;
import com.piaomiao.query.LeavePageQuery;
import com.piaomiao.query.MyLeavePageQuery;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.LeaveService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * 请假单命令实现
 */
@Service
public class LeaveCommandImpl implements LeaveCommand {

    @Autowired
    private LeaveService leaveService;

    @Override
    public LeaveDTO apply(LeaveDTO dto) {
        LeaveModel model = convertToModel(dto);
        LeaveModel result = leaveService.apply(model);
        return convertToDTO(result);
    }

    @Override
    public LeaveDTO findById(Long id) {
        LeaveModel model = leaveService.findById(id);
        return convertToDTO(model);
    }

    @Override
    public PageResult<LeaveApprovalItemDTO> findApprovalList(int page, int size) {
        PageResult<LeaveApprovalListItem> result = leaveService.findApprovalList(page, size);
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::toApprovalItemDTO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }

    @Override
    public LeaveApprovalDetailDTO findApprovalDetailByTaskId(String taskId) {
        TaskModel task = leaveService.findApprovalTaskById(taskId);
        LeaveModel leave = task.getBusinessId() != null ? leaveService.findById(task.getBusinessId()) : null;
        if (leave == null) {
            throw new IllegalArgumentException("请假单不存在");
        }
        return toApprovalDetailDTO(leave, task);
    }

    @Override
    public PageResult<LeaveDTO> findByApplicantId(Long applicantId, MyLeaveQueryDTO queryDTO) {
        MyLeavePageQuery query = new MyLeavePageQuery(
                queryDTO.getPageNum(), queryDTO.getPageSize(),
                queryDTO.getSortField(), queryDTO.getSortOrder());
        PageResult<LeaveModel> result = leaveService.findByApplicantId(applicantId, query);
        return convertPageResult(result);
    }

    @Override
    public PageResult<LeaveDTO> findByPage(LeaveQueryDTO queryDTO) {
        LeavePageQuery query = new LeavePageQuery(
                queryDTO.getPageNum(), queryDTO.getPageSize(),
                queryDTO.getSortField(), queryDTO.getSortOrder(),
                queryDTO.getTitle(), queryDTO.getLeaveType(), queryDTO.getStatus());
        PageResult<LeaveModel> result = leaveService.findByPage(query);
        return convertPageResult(result);
    }

    @Override
    public void withdraw(Long id, String reason, Long currentUserId) {
        leaveService.withdraw(id, reason, currentUserId);
    }

    private LeaveApprovalItemDTO toApprovalItemDTO(LeaveApprovalListItem item) {
        LeaveModel leave = item.getLeave();
        TaskModel task = item.getTask();
        LeaveApprovalItemDTO dto = new LeaveApprovalItemDTO();
        dto.setLeaveId(leave.getId());
        dto.setTitle(leave.getTitle());
        dto.setLeaveType(leave.getLeaveType());
        dto.setStartDate(formatDate(leave.getStartDate()));
        dto.setEndDate(formatDate(leave.getEndDate()));
        dto.setDays(leave.getDays());
        dto.setReason(leave.getReason());
        dto.setApplicantName(leave.getApplicantName());
        dto.setDeptName(leave.getDeptName());
        dto.setStatus(leave.getStatus());
        dto.setCurrentNodeName(task.getTaskName());
        dto.setProcessInstanceId(leave.getProcessInstanceId());
        dto.setLeaveCreateTime(formatDateTime(leave.getCreateTime()));
        dto.setTaskId(task.getTaskId());
        dto.setTaskName(task.getTaskName());
        dto.setTaskCreateTime(formatDateTime(task.getCreateTime()));
        return dto;
    }

    private LeaveApprovalDetailDTO toApprovalDetailDTO(LeaveModel leave, TaskModel task) {
        LeaveApprovalDetailDTO dto = new LeaveApprovalDetailDTO();
        dto.setLeaveId(leave.getId());
        dto.setTitle(leave.getTitle());
        dto.setLeaveType(leave.getLeaveType());
        dto.setStartDate(formatDate(leave.getStartDate()));
        dto.setEndDate(formatDate(leave.getEndDate()));
        dto.setDays(leave.getDays());
        dto.setReason(leave.getReason());
        dto.setApplicantName(leave.getApplicantName());
        dto.setDeptName(leave.getDeptName());
        dto.setStatus(leave.getStatus());
        dto.setCurrentNodeName(task.getTaskName());
        dto.setProcessInstanceId(leave.getProcessInstanceId());
        dto.setApproveComment(leave.getApproveComment());
        dto.setLeaveCreateTime(formatDateTime(leave.getCreateTime()));
        dto.setLeaveUpdateTime(formatDateTime(leave.getUpdateTime()));
        dto.setTaskId(task.getTaskId());
        dto.setTaskName(task.getTaskName());
        dto.setActProcessInstanceId(task.getProcessInstanceId());
        return dto;
    }

    private LeaveModel convertToModel(LeaveDTO dto) {
        if (dto == null) {
            return null;
        }
        LeaveModel model = new LeaveModel();
        BeanUtils.copyProperties(dto, model);
        if (dto.getStartDate() != null && !dto.getStartDate().isEmpty()) {
            model.setStartDate(LocalDate.parse(dto.getStartDate()));
        }
        if (dto.getEndDate() != null && !dto.getEndDate().isEmpty()) {
            model.setEndDate(LocalDate.parse(dto.getEndDate()));
        }
        return model;
    }

    private LeaveDTO convertToDTO(LeaveModel model) {
        if (model == null) {
            return null;
        }
        LeaveDTO dto = new LeaveDTO();
        BeanUtils.copyProperties(model, dto);
        if (model.getStartDate() != null) {
            dto.setStartDate(model.getStartDate().toString());
        }
        if (model.getEndDate() != null) {
            dto.setEndDate(model.getEndDate().toString());
        }
        if (model.getCreateTime() != null) {
            dto.setCreateTime(model.getCreateTime().toString());
        }
        if (model.getUpdateTime() != null) {
            dto.setUpdateTime(model.getUpdateTime().toString());
        }
        return dto;
    }

    private PageResult<LeaveDTO> convertPageResult(PageResult<LeaveModel> result) {
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::convertToDTO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.toString() : null;
    }

    private String formatDateTime(java.time.LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : null;
    }
}
