package com.piaomiao.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.piaomiao.dal.entity.DepartmentDO;
import com.piaomiao.dal.entity.UserDO;

import java.util.List;

/**
 * 部门数据访问接口
 */
public interface DepartmentMapper extends BaseMapper<DepartmentDO> {
    /**
     * 根据编码查询部门
     * @param code 部门编码
     * @return 部门数据访问对象
     */
    DepartmentDO selectByCode(String code);

    /**
     * 查询所有部门
     * @return 部门数据访问对象列表
     */
    List<DepartmentDO> selectAll();

    /**
     * 查询根部门
     * @return 根部门数据访问对象列表
     */
    List<DepartmentDO> selectRootDepartments();

    /**
     * 根据父部门ID查询子部门
     * @param parentId 父部门ID
     * @return 子部门数据访问对象列表
     */
    List<DepartmentDO> selectByParentId(Long parentId);

    /**
     * 根据部门ID查询用户
     * @param departmentId 部门ID
     * @return 用户数据访问对象列表
     */
    List<UserDO> selectUsersByDepartmentId(Long departmentId);
}