package com.piaomiao.command;

import com.piaomiao.dto.ChangePasswordDTO;
import com.piaomiao.dto.LoginDTO;
import com.piaomiao.dto.RegisterDTO;
import com.piaomiao.model.UserModel;

import java.util.Map;

public interface AuthCommand {
    Map<String, Object> login(LoginDTO loginDTO);
    Map<String, Object> register(RegisterDTO registerDTO);
    Map<String, Object> changePassword(ChangePasswordDTO changePasswordDTO);
    UserModel getCurrentUser();
}
