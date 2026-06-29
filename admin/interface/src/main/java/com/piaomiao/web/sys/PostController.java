package com.piaomiao.web.sys;

import com.piaomiao.command.sys.PostCommand;
import com.piaomiao.model.sys.PostModel;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.sys.PostVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位控制器
 */
@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostCommand postCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 根据ID查询岗位
     * @param id 岗位ID
     * @return 岗位模型
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:post:view')")
    public Response<PostModel> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<PostModel>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("岗位ID不能为空");
                }
            }

            @Override
            public PostModel execute() {
                return postCommand.findById(id);
            }
        });
    }

    /**
     * 根据编码查询岗位
     * @param code 岗位编码
     * @return 岗位模型
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAuthority('system:post:view')")
    public Response<PostModel> findByCode(@PathVariable String code) {
        return templateRest.request(new CallbackRest<PostModel>() {
            @Override
            public void check() {
                if (code == null || code.isEmpty()) {
                    throw new IllegalArgumentException("岗位编码不能为空");
                }
            }

            @Override
            public PostModel execute() {
                return postCommand.findByCode(code);
            }
        });
    }

    /**
     * 查询所有岗位
     * @return 岗位列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:post:list')")
    public Response<List<PostModel>> findAll() {
        return templateRest.request(new CallbackRest<List<PostModel>>() {
            @Override
            public void check() {
            }

            @Override
            public List<PostModel> execute() {
                return postCommand.findAll();
            }
        });
    }

    /**
     * 保存岗位
     * @param postVO 岗位VO
     * @return 岗位ID
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:post:add')")
    public Response<Long> save(@RequestBody PostVO postVO) {
        return templateRest.request(new CallbackRest<Long>() {
            @Override
            public void check() {
                if (postVO == null) {
                    throw new IllegalArgumentException("岗位VO不能为空");
                }
                if (postVO.getName() == null || postVO.getName().isEmpty()) {
                    throw new IllegalArgumentException("岗位名称不能为空");
                }
                if (postVO.getCode() == null || postVO.getCode().isEmpty()) {
                    throw new IllegalArgumentException("岗位编码不能为空");
                }
            }

            @Override
            public Long execute() {
                return postCommand.save(PostVO.toDTO(postVO));
            }
        }, true);
    }

    /**
     * 更新岗位
     * @param postVO 岗位VO
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('system:post:edit')")
    public Response<Void> update(@RequestBody PostVO postVO) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (postVO == null) {
                    throw new IllegalArgumentException("岗位VO不能为空");
                }
                if (postVO.getId() == null) {
                    throw new IllegalArgumentException("岗位ID不能为空");
                }
            }

            @Override
            public Void execute() {
                postCommand.update(PostVO.toDTO(postVO));
                return null;
            }
        }, true);
    }

    /**
     * 删除岗位
     * @param id 岗位ID
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:post:delete')")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("岗位ID不能为空");
                }
            }

            @Override
            public Void execute() {
                postCommand.delete(id);
                return null;
            }
        }, true);
    }

    /**
     * 批量删除岗位
     * @param ids 岗位ID列表
     */
    @DeleteMapping("/delete/batch")
    @PreAuthorize("hasAuthority('system:post:delete')")
    public Response<Void> deleteBatch(@RequestBody List<Long> ids) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (ids == null || ids.isEmpty()) {
                    throw new IllegalArgumentException("岗位ID列表不能为空");
                }
            }

            @Override
            public Void execute() {
                postCommand.deleteBatch(ids);
                return null;
            }
        }, true);
    }
}
