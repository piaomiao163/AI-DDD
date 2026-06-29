package com.piaomiao.dto.sys;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private Long departmentId;
}