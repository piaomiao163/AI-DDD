package com.piaomiao.dal.repository.impl;

import com.piaomiao.dal.entity.RoleDO;
import com.piaomiao.dal.entity.PermissionDO;
import com.piaomiao.dal.mapper.RoleMapper;
import com.piaomiao.dal.mapper.PermissionMapper;
import com.piaomiao.model.sys.PermissionModel;
import com.piaomiao.model.sys.RoleModel;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.repository.sys.RoleRepository;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色仓储实现
 */
@Repository
public class RoleRepositoryImpl implements RoleRepository {

    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public RoleModel findById(Long id) {
        RoleDO roleDO = roleMapper.selectById(id);
        return roleDO != null ? roleDO.toModel() : null;
    }

    @Override
    public RoleModel findByCode(String code) {
        RoleDO roleDO = roleMapper.selectByCode(code);
        return roleDO != null ? roleDO.toModel() : null;
    }

    @Override
    public List<RoleModel> findAll() {
        List<RoleDO> roleDOs = roleMapper.selectList(null);
        return roleDOs.stream()
                .map(RoleDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Long save(RoleModel roleModel) {
        RoleDO roleDO = RoleDO.fromModel(roleModel);
        AuditFieldUtil.fillForInsert(roleDO);
        roleMapper.insert(roleDO);
        return roleDO.getId();
    }

    @Override
    public void update(RoleModel roleModel) {
        RoleDO roleDO = RoleDO.fromModel(roleModel);
        AuditFieldUtil.fillForUpdate(roleDO);
        roleMapper.updateById(roleDO);
    }

    @Override
    public void delete(Long id) {
        roleMapper.deleteById(id);
    }

    @Override
    public List<RoleModel> findByUserId(Long userId) {
        List<RoleDO> roleDOs = roleMapper.selectByUserId(userId);
        List<RoleModel> roleModels = new ArrayList<>();
        for (RoleDO roleDO : roleDOs) {
            RoleModel roleModel = roleDO.toModel();
            // 加载角色权限
            List<PermissionDO> permissionDOs = permissionMapper.selectByRoleId(roleDO.getId());
            if (permissionDOs != null && !permissionDOs.isEmpty()) {
                List<PermissionModel> permissions = getPermissionModels(permissionDOs);
                roleModel.setPermissions(permissions);
            }
            roleModels.add(roleModel);
        }
        return roleModels;
    }

    private static @NonNull List<PermissionModel> getPermissionModels(List<PermissionDO> permissionDOs) {
        List<PermissionModel> permissions = new ArrayList<>();
        for (PermissionDO permissionDO : permissionDOs) {
            PermissionModel permissionModel = new PermissionModel();
            permissionModel.setId(permissionDO.getId());
            permissionModel.setName(permissionDO.getName());
            permissionModel.setCode(permissionDO.getCode());
            permissionModel.setDescription(permissionDO.getDescription());
            permissionModel.setType(permissionDO.getType());
            permissionModel.setMenuId(permissionDO.getMenuId());
            permissionModel.setParentId(permissionDO.getParentId());
            permissions.add(permissionModel);
        }
        return permissions;
    }

    @Override
    public List<RoleModel> findByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        List<RoleDO> roleDOs = roleMapper.selectByUserIds(userIds);
        if (roleDOs == null || roleDOs.isEmpty()) {
            return Collections.emptyList();
        }
        
        return roleDOs.stream()
                .map(roleDO -> {
                    RoleModel roleModel = new RoleModel();
                    roleModel.setId(roleDO.getId());
                    roleModel.setName(roleDO.getName());
                    roleModel.setCode(roleDO.getCode());
                    roleModel.setDescription(roleDO.getDescription());
                    roleModel.setStatus(roleDO.getStatus());
                    return roleModel;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void insertRolePermission(Long roleId, Long permissionId) {
        roleMapper.insertRolePermission(roleId, permissionId);
    }

    @Override
    public void deleteRolePermissions(Long roleId) {
        roleMapper.deleteRolePermissions(roleId);
    }

    @Override
    public List<Long> findPermissionIdsByRoleId(Long roleId) {
        return roleMapper.selectPermissionIdsByRoleId(roleId);
    }

    @Override
    public void insertUserRole(Long userId, Long roleId) {
        roleMapper.insertUserRole(userId, roleId);
    }

    @Override
    public void deleteUserRoles(Long userId) {
        roleMapper.deleteUserRoles(userId);
    }
}