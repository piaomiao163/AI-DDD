package com.piaomiao.web;

import com.piaomiao.command.sys.RoleCommand;
import com.piaomiao.model.sys.PermissionModel;
import com.piaomiao.model.sys.RoleModel;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.RoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleCommand roleCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色模型
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:role:view')")
    public Response<RoleModel> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<RoleModel>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("角色ID不能为空");
                }
            }

            @Override
            public RoleModel execute() {
                return roleCommand.findById(id);
            }
        });
    }

    /**
     * 根据编码查询角色
     * @param code 角色编码
     * @return 角色模型
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAuthority('system:role:view')")
    public Response<RoleModel> findByCode(@PathVariable String code) {
        return templateRest.request(new CallbackRest<RoleModel>() {
            @Override
            public void check() {
                if (code == null || code.isEmpty()) {
                    throw new IllegalArgumentException("角色编码不能为空");
                }
            }

            @Override
            public RoleModel execute() {
                return roleCommand.findByCode(code);
            }
        });
    }

    /**
     * 查询所有角色
     * @return 角色列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:role:list')")
    public Response<List<RoleModel>> findAll() {
        return templateRest.request(new CallbackRest<List<RoleModel>>() {
            @Override
            public void check() {
            }

            @Override
            public List<RoleModel> execute() {
                return roleCommand.findAll();
            }
        });
    }

    /**
     * 保存角色
     * @param roleVO 角色VO
     * @return 角色ID
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:role:add')")
    public Response<Long> save(@RequestBody RoleVO roleVO) {
        return templateRest.request(new CallbackRest<Long>() {
            @Override
            public void check() {
                if (roleVO == null) {
                    throw new IllegalArgumentException("角色VO不能为空");
                }
                if (roleVO.getName() == null || roleVO.getName().isEmpty()) {
                    throw new IllegalArgumentException("角色名称不能为空");
                }
                if (roleVO.getCode() == null || roleVO.getCode().isEmpty()) {
                    throw new IllegalArgumentException("角色编码不能为空");
                }
            }

            @Override
            public Long execute() {
                Long roleId = roleCommand.save(RoleVO.toDTO(roleVO));
                if (roleVO.getPermissionIds() != null && !roleVO.getPermissionIds().isEmpty()) {
                    roleCommand.assignPermissions(roleId, roleVO.getPermissionIds());
                }
                return roleId;
            }
        }, true);
    }

    /**
     * 更新角色
     * @param roleVO 角色VO
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public Response<Void> update(@RequestBody RoleVO roleVO) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (roleVO == null) {
                    throw new IllegalArgumentException("角色VO不能为空");
                }
                if (roleVO.getId() == null) {
                    throw new IllegalArgumentException("角色ID不能为空");
                }
            }

            @Override
            public Void execute() {
                roleCommand.update(RoleVO.toDTO(roleVO));
                if (roleVO.getPermissionIds() != null) {
                    roleCommand.assignPermissions(roleVO.getId(), roleVO.getPermissionIds());
                }
                return null;
            }
        }, true);
    }

    /**
     * 删除角色
     * @param id 角色ID
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("角色ID不能为空");
                }
            }

            @Override
            public Void execute() {
                roleCommand.delete(id);
                return null;
            }
        }, true);
    }

    /**
     * 批量删除角色
     * @param ids 角色ID列表
     */
    @DeleteMapping("/delete/batch")
    @PreAuthorize("hasAuthority('system:role:delete')")
    public Response<Void> deleteBatch(@RequestBody List<Long> ids) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (ids == null || ids.isEmpty()) {
                    throw new IllegalArgumentException("角色ID列表不能为空");
                }
            }

            @Override
            public Void execute() {
                roleCommand.deleteBatch(ids);
                return null;
            }
        }, true);
    }

    /**
     * 获取角色权限
     * @param roleId 角色ID
     * @return 权限列表
     */
    @GetMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('system:role:view')")
    public Response<List<PermissionModel>> getRolePermissions(@PathVariable Long roleId) {
        return templateRest.request(new CallbackRest<List<PermissionModel>>() {
            @Override
            public void check() {
                if (roleId == null) {
                    throw new IllegalArgumentException("角色ID不能为空");
                }
            }

            @Override
            public List<PermissionModel> execute() {
                return roleCommand.getRolePermissions(roleId);
            }
        });
    }

    /**
     * 分配角色权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    @PostMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('system:role:assignPermission')")
    public Response<Void> assignRolePermissions(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (roleId == null) {
                    throw new IllegalArgumentException("角色ID不能为空");
                }
                if (permissionIds == null) {
                    throw new IllegalArgumentException("权限ID列表不能为空");
                }
            }

            @Override
            public Void execute() {
                roleCommand.assignPermissions(roleId, permissionIds);
                return null;
            }
        }, true);
    }
}
