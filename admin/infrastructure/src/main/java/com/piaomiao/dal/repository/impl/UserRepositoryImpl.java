package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piaomiao.dal.entity.RoleDO;
import com.piaomiao.dal.entity.UserDO;
import com.piaomiao.dal.mapper.UserMapper;
import com.piaomiao.model.sys.RoleModel;
import com.piaomiao.model.sys.UserModel;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.repository.sys.UserRepository;
import com.piaomiao.response.PageResult;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Repository
public class UserRepositoryImpl implements UserRepository {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private com.piaomiao.dal.mapper.RoleMapper roleMapper;

    @Override
    public UserModel findById(Long id) {
        UserDO userDO = userMapper.selectById(id);
        if (userDO == null) {
            return null;
        }
        UserModel userModel = userDO.toModel();
        // 加载用户角色
        List<com.piaomiao.dal.entity.RoleDO> roleDOs = roleMapper.selectByUserId(id);
        if (roleDOs != null && !roleDOs.isEmpty()) {
            List<RoleModel> roles = getRoleModels(roleDOs);
            userModel.setRoles(roles);
        }
        return userModel;
    }

    private static @NonNull List<RoleModel> getRoleModels(List<RoleDO> roleDOs) {
        List<RoleModel> roles = new ArrayList<>();
        for (RoleDO roleDO : roleDOs) {
            RoleModel roleModel = new RoleModel();
            roleModel.setId(roleDO.getId());
            roleModel.setName(roleDO.getName());
            roleModel.setCode(roleDO.getCode());
            roleModel.setDescription(roleDO.getDescription());
            roleModel.setStatus(roleDO.getStatus());
            roles.add(roleModel);
        }
        return roles;
    }

    @Override
    public UserModel findByUsername(String username) {
        UserDO userDO = userMapper.selectByUsername(username);
        if (userDO == null) {
            return null;
        }
        return userDO.toModel();
    }

    @Override
    public List<UserModel> findAll() {
        List<UserModel> userModels;
        if (com.piaomiao.web.interceptor.DataPermissionContext.isAllData()) {
            List<UserDO> userDOs = userMapper.selectList(null);
            userModels = userDOs.stream()
                    .map(UserDO::toModel)
                    .collect(Collectors.toList());
        } else if (com.piaomiao.web.interceptor.DataPermissionContext.isSelfData()) {
            Long currentUserId = com.piaomiao.web.interceptor.DataPermissionContext.getCurrentUserId();
            if (currentUserId != null) {
                QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
                wrapper.eq("id", currentUserId);
                List<UserDO> userDOs = userMapper.selectList(wrapper);
                userModels = userDOs.stream()
                        .map(UserDO::toModel)
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } else {
            List<Long> departmentIds = com.piaomiao.web.interceptor.DataPermissionContext.getCurrentUserDepartmentIds();
            
            if (departmentIds != null && !departmentIds.isEmpty()) {
                userModels = findByDepartmentIds(departmentIds);
            } else {
                List<Long> userIds = com.piaomiao.web.interceptor.DataPermissionContext.getCurrentUserIds();
                
                if (userIds != null && !userIds.isEmpty()) {
                    QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
                    wrapper.in("id", userIds);
                    List<UserDO> userDOs = userMapper.selectList(wrapper);
                    userModels = userDOs.stream()
                            .map(UserDO::toModel)
                            .collect(Collectors.toList());
                } else {
                    List<UserDO> userDOs = userMapper.selectList(null);
                    userModels = userDOs.stream()
                            .map(UserDO::toModel)
                            .collect(Collectors.toList());
                }
            }
        }
        
        // 为每个用户加载角色信息
        for (UserModel userModel : userModels) {
            List<com.piaomiao.dal.entity.RoleDO> roleDOs = roleMapper.selectByUserId(userModel.getId());
            if (roleDOs != null && !roleDOs.isEmpty()) {
                List<RoleModel> roles = getRoleModels(roleDOs);
                userModel.setRoles(roles);
            }
        }
        
        return userModels;
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
        UserDO userDO = UserDO.fromModel(userModel);
        AuditFieldUtil.fillForInsert(userDO);
        userMapper.insert(userDO);
        return userDO.getId();
    }

    @Override
    public void update(UserModel userModel) {
        UserDO userDO = UserDO.fromModel(userModel);
        AuditFieldUtil.fillForUpdate(userDO);
        userMapper.updateById(userDO);
    }

    @Override
    public void delete(Long id) {
        userMapper.deleteById(id);
    }

    @Override
    public List<UserModel> findByRoleId(Long roleId) {
        return Collections.emptyList();
    }

    @Override
    public List<UserModel> findByDepartmentId(Long departmentId) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id", departmentId);
        List<UserDO> userDOs = userMapper.selectList(wrapper);
        return userDOs.stream()
                .map(UserDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> findByDepartmentIds(List<Long> departmentIds) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.in("department_id", departmentIds);
        List<UserDO> userDOs = userMapper.selectList(wrapper);
        return userDOs.stream()
                .map(UserDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<UserModel> findPage(int pageNum, int pageSize, String sortField, String sortOrder) {
        Page<UserDO> page = new Page<>(pageNum, pageSize);
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        
        // 添加排序
        if (sortField != null && !sortField.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
            if (sortOrder.equals("asc")) {
                queryWrapper.orderByAsc(sortField);
            } else {
                queryWrapper.orderByDesc(sortField);
            }
        }
        
        IPage<UserDO> userDOIPage = userMapper.selectPage(page, queryWrapper);
        List<UserModel> userModels = userDOIPage.getRecords().stream()
                .map(UserDO::toModel)
                .collect(Collectors.toList());
        
        return new PageResult<>(userDOIPage.getTotal(), userModels, pageNum, pageSize);
    }
}