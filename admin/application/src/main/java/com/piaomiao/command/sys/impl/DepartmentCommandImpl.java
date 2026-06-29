package com.piaomiao.command.sys.impl;

import com.piaomiao.command.sys.DepartmentCommand;
import com.piaomiao.dto.sys.DepartmentDTO;
import com.piaomiao.model.sys.DepartmentModel;
import com.piaomiao.model.sys.UserModel;
import com.piaomiao.service.sys.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentCommandImpl implements DepartmentCommand {

    @Autowired
    private DepartmentService departmentService;

    @Override
    public DepartmentModel findById(Long id) {
        return departmentService.findById(id);
    }

    @Override
    public DepartmentModel findByCode(String code) {
        return departmentService.findByCode(code);
    }

    @Override
    public List<DepartmentModel> findAll() {
        return departmentService.findAll();
    }

    @Override
    public List<DepartmentModel> findRootDepartments() {
        return departmentService.findRootDepartments();
    }

    @Override
    public List<DepartmentModel> findByParentId(Long parentId) {
        return departmentService.findByParentId(parentId);
    }

    @Override
    public Long save(DepartmentDTO departmentDTO) {
        DepartmentModel departmentModel = new DepartmentModel();
        departmentModel.setName(departmentDTO.getName());
        departmentModel.setCode(departmentDTO.getCode());
        departmentModel.setParentId(departmentDTO.getParentId());
        departmentModel.setLevel(departmentDTO.getLevel());
        departmentModel.setSort(departmentDTO.getSort());
        departmentModel.setStatus(departmentDTO.getStatus());
        return departmentService.save(departmentModel);
    }

    @Override
    public void update(DepartmentDTO departmentDTO) {
        DepartmentModel departmentModel = new DepartmentModel();
        departmentModel.setId(departmentDTO.getId());
        departmentModel.setName(departmentDTO.getName());
        departmentModel.setCode(departmentDTO.getCode());
        departmentModel.setParentId(departmentDTO.getParentId());
        departmentModel.setLevel(departmentDTO.getLevel());
        departmentModel.setSort(departmentDTO.getSort());
        departmentModel.setStatus(departmentDTO.getStatus());
        departmentService.update(departmentModel);
    }

    @Override
    public void delete(Long id) {
        departmentService.delete(id);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        for (Long id : ids) {
            departmentService.delete(id);
        }
    }

    @Override
    public List<UserModel> findUsersByDepartmentId(Long departmentId) {
        return departmentService.findUsersByDepartmentId(departmentId);
    }

    @Override
    public List<DepartmentModel> buildDepartmentTree() {
        return departmentService.buildDepartmentTree();
    }
}
