package com.piaomiao.service.sys.impl;

import com.piaomiao.model.sys.PermissionModel;
import com.piaomiao.model.sys.RoleModel;
import com.piaomiao.repository.sys.PermissionRepository;
import com.piaomiao.repository.sys.RoleRepository;
import com.piaomiao.service.sys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色服务实现
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public RoleModel findById(Long id) {
        RoleModel roleModel = roleRepository.findById(id);
        if (roleModel != null) {
            // 加载角色权限
            List<PermissionModel> permissions = permissionRepository.findByRoleId(id);
            if (permissions != null && !permissions.isEmpty()) {
                roleModel.setPermissions(permissions);
            }
        }
        return roleModel;
    }

    @Override
    public RoleModel findByCode(String code) {
        return roleRepository.findByCode(code);
    }

    @Override
    public List<RoleModel> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Long save(RoleModel roleModel) {
        return roleRepository.save(roleModel);
    }

    @Override
    public void update(RoleModel roleModel) {
        roleRepository.update(roleModel);
    }

    @Override
    public void delete(Long id) {
        roleRepository.delete(id);
    }

    @Override
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        // 先删除角色的所有权限
        roleRepository.deleteRolePermissions(roleId);
        // 再添加新的权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            for (Long permissionId : permissionIds) {
                roleRepository.insertRolePermission(roleId, permissionId);
            }
        }
    }

    @Override
    public void assignMenus(Long roleId, List<Long> menuIds) {
        // 实现为角色分配菜单的逻辑
        // 这里需要先查询角色，然后更新角色的菜单列表
        RoleModel roleModel = roleRepository.findById(roleId);
        if (roleModel != null) {
            // 这里需要实现菜单的分配逻辑
            // 实际项目中，可能需要通过中间表来管理角色和菜单的关系
            roleRepository.update(roleModel);
        }
    }

    @Override
    public List<RoleModel> findByUserId(Long userId) {
        List<RoleModel> roleModels = roleRepository.findByUserId(userId);
        if (roleModels != null && !roleModels.isEmpty()) {
            // 批量加载角色权限，避免N+1查询
            List<Long> roleIds = roleModels.stream()
                    .map(RoleModel::getId)
                    .collect(Collectors.toList());
            List<PermissionModel> allPermissions = permissionRepository.findByRoleIds(roleIds);
            
            if (allPermissions != null && !allPermissions.isEmpty()) {
                // 按角色ID分组
                Map<Long, List<PermissionModel>> permissionMap = allPermissions.stream()
                        .collect(Collectors.groupingBy(PermissionModel::getRoleId));
                
                // 为每个角色设置权限
                for (RoleModel roleModel : roleModels) {
                    List<PermissionModel> permissions = permissionMap.get(roleModel.getId());
                    if (permissions != null && !permissions.isEmpty()) {
                        roleModel.setPermissions(permissions);
                    }
                }
            }
        }
        return roleModels;
    }

    @Override
    public List<Long> findPermissionIdsByRoleId(Long roleId) {
        return roleRepository.findPermissionIdsByRoleId(roleId);
    }
}