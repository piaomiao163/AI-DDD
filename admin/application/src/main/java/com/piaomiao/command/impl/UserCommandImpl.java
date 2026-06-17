package com.piaomiao.command.impl;

import com.piaomiao.command.UserCommand;
import com.piaomiao.dto.UserDTO;
import com.piaomiao.dto.UserQueryDTO;
import com.piaomiao.model.DepartmentModel;
import com.piaomiao.model.RoleModel;
import com.piaomiao.model.UserModel;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.DepartmentService;
import com.piaomiao.service.RoleService;
import com.piaomiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserCommandImpl implements UserCommand {

    @Autowired
    private UserService userService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserModel findById(Long id) {
        return userService.findById(id);
    }

    @Override
    public UserModel findByUsername(String username) {
        return userService.findByUsername(username);
    }

    @Override
    public List<UserModel> findAll() {
        return userService.findAll();
    }

    @Override
    public PageResult<UserModel> findPage(UserQueryDTO queryDTO) {
        PageResult<UserModel> page = userService.findPage(queryDTO.getPageNum(), queryDTO.getPageSize(), queryDTO.getSortField(), queryDTO.getSortOrder());
        List<UserModel> models = page.getList().stream().peek(this::loadUserDepartmentAndRoles).collect(Collectors.toList());
        page.setList(models);
        return page;

    }

    /**
     * 加载用户的部门和角色信息
     * @param userModel 用户模型
     */
    private void loadUserDepartmentAndRoles(UserModel userModel) {
        // 加载部门信息
        if (userModel.getDepartmentId() != null) {
            DepartmentModel departmentModel = departmentService.findById(userModel.getDepartmentId());
            userModel.setDepartment(departmentModel);
        }
        // 加载角色信息
        List<RoleModel> roleModels = roleService.findByUserId(userModel.getId());
        if (roleModels != null && !roleModels.isEmpty()) {
            userModel.setRoles(roleModels);
        }
    }

    @Override
    public Long save(UserDTO userDTO) {
        UserModel userModel = new UserModel();
        userModel.setUsername(userDTO.getUsername());
        userModel.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userModel.setNickname(userDTO.getNickname());
        userModel.setEmail(userDTO.getEmail());
        userModel.setPhone(userDTO.getPhone());
        userModel.setStatus(userDTO.getStatus());
        userModel.setDepartmentId(userDTO.getDepartmentId());
        return userService.save(userModel);
    }

    @Override
    public void update(UserDTO userDTO) {
        UserModel userModel = new UserModel();
        userModel.setId(userDTO.getId());
        userModel.setUsername(userDTO.getUsername());
        // 密码不为空时才加密更新，为空则不修改密码
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            userModel.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        userModel.setNickname(userDTO.getNickname());
        userModel.setEmail(userDTO.getEmail());
        userModel.setPhone(userDTO.getPhone());
        userModel.setStatus(userDTO.getStatus());
        userModel.setDepartmentId(userDTO.getDepartmentId());
        userService.update(userModel);
    }

    @Override
    public void delete(Long id) {
        userService.delete(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            userService.delete(id);
        }
    }

    @Override
    public List<UserModel> findByRoleId(Long roleId) {
        return userService.findByRoleId(roleId);
    }

    @Override
    public List<com.piaomiao.model.RoleModel> getUserRoles(Long userId) {
        return userService.findById(userId).getRoles();
    }

    @Override
    public void assignUserRoles(Long userId, List<Long> roleIds) {
        userService.assignRoles(userId, roleIds);
    }
}
