package com.piaomiao.web.vo.sys;

import com.piaomiao.dto.sys.RoleDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RoleVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer status;
    private List<Long> permissionIds;

    public static RoleDTO toDTO(RoleVO vo) {
        RoleDTO dto = new RoleDTO();
        dto.setId(vo.getId());
        dto.setName(vo.getName());
        dto.setCode(vo.getCode());
        dto.setDescription(vo.getDescription());
        dto.setStatus(vo.getStatus());
        return dto;
    }
}
