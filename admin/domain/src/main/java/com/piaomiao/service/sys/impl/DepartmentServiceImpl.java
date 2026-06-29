package com.piaomiao.service.sys.impl;

import com.piaomiao.annotation.DataPermission;
import com.piaomiao.model.sys.DepartmentModel;
import com.piaomiao.repository.sys.DepartmentRepository;
import com.piaomiao.service.sys.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门服务实现
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public DepartmentModel findById(Long id) {
        return departmentRepository.findById(id);
    }

    @Override
    public DepartmentModel findByCode(String code) {
        return departmentRepository.findByCode(code);
    }

    @Override
    @DataPermission(value = com.piaomiao.enums.DataPermissionType.DEPARTMENT, includeChildren = true)
    public List<DepartmentModel> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    @DataPermission(value = com.piaomiao.enums.DataPermissionType.DEPARTMENT, includeChildren = true)
    public List<DepartmentModel> findRootDepartments() {
        return departmentRepository.findRootDepartments();
    }

    @Override
    @DataPermission(value = com.piaomiao.enums.DataPermissionType.DEPARTMENT, includeChildren = true)
    public List<DepartmentModel> findByParentId(Long parentId) {
        return departmentRepository.findByParentId(parentId);
    }

    @Override
    public Long save(DepartmentModel departmentModel) {
        return departmentRepository.save(departmentModel);
    }

    @Override
    public void update(DepartmentModel departmentModel) {
        departmentRepository.update(departmentModel);
    }

    @Override
    public void delete(Long id) {
        departmentRepository.delete(id);
    }

    @Override
    public List<com.piaomiao.model.sys.UserModel> findUsersByDepartmentId(Long departmentId) {
        return departmentRepository.findUsersByDepartmentId(departmentId);
    }

    @Override
    public List<DepartmentModel> buildDepartmentTree() {
        // 1. 获取所有部门
        List<DepartmentModel> allDepartments = departmentRepository.findAll();
        
        // 2. 构建部门ID到部门的映射
        Map<Long, DepartmentModel> departmentMap = allDepartments.stream()
                .collect(Collectors.toMap(DepartmentModel::getId, department -> department));
        
        // 3. 构建部门树
        List<DepartmentModel> rootDepartments = new ArrayList<>();
        for (DepartmentModel department : allDepartments) {
            if (department.getParentId() == null || department.getParentId() == 0) {
                // 根部门
                rootDepartments.add(department);
            } else {
                // 子部门，添加到父部门的children列表中
                DepartmentModel parentDepartment = departmentMap.get(department.getParentId());
                if (parentDepartment != null) {
                    List<DepartmentModel> children = parentDepartment.getChildren();
                    if (children == null) {
                        children = new ArrayList<>();
                        parentDepartment.setChildren(children);
                    }
                    children.add(department);
                }
            }
        }
        
        return rootDepartments;
    }

    @Override
    public List<Long> getAllChildDepartmentIds(Long departmentId) {
        List<Long> departmentIds = new ArrayList<>();
        // 添加当前部门ID
        departmentIds.add(departmentId);
        
        // 递归获取所有子部门ID
        collectChildDepartmentIds(departmentId, departmentIds);
        
        return departmentIds;
    }

    /**
     * 递归收集子部门ID
     * @param parentId 父部门ID
     * @param departmentIds 部门ID列表
     */
    private void collectChildDepartmentIds(Long parentId, List<Long> departmentIds) {
        List<DepartmentModel> childDepartments = departmentRepository.findByParentId(parentId);
        for (DepartmentModel childDepartment : childDepartments) {
            departmentIds.add(childDepartment.getId());
            // 递归收集子部门的子部门
            collectChildDepartmentIds(childDepartment.getId(), departmentIds);
        }
    }
}