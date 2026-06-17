package com.piaomiao.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piaomiao.dal.entity.RoleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色Mapper
 */
public interface RoleMapper extends BaseMapper<RoleDO> {
    /**
     * 根据编码查询角色
     * @param code 角色编码
     * @return 角色实体
     */
    RoleDO selectByCode(String code);
    
    /**
     * 根据用户ID查询角色
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleDO> selectByUserId(Long userId);
    
    /**
     * 根据用户ID列表查询角色
     * @param userIds 用户ID列表
     * @return 角色列表
     */
    List<RoleDO> selectByUserIds(@Param("userIds") List<Long> userIds);
    
    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionId 权限ID
     * @return 影响行数
     */
    int insertRolePermission(@Param("roleId") Long roleId, @Param("permissionId") Long permissionId);
    
    /**
     * 删除角色的权限
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteRolePermissions(Long roleId);
    
    /**
     * 为角色分配菜单
     * @param roleId 角色ID
     * @param menuId 菜单ID
     * @return 影响行数
     */
    int insertRoleMenu(@Param("roleId") Long roleId, @Param("menuId") Long menuId);
    
    /**
     * 删除角色的菜单
     * @param roleId 角色ID
     * @return 影响行数
     */
    int deleteRoleMenus(Long roleId);
    
    /**
     * 根据角色ID查询权限ID列表
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> selectPermissionIdsByRoleId(Long roleId);
    
    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleId 角色ID
     * @return 影响行数
     */
    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);
    
    /**
     * 删除用户的角色
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteUserRoles(Long userId);
}