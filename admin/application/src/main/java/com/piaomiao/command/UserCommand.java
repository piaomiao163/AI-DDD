package com.piaomiao.command;

import com.piaomiao.dto.UserDTO;
import com.piaomiao.dto.UserQueryDTO;
import com.piaomiao.model.UserModel;
import com.piaomiao.response.PageResult;

import java.util.List;

public interface UserCommand {
    UserModel findById(Long id);
    UserModel findByUsername(String username);
    List<UserModel> findAll();
    PageResult<UserModel> findPage(UserQueryDTO queryDTO);
    Long save(UserDTO userDTO);
    void update(UserDTO userDTO);
    void delete(Long id);
    void deleteBatch(List<Long> ids);
    List<UserModel> findByRoleId(Long roleId);
    List<com.piaomiao.model.RoleModel> getUserRoles(Long userId);
    void assignUserRoles(Long userId, List<Long> roleIds);
}
