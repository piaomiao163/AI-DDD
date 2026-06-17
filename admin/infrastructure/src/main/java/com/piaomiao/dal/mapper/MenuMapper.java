package com.piaomiao.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piaomiao.dal.entity.MenuDO;

import java.util.List;

/**
 * 菜单Mapper
 */
public interface MenuMapper extends BaseMapper<MenuDO> {
    /**
     * 查询所有菜单
     * @return 菜单列表
     */
    List<MenuDO> selectAll();
    
    /**
     * 查询根菜单
     * @return 根菜单列表
     */
    List<MenuDO> selectRootMenus();
    
    /**
     * 根据父菜单ID查询子菜单
     * @param parentId 父菜单ID
     * @return 子菜单列表
     */
    List<MenuDO> selectByParentId(Long parentId);
    
    /**
     * 根据角色ID查询菜单
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<MenuDO> selectByRoleId(Long roleId);
}