package com.piaomiao.dto;

import lombok.Data;

@Data
public class PermissionDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private Integer type;
    private Long menuId;
    private Long parentId;
}