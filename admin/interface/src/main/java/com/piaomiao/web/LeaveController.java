package com.piaomiao.web;

import com.piaomiao.command.LeaveCommand;
import com.piaomiao.dto.LeaveApprovalDetailDTO;
import com.piaomiao.dto.LeaveApprovalItemDTO;
import com.piaomiao.dto.LeaveDTO;
import com.piaomiao.model.sys.UserModel;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.service.sys.UserService;
import com.piaomiao.util.UserContext;
import com.piaomiao.web.vo.ApplyLeaveVO;
import com.piaomiao.web.vo.LeaveApprovalDetailVO;
import com.piaomiao.web.vo.LeaveApprovalItemVO;
import com.piaomiao.web.vo.LeaveQueryVO;
import com.piaomiao.web.vo.LeaveVO;
import com.piaomiao.web.vo.MyLeaveQueryVO;
import com.piaomiao.web.vo.WithdrawProcessVO;
import com.piaomiao.base.BasePageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * 请假单控制器
 */
@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveCommand leaveCommand;

    @Autowired
    private TemplateRest templateRest;

    @Autowired
    private UserService userService;

    /**
     * 申请请假
     */
    @PostMapping("/apply")
    @PreAuthorize("hasRole('admin') or hasAuthority('leave:apply')")
    public Response<LeaveVO> apply(@RequestBody ApplyLeaveVO vo) {
        return templateRest.request(new CallbackRest<LeaveVO>() {
            @Override
            public void check() {
                // 业务校验由 LeaveModel.submit() 承担
            }

            @Override
            public LeaveVO execute() {
                String username = UserContext.requireCurrentUsername();
                UserModel userModel = userService.findByUsername(username);
                Long userId = userModel != null ? userModel.getId() : null;

                LeaveDTO dto = new LeaveDTO();
                BeanUtils.copyProperties(vo, dto);
                dto.setApplicantId(userId);
                dto.setApplicantName(userModel != null ? userModel.getNickname() : username);
                if (userModel != null && userModel.getDepartment() != null) {
                    dto.setDeptName(userModel.getDepartment().getName());
                }
                dto.setCreateBy(username);

                LeaveDTO result = leaveCommand.apply(dto);
                return convertToVO(result);
            }
        }, true);
    }

    /**
     * 我的请假列表
     */
    @PostMapping("/myList")
    @PreAuthorize("hasRole('admin') or hasAuthority('leave:my:list')")
    public Response<PageResult<LeaveVO>> myList(@RequestBody MyLeaveQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<LeaveVO>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<LeaveVO> execute() {
                Long userId = getCurrentUserDbId();
                PageResult<LeaveDTO> result = leaveCommand.findByApplicantId(userId, MyLeaveQueryVO.toDTO(vo));
                return convertPageResult(result);
            }
        });
    }

    /**
     * 管理端分页查询
     */
    @PostMapping("/page")
    @PreAuthorize("hasRole('admin') or hasAuthority('leave:list')")
    public Response<PageResult<LeaveVO>> page(@RequestBody LeaveQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<LeaveVO>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<LeaveVO> execute() {
                PageResult<LeaveDTO> result = leaveCommand.findByPage(LeaveQueryVO.toDTO(vo));
                return convertPageResult(result);
            }
        });
    }

    /**
     * 请假详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasAuthority('leave:view')")
    public Response<LeaveVO> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<LeaveVO>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("请假单ID不能为空");
                }
            }

            @Override
            public LeaveVO execute() {
                LeaveDTO dto = leaveCommand.findById(id);
                return convertToVO(dto);
            }
        });
    }

    /**
     * 撤回请假
     */
    @PostMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('admin') or hasAuthority('leave:withdraw')")
    public Response<Void> withdraw(@PathVariable Long id, @RequestBody WithdrawProcessVO vo) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("请假单ID不能为空");
                }
            }

            @Override
            public Void execute() {
                Long currentUserId = getCurrentUserDbId();
                leaveCommand.withdraw(id, vo.getReason(), currentUserId);
                return null;
            }
        }, true);
    }

    /**
     * 请假审批列表（当前用户的待审批请假任务）
     */
    @PostMapping("/approval/list")
    @PreAuthorize("hasRole('admin') or hasAuthority('leave:approval:list')")
    public Response<PageResult<LeaveApprovalItemVO>> approvalList(@RequestBody BasePageVO vo) {
        return templateRest.request(new CallbackRest<PageResult<LeaveApprovalItemVO>>() {
            @Override
            public void check() {}

            @Override
            public PageResult<LeaveApprovalItemVO> execute() {
                PageResult<LeaveApprovalItemDTO> result = leaveCommand.findApprovalList(vo.getPageNum(), vo.getPageSize());
                return convertApprovalPageResult(result);
            }
        });
    }

    /**
     * 根据任务ID获取请假审批详情（供抽屉组件使用）
     */
    @GetMapping("/approval/detail/by-task/{taskId}")
    @PreAuthorize("hasRole('admin') or hasAuthority('leave:approval:view')")
    public Response<LeaveApprovalDetailVO> approvalDetailByTaskId(@PathVariable String taskId) {
        return templateRest.request(new CallbackRest<LeaveApprovalDetailVO>() {
            @Override
            public void check() {
                if (taskId == null || taskId.isEmpty()) {
                    throw new IllegalArgumentException("任务ID不能为空");
                }
            }

            @Override
            public LeaveApprovalDetailVO execute() {
                LeaveApprovalDetailDTO dto = leaveCommand.findApprovalDetailByTaskId(taskId);
                return convertApprovalDetailToVO(dto);
            }
        });
    }

    private LeaveApprovalItemVO convertApprovalItemToVO(LeaveApprovalItemDTO dto) {
        if (dto == null) return null;
        LeaveApprovalItemVO vo = new LeaveApprovalItemVO();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private LeaveApprovalDetailVO convertApprovalDetailToVO(LeaveApprovalDetailDTO dto) {
        if (dto == null) return null;
        LeaveApprovalDetailVO vo = new LeaveApprovalDetailVO();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private PageResult<LeaveApprovalItemVO> convertApprovalPageResult(PageResult<LeaveApprovalItemDTO> result) {
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::convertApprovalItemToVO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }

    private LeaveVO convertToVO(LeaveDTO dto) {
        if (dto == null) return null;
        LeaveVO vo = new LeaveVO();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private PageResult<LeaveVO> convertPageResult(PageResult<LeaveDTO> result) {
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::convertToVO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }

    private Long getCurrentUserDbId() {
        String username = UserContext.requireCurrentUsername();
        UserModel userModel = userService.findByUsername(username);
        if (userModel == null) {
            throw new IllegalStateException("当前用户不存在");
        }
        return userModel.getId();
    }
}
