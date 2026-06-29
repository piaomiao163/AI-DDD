package com.piaomiao.web.bid;

import com.piaomiao.command.BidTenderCommand;
import com.piaomiao.dto.BidTenderDTO;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 投标书控制器
 */
@RestController
@RequestMapping("/bid/tender")
public class BidTenderController {

    @Autowired
    private BidTenderCommand bidTenderCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 保存投标书（草稿）
     * @param dto 投标书信息
     * @return 保存后的投标书信息
     */
    @PostMapping("/save")
    public Response<BidTenderDTO> save(@RequestBody BidTenderDTO dto) {
        return templateRest.request(new CallbackRest<BidTenderDTO>() {
            @Override
            public void check() {
                if (dto.getProjectId() == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public BidTenderDTO execute() {
                return bidTenderCommand.save(dto);
            }
        }, true);
    }

    /**
     * 提交投标书
     * @param id 投标书ID
     * @return 更新后的投标书信息
     */
    @PostMapping("/submit/{id}")
    public Response<BidTenderDTO> submit(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidTenderDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("投标书ID不能为空");
            }

            @Override
            public BidTenderDTO execute() {
                return bidTenderCommand.submit(id);
            }
        }, true);
    }

    /**
     * 根据ID查询投标书
     * @param id 投标书ID
     * @return 投标书信息
     */
    @GetMapping("/{id}")
    public Response<BidTenderDTO> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<BidTenderDTO>() {
            @Override
            public void check() {
                if (id == null) throw new IllegalArgumentException("投标书ID不能为空");
            }

            @Override
            public BidTenderDTO execute() {
                return bidTenderCommand.findById(id);
            }
        });
    }

    /**
     * 根据项目ID查询所有投标书
     * @param projectId 项目ID
     * @return 投标书列表
     */
    @GetMapping("/project/{projectId}")
    public Response<List<BidTenderDTO>> getByProject(@PathVariable Long projectId) {
        return templateRest.request(new CallbackRest<List<BidTenderDTO>>() {
            @Override
            public void check() {
                if (projectId == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public List<BidTenderDTO> execute() {
                return bidTenderCommand.findByProjectId(projectId);
            }
        });
    }

    /**
     * 查询我在指定项目的投标书
     * @param projectId 项目ID
     * @return 投标书信息
     */
    @GetMapping("/my/{projectId}")
    public Response<BidTenderDTO> getMyTender(@PathVariable Long projectId) {
        return templateRest.request(new CallbackRest<BidTenderDTO>() {
            @Override
            public void check() {
                if (projectId == null) throw new IllegalArgumentException("项目ID不能为空");
            }

            @Override
            public BidTenderDTO execute() {
                Long userId = UserContext.getCurrentUserId();
                return bidTenderCommand.findMyTender(projectId, userId);
            }
        });
    }
}
