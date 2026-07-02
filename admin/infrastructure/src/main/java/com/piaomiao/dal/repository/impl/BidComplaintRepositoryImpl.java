package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piaomiao.dal.entity.BidComplaintDO;
import com.piaomiao.dal.mapper.BidComplaintMapper;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.model.BidComplaintModel;
import com.piaomiao.query.BidComplaintPageQuery;
import com.piaomiao.repository.BidComplaintRepository;
import com.piaomiao.response.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 质疑投诉仓储实现
 */
@Repository
public class BidComplaintRepositoryImpl implements BidComplaintRepository {

    @Autowired
    private BidComplaintMapper bidComplaintMapper;

    @Override
    public BidComplaintModel save(BidComplaintModel model) {
        BidComplaintDO entity = BidComplaintDO.fromModel(model);
        AuditFieldUtil.fillForInsert(entity);
        bidComplaintMapper.insert(entity);
        model.setId(entity.getId());
        return model;
    }

    @Override
    public BidComplaintModel update(BidComplaintModel model) {
        BidComplaintDO entity = BidComplaintDO.fromModel(model);
        AuditFieldUtil.fillForUpdate(entity);
        bidComplaintMapper.updateById(entity);
        return model;
    }

    @Override
    public BidComplaintModel findById(Long id) {
        BidComplaintDO entity = bidComplaintMapper.selectById(id);
        return entity != null ? entity.toModel() : null;
    }

    @Override
    public PageResult<BidComplaintModel> findByPage(BidComplaintPageQuery query) {
        LambdaQueryWrapper<BidComplaintDO> countWrapper = buildQueryWrapper(query);
        long total = bidComplaintMapper.selectCount(countWrapper);

        LambdaQueryWrapper<BidComplaintDO> pageWrapper = buildQueryWrapper(query);
        pageWrapper.orderByDesc(BidComplaintDO::getCreateTime);

        int pageNum = query.getPageNum() != null ? query.getPageNum() : 1;
        int pageSize = query.getPageSize() != null ? query.getPageSize() : 10;
        Page<BidComplaintDO> pageParam = new Page<>(pageNum, pageSize, false);
        IPage<BidComplaintDO> result = bidComplaintMapper.selectPage(pageParam, pageWrapper);
        List<BidComplaintModel> models = result.getRecords().stream()
                .map(BidComplaintDO::toModel)
                .collect(Collectors.toList());
        return new PageResult<>(total, models, pageNum, pageSize);
    }

    private LambdaQueryWrapper<BidComplaintDO> buildQueryWrapper(BidComplaintPageQuery query) {
        LambdaQueryWrapper<BidComplaintDO> wrapper = new LambdaQueryWrapper<>();
        if (query.getProjectId() != null) {
            wrapper.eq(BidComplaintDO::getProjectId, query.getProjectId());
        }
        if (query.getType() != null) {
            wrapper.eq(BidComplaintDO::getType, query.getType());
        }
        if (query.getStatus() != null) {
            wrapper.eq(BidComplaintDO::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
