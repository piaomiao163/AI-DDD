package com.piaomiao.service;

import com.piaomiao.model.RoleModel;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {
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
     * 为角色分配权限
     * @param roleId 角色ID
     * @param permissionIds 权限ID列表
     */
    void assignPermissions(Long roleId, List<Long> permissionIds);
    
    /**
     * 为角色分配菜单
     * @param roleId 角色ID
     * @param menuIds 菜单ID列表
     */
    void assignMenus(Long roleId, List<Long> menuIds);
    
    /**
     * 根据用户ID查询角色
     * @param userId 用户ID
     * @return 角色列表
     */
    List<RoleModel> findByUserId(Long userId);
    
    /**
     * 根据角色ID查询权限ID列表
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> findPermissionIdsByRoleId(Long roleId);
}