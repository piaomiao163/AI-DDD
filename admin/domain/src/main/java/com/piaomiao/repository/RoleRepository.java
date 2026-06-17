package com.piaomiao.repository;

import com.piaomiao.model.RoleModel;

import java.util.List;

/**
 * 角色仓储接口
 */
public interface RoleRepository {
    /**
     * 根据ID查询角色
     * @param id 角色ID
     * @return 角色模型
     */
    RoleModel findById(Long id);
    
    /**
     * 根据编码查询角色
     * @param code 角色编码
     * @return 角色模型
     */
    RoleModel findByCode(String code);
    
    /**
     * 查询所有角色
     * @return 角色列表
     */
    List<RoleModel> findAll();
    
    /**
     * 保存角色
     * @param roleModel 角色模型
     * @return 角色ID
     */
    Long save(RoleModel roleModel);
    
    /**
     * 更新角色
     * @param roleModel 角色模型
     */
    void update(RoleModel roleModel);
    
    /**
     * 删除角色
     * @param id 角色ID
     */
    void delete(Long id);
    
    /**
     * 根据用户ID查询角色
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleModel> findByUserId(Long userId);
    
    /**
     * 根据用户ID列表查询角色
     * @param userIds 用户ID列表
     * @return 角色列表
     */
    List<RoleModel> findByUserIds(List<Long> userIds);
    
    /**
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionId 权限ID
     */
    void insertRolePermission(Long roleId, Long permissionId);
    
    /**
     * 删除角色的所有权限
     * @param roleId 角色ID
     */
    void deleteRolePermissions(Long roleId);
    
    /**
     * 根据角色ID查询权限ID列表
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> findPermissionIdsByRoleId(Long roleId);
    
    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void insertUserRole(Long userId, Long roleId);
    
    /**
     * 删除用户的角色
     * @param userId 用户ID
     */
    void deleteUserRoles(Long userId);
}