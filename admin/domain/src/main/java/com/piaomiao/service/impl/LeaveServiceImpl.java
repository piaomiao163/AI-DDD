package com.piaomiao.service.impl;

import com.piaomiao.event.BaseDomainEvent;
import com.piaomiao.event.DomainEventPublisher;
import com.piaomiao.model.LeaveApprovalListItem;
import com.piaomiao.model.LeaveModel;
import com.piaomiao.model.ProcessInstanceModel;
import com.piaomiao.model.TaskModel;
import com.piaomiao.query.LeavePageQuery;
import com.piaomiao.query.MyLeavePageQuery;
import com.piaomiao.repository.LeaveRepository;
import com.piaomiao.repository.ProcessInstanceRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.LeaveService;
import com.piaomiao.service.TaskAppService;
import com.piaomiao.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 请假单服务实现
 */
@Service
public class LeaveServiceImpl implements LeaveService {

    /**
     * 请假业务类型
     */
    private static final String BUSINESS_TYPE_LEAVE = "leave";

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private ProcessInstanceRepository processInstanceRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private TaskAppService taskAppService;

    @Override
    public LeaveModel apply(LeaveModel model) {
        model.submit();
        LeaveModel saved = leaveRepository.save(model);

        ProcessInstanceModel processInstance = processInstanceRepository.startProcess(
                saved.resolveProcessDefinitionKey(),
                String.valueOf(saved.getId()),
                saved.getCreateBy(),
                saved.getProcessVariables(),
                saved.getProcessTitle(),
                "leave",
                saved.getId(),
                0
        );

        saved.startApproving(
                processInstance.getId(),
                processInstance.getProcessDefinitionDbId(),
                processInstance.getCurrentAssignees(),
                processInstance.getCurrentNodeName(),
                processInstance.getCurrentNodeId()
        );
        leaveRepository.update(saved);

        for (BaseDomainEvent<?> event : saved.pullEvents()) {
            domainEventPublisher.publish(event);
        }
        return saved;
    }

    @Override
    public LeaveModel findById(Long id) {
        LeaveModel model = leaveRepository.findById(id);
        enrichCurrentNodeName(model);
        return model;
    }

    @Override
    public LeaveModel findByProcessInstanceId(Long processInstanceId) {
        LeaveModel model = leaveRepository.findByProcessInstanceId(processInstanceId);
        enrichCurrentNodeName(model);
        return model;
    }

    @Override
    public List<LeaveModel> findByIds(List<Long> ids) {
        return leaveRepository.findByIds(ids);
    }

    @Override
    public PageResult<LeaveModel> findByApplicantId(Long applicantId, MyLeavePageQuery query) {
        PageResult<LeaveModel> result = leaveRepository.findByApplicantId(applicantId, query);
        enrichCurrentNodeName(result.getList());
        return result;
    }

    @Override
    public PageResult<LeaveModel> findByPage(LeavePageQuery query) {
        PageResult<LeaveModel> result = leaveRepository.findByPage(query);
        enrichCurrentNodeName(result.getList());
        return result;
    }

    @Override
    public PageResult<LeaveApprovalListItem> findApprovalList(int page, int size) {
        String username = UserContext.requireCurrentUsername();
        PageResult<TaskModel> taskResult = taskAppService.findTodoTasksByBusinessType(username, BUSINESS_TYPE_LEAVE, page, size);
        if (taskResult.getList().isEmpty()) {
            return new PageResult<>(taskResult.getTotal(), new ArrayList<>(),
                    taskResult.getPageNum(), taskResult.getPageSize());
        }

        List<Long> leaveIds = taskResult.getList().stream()
                .map(TaskModel::getBusinessId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, LeaveModel> leaveMap = leaveRepository.findByIds(leaveIds).stream()
                .collect(Collectors.toMap(LeaveModel::getId, Function.identity(), (left, right) -> left));
        enrichCurrentNodeName(new ArrayList<>(leaveMap.values()));

        List<LeaveApprovalListItem> items = new ArrayList<>();
        for (TaskModel task : taskResult.getList()) {
            if (task.getBusinessId() == null) {
                continue;
            }
            LeaveModel leave = leaveMap.get(task.getBusinessId());
            if (leave == null) {
                continue;
            }
            items.add(new LeaveApprovalListItem(leave, task));
        }
        return new PageResult<>(taskResult.getTotal(), items, taskResult.getPageNum(), taskResult.getPageSize());
    }

    @Override
    public TaskModel findApprovalTaskById(String taskId) {
        TaskModel task = taskAppService.findById(taskId);
        if (task == null) {
            throw new IllegalArgumentException("任务不存在");
        }
        return task;
    }

    @Override
    public void withdraw(Long id, String reason, Long currentUserId) {
        LeaveModel model = leaveRepository.findById(id);
        if (model == null) {
            throw new IllegalArgumentException("请假单不存在");
        }

        model.withdraw(currentUserId, reason);
        if (model.getProcessInstanceId() != null) {
            processInstanceRepository.withdrawProcess(model.getProcessInstanceId(), reason, currentUserId);
            return;
        }
        leaveRepository.update(model);
    }

    private void enrichCurrentNodeName(LeaveModel model) {
        if (model == null || model.getProcessInstanceId() == null) {
            return;
        }
        ProcessInstanceModel processInstance = processInstanceRepository.findById(model.getProcessInstanceId());
        if (processInstance != null) {
            model.setCurrentNodeName(processInstance.getCurrentNodeName());
        }
    }

    private void enrichCurrentNodeName(List<LeaveModel> models) {
        if (models == null || models.isEmpty()) {
            return;
        }
        List<Long> processInstanceIds = models.stream()
                .map(LeaveModel::getProcessInstanceId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        if (processInstanceIds.isEmpty()) {
            return;
        }
        Map<Long, ProcessInstanceModel> processInstanceMap = processInstanceRepository.findByIds(processInstanceIds)
                .stream()
                .collect(Collectors.toMap(ProcessInstanceModel::getId, Function.identity(), (left, right) -> left));
        for (LeaveModel model : models) {
            if (model.getProcessInstanceId() == null) {
                continue;
            }
            ProcessInstanceModel processInstance = processInstanceMap.get(model.getProcessInstanceId());
            if (processInstance != null) {
                model.setCurrentNodeName(processInstance.getCurrentNodeName());
            }
        }
    }
}
