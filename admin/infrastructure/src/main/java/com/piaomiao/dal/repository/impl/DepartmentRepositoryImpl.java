package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.piaomiao.dal.entity.DepartmentDO;
import com.piaomiao.dal.entity.UserDO;
import com.piaomiao.dal.mapper.DepartmentMapper;
import com.piaomiao.dal.mapper.UserMapper;
import com.piaomiao.model.DepartmentModel;
import com.piaomiao.model.UserModel;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DepartmentRepositoryImpl implements DepartmentRepository {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public DepartmentModel findById(Long id) {
        DepartmentDO departmentDO = departmentMapper.selectById(id);
        return departmentDO != null ? departmentDO.toModel() : null;
    }

    @Override
    public DepartmentModel findByCode(String code) {
        DepartmentDO departmentDO = departmentMapper.selectByCode(code);
        return departmentDO != null ? departmentDO.toModel() : null;
    }

    @Override
    public List<DepartmentModel> findAll() {
        List<DepartmentDO> departmentDOList = departmentMapper.selectAll();
        return departmentDOList.stream()
                .map(DepartmentDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentModel> findRootDepartments() {
        List<DepartmentDO> departmentDOList = departmentMapper.selectRootDepartments();
        return departmentDOList.stream()
                .map(DepartmentDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<DepartmentModel> findByParentId(Long parentId) {
        List<DepartmentDO> departmentDOList = departmentMapper.selectByParentId(parentId);
        return departmentDOList.stream()
                .map(DepartmentDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Long save(DepartmentModel departmentModel) {
        DepartmentDO departmentDO = DepartmentDO.fromModel(departmentModel);
        AuditFieldUtil.fillForInsert(departmentDO);
        departmentMapper.insert(departmentDO);
        return departmentDO.getId();
    }

    @Override
    public void update(DepartmentModel departmentModel) {
        DepartmentDO departmentDO = DepartmentDO.fromModel(departmentModel);
        AuditFieldUtil.fillForUpdate(departmentDO);
        departmentMapper.updateById(departmentDO);
    }

    @Override
    public void delete(Long id) {
        departmentMapper.deleteById(id);
    }

    @Override
    public List<UserModel> findUsersByDepartmentId(Long departmentId) {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("department_id", departmentId);
        List<UserDO> userDOList = userMapper.selectList(wrapper);
        
        return userDOList.stream()
                .map(UserDO::toModel)
                .collect(Collectors.toList());
    }
}
