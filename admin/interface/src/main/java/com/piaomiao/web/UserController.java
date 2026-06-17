package com.piaomiao.web;

import com.piaomiao.command.UserCommand;
import com.piaomiao.model.UserModel;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.UserQueryVO;
import com.piaomiao.web.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserCommand userCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户VO
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:user:view')")
    public Response<UserVO> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<UserVO>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("用户ID不能为空");
                }
            }

            @Override
            public UserVO execute() {
                return UserVO.fromModel(userCommand.findById(id));
            }
        });
    }

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户VO
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasAuthority('system:user:view')")
    public Response<UserVO> findByUsername(@PathVariable String username) {
        return templateRest.request(new CallbackRest<UserVO>() {
            @Override
            public void check() {
                if (username == null || username.isEmpty()) {
                    throw new IllegalArgumentException("用户名不能为空");
                }
            }

            @Override
            public UserVO execute() {
                return UserVO.fromModel(userCommand.findByUsername(username));
            }
        });
    }

    /**
     * 查询所有用户
     * @return 用户列表
     */
    @GetMapping("/all")
    @PreAuthorize("hasAuthority('system:user:list')")
    public Response<List<UserVO>> findAll() {
        return templateRest.request(new CallbackRest<List<UserVO>>() {
            @Override
            public void check() {
            }

            @Override
            public List<UserVO> execute() {
                return userCommand.findAll().stream()
                        .map(UserVO::fromModel)
                        .collect(Collectors.toList());
            }
        });
    }

    /**
     * 分页查询用户
     * @param vo 查询条件
     * @return 分页结果
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:user:list')")
    @com.piaomiao.annotation.DataPermission(value = com.piaomiao.enums.DataPermissionType.DEPARTMENT, includeChildren = true)
    public Response<PageResult<UserVO>> findPage(UserQueryVO vo) {
        return templateRest.request(new CallbackRest<PageResult<UserVO>>() {
            @Override
            public void check() {
            }

            @Override
            public PageResult<UserVO> execute() {
                PageResult<UserModel> result = userCommand.findPage(UserQueryVO.toDTO(vo));
                return new PageResult<>(result.getTotal(),
                        result.getList().stream().map(UserVO::fromModel).collect(Collectors.toList()),
                        result.getPageNum(), result.getPageSize());
            }
        });
    }

    /**
     * 保存用户
     * @param userVO 用户VO
     * @return 用户ID
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:user:add')")
    public Response<Long> save(@RequestBody UserVO userVO) {
        return templateRest.request(new CallbackRest<Long>() {
            @Override
            public void check() {
                if (userVO == null) {
                    throw new IllegalArgumentException("用户VO不能为空");
                }
                if (userVO.getUsername() == null || userVO.getUsername().isEmpty()) {
                    throw new IllegalArgumentException("用户名不能为空");
                }
                if (userVO.getPassword() == null || userVO.getPassword().isEmpty()) {
                    throw new IllegalArgumentException("密码不能为空");
                }
            }

            @Override
            public Long execute() {
                Long userId = userCommand.save(UserVO.toDTO(userVO));
                if (userVO.getRoleIds() != null && !userVO.getRoleIds().isEmpty()) {
                    userCommand.assignUserRoles(userId, userVO.getRoleIds());
                }
                return userId;
            }
        }, true);
    }

    /**
     * 更新用户
     * @param userVO 用户VO
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Response<Void> update(@RequestBody UserVO userVO) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (userVO == null) {
                    throw new IllegalArgumentException("用户VO不能为空");
                }
                if (userVO.getId() == null) {
                    throw new IllegalArgumentException("用户ID不能为空");
                }
            }

            @Override
            public Void execute() {
                userCommand.update(UserVO.toDTO(userVO));
                if (userVO.getRoleIds() != null) {
                    userCommand.assignUserRoles(userVO.getId(), userVO.getRoleIds());
                }
                return null;
            }
        }, true);
    }

    /**
     * 删除用户
     * @param id 用户ID
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("用户ID不能为空");
                }
            }

            @Override
            public Void execute() {
                userCommand.delete(id);
                return null;
            }
        }, true);
    }

    /**
     * 批量删除用户
     * @param ids 用户ID列表
     */
    @DeleteMapping("/delete/batch")
    @PreAuthorize("hasAuthority('system:user:delete')")
    public Response<Void> deleteBatch(@RequestBody List<Long> ids) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (ids == null || ids.isEmpty()) {
                    throw new IllegalArgumentException("用户ID列表不能为空");
                }
            }

            @Override
            public Void execute() {
                userCommand.deleteBatch(ids);
                return null;
            }
        }, true);
    }

    /**
     * 获取用户角色
     * @param userId 用户ID
     * @return 角色列表
     */
    @GetMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('system:user:view')")
    public Response<List<com.piaomiao.model.RoleModel>> getUserRoles(@PathVariable Long userId) {
        return templateRest.request(new CallbackRest<List<com.piaomiao.model.RoleModel>>() {
            @Override
            public void check() {
                if (userId == null) {
                    throw new IllegalArgumentException("用户ID不能为空");
                }
            }

            @Override
            public List<com.piaomiao.model.RoleModel> execute() {
                return userCommand.getUserRoles(userId);
            }
        });
    }

    /**
     * 分配用户角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    @PostMapping("/{userId}/roles")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public Response<Void> assignUserRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (userId == null) {
                    throw new IllegalArgumentException("用户ID不能为空");
                }
                if (roleIds == null) {
                    throw new IllegalArgumentException("角色ID列表不能为空");
                }
            }

            @Override
            public Void execute() {
                userCommand.assignUserRoles(userId, roleIds);
                return null;
            }
        }, true);
    }

    /**
     * 根据角色ID查询用户
     * @param roleId 角色ID
     * @return 用户列表
     */
    @GetMapping("/byRole/{roleId}")
    @PreAuthorize("hasAuthority('system:user:byRole')")
    public Response<List<UserVO>> findByRoleId(@PathVariable Long roleId) {
        return templateRest.request(new CallbackRest<List<UserVO>>() {
            @Override
            public void check() {
                if (roleId == null) {
                    throw new IllegalArgumentException("角色ID不能为空");
                }
            }

            @Override
            public List<UserVO> execute() {
                return userCommand.findByRoleId(roleId).stream()
                        .map(UserVO::fromModel)
                        .collect(Collectors.toList());
            }
        });
    }
}