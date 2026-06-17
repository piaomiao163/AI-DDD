package com.piaomiao.command.impl;

import com.piaomiao.command.AuthCommand;
import com.piaomiao.dto.ChangePasswordDTO;
import com.piaomiao.dto.LoginDTO;
import com.piaomiao.dto.RegisterDTO;
import com.piaomiao.model.UserModel;
import com.piaomiao.model.DepartmentModel;
import com.piaomiao.model.RoleModel;
import com.piaomiao.service.DepartmentService;
import com.piaomiao.service.RoleService;
import com.piaomiao.service.UserService;
import com.piaomiao.service.PermissionService;
import com.piaomiao.util.JwtUtil;
import com.piaomiao.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthCommandImpl implements AuthCommand {

    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        UserModel userModel = userService.findByUsername(loginDTO.getUsername());

        if (userModel == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "用户名或密码错误");
            return result;
        }
        boolean matches = passwordEncoder.matches(loginDTO.getPassword(), userModel.getPassword());
        if (!matches) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "用户名或密码错误");
            return result;
        }

        String token = JwtUtil.generateToken(userModel.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("token", token);
        result.put("user", userModel);
        return result;
    }

    @Override
    public Map<String, Object> register(RegisterDTO registerDTO) {
        UserModel existingUser = userService.findByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "用户名已存在");
            return result;
        }

        UserModel userModel = new UserModel();
        userModel.setUsername(registerDTO.getUsername());
        userModel.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        userModel.setNickname(registerDTO.getNickname());
        userModel.setEmail(registerDTO.getEmail());
        userModel.setPhone(registerDTO.getPhone());
        userModel.setDepartmentId(registerDTO.getDepartmentId());
        userModel.setStatus(1);


        String token = JwtUtil.generateToken(userModel.getUsername());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "注册成功");
        result.put("token", token);
        result.put("user", userModel);
        return result;
    }

    @Override
    public Map<String, Object> changePassword(ChangePasswordDTO changePasswordDTO) {
        UserModel userModel = userService.findById(changePasswordDTO.getId());
        if (userModel == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "用户不存在");
            return result;
        }

        boolean matches = passwordEncoder.matches(changePasswordDTO.getOldPassword(), userModel.getPassword());
        if (!matches) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "旧密码错误");
            return result;
        }

        userModel.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        userService.update(userModel);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "密码修改成功");
        return result;
    }

    @Override
    public UserModel getCurrentUser() {
        if (!UserContext.isAuthenticated()) {
            return null;
        }
        String username = UserContext.getCurrentUsername();
        UserModel userModel = userService.findByUsername(username);
        if (userModel != null) {
            // 加载用户的部门和角色信息
            if (userModel.getDepartmentId() != null) {
                DepartmentModel departmentModel = departmentService.findById(userModel.getDepartmentId());
                userModel.setDepartment(departmentModel);
            }
            // 加载角色信息（已包含权限信息）
            List<RoleModel> roleModels = roleService.findByUserId(userModel.getId());
            if (roleModels != null && !roleModels.isEmpty()) {
                // 检查是否有admin角色
                boolean isAdmin = false;
                for (RoleModel roleModel : roleModels) {
                    if ("admin".equals(roleModel.getCode())) {
                        isAdmin = true;
                        // 为admin角色加载所有权限
                        List<com.piaomiao.model.PermissionModel> allPermissions = permissionService.findAll();
                        roleModel.setPermissions(allPermissions);
                    }
                }
                userModel.setRoles(roleModels);
            }
        }
        return userModel;
    }
}
