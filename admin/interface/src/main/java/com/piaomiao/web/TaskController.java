package com.piaomiao.web;

import com.piaomiao.command.TaskCommand;
import com.piaomiao.dto.TaskCompleteDTO;
import com.piaomiao.dto.TaskDTO;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.piaomiao.util.UserContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * 任务控制器
 */
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskCommand taskCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 查询待处理任务
     */
    @PostMapping("/todo")
    @PreAuthorize("hasRole('admin') or hasAuthority('task:todo')")
    public Response<PageResult<TaskVO>> findTodoTasks(@RequestBody TaskQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<TaskVO>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<TaskVO> execute() {
                String userId = getCurrentUserId();
                PageResult<TaskDTO> result = taskCommand.findTodoTasks(userId, TaskQueryVO.toDTO(vo));
                return convertPageResult(result);
            }
        });
    }

    /**
     * 查询已处理任务
     */
    @PostMapping("/done")
    @PreAuthorize("hasRole('admin') or hasAuthority('task:done')")
    public Response<PageResult<TaskVO>> findDoneTasks(@RequestBody TaskQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<TaskVO>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<TaskVO> execute() {
                String userId = getCurrentUserId();
                PageResult<TaskDTO> result = taskCommand.findDoneTasks(userId, TaskQueryVO.toDTO(vo));
                return convertPageResult(result);
            }
        });
    }

    /**
     * 查询可领取任务
     */
    @PostMapping("/claimable")
    @PreAuthorize("hasRole('admin') or hasAuthority('task:claimable')")
    public Response<PageResult<TaskVO>> findClaimableTasks(@RequestBody TaskQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<TaskVO>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<TaskVO> execute() {
                String userId = getCurrentUserId();
                PageResult<TaskDTO> result = taskCommand.findClaimableTasks(userId, TaskQueryVO.toDTO(vo));
                return convertPageResult(result);
            }
        });
    }

    /**
     * 根据ID查询任务
     * @param taskId 任务ID
     * @return Response<TaskVO>
     */
    @GetMapping("/{taskId}")
    @PreAuthorize("hasRole('admin') or hasAuthority('task:view')")
    public Response<TaskVO> findById(@PathVariable String taskId) {
        return templateRest.request(new CallbackRest<TaskVO>() {
            @Override
            public void check() {
                if (taskId == null || taskId.isEmpty()) {
                    throw new IllegalArgumentException("任务ID不能为空");
                }
            }

            @Override
            public TaskVO execute() {
                TaskDTO dto = taskCommand.findById(taskId);
                return convertToVO(dto);
            }
        });
    }

    /**
     * 领取任务
     * @param taskId 任务ID
     * @return Response<Void>
     */
    @PostMapping("/{taskId}/claim")
    @PreAuthorize("hasRole('admin') or hasAuthority('task:claim')")
    public Response<Void> claimTask(@PathVariable String taskId) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (taskId == null || taskId.isEmpty()) {
                    throw new IllegalArgumentException("任务ID不能为空");
                }
            }

            @Override
            public Void execute() {
                String userId = getCurrentUserId();
                taskCommand.claimTask(taskId, userId);
                return null;
            }
        }, true);
    }

    /**
     * 释放任务
     * @param taskId 任务ID
     * @return Response<Void>
     */
    @PostMapping("/{taskId}/unclaim")
    @PreAuthorize("hasRole('admin') or hasAuthority('task:unclaim')")
    public Response<Void> unclaimTask(@PathVariable String taskId) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (taskId == null || taskId.isEmpty()) {
                    throw new IllegalArgumentException("任务ID不能为空");
                }
            }

            @Override
            public Void execute() {
                taskCommand.unclaimTask(taskId);
                return null;
            }
        }, true);
    }

    /**
     * 完成任务
     * @param taskId 任务ID
     * @param vo 完成任务参数
     * @return Response<Void>
     */
    @PostMapping("/{taskId}/complete")
    @PreAuthorize("hasRole('admin') or hasAuthority('task:complete')")
    public Response<Void> completeTask(@PathVariable String taskId,
                                        @RequestBody(required = false) CompleteTaskVO vo) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (taskId == null || taskId.isEmpty()) {
                    throw new IllegalArgumentException("任务ID不能为空");
                }
            }

            @Override
            public Void execute() {
                TaskCompleteDTO dto = new TaskCompleteDTO();
                if (vo != null) {
                    BeanUtils.copyProperties(vo, dto);
                }
                if (dto.getOutcome() == null || dto.getOutcome().isEmpty()) {
                    dto.setOutcome("approved");
                }
                taskCommand.completeTask(taskId, dto);
                return null;
            }
        }, true);
    }

    /**
     * 委派任务
     * @param taskId 任务ID
     * @param vo 委派任务参数
     * @return Response<Void>
     */
    @PostMapping("/{taskId}/delegate")
    @PreAuthorize("hasRole('admin') or hasAuthority('task:delegate')")
    public Response<Void> delegateTask(@PathVariable String taskId,
                                        @RequestBody DelegateTaskVO vo) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (taskId == null || taskId.isEmpty()) {
                    throw new IllegalArgumentException("任务ID不能为空");
                }
                if (vo.getDelegateUserId() == null || vo.getDelegateUserId().isEmpty()) {
                    throw new IllegalArgumentException("委派用户ID不能为空");
                }
            }

            @Override
            public Void execute() {
                taskCommand.delegateTask(taskId, vo.getDelegateUserId());
                return null;
            }
        }, true);
    }

    /**
     * 拒绝任务
     * @param taskId 任务ID
     * @param vo 拒绝任务参数
     * @return Response<Void>
     */
    @PostMapping("/{taskId}/reject")
    @PreAuthorize("hasRole('admin') or hasAuthority('task:reject')")
    public Response<Void> rejectTask(@PathVariable String taskId,
                                      @RequestBody RejectTaskVO vo) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (taskId == null || taskId.isEmpty()) {
                    throw new IllegalArgumentException("任务ID不能为空");
                }
                if (vo.getProcessInstanceId() == null || vo.getProcessInstanceId().isEmpty()) {
                    throw new IllegalArgumentException("流程实例ID不能为空");
                }
                if (vo.getReason() == null || vo.getReason().isEmpty()) {
                    throw new IllegalArgumentException("驳回原因不能为空");
                }
            }

            @Override
            public Void execute() {
                taskCommand.rejectTask(taskId, vo.getProcessInstanceId(), vo.getReason());
                return null;
            }
        }, true);
    }

    /**
     * 转换任务DTO为VO
     * @param dto 任务DTO
     * @return TaskVO
     */
    private TaskVO convertToVO(TaskDTO dto) {
        if (dto == null) return null;
        TaskVO vo = new TaskVO();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    /**
     * 转换分页结果
     * @param result 分页结果
     * @return PageResult<TaskVO>
     */
    private PageResult<TaskVO> convertPageResult(PageResult<TaskDTO> result) {
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::convertToVO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }

    /**
     * 获取当前用户ID
     * @return 当前用户ID
     */
    private String getCurrentUserId() {
        return UserContext.requireCurrentUsername();
    }
}
