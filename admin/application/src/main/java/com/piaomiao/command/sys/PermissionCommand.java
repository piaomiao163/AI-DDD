package com.piaomiao.command.sys;

import com.piaomiao.dto.sys.PermissionDTO;
import com.piaomiao.dto.sys.PermissionQueryPageDTO;
import com.piaomiao.model.sys.PermissionModel;
import com.piaomiao.response.PageResult;

import java.util.List;

public interface PermissionCommand {
    PermissionModel findById(Long id);
    PermissionModel findByCode(String code);
    List<PermissionModel> findAll();

    Long save(PermissionDTO permissionDTO);
    void update(PermissionDTO permissionDTO);
    void delete(Long id);

    PageResult<PermissionModel> findPage(PermissionQueryPageDTO dto);
}
