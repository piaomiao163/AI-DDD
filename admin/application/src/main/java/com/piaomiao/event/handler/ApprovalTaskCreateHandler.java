package com.piaomiao.event.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piaomiao.event.process.LeaveAppliedEvent;
import com.piaomiao.model.ApprovalTaskModel;
import com.piaomiao.model.sys.UserModel;
import com.piaomiao.repository.ApprovalTaskRepository;
import com.piaomiao.service.sys.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 审批任务创建处理器
 * <p>
 * 监听请假申请事件，在 biz_approval_task 表中创建审批任务记录。
 * 使用 BEFORE_COMMIT 保证与主事务强一致性。
 */
@Slf4j
@Component
public class ApprovalTaskCreateHandler {

    @Autowired
    private ApprovalTaskRepository approvalTaskRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleLeaveApplied(LeaveAppliedEvent event) {
        ApprovalTaskModel model = new ApprovalTaskModel();
        model.setInstanceId(event.getProcessInstanceId());
        model.setProcessDefinitionId(event.getProcessDefinitionId());
        model.setNodeId(event.getCurrentNodeId());
        model.setNodeName(event.getCurrentNodeName());
        model.setNodeType(ApprovalTaskModel.NODE_TYPE_APPROVE);
        model.setNodeIndex(1);
        model.setApproveMode(ApprovalTaskModel.APPROVE_MODE_SINGLE);
        model.setStatus(ApprovalTaskModel.STATUS_PENDING);
        model.setReceiveTime(LocalDateTime.now());
        model.setCandidateUsersJson(event.getCurrentAssignees());

        parseAndSetAssignee(model, event.getCurrentAssignees());

        Long id = approvalTaskRepository.save(model);
        log.info("请假审批任务已创建 - leaveId:{}, taskId:{}", event.getEntityId(), id);
    }

    /**
     * 从审批人JSON中解析审批人信息，设置assigneeId和assigneeName
     * JSON格式: [{"username":"alice"},{"username":"bob"}]
     * username字段存的是用户名，需要反查用户表获取ID和昵称
     */
    private void parseAndSetAssignee(ApprovalTaskModel model, String currentAssignees) {
        if (currentAssignees == null || currentAssignees.isEmpty()) {
            return;
        }
        try {
            List<Map<String, Object>> assigneeList = objectMapper.readValue(
                    currentAssignees, new TypeReference<List<Map<String, Object>>>() {});
            if (!assigneeList.isEmpty()) {
                Object usernameObj = assigneeList.get(0).get("username");
                if (usernameObj != null) {
                    String username = usernameObj.toString();
                    // 通过username查询用户ID和昵称
                    UserModel userModel = userService.findByUsername(username);
                    if (userModel != null) {
                        model.setAssigneeId(userModel.getId());
                        model.setAssigneeName(userModel.getUsername() != null ? userModel.getUsername() : username);
                    } else {
                        model.setAssigneeName(username);
                    }
                    model.setAssigneeType(ApprovalTaskModel.ASSIGNEE_TYPE_USER);
                }
            }
        } catch (Exception e) {
            log.warn("解析审批人JSON失败: {}", currentAssignees, e);
        }
    }
}
