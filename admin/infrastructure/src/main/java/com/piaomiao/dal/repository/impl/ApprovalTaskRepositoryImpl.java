package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piaomiao.dal.entity.ApprovalTaskDO;
import com.piaomiao.dal.mapper.ApprovalTaskMapper;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.model.ApprovalTaskModel;
import com.piaomiao.repository.ApprovalTaskRepository;
import com.piaomiao.response.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 审批任务仓储实现
 *
 * @author system
 * @date 2026-06-14
 */
@Repository
public class ApprovalTaskRepositoryImpl implements ApprovalTaskRepository {

    @Autowired
    private ApprovalTaskMapper approvalTaskMapper;

    @Override
    public ApprovalTaskModel findById(Long id) {
        ApprovalTaskDO DO = approvalTaskMapper.selectById(id);
        return DO != null ? DO.toModel() : null;
    }

    @Override
    public List<ApprovalTaskModel> findByInstanceId(Long instanceId) {
        QueryWrapper<ApprovalTaskDO> wrapper = new QueryWrapper<>();
        wrapper.eq("instance_id", instanceId);
        wrapper.orderByAsc("node_index");
        return approvalTaskMapper.selectList(wrapper).stream()
                .map(ApprovalTaskDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApprovalTaskModel> findPendingByAssigneeId(Long assigneeId) {
        QueryWrapper<ApprovalTaskDO> wrapper = new QueryWrapper<>();
        wrapper.eq("assignee_id", assigneeId);
        wrapper.eq("status", ApprovalTaskDO.STATUS_PENDING);
        wrapper.orderByDesc("create_time");
        return approvalTaskMapper.selectList(wrapper).stream()
                .map(ApprovalTaskDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public PageResult<ApprovalTaskModel> findByPage(int page, int size, Long instanceId, String nodeName, Integer status) {
        QueryWrapper<ApprovalTaskDO> wrapper = new QueryWrapper<>();
        if (instanceId != null) {
            wrapper.eq("instance_id", instanceId);
        }
        if (nodeName != null && !nodeName.isEmpty()) {
            wrapper.like("node_name", nodeName);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        wrapper.orderByDesc("create_time");

        com.baomidou.mybatisplus.core.metadata.IPage<ApprovalTaskDO> pageResult =
                approvalTaskMapper.selectPage(new Page<>(page, size), wrapper);

        List<ApprovalTaskModel> models = pageResult.getRecords().stream()
                .map(ApprovalTaskDO::toModel)
                .collect(Collectors.toList());

        return new PageResult<>(pageResult.getTotal(), models, page, size);
    }

    @Override
    public Long save(ApprovalTaskModel model) {
        ApprovalTaskDO DO = ApprovalTaskDO.fromModel(model);
        AuditFieldUtil.fillForInsert(DO);
        approvalTaskMapper.insert(DO);
        return DO.getId();
    }

    @Override
    public void update(ApprovalTaskModel model) {
        ApprovalTaskDO DO = ApprovalTaskDO.fromModel(model);
        AuditFieldUtil.fillForUpdate(DO);
        approvalTaskMapper.updateById(DO);
    }

    @Override
    public void delete(Long id) {
        approvalTaskMapper.deleteById(id);
    }
}
