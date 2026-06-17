package com.piaomiao.web.vo;

import com.piaomiao.dto.MenuDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class MenuVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String path;
    private String icon;
    private Long parentId;
    private Integer sort;
    private Integer status;

    public static MenuDTO toDTO(MenuVO vo) {
        MenuDTO dto = new MenuDTO();
        dto.setId(vo.getId());
        dto.setName(vo.getName());
        dto.setPath(vo.getPath());
        dto.setIcon(vo.getIcon());
        dto.setParentId(vo.getParentId());
        dto.setSort(vo.getSort());
        dto.setStatus(vo.getStatus());
        return dto;
    }
}
