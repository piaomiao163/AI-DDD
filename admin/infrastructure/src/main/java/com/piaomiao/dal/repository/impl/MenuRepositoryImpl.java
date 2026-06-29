package com.piaomiao.dal.repository.impl;

import com.piaomiao.dal.entity.MenuDO;
import com.piaomiao.dal.mapper.MenuMapper;
import com.piaomiao.model.sys.MenuModel;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.repository.sys.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单仓储实现
 */
@Repository
public class MenuRepositoryImpl implements MenuRepository {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public MenuModel findById(Long id) {
        MenuDO menuDO = menuMapper.selectById(id);
        return menuDO != null ? menuDO.toModel() : null;
    }

    @Override
    public List<MenuModel> findAll() {
        List<MenuDO> menuDOs = menuMapper.selectAll();
        return menuDOs.stream()
                .map(MenuDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuModel> findRootMenus() {
        List<MenuDO> menuDOs = menuMapper.selectRootMenus();
        return menuDOs.stream()
                .map(MenuDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuModel> findByParentId(Long parentId) {
        List<MenuDO> menuDOs = menuMapper.selectByParentId(parentId);
        return menuDOs.stream()
                .map(MenuDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Long save(MenuModel menuModel) {
        MenuDO menuDO = MenuDO.fromModel(menuModel);
        AuditFieldUtil.fillForInsert(menuDO);
        menuMapper.insert(menuDO);
        return menuDO.getId();
    }

    @Override
    public void update(MenuModel menuModel) {
        MenuDO menuDO = MenuDO.fromModel(menuModel);
        AuditFieldUtil.fillForUpdate(menuDO);
        menuMapper.updateById(menuDO);
    }

    @Override
    public void delete(Long id) {
        menuMapper.deleteById(id);
    }

    @Override
    public List<MenuModel> findByRoleId(Long roleId) {
        List<MenuDO> menuDOs = menuMapper.selectByRoleId(roleId);
        return menuDOs.stream()
                .map(MenuDO::toModel)
                .collect(Collectors.toList());
    }
}