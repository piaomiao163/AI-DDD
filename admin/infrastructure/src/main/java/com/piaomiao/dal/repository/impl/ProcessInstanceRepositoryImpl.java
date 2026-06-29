package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piaomiao.dal.entity.ProcessDefinitionDO;
import com.piaomiao.dal.entity.ProcessInstanceDO;
import com.piaomiao.dal.mapper.ProcessDefinitionMapper;
import com.piaomiao.dal.mapper.ProcessInstanceMapper;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.event.DomainEventPublisher;
import com.piaomiao.event.process.ProcessInstanceStartedEvent;
import com.piaomiao.event.process.ProcessInstanceTerminatedEvent;
import com.piaomiao.event.process.ProcessInstanceWithdrawnEvent;
import com.piaomiao.model.ProcessInstanceModel;
import com.piaomiao.model.sys.UserModel;
import com.piaomiao.repository.ProcessInstanceRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.sys.UserService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class ProcessInstanceRepositoryImpl implements ProcessInstanceRepository {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessDiagramGenerator processDiagramGenerator;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private ProcessInstanceMapper processInstanceMapper;

    @Autowired
    private ProcessDefinitionMapper processDefinitionMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Override
    public ProcessInstanceModel startProcess(String processDefinitionKey, String businessKey,
                                               String userId, Map<String, Object> variables,
                                               String title, String businessType, Long businessId,
                                               Integer priority) {
        // 1. 设置Activiti认证用户
        org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(userId);

        // 2. 调用Activiti启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);

        // 3. 构建Model
        ProcessInstanceModel model = new ProcessInstanceModel();
        model.setProcessInstanceId(instance.getId());
        model.setProcessDefinitionId(instance.getProcessDefinitionId());
        model.setProcessDefinitionKey(instance.getProcessDefinitionKey());
        model.setProcessName(instance.getProcessDefinitionName());
        model.setBusinessKey(instance.getBusinessKey());
        model.setStartUserId(userId);
        model.setStatus(0); // 运行中
        if (instance.getStartTime() != null) {
            model.setStartTime(toLocalDateTime(instance.getStartTime()));
        }

        // 4. 获取发起人信息
        UserModel userModel = userService.findByUsername(userId);
        if (userModel != null) {
            model.setStartUserDbId(userModel.getId());
            model.setStartUserName(userModel.getNickname());
            model.setApplicantDeptId(userModel.getDepartmentId());
        }

        // 5. 获取第一个用户任务信息
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(instance.getId())
                .list();
        if (!tasks.isEmpty()) {
            Task firstTask = tasks.get(0);
            model.setCurrentNodeName(firstTask.getName());
            model.setCurrentNodeId(firstTask.getTaskDefinitionKey());
            // 构建当前审批人列表JSON
            List<Map<String, Object>> assigneeList = new ArrayList<>();
            for (Task t : tasks) {
                Map<String, Object> assignee = new HashMap<>();
                assignee.put("username", t.getAssignee());
                assigneeList.add(assignee);
            }
            try {
                model.setCurrentAssignees(objectMapper.writeValueAsString(assigneeList));
            } catch (JsonProcessingException e) {
                // 忽略
            }
        }

        // 6. 计算总审批节点数（从BPMN模型中获取用户任务数量）
        org.activiti.bpmn.model.BpmnModel bpmnModel = repositoryService
                .getBpmnModel(instance.getProcessDefinitionId());
        if (bpmnModel != null) {
            int userTaskCount = bpmnModel.getMainProcess().findFlowElementsOfType(
                    org.activiti.bpmn.model.UserTask.class).size();
            model.setTotalNodes(userTaskCount);
            model.setCompletedNodes(0);
        }

        // 7. 设置业务字段
        model.setTitle(title != null ? title : instance.getProcessDefinitionName());
        model.setBusinessType(businessType);
        model.setBusinessId(businessId);
        model.setPriority(priority != null ? priority : 0);
        model.setVariables(variables);
        model.setVariablesJson(serializeVariables(variables));
        model.setStartTime(toLocalDateTime(instance.getStartTime()));

        // 8. 生成实例编号
        model.setInstanceNo(generateInstanceNo());

        // 9. 查找流程定义表ID
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<ProcessDefinitionDO> pdWrapper = new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        pdWrapper.eq("process_key", processDefinitionKey);
        ProcessDefinitionDO processDefinitionDO = processDefinitionMapper.selectOne(pdWrapper);
        model.setProcessDefinitionDbId(processDefinitionDO != null ? processDefinitionDO.getId() : null);

        // 10. 双写：插入自有表
        ProcessInstanceDO DO = ProcessInstanceDO.fromModel(model);
        AuditFieldUtil.fillForInsert(DO);
        processInstanceMapper.insert(DO);
        model.setId(DO.getId());

        // 11. 发布流程实例启动事件
        domainEventPublisher.publish(new ProcessInstanceStartedEvent(this, model));

        return model;
    }

    @Override
    public ProcessInstanceModel findById(Long id) {
        ProcessInstanceDO DO = processInstanceMapper.selectById(id);
        return DO != null ? DO.toModel() : null;
    }

    @Override
    public ProcessInstanceModel findByActProcessInstanceId(String actProcessInstanceId) {
        LambdaQueryWrapper<ProcessInstanceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessInstanceDO::getActProcessInstanceId, actProcessInstanceId);
        ProcessInstanceDO DO = processInstanceMapper.selectOne(wrapper);
        return DO != null ? DO.toModel() : null;
    }

    @Override
    public List<ProcessInstanceModel> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return processInstanceMapper.selectBatchIds(ids).stream()
                .map(ProcessInstanceDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<ProcessInstanceModel> findRunningByUser(Long startUserDbId, int page, int size) {
        LambdaQueryWrapper<ProcessInstanceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessInstanceDO::getStartUserId, startUserDbId)
               .eq(ProcessInstanceDO::getStatus, 0)
               .orderByDesc(ProcessInstanceDO::getStartTime);
        return queryByPage(wrapper, page, size);
    }

    @Override
    public PageResult<ProcessInstanceModel> findHistoryByUser(Long startUserDbId, int page, int size) {
        LambdaQueryWrapper<ProcessInstanceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessInstanceDO::getStartUserId, startUserDbId)
               .in(ProcessInstanceDO::getStatus, 1, 2, 3)
               .orderByDesc(ProcessInstanceDO::getEndTime);
        return queryByPage(wrapper, page, size);
    }

    @Override
    public PageResult<ProcessInstanceModel> findByPage(int page, int size, String title, Integer status, String businessType) {
        LambdaQueryWrapper<ProcessInstanceDO> wrapper = new LambdaQueryWrapper<>();
        if (title != null && !title.isEmpty()) {
            wrapper.like(ProcessInstanceDO::getTitle, title);
        }
        if (status != null) {
            wrapper.eq(ProcessInstanceDO::getStatus, status);
        }
        if (businessType != null && !businessType.isEmpty()) {
            wrapper.eq(ProcessInstanceDO::getBusinessType, businessType);
        }
        wrapper.orderByDesc(ProcessInstanceDO::getStartTime);
        return queryByPage(wrapper, page, size);
    }

    @Override
    public void terminateProcess(Long id, String reason) {
        // 1. 查自有表获取actProcessInstanceId
        ProcessInstanceDO DO = processInstanceMapper.selectById(id);
        if (DO == null) {
            throw new RuntimeException("流程实例不存在");
        }

        // 2. 调用Activiti删除实例
        ProcessInstanceModel model = DO.toModel();
        runtimeService.deleteProcessInstance(DO.getActProcessInstanceId(), reason);

        // 3. 更新自有表
        LambdaUpdateWrapper<ProcessInstanceDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProcessInstanceDO::getId, id)
                     .set(ProcessInstanceDO::getStatus, 2)
                     .set(ProcessInstanceDO::getEndTime, LocalDateTime.now())
                     .set(ProcessInstanceDO::getDeleteReason, reason)
                     .set(ProcessInstanceDO::getCurrentNodeName, null);
        processInstanceMapper.update(null, updateWrapper);

        // 4. 发布终止事件
        model.setStatus(2);
        model.setEndTime(LocalDateTime.now());
        model.setDeleteReason(reason);
        domainEventPublisher.publish(new ProcessInstanceTerminatedEvent(this, model, reason));
    }

    @Override
    public void withdrawProcess(Long id, String reason, Long currentUserId) {
        // 1. 查自有表
        ProcessInstanceDO DO = processInstanceMapper.selectById(id);
        if (DO == null) {
            throw new RuntimeException("流程实例不存在");
        }
        if (DO.getStatus() != 0) {
            throw new IllegalStateException("只有运行中的流程才能撤回");
        }
        if (!DO.getStartUserId().equals(currentUserId)) {
            throw new IllegalStateException("只有发起人才能撤回流程");
        }

        // 2. 校验任务未被审批（查询已完成的任务数）
        long completedTaskCount = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(DO.getActProcessInstanceId())
                .finished()
                .count();
        if (completedTaskCount > 0) {
            throw new IllegalStateException("流程已被审批，无法撤回");
        }

        // 3. 调用Activiti删除实例
        ProcessInstanceModel model = DO.toModel();
        String withdrawReason = reason != null ? "撤回: " + reason : "撤回";
        runtimeService.deleteProcessInstance(DO.getActProcessInstanceId(), withdrawReason);

        // 4. 更新自有表
        LambdaUpdateWrapper<ProcessInstanceDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ProcessInstanceDO::getId, id)
                     .set(ProcessInstanceDO::getStatus, 3)
                     .set(ProcessInstanceDO::getEndTime, LocalDateTime.now())
                     .set(ProcessInstanceDO::getDeleteReason, reason)
                     .set(ProcessInstanceDO::getCurrentNodeName, null);
        processInstanceMapper.update(null, updateWrapper);

        // 5. 发布撤回事件
        model.setStatus(3);
        model.setEndTime(LocalDateTime.now());
        model.setDeleteReason(reason);
        domainEventPublisher.publish(new ProcessInstanceWithdrawnEvent(this, model, reason));
    }

    @Override
    public ProcessInstanceModel save(ProcessInstanceModel model) {
        ProcessInstanceDO DO = ProcessInstanceDO.fromModel(model);
        AuditFieldUtil.fillForInsert(DO);
        processInstanceMapper.insert(DO);
        model.setId(DO.getId());
        return model;
    }

    @Override
    public ProcessInstanceModel update(ProcessInstanceModel model) {
        ProcessInstanceDO DO = ProcessInstanceDO.fromModel(model);
        AuditFieldUtil.fillForUpdate(DO);
        processInstanceMapper.updateById(DO);
        return model;
    }

    @Override
    public void updateStatusByActId(String actProcessInstanceId, Integer status,
                                     LocalDateTime endTime, String deleteReason) {
        LambdaUpdateWrapper<ProcessInstanceDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ProcessInstanceDO::getActProcessInstanceId, actProcessInstanceId)
               .set(ProcessInstanceDO::getStatus, status)
               .set(ProcessInstanceDO::getEndTime, endTime)
               .set(ProcessInstanceDO::getDeleteReason, deleteReason);
        if (status != 0) {
            wrapper.set(ProcessInstanceDO::getCurrentNodeName, null);
        }
        processInstanceMapper.update(null, wrapper);
    }

    @Override
    public void updateCurrentNodeInfo(String actProcessInstanceId, String currentNodeName,
                                       String currentNodeId, String currentAssignees, Integer completedNodes) {
        LambdaUpdateWrapper<ProcessInstanceDO> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(ProcessInstanceDO::getActProcessInstanceId, actProcessInstanceId)
               .set(ProcessInstanceDO::getCurrentNodeName, currentNodeName)
               .set(ProcessInstanceDO::getCurrentNodeId, currentNodeId)
               .set(ProcessInstanceDO::getCurrentAssignees, currentAssignees)
               .set(ProcessInstanceDO::getCompletedNodes, completedNodes);
        processInstanceMapper.update(null, wrapper);
    }

    @Override
    public InputStream generateDiagram(Long id) {
        // 先查自有表获取actProcessInstanceId
        ProcessInstanceDO DO = processInstanceMapper.selectById(id);
        if (DO == null) {
            throw new RuntimeException("流程实例不存在");
        }
        String actProcessInstanceId = DO.getActProcessInstanceId();

        String processDefinitionId = null;
        List<String> activeActivityIds = new ArrayList<>();

        // 尝试从运行中实例获取
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(actProcessInstanceId).singleResult();
        if (processInstance != null) {
            processDefinitionId = processInstance.getProcessDefinitionId();
            activeActivityIds = runtimeService.getActiveActivityIds(actProcessInstanceId);
        } else {
            // 从历史实例获取
            HistoricProcessInstance historicInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(actProcessInstanceId).singleResult();
            if (historicInstance != null) {
                processDefinitionId = historicInstance.getProcessDefinitionId();
            }
        }

        if (processDefinitionId == null) {
            throw new RuntimeException("流程实例不存在");
        }

        // 获取BPMN模型并生成带高亮的流程图（SVG格式）
        org.activiti.bpmn.model.BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        return processDiagramGenerator.generateDiagram(
                bpmnModel,
                activeActivityIds,
                Collections.<String>emptyList(),
                "宋体", "宋体", "宋体",
                false,
                "svg");
    }

    /**
     * 通用分页查询
     */
    private PageResult<ProcessInstanceModel> queryByPage(LambdaQueryWrapper<ProcessInstanceDO> wrapper, int page, int size) {
        Page<ProcessInstanceDO> pageParam = new Page<>(page, size);
        IPage<ProcessInstanceDO> result = processInstanceMapper.selectPage(pageParam, wrapper);
        List<ProcessInstanceModel> models = result.getRecords().stream()
                .map(ProcessInstanceDO::toModel)
                .collect(Collectors.toList());
        return new PageResult<>(result.getTotal(), models, page, size);
    }

    /**
     * 序列化变量为JSON
     */
    private String serializeVariables(Map<String, Object> variables) {
        if (variables == null || variables.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(variables);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private LocalDateTime toLocalDateTime(java.util.Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /** 实例编号自增序号(同一秒内) */
    private static final AtomicInteger SEQ = new AtomicInteger(0);

    /**
     * 生成实例编号，格式: PROC20260614001
     */
    private String generateInstanceNo() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int seq = SEQ.incrementAndGet() % 1000;
        return "PROC" + datePart + String.format("%03d", seq);
    }
}
