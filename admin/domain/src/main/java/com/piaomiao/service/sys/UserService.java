package com.piaomiao.service.sys;

import com.piaomiao.model.sys.UserModel;
import com.piaomiao.response.PageResult;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 根据ID查询用户
     * @param id 用户ID
     * @return 用户模型
     */
    UserModel findById(Long id);
    
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户模型
     */
    UserModel findByUsername(String username);
    
    /**
     * 查询所有用户
     * @return 用户列表
     */
    List<UserModel> findAll();

    /**
     * 分页查询用户
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return 分页结果
     */
    PageResult<UserModel> findPage(int pageNum, int pageSize, String sortField, String sortOrder);

    /**
     * 保存用户
     * @param userModel 用户模型
     * @return 用户ID
     */
    Long save(UserModel userModel);
    
    /**
     * 更新用户
     * @param userModel 用户模型
     */
    void update(UserModel userModel);
    
    /**
     * 删除用户
     * @param id 用户ID
     */
    void delete(Long id);
    
    /**
     * 为用户分配角色
     * @param userId 用户ID
     * @param roleIds 角色ID列表
     */
    void assignRoles(Long userId, List<Long> roleIds);
    
    /**
     * 根据角色ID查询用户
     * @param roleId 角色ID
     * @return 用户列表
     */
    List<UserModel> findByRoleId(Long roleId);
    
    /**
     * 根据部门ID查询用户
     * @param departmentId 部门ID
     * @return 用户列表
     */
    List<UserModel> findByDepartmentId(Long departmentId);
}
