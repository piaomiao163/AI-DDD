package com.piaomiao.dto.sys;

import lombok.Data;

@Data
public class MenuDTO {
    private Long id;
    private String name;
    private String path;
    private String icon;
    private Long parentId;
    private Integer sort;
    private Integer status;
}
