package com.piaomiao.repository;

import com.piaomiao.model.MenuModel;

import java.util.List;

/**
 * 菜单仓储接口
 */
public interface MenuRepository {
    /**
     * 根据ID查询菜单
     * @param id 菜单ID
     * @return 菜单模型
     */
    MenuModel findById(Long id);
    
    /**
     * 查询所有菜单
     * @return 菜单列表
     */
    List<MenuModel> findAll();
    
    /**
     * 查询根菜单
     * @return 根菜单列表
     */
    List<MenuModel> findRootMenus();
    
    /**
     * 根据父菜单ID查询子菜单
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    List<MenuModel> findByParentId(Long parentId);
    
    /**
     * 保存菜单
     * @param menuModel 菜单模型
     * @return 菜单ID
     */
    Long save(MenuModel menuModel);
    
    /**
     * 更新菜单
     * @param menuModel 菜单模型
     */
    void update(MenuModel menuModel);
    
    /**
     * 删除菜单
     * @param id 菜单ID
     */
    void delete(Long id);
    
    /**
     * 根据角色ID查询菜单
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<MenuModel> findByRoleId(Long roleId);
}