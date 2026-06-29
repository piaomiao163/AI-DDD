package com.piaomiao.command.sys;

import com.piaomiao.dto.sys.ChangePasswordDTO;
import com.piaomiao.dto.sys.LoginDTO;
import com.piaomiao.dto.sys.RegisterDTO;
import com.piaomiao.model.sys.UserModel;

import java.util.Map;

public interface AuthCommand {
    Map<String, Object> login(LoginDTO loginDTO);
    Map<String, Object> register(RegisterDTO registerDTO);
    Map<String, Object> changePassword(ChangePasswordDTO changePasswordDTO);
    UserModel getCurrentUser();
}
