package com.piaomiao.command.sys.impl;

import com.piaomiao.command.sys.MenuCommand;
import com.piaomiao.dto.sys.MenuDTO;
import com.piaomiao.model.sys.MenuModel;
import com.piaomiao.service.sys.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuCommandImpl implements MenuCommand {

    @Autowired
    private MenuService menuService;

    @Override
    public MenuModel findById(Long id) {
        return menuService.findById(id);
    }

    @Override
    public List<MenuModel> findAll() {
        return menuService.findAll();
    }

    @Override
    public List<MenuModel> findTree() {
        return menuService.buildMenuTree();
    }

    @Override
    public Long save(MenuDTO menuDTO) {
        MenuModel menuModel = new MenuModel();
        menuModel.setName(menuDTO.getName());
        menuModel.setPath(menuDTO.getPath());
        menuModel.setIcon(menuDTO.getIcon());
        menuModel.setParentId(menuDTO.getParentId());
        menuModel.setSort(menuDTO.getSort());
        menuModel.setStatus(menuDTO.getStatus());
        return menuService.save(menuModel);
    }

    @Override
    public void update(MenuDTO menuDTO) {
        MenuModel menuModel = new MenuModel();
        menuModel.setId(menuDTO.getId());
        menuModel.setName(menuDTO.getName());
        menuModel.setPath(menuDTO.getPath());
        menuModel.setIcon(menuDTO.getIcon());
        menuModel.setParentId(menuDTO.getParentId());
        menuModel.setSort(menuDTO.getSort());
        menuModel.setStatus(menuDTO.getStatus());
        menuService.update(menuModel);
    }

    @Override
    public void delete(Long id) {
        menuService.delete(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            menuService.delete(id);
        }
    }
}
