package com.piaomiao.web;

import com.piaomiao.command.MenuCommand;
import com.piaomiao.model.MenuModel;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单控制器
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuCommand menuCommand;

    @Autowired
    private TemplateRest templateRest;

    /**
     * 根据ID查询菜单
     * @param id 菜单ID
     * @return 菜单模型
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:menu:view')")
    public Response<MenuModel> findById(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<MenuModel>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("菜单ID不能为空");
                }
            }

            @Override
            public MenuModel execute() {
                return menuCommand.findById(id);
            }
        });
    }

    /**
     * 查询所有菜单
     * @return 菜单列表
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public Response<List<MenuModel>> findAll() {
        return templateRest.request(new CallbackRest<List<MenuModel>>() {
            @Override
            public void check() {
            }

            @Override
            public List<MenuModel> execute() {
                return menuCommand.findAll();
            }
        });
    }

    /**
     * 查询根菜单
     * @return 根菜单列表
     */
    @GetMapping("/root")
    @PreAuthorize("hasAuthority('system:menu:root')")
    public Response<List<MenuModel>> findRootMenus() {
        return templateRest.request(new CallbackRest<List<MenuModel>>() {
            @Override
            public void check() {
            }

            @Override
            public List<MenuModel> execute() {
                return menuCommand.findById(1L).getChildren();
            }
        });
    }

    /**
     * 根据父菜单ID查询子菜单
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    @GetMapping("/children/{parentId}")
    @PreAuthorize("hasAuthority('system:menu:children')")
    public Response<List<MenuModel>> findByParentId(@PathVariable Long parentId) {
        return templateRest.request(new CallbackRest<List<MenuModel>>() {
            @Override
            public void check() {
                if (parentId == null) {
                    throw new IllegalArgumentException("父菜单ID不能为空");
                }
            }

            @Override
            public List<MenuModel> execute() {
                return menuCommand.findById(parentId).getChildren();
            }
        });
    }

    /**
     * 保存菜单
     * @param menuVO 菜单VO
     * @return 菜单ID
     */
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('system:menu:add')")
    public Response<Long> save(@RequestBody MenuVO menuVO) {
        return templateRest.request(new CallbackRest<Long>() {
            @Override
            public void check() {
                if (menuVO == null) {
                    throw new IllegalArgumentException("菜单VO不能为空");
                }
                if (menuVO.getName() == null || menuVO.getName().isEmpty()) {
                    throw new IllegalArgumentException("菜单名称不能为空");
                }
            }

            @Override
            public Long execute() {
                return menuCommand.save(MenuVO.toDTO(menuVO));
            }
        }, true);
    }

    /**
     * 更新菜单
     * @param menuVO 菜单VO
     */
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public Response<Void> update(@RequestBody MenuVO menuVO) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (menuVO == null) {
                    throw new IllegalArgumentException("菜单VO不能为空");
                }
                if (menuVO.getId() == null) {
                    throw new IllegalArgumentException("菜单ID不能为空");
                }
            }

            @Override
            public Void execute() {
                menuCommand.update(MenuVO.toDTO(menuVO));
                return null;
            }
        }, true);
    }

    /**
     * 删除菜单
     * @param id 菜单ID
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public Response<Void> delete(@PathVariable Long id) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (id == null) {
                    throw new IllegalArgumentException("菜单ID不能为空");
                }
            }

            @Override
            public Void execute() {
                menuCommand.delete(id);
                return null;
            }
        }, true);
    }

    /**
     * 批量删除菜单
     * @param ids 菜单ID列表
     */
    @DeleteMapping("/delete/batch")
    @PreAuthorize("hasAuthority('system:menu:delete')")
    public Response<Void> deleteBatch(@RequestBody List<Long> ids) {
        return templateRest.request(new CallbackRest<Void>() {
            @Override
            public void check() {
                if (ids == null || ids.isEmpty()) {
                    throw new IllegalArgumentException("菜单ID列表不能为空");
                }
            }

            @Override
            public Void execute() {
                menuCommand.deleteBatch(ids);
                return null;
            }
        }, true);
    }

    /**
     * 构建菜单树
     * @return 菜单树
     */
    @GetMapping("/tree")
    @PreAuthorize("hasAuthority('system:menu:tree')")
    public Response<List<MenuModel>> buildMenuTree() {
        return templateRest.request(new CallbackRest<List<MenuModel>>() {
            @Override
            public void check() {
            }

            @Override
            public List<MenuModel> execute() {
                return menuCommand.findTree();
            }
        });
    }
}
