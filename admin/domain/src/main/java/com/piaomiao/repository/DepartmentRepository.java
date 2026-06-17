package com.piaomiao.repository;

import com.piaomiao.model.DepartmentModel;

import java.util.List;

/**
 * 部门仓库接口
 */
public interface DepartmentRepository {
    /**
     * 根据ID查询部门
     * @param id 部门ID
     * @return 部门模型
     */
    DepartmentModel findById(Long id);

    /**
     * 根据编码查询部门
     * @param code 部门编码
     * @return 部门模型
     */
    DepartmentModel findByCode(String code);

    /**
     * 查询所有部门
     * @return 部门列表
     */
    List<DepartmentModel> findAll();

    /**
     * 查询根部门
     * @return 根部门列表
     */
    List<DepartmentModel> findRootDepartments();

    /**
     * 根据父部门ID查询子部门
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<DepartmentModel> findByParentId(Long parentId);

    /**
     * 保存部门
     * @param departmentModel 部门模型
     * @return 部门ID
     */
    Long save(DepartmentModel departmentModel);

    /**
     * 更新部门
     * @param departmentModel 部门模型
     */
    void update(DepartmentModel departmentModel);

    /**
     * 删除部门
     * @param id 部门ID
     */
    void delete(Long id);

    /**
     * 根据部门ID查询用户
     * @param departmentId 部门ID
     * @return 用户列表
     */
    List<com.piaomiao.model.UserModel> findUsersByDepartmentId(Long departmentId);
}