package com.piaomiao.web;

import com.piaomiao.command.ProcessInstanceCommand;
import com.piaomiao.dto.ProcessInstanceDTO;
import com.piaomiao.model.UserModel;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.service.UserService;
import com.piaomiao.util.UserContext;
import com.piaomiao.web.vo.MyProcessInstanceQueryVO;
import com.piaomiao.web.vo.ProcessInstanceQueryVO;
import com.piaomiao.web.vo.ProcessInstanceVO;
import com.piaomiao.web.vo.StartProcessVO;
import com.piaomiao.web.vo.TerminateProcessVO;
import com.piaomiao.web.vo.WithdrawProcessVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * 流程实例控制器
 */
@RestController
@RequestMapping("/process-instance")
public class ProcessInstanceController {

    @Autowired
    private ProcessInstanceCommand processInstanceCommand;

    @Autowired
    private TemplateRest templateRest;

    @Autowired
    private UserService userService;

    /**
     * 启动流程实例
     * @param vo 启动流程实例请求参数
     * @return 流程实例信息
     */
    @PostMapping("/start")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:instance:start')")
    public Response<ProcessInstanceVO> startProcess(@RequestBody StartProcessVO vo) {
        return templateRest.request(new CallbackRest<ProcessInstanceVO>() {
            @Override
            public void check() {
                if (vo.getProcessDefinitionKey() == null || vo.getProcessDefinitionKey().isEmpty()) {
                    throw new IllegalArgumentException("流程定义Key不能为空");
                }
                if (vo.getTitle() == null || vo.getTitle().isEmpty()) {
                    throw new IllegalArgumentException("审批标题不能为空");
                }
            }

            @Override
            public ProcessInstanceVO execute() {
                String username = UserContext.requireCurrentUsername();
                ProcessInstanceDTO dto = processInstanceCommand.startProcess(
                        vo.getProcessDefinitionKey(), vo.getBusinessKey(), username, vo.getVariables(),
                        vo.getTitle(), vo.getBusinessType(), vo.getBusinessId(), vo.getPriority());
                return convertToVO(dto);
            }
        }, true);
    }

    /**
     * 查询运行中的流程实例
     */
    @PostMapping("/running")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:instance:my')")
    public Response<PageResult<ProcessInstanceVO>> findRunning(@RequestBody MyProcessInstanceQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<ProcessInstanceVO>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<ProcessInstanceVO> execute() {
                Long userId = getCurrentUserDbId();
                PageResult<ProcessInstanceDTO> result = processInstanceCommand.findRunningByUser(userId, MyProcessInstanceQueryVO.toDTO(vo));
                return convertPageResult(result);
            }
        });
    }

    /**
     * 查询历史流程实例
     */
    @PostMapping("/history")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:instance:my')")
    public Response<PageResult<ProcessInstanceVO>> findHistory(@RequestBody MyProcessInstanceQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<ProcessInstanceVO>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<ProcessInstanceVO> execute() {
                Long userId = getCurrentUserDbId();
                PageResult<ProcessInstanceDTO> result = processInstanceCommand.findHistoryByUser(userId, MyProcessInstanceQueryVO.toDTO(vo));
                return convertPageResult(result);
            }
        });
    }

    /**
     * 分页查询流程实例
     */
    @PostMapping("/page")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:instance:list')")
    public Response<PageResult<ProcessInstanceVO>> findByPage(@RequestBody ProcessInstanceQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<ProcessInstanceVO>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<ProcessInstanceVO> execute() {
                PageResult<ProcessInstanceDTO> result = processInstanceCommand.findByPage(ProcessInstanceQueryVO.toDTO(vo));
                return convertPageResult(result);
            }
        });
    }
    /**
     * 根据ID查询流程实例
     * @param id 流程实例ID
     * @return 流程实例信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:instance:view')")
    public Response<ProcessInstanceVO> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<ProcessInstanceVO>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("流程实例ID不能为空");
                }
            }

            @Override
            public ProcessInstanceVO execute() {
                ProcessInstanceDTO dto = processInstanceCommand.findById(id);
                return convertToVO(dto);
            }
        });
    }

    /**
     * 终止流程实例
     * @param id 流程实例ID
     * @param vo 终止流程实例请求参数
     * @return void
     */
    @PostMapping("/{id}/terminate")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:instance:terminate')")
    public Response<Void> terminateProcess(@PathVariable Long id, @RequestBody TerminateProcessVO vo) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("流程实例ID不能为空");
                }
            }

            @Override
            public Void execute() {
                processInstanceCommand.terminateProcess(id, vo.getReason());
                return null;
            }
        }, true);
    }

    /**
     * 撤销流程实例
     * @param id 流程实例ID
     * @param vo 撤销流程实例请求参数
     * @return void
     */
    @PostMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('admin') or hasAuthority('process:instance:withdraw')")
    public Response<Void> withdrawProcess(@PathVariable Long id, @RequestBody WithdrawProcessVO vo) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("流程实例ID不能为空");
                }
            }

            @Override
            public Void execute() {
                Long currentUserId = getCurrentUserDbId();
                processInstanceCommand.withdrawProcess(id, vo.getReason(), currentUserId);
                return null;
            }
        }, true);
    }

    private ProcessInstanceVO convertToVO(ProcessInstanceDTO dto) {
        if (dto == null) return null;
        ProcessInstanceVO vo = new ProcessInstanceVO();
        BeanUtils.copyProperties(dto, vo);
        return vo;
    }

    private PageResult<ProcessInstanceVO> convertPageResult(PageResult<ProcessInstanceDTO> result) {
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::convertToVO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }

    /**
     * 获取当前登录用户的数据库ID
     */
    private Long getCurrentUserDbId() {
        String username = UserContext.requireCurrentUsername();
        UserModel userModel = userService.findByUsername(username);
        if (userModel == null) {
            throw new IllegalStateException("当前用户不存在");
        }
        return userModel.getId();
    }
}
