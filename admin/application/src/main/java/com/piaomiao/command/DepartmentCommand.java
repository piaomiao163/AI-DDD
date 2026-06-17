package com.piaomiao.command;

import com.piaomiao.dto.DepartmentDTO;
import com.piaomiao.model.DepartmentModel;
import com.piaomiao.model.UserModel;

import java.util.List;

public interface DepartmentCommand {
    DepartmentModel findById(Long id);
    DepartmentModel findByCode(String code);
    List<DepartmentModel> findAll();
    List<DepartmentModel> findRootDepartments();
    List<DepartmentModel> findByParentId(Long parentId);
    Long save(DepartmentDTO departmentDTO);
    void update(DepartmentDTO departmentDTO);
    void delete(Long id);
    void deleteBatch(List<Long> ids);
    List<UserModel> findUsersByDepartmentId(Long departmentId);
    List<DepartmentModel> buildDepartmentTree();
}
