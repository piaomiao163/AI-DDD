package com.piaomiao.web.bid;

import com.piaomiao.command.BidEvaluationCommand;
import com.piaomiao.dto.BidEvaluationDTO;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 评标控制器
 */
@RestController
@RequestMapping("/bid/evaluation")
public class BidEvaluationController {

    @Autowired
    private BidEvaluationCommand bidEvaluationCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 保存评标记录（草稿）
     * @param dto 评标信息
     * @return 保存后的评标信息
     */
    @PostMapping("/save")
    public Response<BidEvaluationDTO> save(@RequestBody BidEvaluationDTO dto) {
        return templateRest.request(new CallbackRest<BidEvaluationDTO>() {
            @Override
            public void check() {
                if (dto.getProjectId() == null) throw new IllegalArgumentException("项目ID不能为空");
                if (dto.getTenderId() == null) throw new IllegalArgumentException("投标书ID不能为空");
            }

            @Override
            public BidEvaluationDTO execute() {
                return bidEvaluationCommand.save(dto);
            }
        }, true);
    }

    /**
     * 提交评标记录
     * @param id 评标记录ID
     * @return 更新后的评标信息
     */
    @PostMapping("/submit/{id}")
    public Response<BidEvaluationDTO> submit(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidEvaluationDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("评标记录ID不能为空");
            }

            @Override
            public BidEvaluationDTO execute() {
                return bidEvaluationCommand.submit(id);
            }
        }, true);
    }

    /**
     * 根据项目ID查询所有评标记录
     * @param projectId 项目ID
     * @return 评标记录列表
     */
    @GetMapping("/project/{projectId}")
    public Response<List<BidEvaluationDTO>> getByProject(@PathVariable Long projectId) {
        return templateRest.request(new CallbackRest<List<BidEvaluationDTO>>() {
            @Override
            public void check() {
                if (projectId == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public List<BidEvaluationDTO> execute() {
                return bidEvaluationCommand.findByProjectId(projectId);
            }
        });
    }

    /**
     * 查询我的评标记录
     * @param tenderId 投标书ID
     * @param expertId 专家ID
     * @return 评标信息
     */
    @GetMapping("/my")
    public Response<BidEvaluationDTO> getMyEvaluation(@RequestParam(required = false) Long tenderId,
                                                       @RequestParam(required = false) Long expertId) {
        return templateRest.request(new CallbackRest<BidEvaluationDTO>() {
            @Override
            public void check() {}

            @Override
            public BidEvaluationDTO execute() {
                if (tenderId == null || expertId == null) return null;
                return bidEvaluationCommand.findMyEvaluation(tenderId, expertId);
            }
        });
    }
}
