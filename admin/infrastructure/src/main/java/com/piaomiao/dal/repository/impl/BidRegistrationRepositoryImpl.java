package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piaomiao.dal.entity.BidRegistrationDO;
import com.piaomiao.dal.mapper.BidRegistrationMapper;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.model.BidRegistrationModel;
import com.piaomiao.query.BidRegistrationPageQuery;
import com.piaomiao.repository.BidRegistrationRepository;
import com.piaomiao.response.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应商报名仓储实现
 */
@Repository
public class BidRegistrationRepositoryImpl implements BidRegistrationRepository {

    @Autowired
    private BidRegistrationMapper bidRegistrationMapper;

    @Override
    public BidRegistrationModel save(BidRegistrationModel model) {
        BidRegistrationDO entity = BidRegistrationDO.fromModel(model);
        AuditFieldUtil.fillForInsert(entity);
        bidRegistrationMapper.insert(entity);
        model.setId(entity.getId());
        return model;
    }

    @Override
    public BidRegistrationModel update(BidRegistrationModel model) {
        BidRegistrationDO entity = BidRegistrationDO.fromModel(model);
        AuditFieldUtil.fillForUpdate(entity);
        bidRegistrationMapper.updateById(entity);
        return model;
    }

    @Override
    public BidRegistrationModel findById(Long id) {
        BidRegistrationDO entity = bidRegistrationMapper.selectById(id);
        return entity != null ? entity.toModel() : null;
    }

    @Override
    public List<BidRegistrationModel> findByProjectId(Long projectId) {
        LambdaQueryWrapper<BidRegistrationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BidRegistrationDO::getProjectId, projectId)
                .orderByDesc(BidRegistrationDO::getCreateTime);
        return bidRegistrationMapper.selectList(wrapper).stream()
                .map(BidRegistrationDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public BidRegistrationModel findByProjectIdAndUserId(Long projectId, Long userId) {
        LambdaQueryWrapper<BidRegistrationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BidRegistrationDO::getProjectId, projectId)
                .eq(BidRegistrationDO::getUserId, userId)
                .last("LIMIT 1");
        BidRegistrationDO entity = bidRegistrationMapper.selectOne(wrapper);
        return entity != null ? entity.toModel() : null;
    }

    @Override
    public PageResult<BidRegistrationModel> findByPage(BidRegistrationPageQuery query) {
        LambdaQueryWrapper<BidRegistrationDO> countWrapper = buildQueryWrapper(query);
        long total = bidRegistrationMapper.selectCount(countWrapper);

        LambdaQueryWrapper<BidRegistrationDO> pageWrapper = buildQueryWrapper(query);
        pageWrapper.orderByDesc(BidRegistrationDO::getCreateTime);

        int pageNum = query.getPageNum() != null ? query.getPageNum() : 1;
        int pageSize = query.getPageSize() != null ? query.getPageSize() : 10;
        Page<BidRegistrationDO> pageParam = new Page<>(pageNum, pageSize, false);
        IPage<BidRegistrationDO> result = bidRegistrationMapper.selectPage(pageParam, pageWrapper);
        List<BidRegistrationModel> models = result.getRecords().stream()
                .map(BidRegistrationDO::toModel)
                .collect(Collectors.toList());
        return new PageResult<>(total, models, pageNum, pageSize);
    }

    private LambdaQueryWrapper<BidRegistrationDO> buildQueryWrapper(BidRegistrationPageQuery query) {
        LambdaQueryWrapper<BidRegistrationDO> wrapper = new LambdaQueryWrapper<>();
        if (query.getProjectId() != null) {
            wrapper.eq(BidRegistrationDO::getProjectId, query.getProjectId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(BidRegistrationDO::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
