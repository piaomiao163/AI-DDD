package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piaomiao.dal.entity.LeaveDO;
import com.piaomiao.dal.mapper.LeaveMapper;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.model.LeaveModel;
import com.piaomiao.query.LeavePageQuery;
import com.piaomiao.query.MyLeavePageQuery;
import com.piaomiao.repository.LeaveRepository;
import com.piaomiao.response.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class LeaveRepositoryImpl implements LeaveRepository {

    @Autowired
    private LeaveMapper leaveMapper;

    @Override
    public LeaveModel save(LeaveModel model) {
        LeaveDO leaveDO = LeaveDO.fromModel(model);
        AuditFieldUtil.fillForInsert(leaveDO);
        leaveMapper.insert(leaveDO);
        model.setId(leaveDO.getId());
        return model;
    }

    @Override
    public LeaveModel update(LeaveModel model) {
        LeaveDO leaveDO = LeaveDO.fromModel(model);
        AuditFieldUtil.fillForUpdate(leaveDO);
        leaveMapper.updateById(leaveDO);
        return model;
    }

    @Override
    public LeaveModel findById(Long id) {
        LeaveDO leaveDO = leaveMapper.selectById(id);
        return leaveDO != null ? leaveDO.toModel() : null;
    }

    @Override
    public LeaveModel findByProcessInstanceId(Long processInstanceId) {
        LambdaQueryWrapper<LeaveDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LeaveDO::getProcessInstanceId, processInstanceId);
        LeaveDO leaveDO = leaveMapper.selectOne(wrapper);
        return leaveDO != null ? leaveDO.toModel() : null;
    }

    @Override
    public List<LeaveModel> findByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        List<LeaveDO> dos = leaveMapper.selectBatchIds(ids);
        return dos.stream().map(LeaveDO::toModel).collect(Collectors.toList());
    }

    @Override
    public PageResult<LeaveModel> findByApplicantId(Long applicantId, MyLeavePageQuery query) {
        LambdaQueryWrapper<LeaveDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LeaveDO::getApplicantId, applicantId)
               .orderByDesc(LeaveDO::getCreateTime);
        int pageNum = query.getPageNum() != null ? query.getPageNum() : 1;
        int pageSize = query.getPageSize() != null ? query.getPageSize() : 10;
        return queryByPage(wrapper, pageNum, pageSize);
    }

    @Override
    public PageResult<LeaveModel> findByPage(LeavePageQuery query) {
        LambdaQueryWrapper<LeaveDO> wrapper = new LambdaQueryWrapper<>();
        if (query.getTitle() != null && !query.getTitle().isEmpty()) {
            wrapper.like(LeaveDO::getTitle, query.getTitle());
        }
        if (query.getLeaveType() != null) {
            wrapper.eq(LeaveDO::getLeaveType, query.getLeaveType());
        }
        if (query.getStatus() != null) {
            wrapper.eq(LeaveDO::getStatus, query.getStatus());
        }
        wrapper.orderByDesc(LeaveDO::getCreateTime);
        int pageNum = query.getPageNum() != null ? query.getPageNum() : 1;
        int pageSize = query.getPageSize() != null ? query.getPageSize() : 10;
        return queryByPage(wrapper, pageNum, pageSize);
    }

    private PageResult<LeaveModel> queryByPage(LambdaQueryWrapper<LeaveDO> wrapper, int page, int size) {
        Page<LeaveDO> pageParam = new Page<>(page, size);
        IPage<LeaveDO> result = leaveMapper.selectPage(pageParam, wrapper);
        List<LeaveModel> models = result.getRecords().stream()
                .map(LeaveDO::toModel)
                .collect(Collectors.toList());
        return new PageResult<>(result.getTotal(), models, page, size);
    }
}
