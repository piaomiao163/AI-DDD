package com.piaomiao.command;

import com.piaomiao.dto.RoleDTO;
import com.piaomiao.model.RoleModel;

import java.util.List;

public interface RoleCommand {
    RoleModel findById(Long id);
    RoleModel findByCode(String code);
    List<RoleModel> findAll();
    Long save(RoleDTO roleDTO);
    void update(RoleDTO roleDTO);
    void delete(Long id);
    void deleteBatch(List<Long> ids);
    List<com.piaomiao.model.PermissionModel> getRolePermissions(Long roleId);
    void assignPermissions(Long roleId, List<Long> permissionIds);
}
