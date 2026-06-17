package com.piaomiao.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private Integer status;
    private Long departmentId;
    private String createBy;
    private String updateBy;
    private java.util.List<Long> roles;
}
