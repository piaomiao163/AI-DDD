package com.piaomiao.dto.sys;

import com.piaomiao.base.BasePageDTO;
import com.piaomiao.model.sys.PermissionModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PermissionQueryPageDTO extends BasePageDTO {
    private String name;


    public  PermissionModel toModel() {
        PermissionModel permissionModel = new PermissionModel();
        permissionModel.setName(name);
        return permissionModel;
    }
}
