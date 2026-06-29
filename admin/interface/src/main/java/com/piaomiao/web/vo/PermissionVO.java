package com.piaomiao.web.vo;

import com.piaomiao.dto.sys.PermissionDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class PermissionVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer type;
    private Long menuId;
    private Long parentId;

    public static PermissionDTO toDTO(PermissionVO vo) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(vo.getId());
        dto.setName(vo.getName());
        dto.setCode(vo.getCode());
        dto.setDescription(vo.getDescription());
        dto.setType(vo.getType());
        dto.setMenuId(vo.getMenuId());
        dto.setParentId(vo.getParentId());
        return dto;
    }
}
