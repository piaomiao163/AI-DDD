package com.piaomiao.web;

import com.piaomiao.command.DepartmentCommand;
import com.piaomiao.model.DepartmentModel;
import com.piaomiao.model.UserModel;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.DepartmentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门控制器
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentCommand departmentCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 根据ID查询部门
     * @param id 部门ID
     * @return 部门模型
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:department:view')")
    public Response<DepartmentModel> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<DepartmentModel>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("部门ID不能为空");
                }
            }

            @Override
            public DepartmentModel execute() {
                return departmentCommand.findById(id);
            }
        });
    }

    /**
     * 根据编码查询部门
     * @param code 部门编码
     * @return 部门模型
     */
    @GetMapping("/code/{code}")
    @PreAuthorize("hasAuthority('system:department:view')")
    public Response<DepartmentModel> findByCode(@PathVariable String code) {
        return templateRest.request(new CallbackRest<DepartmentModel>() {
            @Override
            public void check() {
                if (code == null || code.isEmpty()) {
                    throw new IllegalArgumentException("部门编码不能为空");
                }
            }

            @Override
            public DepartmentModel execute() {
                return departmentCommand.findByCode(code);
            }
        });
    }

    /**
     * 查询所有部门
     * @return 部门列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:department:list')")
    public Response<List<DepartmentModel>> findAll() {
        return templateRest.request(new CallbackRest<List<DepartmentModel>>() {
            @Override
            public void check() {
            }

            @Override
            public List<DepartmentModel> execute() {
                return departmentCommand.findAll();
            }
        });
    }

    /**
     * 查询根部门
     * @return 根部门列表
     */
    @GetMapping("/root")
    @PreAuthorize("hasAuthority('system:department:root')")
    public Response<List<DepartmentModel>> findRootDepartments() {
        return templateRest.request(new CallbackRest<List<DepartmentModel>>() {
            @Override
            public void check() {
            }

            @Override
            public List<DepartmentModel> execute() {
                return departmentCommand.findRootDepartments();
            }
        });
    }

    /**
     * 根据父部门ID查询子部门
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    @GetMapping("/children/{parentId}")
    @PreAuthorize("hasAuthority('system:department:children')")
    public Response<List<DepartmentModel>> findByParentId(@PathVariable Long parentId) {
        return templateRest.request(new CallbackRest<List<DepartmentModel>>() {
            @Override
            public void check() {
                if (parentId == null) {
                    throw new IllegalArgumentException("父部门ID不能为空");
                }
            }

            @Override
            public List<DepartmentModel> execute() {
                return departmentCommand.findByParentId(parentId);
            }
        });
    }

    /**
     * 保存部门
     * @param departmentVO 部门VO
     * @return 部门ID
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:department:add')")
    public Response<Long> save(@RequestBody DepartmentVO departmentVO) {
        return templateRest.request(new CallbackRest<Long>() {
            @Override
            public void check() {
                if (departmentVO == null) {
                    throw new IllegalArgumentException("部门VO不能为空");
                }
                if (departmentVO.getName() == null || departmentVO.getName().isEmpty()) {
                    throw new IllegalArgumentException("部门名称不能为空");
                }
                if (departmentVO.getCode() == null || departmentVO.getCode().isEmpty()) {
                    throw new IllegalArgumentException("部门编码不能为空");
                }
            }

            @Override
            public Long execute() {
                return departmentCommand.save(DepartmentVO.toDTO(departmentVO));
            }
        }, true);
    }

    /**
     * 更新部门
     * @param departmentVO 部门VO
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('system:department:edit')")
    public Response<Void> update(@RequestBody DepartmentVO departmentVO) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (departmentVO == null) {
                    throw new IllegalArgumentException("部门VO不能为空");
                }
                if (departmentVO.getId() == null) {
                    throw new IllegalArgumentException("部门ID不能为空");
                }
            }

            @Override
            public Void execute() {
                departmentCommand.update(DepartmentVO.toDTO(departmentVO));
                return null;
            }
        }, true);
    }

    /**
     * 删除部门
     * @param id 部门ID
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:department:delete')")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("部门ID不能为空");
                }
            }

            @Override
            public Void execute() {
                departmentCommand.delete(id);
                return null;
            }
        }, true);
    }

    /**
     * 批量删除部门
     * @param ids 部门ID列表
     */
    @DeleteMapping("/delete/batch")
    @PreAuthorize("hasAuthority('system:department:delete')")
    public Response<Void> deleteBatch(@RequestBody List<Long> ids) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (ids == null || ids.isEmpty()) {
                    throw new IllegalArgumentException("部门ID列表不能为空");
                }
            }

            @Override
            public Void execute() {
                departmentCommand.deleteBatch(ids);
                return null;
            }
        }, true);
    }

    /**
     * 根据部门ID查询用户
     * @param departmentId 部门ID
     * @return 用户列表
     */
    @GetMapping("/users/{departmentId}")
    @PreAuthorize("hasAuthority('system:department:users')")
    public Response<List<UserModel>> findUsersByDepartmentId(@PathVariable Long departmentId) {
        return templateRest.request(new CallbackRest<List<UserModel>>() {
            @Override
            public void check() {
                if (departmentId == null) {
                    throw new IllegalArgumentException("部门ID不能为空");
                }
            }

            @Override
            public List<UserModel> execute() {
                return departmentCommand.findUsersByDepartmentId(departmentId);
            }
        });
    }

    /**
     * 构建部门树
     * @return 部门树
     */
    @GetMapping("/tree")
    public Response<List<DepartmentModel>> buildDepartmentTree() {
        return templateRest.request(new CallbackRest<List<DepartmentModel>>() {
            @Override
            public void check() {
            }

            @Override
            public List<DepartmentModel> execute() {
                return departmentCommand.buildDepartmentTree();
            }
        });
    }
}
