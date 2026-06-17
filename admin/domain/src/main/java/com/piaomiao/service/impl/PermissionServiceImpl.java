package com.piaomiao.service.impl;

import com.piaomiao.model.PermissionModel;
import com.piaomiao.repository.PermissionRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限服务实现
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public PermissionModel findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    public PermissionModel findByCode(String code) {
        return permissionRepository.findByCode(code);
    }

    @Override
    public List<PermissionModel> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Long save(PermissionModel permissionModel) {
        return permissionRepository.save(permissionModel);
    }

    @Override
    public void update(PermissionModel permissionModel) {
        permissionRepository.update(permissionModel);
    }

    @Override
    public void delete(Long id) {
        permissionRepository.delete(id);
    }

    @Override
    public List<PermissionModel> findByRoleId(Long roleId) {
        return permissionRepository.findByRoleId(roleId);
    }

    @Override
    public List<PermissionModel> findByMenuId(Long menuId) {
        return permissionRepository.findByMenuId(menuId);
    }

    @Override
    public PageResult<PermissionModel> findPage(PermissionModel permissionModel) {
        return permissionRepository.findPage(permissionModel);
    }
}