package com.piaomiao.web.bid;

import com.piaomiao.command.BidProjectCommand;
import com.piaomiao.dto.BidProjectDTO;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.bid.BidProjectAwardVO;
import com.piaomiao.web.vo.bid.BidProjectPublicizeVO;
import com.piaomiao.web.vo.bid.BidProjectQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 招标项目控制器
 */
@RestController
@RequestMapping("/bid/project")
public class BidProjectController {

    @Autowired
    private BidProjectCommand bidProjectCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 分页查询招标项目（管理端）
     */
    @PostMapping("/page")
    public Response<PageResult<BidProjectDTO>> page(@RequestBody BidProjectQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<BidProjectDTO>>() {
            @Override
            public void check() {}

            @Override
            public PageResult<BidProjectDTO> execute() {
                return bidProjectCommand.findByPage(BidProjectQueryVO.toQuery(vo));
            }
        });
    }

    /**
     * 招标大厅（公开列表）
     */
    @PostMapping("/hall")
    public Response<PageResult<BidProjectDTO>> hall(@RequestBody BidProjectQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<BidProjectDTO>>() {
            @Override
            public void check() {}

            @Override
            public PageResult<BidProjectDTO> execute() {
                return bidProjectCommand.findByPage(BidProjectQueryVO.toQuery(vo));
            }
        });
    }

    /**
     * 根据ID查询招标项目详情
     */
    @GetMapping("/{id}")
    public Response<BidProjectDTO> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidProjectDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public BidProjectDTO execute() {
                return bidProjectCommand.findById(id);
            }
        });
    }

    /**
     * 新建招标项目（草稿）
     */
    @PostMapping("/save")
    public Response<BidProjectDTO> save(@RequestBody BidProjectDTO dto) {
        return templateRest.request(new CallbackRest<BidProjectDTO>() {
            @Override
            public void check() {
                if (dto.getProjectName() == null || dto.getProjectName().isEmpty()) {
                    throw new IllegalArgumentException("项目名称不能为空");
                }
            }

            @Override
            public BidProjectDTO execute() {
                return bidProjectCommand.save(dto);
            }
        }, true);
    }

    /**
     * 更新招标项目
     */
    @PutMapping("/update")
    public Response<BidProjectDTO> update(@RequestBody BidProjectDTO dto) {
        return templateRest.request(new CallbackRest<BidProjectDTO>() {
            @Override
            public void check() {
                if (dto.getId() == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public BidProjectDTO execute() {
                return bidProjectCommand.update(dto);
            }
        }, true);
    }

    /**
     * 提交审核：草稿 → 待审核
     */
    @PostMapping("/submit/{id}")
    public Response<BidProjectDTO> submit(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidProjectDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public BidProjectDTO execute() {
                return bidProjectCommand.submit(id);
            }
        }, true);
    }

    /**
     * 发布项目：待审核 → 报名中
     */
    @PostMapping("/publish/{id}")
    public Response<BidProjectDTO> publish(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidProjectDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public BidProjectDTO execute() {
                return bidProjectCommand.publish(id);
            }
        }, true);
    }

    /**
     * 开标：报名中 → 开标中
     */
    @PostMapping("/open/{id}")
    public Response<BidProjectDTO> openBidding(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidProjectDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public BidProjectDTO execute() {
                return bidProjectCommand.openBidding(id);
            }
        }, true);
    }

    /**
     * 开始评标：开标中 → 评标中
     */
    @PostMapping("/evaluate/{id}")
    public Response<BidProjectDTO> startEvaluation(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidProjectDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public BidProjectDTO execute() {
                return bidProjectCommand.startEvaluation(id);
            }
        }, true);
    }

    /**
     * 发起公示：评标中 → 公示中
     */
    @PostMapping("/publicize/{id}")
    public Response<BidProjectDTO> publicize(@PathVariable Long id,
                                              @RequestBody(required = false) BidProjectPublicizeVO vo) {
        return templateRest.request(new CallbackRest<BidProjectDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public BidProjectDTO execute() {
                int days = (vo != null && vo.getDays() != null) ? vo.getDays() : 7;
                return bidProjectCommand.publicize(id, days);
            }
        }, true);
    }

    /**
     * 定标：公示中 → 已定标
     */
    @PostMapping("/award")
    public Response<BidProjectDTO> award(@RequestBody BidProjectAwardVO vo) {
        return templateRest.request(new CallbackRest<BidProjectDTO>() {
            @Override
            public void check() {
                if (vo.getProjectId() == null) throw new IllegalArgumentException("项目ID不能为空");
                if (vo.getWinnerTenderId() == null) throw new IllegalArgumentException("中标投标书ID不能为空");
            }

            @Override
            public BidProjectDTO execute() {
                return bidProjectCommand.award(vo.getProjectId(), vo.getWinnerTenderId());
            }
        }, true);
    }

    /**
     * 删除招标项目（仅草稿状态可删除）
     */
    @DeleteMapping("/delete/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public Void execute() {
                bidProjectCommand.deleteById(id);
                return null;
            }
        }, true);
    }
}
