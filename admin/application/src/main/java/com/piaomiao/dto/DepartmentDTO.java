package com.piaomiao.dto;

import lombok.Data;

@Data
public class DepartmentDTO {
    private Long id;
    private String name;
    private String code;
    private Long parentId;
    private Integer level;
    private Integer sort;
    private Integer status;
    private String createBy;
    private String updateBy;
}