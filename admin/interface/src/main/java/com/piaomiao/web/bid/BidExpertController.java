package com.piaomiao.web.bid;

import com.piaomiao.command.BidExpertCommand;
import com.piaomiao.dto.BidExpertDTO;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.bid.BidExpertBlacklistVO;
import com.piaomiao.web.vo.bid.BidExpertQueryVO;
import com.piaomiao.web.vo.bid.BidExpertSelectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评标专家控制器
 */
@RestController
@RequestMapping("/bid/expert")
public class BidExpertController {

    @Autowired
    private BidExpertCommand bidExpertCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 分页查询专家
     * @param vo 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<BidExpertDTO>> page(@RequestBody(required = false) BidExpertQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<BidExpertDTO>>() {
            @Override
            public void check() {}

            @Override
            public PageResult<BidExpertDTO> execute() {
                return bidExpertCommand.findByPage(BidExpertQueryVO.toQuery(vo));
            }
        });
    }

    /**
     * 根据ID查询专家
     * @param id 专家ID
     * @return 专家信息
     */
    @GetMapping("/{id}")
    public Response<BidExpertDTO> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidExpertDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("专家ID不能为空");
            }

            @Override
            public BidExpertDTO execute() {
                return bidExpertCommand.findById(id);
            }
        });
    }

    /**
     * 新增专家
     * @param dto 专家信息
     * @return 保存后的专家信息
     */
    @PostMapping("/save")
    public Response<BidExpertDTO> save(@RequestBody BidExpertDTO dto) {
        return templateRest.request(new CallbackRest<BidExpertDTO>() {
            @Override
            public void check() {
                if (dto.getName() == null || dto.getName().isEmpty()) {
                    throw new IllegalArgumentException("专家姓名不能为空");
                }
            }

            @Override
            public BidExpertDTO execute() {
                return bidExpertCommand.save(dto);
            }
        }, true);
    }

    /**
     * 更新专家
     * @param dto 专家信息
     * @return 更新后的专家信息
     */
    @PutMapping("/update")
    public Response<BidExpertDTO> update(@RequestBody BidExpertDTO dto) {
        return templateRest.request(new CallbackRest<BidExpertDTO>() {
            @Override
            public void check() {
                if (dto.getId() == null) throw new IllegalArgumentException("专家ID不能为空");
            }

            @Override
            public BidExpertDTO execute() {
                return bidExpertCommand.update(dto);
            }
        }, true);
    }

    /**
     * 删除专家
     * @param id 专家ID
     * @return 无
     */
    @DeleteMapping("/delete/{id}")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("专家ID不能为空");
            }

            @Override
            public Void execute() {
                bidExpertCommand.deleteById(id);
                return null;
            }
        }, true);
    }

    /**
     * 加入黑名单
     * @param id 专家ID
     * @param vo 黑名单信息
     * @return 更新后的专家信息
     */
    @PostMapping("/blacklist/{id}")
    public Response<BidExpertDTO> blacklist(@PathVariable Long id,
                                             @RequestBody(required = false) BidExpertBlacklistVO vo) {
        return templateRest.request(new CallbackRest<BidExpertDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("专家ID不能为空");
            }

            @Override
            public BidExpertDTO execute() {
                String reason = vo != null ? vo.getReason() : null;
                return bidExpertCommand.blacklist(id, reason);
            }
        }, true);
    }

    /**
     * 移出黑名单
     * @param id 专家ID
     * @return 更新后的专家信息
     */
    @PostMapping("/unblacklist/{id}")
    public Response<BidExpertDTO> unblacklist(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidExpertDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("专家ID不能为空");
            }

            @Override
            public BidExpertDTO execute() {
                return bidExpertCommand.unblacklist(id);
            }
        }, true);
    }

    /**
     * 随机抽取专家
     * @param vo 抽取条件
     * @return 抽取到的专家列表
     */
    @PostMapping("/select")
    public Response<List<BidExpertDTO>> selectExperts(@RequestBody BidExpertSelectVO vo) {
        return templateRest.request(new CallbackRest<List<BidExpertDTO>>() {
            @Override
            public void check() {}

            @Override
            public List<BidExpertDTO> execute() {
                return java.util.Collections.emptyList();
            }
        });
    }
}
