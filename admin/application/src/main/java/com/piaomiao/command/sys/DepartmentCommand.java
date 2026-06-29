package com.piaomiao.command.sys;

import com.piaomiao.dto.sys.DepartmentDTO;
import com.piaomiao.model.sys.DepartmentModel;
import com.piaomiao.model.sys.UserModel;

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
