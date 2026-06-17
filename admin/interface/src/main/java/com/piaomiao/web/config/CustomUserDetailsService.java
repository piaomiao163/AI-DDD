package com.piaomiao.web.config;

import com.piaomiao.model.PermissionModel;
import com.piaomiao.model.RoleModel;
import com.piaomiao.model.UserModel;
import com.piaomiao.service.PermissionService;
import com.piaomiao.service.RoleService;
import com.piaomiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义用户详情服务
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("========== 开始加载用户: " + username + " ==========");
        
        // 从数据库获取用户信息
        UserModel userModel = userService.findByUsername(username);
        if (userModel == null) {
            System.out.println("用户不存在: " + username);
            throw new UsernameNotFoundException("用户不存在");
        }

        System.out.println("找到用户: " + userModel.getUsername() + ", ID: " + userModel.getId());

        // 构建权限列表
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 获取用户角色
        List<RoleModel> roles = roleService.findByUserId(userModel.getId());
        System.out.println("用户 " + username + " 的角色数量: " + (roles != null ? roles.size() : 0));
        
        boolean isAdmin = false;
        for (RoleModel role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getCode()));
            System.out.println("添加角色: ROLE_" + role.getCode());
            if ("admin".equals(role.getCode())) {
                isAdmin = true;
                System.out.println("用户 " + username + " 是管理员");
            }
        }

        // 如果是admin用户，分配所有权限
        if (isAdmin) {
            List<PermissionModel> allPermissions = permissionService.findAll();
            System.out.println("管理员用户，分配所有权限，总数: " + allPermissions.size());
            for (PermissionModel permission : allPermissions) {
                authorities.add(new SimpleGrantedAuthority(permission.getCode()));
            }
        } else {
            // 其他用户从数据库加载权限
            System.out.println("普通用户，从数据库加载权限");
            for (RoleModel role : roles) {
                List<PermissionModel> rolePermissions = permissionService.findByRoleId(role.getId());
                System.out.println("角色 " + role.getCode() + " 的权限数量: " + (rolePermissions != null ? rolePermissions.size() : 0));
                for (PermissionModel permission : rolePermissions) {
                    authorities.add(new SimpleGrantedAuthority(permission.getCode()));
                    System.out.println("用户 " + username + " 添加权限: " + permission.getCode());
                }
            }
            
            System.out.println("用户 " + username + " 的总权限列表: " + authorities);
        }

        // 构建用户详情
        UserDetails userDetails = User.withUsername(userModel.getUsername())
                .password(userModel.getPassword())
                .authorities(authorities)
                .build();
        
        System.out.println("用户 " + username + " 加载完成，权限总数: " + authorities.size());
        System.out.println("========== 用户加载结束 ==========");
        
        return userDetails;
    }
}