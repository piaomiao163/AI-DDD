package com.piaomiao.web.vo.sys;

import com.piaomiao.dto.sys.DepartmentDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class DepartmentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String code;
    private Long parentId;
    private Integer level;
    private Integer sort;
    private Integer status;

    public static DepartmentDTO toDTO(DepartmentVO vo) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(vo.getId());
        dto.setName(vo.getName());
        dto.setCode(vo.getCode());
        dto.setParentId(vo.getParentId());
        dto.setLevel(vo.getLevel());
        dto.setSort(vo.getSort());
        dto.setStatus(vo.getStatus());
        return dto;
    }
}
