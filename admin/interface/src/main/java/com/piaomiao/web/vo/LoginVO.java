package com.piaomiao.web.vo;

import com.piaomiao.dto.LoginDTO;
import lombok.Data;

@Data
public class LoginVO {
    private String username;
    private String password;

    public static LoginDTO toDTO(LoginVO vo) {
        LoginDTO dto = new LoginDTO();
        dto.setUsername(vo.getUsername());
        dto.setPassword(vo.getPassword());
        return dto;
    }
}