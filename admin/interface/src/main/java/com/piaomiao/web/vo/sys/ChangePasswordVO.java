package com.piaomiao.web.vo.sys;

import com.piaomiao.dto.sys.ChangePasswordDTO;
import lombok.Data;

@Data
public class ChangePasswordVO {
    private Long id;
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public String getPassword() {
        return newPassword;
    }

    public static ChangePasswordDTO toDTO(ChangePasswordVO vo) {
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setId(vo.getId());
        dto.setOldPassword(vo.getOldPassword());
        dto.setNewPassword(vo.getNewPassword());
        dto.setConfirmPassword(vo.getConfirmPassword());
        return dto;
    }
}