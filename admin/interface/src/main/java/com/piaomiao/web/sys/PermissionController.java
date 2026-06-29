package com.piaomiao.web.sys;

import com.piaomiao.command.sys.PermissionCommand;
import com.piaomiao.model.sys.PermissionModel;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.sys.PermissionQueryPageVO;
import com.piaomiao.web.vo.sys.PermissionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionCommand permissionCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 根据ID查询权限
     * @param id 权限ID
     * @return 权限模型
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:permission:view')")
    public Response<PermissionModel> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<PermissionModel>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("权限ID不能为空");
                }
            }

            @Override
            public PermissionModel execute() {
                return permissionCommand.findById(id);
            }
        });
    }

    /**
     * 根据编码查询权限
     * @param code 权限编码
     * @return 权限模型
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAuthority('system:permission:view')")
    public Response<PermissionModel> findByCode(@PathVariable String code) {
        return templateRest.request(new CallbackRest<PermissionModel>() {
            @Override
            public void check() {
                if (code == null || code.isEmpty()) {
                    throw new IllegalArgumentException("权限编码不能为空");
                }
            }

            @Override
            public PermissionModel execute() {
                return permissionCommand.findByCode(code);
            }
        });
    }

    /**
     * 查询所有权限（过滤内部权限）
     * @return 权限列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:permission:list')")
    public Response<List<PermissionModel>> findAll() {
        return templateRest.request(new CallbackRest<List<PermissionModel>>() {
            @Override
            public void check() {
            }

            @Override
            public List<PermissionModel> execute() {
                return permissionCommand.findAll();
            }
        });
    }


    /**
     * 查询所有权限（过滤内部权限）
     * @return 权限列表
     */
    @GetMapping("/page")
    @PreAuthorize("hasAuthority('system:permission:list')")
    public Response<PageResult<PermissionModel>> findPage(PermissionQueryPageVO vo) {
        return templateRest.request(new CallbackRest<PageResult<PermissionModel>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<PermissionModel> execute() {
                return permissionCommand.findPage(PermissionQueryPageVO.toDTO(vo));
            }
        });
    }



    /**
     * 保存权限
     * @param permissionVO 权限VO
     * @return 权限ID
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:permission:add')")
    public Response<Long> save(@RequestBody PermissionVO permissionVO) {
        return templateRest.request(new CallbackRest<Long>() {
            @Override
            public void check() {
                if (permissionVO == null) {
                    throw new IllegalArgumentException("权限VO不能为空");
                }
                if (permissionVO.getName() == null || permissionVO.getName().isEmpty()) {
                    throw new IllegalArgumentException("权限名称不能为空");
                }
                if (permissionVO.getCode() == null || permissionVO.getCode().isEmpty()) {
                    throw new IllegalArgumentException("权限编码不能为空");
                }
            }

            @Override
            public Long execute() {
                return permissionCommand.save(PermissionVO.toDTO(permissionVO));
            }
        }, true);
    }

    /**
     * 更新权限
     * @param permissionVO 权限VO
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('system:permission:edit')")
    public Response<Void> update(@RequestBody PermissionVO permissionVO) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (permissionVO == null) {
                    throw new IllegalArgumentException("权限VO不能为空");
                }
                if (permissionVO.getId() == null) {
                    throw new IllegalArgumentException("权限ID不能为空");
                }
            }

            @Override
            public Void execute() {
                permissionCommand.update(PermissionVO.toDTO(permissionVO));
                return null;
            }
        }, true);
    }

    /**
     * 删除权限
     * @param id 权限ID
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:permission:delete')")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("权限ID不能为空");
                }
            }

            @Override
            public Void execute() {
                permissionCommand.delete(id);
                return null;
            }
        }, true);
    }

    /**
     * 批量删除权限
     * @param ids 权限ID列表
     */
    @DeleteMapping("/delete/batch")
    @PreAuthorize("hasAuthority('system:permission:delete')")
    public Response<Void> deleteBatch(@RequestBody List<Long> ids) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (ids == null || ids.isEmpty()) {
                    throw new IllegalArgumentException("权限ID列表不能为空");
                }
            }

            @Override
            public Void execute() {
                for (Long id : ids) {
                    permissionCommand.delete(id);
                }
                return null;
            }
        }, true);
    }
}
