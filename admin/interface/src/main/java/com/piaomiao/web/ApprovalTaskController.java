package com.piaomiao.web;

import com.piaomiao.command.ApprovalTaskCommand;
import com.piaomiao.model.ApprovalTaskModel;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.ApprovalTaskQueryVO;
import com.piaomiao.web.vo.ApprovalTaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审批任务控制器
 *
 * @author system
 * @date 2026-06-14
 */
@RestController
@RequestMapping("/approval-task")
public class ApprovalTaskController {

    @Autowired
    private ApprovalTaskCommand approvalTaskCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 根据ID查询审批任务
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:approval-task:view')")
    public Response<ApprovalTaskModel> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<ApprovalTaskModel>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("审批任务ID不能为空");
                }
            }

            @Override
            public ApprovalTaskModel execute() {
                return approvalTaskCommand.findById(id);
            }
        });
    }

    /**
     * 根据实例ID查询审批任务列表
     */
    @GetMapping("/instance/{instanceId}")
    @PreAuthorize("hasAuthority('system:approval-task:view')")
    public Response<List<ApprovalTaskModel>> findByInstanceId(@PathVariable Long instanceId) {
        return templateRest.request(new CallbackRest<List<ApprovalTaskModel>>() {
            @Override
            public void check() {
                if (instanceId == null) {
                    throw new IllegalArgumentException("实例ID不能为空");
                }
            }

            @Override
            public List<ApprovalTaskModel> execute() {
                return approvalTaskCommand.findByInstanceId(instanceId);
            }
        });
    }

    /**
     * 查询待审批任务
     */
    @GetMapping("/pending/{assigneeId}")
    @PreAuthorize("hasAuthority('system:approval-task:view')")
    public Response<List<ApprovalTaskModel>> findPendingByAssigneeId(@PathVariable Long assigneeId) {
        return templateRest.request(new CallbackRest<List<ApprovalTaskModel>>() {
            @Override
            public void check() {
                if (assigneeId == null) {
                    throw new IllegalArgumentException("审批人ID不能为空");
                }
            }

            @Override
            public List<ApprovalTaskModel> execute() {
                return approvalTaskCommand.findPendingByAssigneeId(assigneeId);
            }
        });
    }

    /**
     * 分页查询审批任务
     */
    @PostMapping("/page")
    @PreAuthorize("hasAuthority('system:approval-task:list')")
    public Response<PageResult<ApprovalTaskModel>> findByPage(@RequestBody ApprovalTaskQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<ApprovalTaskModel>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<ApprovalTaskModel> execute() {
                return approvalTaskCommand.findByPage(ApprovalTaskQueryVO.toDTO(vo));
            }
        });
    }

    /**
     * 保存审批任务
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:approval-task:add')")
    public Response<Long> save(@RequestBody ApprovalTaskVO vo) {
        return templateRest.request(new CallbackRest<Long>() {
            @Override
            public void check() {
                if (vo == null) {
                    throw new IllegalArgumentException("审批任务信息不能为空");
                }
                if (vo.getInstanceId() == null) {
                    throw new IllegalArgumentException("关联实例ID不能为空");
                }
                if (vo.getNodeId() == null || vo.getNodeId().isEmpty()) {
                    throw new IllegalArgumentException("节点ID不能为空");
                }
            }

            @Override
            public Long execute() {
                return approvalTaskCommand.save(ApprovalTaskVO.toDTO(vo));
            }
        }, true);
    }

    /**
     * 更新审批任务
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('system:approval-task:edit')")
    public Response<Void> update(@RequestBody ApprovalTaskVO vo) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (vo == null) {
                    throw new IllegalArgumentException("审批任务信息不能为空");
                }
                if (vo.getId() == null) {
                    throw new IllegalArgumentException("审批任务ID不能为空");
                }
            }

            @Override
            public Void execute() {
                approvalTaskCommand.update(ApprovalTaskVO.toDTO(vo));
                return null;
            }
        }, true);
    }

    /**
     * 删除审批任务
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:approval-task:delete')")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("审批任务ID不能为空");
                }
            }

            @Override
            public Void execute() {
                approvalTaskCommand.delete(id);
                return null;
            }
        }, true);
    }
}
