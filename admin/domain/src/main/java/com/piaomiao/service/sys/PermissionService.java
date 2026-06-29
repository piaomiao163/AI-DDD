package com.piaomiao.service.sys;

import com.piaomiao.model.sys.PermissionModel;
import com.piaomiao.response.PageResult;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService {
    /**
     * 根据ID查询权限
     * @param id 权限ID
     * @return 权限模型
     */
    PermissionModel findById(Long id);
    
    /**
     * 根据编码查询权限
     * @param code 权限编码
     * @return 权限模型
     */
    PermissionModel findByCode(String code);
    
    /**
     * 查询所有权限
     * @return 权限列表
     */
    List<PermissionModel> findAll();
    
    /**
     * 保存权限
     * @param permissionModel 权限模型
     * @return 权限ID
     */
    Long save(PermissionModel permissionModel);
    
    /**
     * 更新权限
     * @param permissionModel 权限模型
     */
    void update(PermissionModel permissionModel);
    
    /**
     * 删除权限
     * @param id 权限ID
     */
    void delete(Long id);
    
    /**
     * 根据角色ID查询权限
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<PermissionModel> findByRoleId(Long roleId);
    
    /**
     * 根据菜单ID查询权限
     * @param menuId 菜单ID
     * @return 权限列表
     */
    List<PermissionModel> findByMenuId(Long menuId);

    PageResult<PermissionModel> findPage(PermissionModel permissionModel);
}
