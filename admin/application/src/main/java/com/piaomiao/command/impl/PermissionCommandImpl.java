package com.piaomiao.command.impl;

import com.piaomiao.command.PermissionCommand;
import com.piaomiao.dto.PermissionDTO;
import com.piaomiao.dto.PermissionQueryPageDTO;
import com.piaomiao.model.PermissionModel;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionCommandImpl implements PermissionCommand {

    @Autowired
    private PermissionService permissionService;

    // 需要过滤掉的内部权限编码
    private static final List<String> HIDDEN_PERMISSIONS = Arrays.asList(
            "system:menu:root",
            "system:menu:children",
            "system:menu:tree",
            "system:department:root",
            "system:department:children",
            "system:department:users",
            "system:user:byRole"
    );

    @Override
    public PermissionModel findById(Long id) {
        return permissionService.findById(id);
    }

    @Override
    public PermissionModel findByCode(String code) {
        return permissionService.findByCode(code);
    }

    @Override
    public List<PermissionModel> findAll() {
        // 过滤掉内部使用的权限
        return permissionService.findAll().stream()
                .filter(permission -> !HIDDEN_PERMISSIONS.contains(permission.getCode()))
                .collect(Collectors.toList());
    }


    @Override
    public Long save(PermissionDTO permissionDTO) {
        PermissionModel permissionModel = new PermissionModel();
        permissionModel.setName(permissionDTO.getName());
        permissionModel.setCode(permissionDTO.getCode());
        permissionModel.setDescription(permissionDTO.getDescription());
        permissionModel.setType(permissionDTO.getType());
        permissionModel.setMenuId(permissionDTO.getMenuId());
        permissionModel.setParentId(permissionDTO.getParentId());
        return permissionService.save(permissionModel);
    }

    @Override
    public void update(PermissionDTO permissionDTO) {
        PermissionModel permissionModel = new PermissionModel();
        permissionModel.setId(permissionDTO.getId());
        permissionModel.setName(permissionDTO.getName());
        permissionModel.setCode(permissionDTO.getCode());
        permissionModel.setDescription(permissionDTO.getDescription());
        permissionModel.setType(permissionDTO.getType());
        permissionModel.setMenuId(permissionDTO.getMenuId());
        permissionModel.setParentId(permissionDTO.getParentId());
        permissionService.update(permissionModel);
    }

    @Override
    public void delete(Long id) {
        permissionService.delete(id);
    }

    @Override
    public PageResult<PermissionModel> findPage(PermissionQueryPageDTO dto) {
        PermissionModel model = dto.toModel();
        model.setPageNum(dto.getPageNum());
        model.setPageSize(dto.getPageSize());
        PageResult<PermissionModel> page = permissionService.findPage(model);
        // 过滤掉内部使用的权限
        List<PermissionModel> models = page.getList().stream()
                .filter(permission -> !HIDDEN_PERMISSIONS.contains(permission.getCode()))
                .collect(Collectors.toList());
        page.setList(models);
        return page;
    }
}
