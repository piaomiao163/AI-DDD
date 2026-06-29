package com.piaomiao.web.bid;

import com.piaomiao.command.BidContractCommand;
import com.piaomiao.dto.BidContractDTO;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.bid.BidContractQueryVO;
import com.piaomiao.web.vo.bid.BidContractSignVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 合同控制器
 */
@RestController
@RequestMapping("/bid/contract")
public class BidContractController {

    @Autowired
    private BidContractCommand bidContractCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 分页查询合同
     * @param vo 查询条件
     * @return 分页结果
     */
    @PostMapping("/page")
    public Response<PageResult<BidContractDTO>> page(@RequestBody(required = false) BidContractQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<BidContractDTO>>() {
            @Override
            public void check() {}

            @Override
            public PageResult<BidContractDTO> execute() {
                return bidContractCommand.findByPage(BidContractQueryVO.toQuery(vo));
            }
        });
    }

    /**
     * 根据ID查询合同
     * @param id 合同ID
     * @return 合同信息
     */
    @GetMapping("/{id}")
    public Response<BidContractDTO> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidContractDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("合同ID不能为空");
            }

            @Override
            public BidContractDTO execute() {
                return bidContractCommand.findById(id);
            }
        });
    }

    /**
     * 创建合同
     * @param dto 合同信息
     * @return 保存后的合同信息
     */
    @PostMapping("/save")
    public Response<BidContractDTO> save(@RequestBody BidContractDTO dto) {
        return templateRest.request(new CallbackRest<BidContractDTO>() {
            @Override
            public void check() {
                if (dto.getContractName() == null || dto.getContractName().isEmpty()) {
                    throw new IllegalArgumentException("合同名称不能为空");
                }
            }

            @Override
            public BidContractDTO execute() {
                return bidContractCommand.save(dto);
            }
        }, true);
    }

    /**
     * 更新合同
     * @param dto 合同信息
     * @return 更新后的合同信息
     */
    @PutMapping("/update")
    public Response<BidContractDTO> update(@RequestBody BidContractDTO dto) {
        return templateRest.request(new CallbackRest<BidContractDTO>() {
            @Override
            public void check() {
                if (dto.getId() == null) throw new IllegalArgumentException("合同ID不能为空");
            }

            @Override
            public BidContractDTO execute() {
                return bidContractCommand.update(dto);
            }
        }, true);
    }

    /**
     * 签署合同
     * @param id 合同ID
     * @param vo 签署信息
     * @return 更新后的合同信息
     */
    @PostMapping("/sign/{id}")
    public Response<BidContractDTO> sign(@PathVariable Long id,
                                          @RequestBody(required = false) BidContractSignVO vo) {
        return templateRest.request(new CallbackRest<BidContractDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("合同ID不能为空");
            }

            @Override
            public BidContractDTO execute() {
                String signDate = vo != null ? vo.getSignDate() : null;
                return bidContractCommand.sign(id, signDate);
            }
        }, true);
    }

    /**
     * 开始履行
     * @param id 合同ID
     * @return 更新后的合同信息
     */
    @PostMapping("/execute/{id}")
    public Response<BidContractDTO> execute(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidContractDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("合同ID不能为空");
            }

            @Override
            public BidContractDTO execute() {
                return bidContractCommand.execute(id);
            }
        }, true);
    }

    /**
     * 完结合同
     * @param id 合同ID
     * @return 更新后的合同信息
     */
    @PostMapping("/complete/{id}")
    public Response<BidContractDTO> complete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidContractDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("合同ID不能为空");
            }

            @Override
            public BidContractDTO execute() {
                return bidContractCommand.complete(id);
            }
        }, true);
    }

    /**
     * 解除合同
     * @param id 合同ID
     * @return 更新后的合同信息
     */
    @PostMapping("/terminate/{id}")
    public Response<BidContractDTO> terminate(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidContractDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("合同ID不能为空");
            }

            @Override
            public BidContractDTO execute() {
                return bidContractCommand.terminate(id);
            }
        }, true);
    }
}
