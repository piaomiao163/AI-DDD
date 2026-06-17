package com.piaomiao.web.vo;

import com.piaomiao.dto.RegisterDTO;
import lombok.Data;

@Data
public class RegisterVO {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private Long departmentId;

    public static RegisterDTO toDTO(RegisterVO vo) {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername(vo.getUsername());
        dto.setPassword(vo.getPassword());
        dto.setNickname(vo.getNickname());
        dto.setEmail(vo.getEmail());
        dto.setPhone(vo.getPhone());
        dto.setDepartmentId(vo.getDepartmentId());
        return dto;
    }
}