package com.piaomiao.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piaomiao.dal.entity.PermissionDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限Mapper
 */
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionDO> {
    /**
     * 根据编码查询权限
     * @param code 权限编码
     * @return 权限实体
     */
    PermissionDO selectByCode(@Param("code")String code);
    
    /**
     * 查询所有权限
     * @return 权限列表
     */
    List<PermissionDO> selectAll();
    
    /**
     * 根据角色ID查询权限
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<PermissionDO> selectByRoleId(@Param("roleId")Long roleId);
    
    /**
     * 根据菜单ID查询权限
     * @param menuId 菜单ID
     * @return 权限列表
     */
    List<PermissionDO> selectByMenuId(@Param("menuId")Long menuId);
    
    /**
     * 根据角色ID列表查询权限
     * @param roleIds 角色ID列表
     * @return 权限列表
     */
    List<PermissionDO> selectByRoleIds(@Param("roleIds") List<Long> roleIds);
}
