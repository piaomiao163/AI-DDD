package com.piaomiao.command.sys.impl;

import com.piaomiao.command.sys.RoleCommand;
import com.piaomiao.dto.sys.RoleDTO;
import com.piaomiao.model.sys.RoleModel;
import com.piaomiao.service.sys.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleCommandImpl implements RoleCommand {

    @Autowired
    private RoleService roleService;

    @Override
    public RoleModel findById(Long id) {
        return roleService.findById(id);
    }

    @Override
    public RoleModel findByCode(String code) {
        return roleService.findByCode(code);
    }

    @Override
    public List<RoleModel> findAll() {
        return roleService.findAll();
    }

    @Override
    public Long save(RoleDTO roleDTO) {
        RoleModel roleModel = new RoleModel();
        roleModel.setName(roleDTO.getName());
        roleModel.setCode(roleDTO.getCode());
        roleModel.setDescription(roleDTO.getDescription());
        roleModel.setStatus(roleDTO.getStatus());
        return roleService.save(roleModel);
    }

    @Override
    public void update(RoleDTO roleDTO) {
        RoleModel roleModel = new RoleModel();
        roleModel.setId(roleDTO.getId());
        roleModel.setName(roleDTO.getName());
        roleModel.setCode(roleDTO.getCode());
        roleModel.setDescription(roleDTO.getDescription());
        roleModel.setStatus(roleDTO.getStatus());
        roleService.update(roleModel);
    }

    @Override
    public void delete(Long id) {
        roleService.delete(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            roleService.delete(id);
        }
    }

    @Override
    public List<com.piaomiao.model.sys.PermissionModel> getRolePermissions(Long roleId) {
        return roleService.findById(roleId).getPermissions();
    }

    @Override
    public void assignPermissions(Long roleId, List<Long> permissionIds) {
        roleService.assignPermissions(roleId, permissionIds);
    }
}
