package com.piaomiao.service.sys.impl;

import com.piaomiao.model.sys.MenuModel;
import com.piaomiao.repository.sys.MenuRepository;
import com.piaomiao.service.sys.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 菜单服务实现
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public MenuModel findById(Long id) {
        return menuRepository.findById(id);
    }

    @Override
    public List<MenuModel> findAll() {
        return menuRepository.findAll();
    }

    @Override
    public List<MenuModel> findRootMenus() {
        return menuRepository.findRootMenus();
    }

    @Override
    public List<MenuModel> findByParentId(Long parentId) {
        return menuRepository.findByParentId(parentId);
    }

    @Override
    public Long save(MenuModel menuModel) {
        return menuRepository.save(menuModel);
    }

    @Override
    public void update(MenuModel menuModel) {
        menuRepository.update(menuModel);
    }

    @Override
    public void delete(Long id) {
        menuRepository.delete(id);
    }

    @Override
    public List<MenuModel> findByRoleId(Long roleId) {
        return menuRepository.findByRoleId(roleId);
    }

    @Override
    public List<MenuModel> buildMenuTree() {
        // 1. 获取所有菜单
        List<MenuModel> allMenus = menuRepository.findAll();
        
        // 2. 构建菜单ID到菜单的映射
        Map<Long, MenuModel> menuMap = allMenus.stream()
                .collect(Collectors.toMap(MenuModel::getId, menu -> menu));
        
        // 3. 构建菜单树
        List<MenuModel> rootMenus = new ArrayList<>();
        for (MenuModel menu : allMenus) {
            if (menu.getParentId() == null || menu.getParentId() == 0) {
                // 根菜单
                rootMenus.add(menu);
            } else {
                // 子菜单，添加到父菜单的children列表中
                MenuModel parentMenu = menuMap.get(menu.getParentId());
                if (parentMenu != null) {
                    List<MenuModel> children = parentMenu.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        parentMenu.setChildren(children);
                    }
                    children.add(menu);
                }
            }
        }
        
        return rootMenus;
    }
}