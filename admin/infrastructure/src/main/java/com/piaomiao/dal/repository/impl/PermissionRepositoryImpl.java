package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piaomiao.dal.entity.PermissionDO;
import com.piaomiao.dal.mapper.PermissionMapper;
import com.piaomiao.model.PermissionModel;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.repository.PermissionRepository;
import com.piaomiao.response.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限仓储实现
 */
@Repository
public class PermissionRepositoryImpl implements PermissionRepository {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public PermissionModel findById(Long id) {
        PermissionDO permissionDO = permissionMapper.selectById(id);
        return permissionDO != null ? permissionDO.toModel() : null;
    }

    @Override
    public PermissionModel findByCode(String code) {
        PermissionDO permissionDO = permissionMapper.selectByCode(code);
        return permissionDO != null ? permissionDO.toModel() : null;
    }

    @Override
    public List<PermissionModel> findAll() {
        List<PermissionDO> permissionDOs = permissionMapper.selectAll();
        return permissionDOs.stream()
                .map(PermissionDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public Long save(PermissionModel permissionModel) {
        PermissionDO permissionDO = PermissionDO.fromModel(permissionModel);
        AuditFieldUtil.fillForInsert(permissionDO);
        permissionMapper.insert(permissionDO);
        return permissionDO.getId();
    }

    @Override
    public void update(PermissionModel permissionModel) {
        PermissionDO permissionDO = PermissionDO.fromModel(permissionModel);
        AuditFieldUtil.fillForUpdate(permissionDO);
        permissionMapper.updateById(permissionDO);
    }

    @Override
    public void delete(Long id) {
        permissionMapper.deleteById(id);
    }

    @Override
    public List<PermissionModel> findByRoleId(Long roleId) {
        List<PermissionDO> permissionDOs = permissionMapper.selectByRoleId(roleId);
        return permissionDOs.stream()
                .map(PermissionDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermissionModel> findByMenuId(Long menuId) {
        List<PermissionDO> permissionDOs = permissionMapper.selectByMenuId(menuId);
        return permissionDOs.stream()
                .map(PermissionDO::toModel)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<PermissionModel> findByRoleIds(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<PermissionDO> permissionDOs = permissionMapper.selectByRoleIds(roleIds);
        return permissionDOs.stream()
                .map(PermissionDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<PermissionModel> findPage(PermissionModel permissionModel) {
        Page<PermissionDO> page = new Page<>(permissionModel.getPageNum(), permissionModel.getPageSize());
        QueryWrapper<PermissionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNotNull("parent_id");
        String sortField = permissionModel.getSortField();
        String sortOrder = permissionModel.getSortOrder();

        // 添加排序
        if (sortField != null && !sortField.isEmpty() && sortOrder != null && !sortOrder.isEmpty()) {
            if (sortOrder.equals("asc")) {
                queryWrapper.orderByAsc(sortField);
            } else {
                queryWrapper.orderByDesc(sortField);
            }
        }

        IPage<PermissionDO> permissionDOIPage = permissionMapper.selectPage(page, queryWrapper);
        List<PermissionModel> permissionModels = permissionDOIPage.getRecords().stream()
                .map(PermissionDO::toModel)
                .collect(Collectors.toList());
        // 查询子节点
        List<Long> parentIds = permissionDOIPage.getRecords().stream().map(PermissionDO::getParentId).collect(Collectors.toList());
        Map<Long, List<PermissionDO>> permissionDOMap = this.selectInParentId(parentIds)
                .stream()
                .collect(Collectors.groupingBy(PermissionDO::getParentId));
        // 转换为树状结构
        permissionModels.forEach(item -> {
            List<PermissionDO> children = permissionDOMap.getOrDefault(item.getParentId(), Collections.emptyList());
            List<PermissionModel> childrenModels = children.stream().map(PermissionDO::toModel)
                .collect(Collectors.toList());
            item.setChildren(childrenModels);
        });
        return new PageResult<>(permissionDOIPage.getTotal(), permissionModels, permissionModel.getPageNum(), permissionModel.getPageSize());
    }

    private List<PermissionDO> selectInParentId(List<Long> parentIds) {
        if (parentIds == null || parentIds.isEmpty()) {
            return Collections.emptyList();
        }
        QueryWrapper<PermissionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("parent_id", parentIds);
        return permissionMapper.selectList(queryWrapper);
    }

    private List<PermissionDO> selectByParentId(Long parentId) {
        QueryWrapper<PermissionDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        return permissionMapper.selectList(queryWrapper);
    }
}