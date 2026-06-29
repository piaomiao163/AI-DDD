package com.piaomiao.service.sys.impl;

import com.piaomiao.annotation.DataPermission;
import com.piaomiao.enums.DataPermissionType;
import com.piaomiao.model.sys.RoleModel;
import com.piaomiao.model.sys.UserModel;
import com.piaomiao.repository.sys.RoleRepository;
import com.piaomiao.repository.sys.UserRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 用户服务实现
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserModel findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public UserModel findByUsername(String username) {
        UserModel userModel = userRepository.findByUsername(username);
        if (userModel != null) {
            // 加载用户角色
            List<RoleModel> roles = roleRepository.findByUserId(userModel.getId());
            if (roles != null && !roles.isEmpty()) {
                userModel.setRoles(roles);
            }
        }
        return userModel;
    }

    @Override
    @DataPermission(value = com.piaomiao.enums.DataPermissionType.DEPARTMENT, includeChildren = true)
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }


    @Override
    @DataPermission(value = DataPermissionType.DEPARTMENT, includeChildren = true) // 使用默认值即可
    public PageResult<UserModel> findPage(int pageNum, int pageSize, String sortField, String sortOrder) {
        PageResult<UserModel> pageResult = userRepository.findPage(pageNum, pageSize, sortField, sortOrder);
        
        // 批量加载角色信息，避免N+1查询
        List<UserModel> userModels = pageResult.getList();
        if (!userModels.isEmpty()) {
            // 为每个用户单独查询角色信息
            for (UserModel userModel : userModels) {
                List<RoleModel> roles = roleRepository.findByUserId(userModel.getId());
                if (roles != null && !roles.isEmpty()) {
                    userModel.setRoles(roles);
                }
            }
        }
        
        return pageResult;
    }

    @Override
    public Long save(UserModel userModel) {
        try {
            if (userModel.getNickname() != null) {
                userModel.setNickname(new String(userModel.getNickname().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            }
            if (userModel.getEmail() != null) {
                userModel.setEmail(new String(userModel.getEmail().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            }
            if (userModel.getPhone() != null) {
                userModel.setPhone(new String(userModel.getPhone().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return userRepository.save(userModel);
    }

    @Override
    public void update(UserModel userModel) {
        userRepository.update(userModel);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(id);
    }

    @Override
    public void assignRoles(Long userId, List<Long> roleIds) {
        // 先删除用户的所有现有角色
        roleRepository.deleteUserRoles(userId);
        // 为用户分配新的角色
        if (roleIds != null && !roleIds.isEmpty()) {
            for (Long roleId : roleIds) {
                roleRepository.insertUserRole(userId, roleId);
            }
        }
    }

    @Override
    public List<UserModel> findByRoleId(Long roleId) {
        return userRepository.findByRoleId(roleId);
    }

    @Override
    public List<UserModel> findByDepartmentId(Long departmentId) {
        return userRepository.findByDepartmentId(departmentId);
    }
}