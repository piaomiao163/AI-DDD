package com.piaomiao.dto.sys;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private Long id;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public String getPassword() {
        return newPassword;
    }
}