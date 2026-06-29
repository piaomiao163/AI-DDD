package com.piaomiao.dto.sys;

import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String code;
    private String name;
    private Integer sort;
    private Integer status;
    private String remark;
}
