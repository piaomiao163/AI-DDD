package com.piaomiao.web.bid;

import com.piaomiao.command.BidComplaintCommand;
import com.piaomiao.dto.BidComplaintDTO;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.bid.BidComplaintQueryVO;
import com.piaomiao.web.vo.bid.BidComplaintReplyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * 质疑投诉控制器
 */
@RestController
@RequestMapping("/bid/complaint")
public class BidComplaintController {

    @Autowired
    private BidComplaintCommand bidComplaintCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 分页查询质疑投诉
     * @param vo 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<BidComplaintDTO>> page(@RequestBody(required = false) BidComplaintQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<BidComplaintDTO>>() {
            @Override
            public void check() {}

            @Override
            public PageResult<BidComplaintDTO> execute() {
                return bidComplaintCommand.findByPage(BidComplaintQueryVO.toQuery(vo));
            }
        });
    }

    /**
     * 查询我的质疑投诉列表
     * @return 质疑投诉列表
     */
    @GetMapping("/my")
    public Response<List<BidComplaintDTO>> getMyComplaints() {
        return templateRest.request(new CallbackRest<List<BidComplaintDTO>>() {
            @Override
            public void check() {}

            @Override
            public List<BidComplaintDTO> execute() {
                return Collections.emptyList();
            }
        });
    }

    /**
     * 提交质疑投诉
     * @param dto 质疑投诉信息
     * @return 保存后的质疑投诉信息
     */
    @PostMapping("/save")
    public Response<BidComplaintDTO> save(@RequestBody BidComplaintDTO dto) {
        return templateRest.request(new CallbackRest<BidComplaintDTO>() {
            @Override
            public void check() {
                if (dto.getTitle() == null || dto.getTitle().isEmpty()) {
                    throw new IllegalArgumentException("标题不能为空");
                }
            }

            @Override
            public BidComplaintDTO execute() {
                return bidComplaintCommand.save(dto);
            }
        }, true);
    }

    /**
     * 回复质疑
     * @param id 质疑ID
     * @param vo 回复信息
     * @return 更新后的质疑投诉信息
     */
    @PostMapping("/reply/{id}")
    public Response<BidComplaintDTO> reply(@PathVariable Long id,
                                            @RequestBody(required = false) BidComplaintReplyVO vo) {
        return templateRest.request(new CallbackRest<BidComplaintDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("质疑ID不能为空");
            }

            @Override
            public BidComplaintDTO execute() {
                String content = vo != null ? vo.getContent() : null;
                return bidComplaintCommand.reply(id, content);
            }
        }, true);
    }

    /**
     * 关闭质疑
     * @param id 质疑ID
     * @return 更新后的质疑投诉信息
     */
    @PostMapping("/close/{id}")
    public Response<BidComplaintDTO> close(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidComplaintDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("质疑ID不能为空");
            }

            @Override
            public BidComplaintDTO execute() {
                return bidComplaintCommand.close(id);
            }
        }, true);
    }

    /**
     * 升级为投诉
     * @param id 质疑ID
     * @return 更新后的质疑投诉信息
     */
    @PostMapping("/escalate/{id}")
    public Response<BidComplaintDTO> escalate(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidComplaintDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("质疑ID不能为空");
            }

            @Override
            public BidComplaintDTO execute() {
                return bidComplaintCommand.escalate(id);
            }
        }, true);
    }
}
