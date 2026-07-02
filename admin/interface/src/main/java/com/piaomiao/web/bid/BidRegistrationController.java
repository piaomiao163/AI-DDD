package com.piaomiao.web.bid;

import com.piaomiao.command.BidRegistrationCommand;
import com.piaomiao.dto.BidRegistrationDTO;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.util.UserContext;
import com.piaomiao.web.vo.bid.BidRegistrationQueryVO;
import com.piaomiao.web.vo.bid.BidRegistrationRejectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 供应商报名控制器
 */
@RestController
@RequestMapping("/bid/registration")
public class BidRegistrationController {

    @Autowired
    private BidRegistrationCommand bidRegistrationCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 分页查询报名
     * @param vo 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<BidRegistrationDTO>> page(@RequestBody BidRegistrationQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<BidRegistrationDTO>>() {
            @Override
            public void check() {}

            @Override
            public PageResult<BidRegistrationDTO> execute() {
                return bidRegistrationCommand.findByPage(BidRegistrationQueryVO.toQuery(vo));
            }
        });
    }

    /**
     * 根据项目ID查询报名列表
     * @param projectId 项目ID
     * @return 报名列表
     */
    @GetMapping("/project/{projectId}")
    public Response<List<BidRegistrationDTO>> getByProject(@PathVariable Long projectId) {
        return templateRest.request(new CallbackRest<List<BidRegistrationDTO>>() {
            @Override
            public void check() {
                if (projectId == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public List<BidRegistrationDTO> execute() {
                return bidRegistrationCommand.findByProjectId(projectId);
            }
        });
    }

    /**
     * 查询我在指定项目的报名
     * @param projectId 项目ID
     * @return 报名信息
     */
    @GetMapping("/my")
    public Response<BidRegistrationDTO> getMyRegistration(@RequestParam(required = false) Long projectId) {
        return templateRest.request(new CallbackRest<BidRegistrationDTO>() {
            @Override
            public void check() {}

            @Override
            public BidRegistrationDTO execute() {
                if (projectId == null) return null;
                Long userId = UserContext.getCurrentUserId();
                return bidRegistrationCommand.findMyRegistration(projectId, userId);
            }
        });
    }

    /**
     * 提交报名
     * @param dto 报名信息
     * @return 保存后的报名信息
     */
    @PostMapping("/save")
    public Response<BidRegistrationDTO> save(@RequestBody BidRegistrationDTO dto) {
        return templateRest.request(new CallbackRest<BidRegistrationDTO>() {
            @Override
            public void check() {
                if (dto.getProjectId() == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public BidRegistrationDTO execute() {
                return bidRegistrationCommand.save(dto);
            }
        }, true);
    }

    /**
     * 审核通过
     * @param id 报名ID
     * @return 更新后的报名信息
     */
    @PostMapping("/approve/{id}")
    public Response<BidRegistrationDTO> approve(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidRegistrationDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("报名ID不能为空");
            }

            @Override
            public BidRegistrationDTO execute() {
                return bidRegistrationCommand.approve(id);
            }
        }, true);
    }

    /**
     * 审核拒绝
     * @param id 报名ID
     * @param vo 拒绝信息
     * @return 更新后的报名信息
     */
    @PostMapping("/reject/{id}")
    public Response<BidRegistrationDTO> reject(@PathVariable Long id,
                                                @RequestBody(required = false) BidRegistrationRejectVO vo) {
        return templateRest.request(new CallbackRest<BidRegistrationDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("报名ID不能为空");
            }

            @Override
            public BidRegistrationDTO execute() {
                String reason = vo != null ? vo.getReason() : null;
                return bidRegistrationCommand.reject(id, reason);
            }
        }, true);
    }
}
