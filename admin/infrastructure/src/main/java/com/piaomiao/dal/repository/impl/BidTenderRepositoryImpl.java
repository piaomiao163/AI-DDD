package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.piaomiao.dal.entity.BidTenderDO;
import com.piaomiao.dal.mapper.BidTenderMapper;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.model.BidTenderModel;
import com.piaomiao.repository.BidTenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 投标书仓储实现
 */
@Repository
public class BidTenderRepositoryImpl implements BidTenderRepository {

    @Autowired
    private BidTenderMapper bidTenderMapper;

    @Override
    public BidTenderModel save(BidTenderModel model) {
        BidTenderDO entity = BidTenderDO.fromModel(model);
        AuditFieldUtil.fillForInsert(entity);
        bidTenderMapper.insert(entity);
        model.setId(entity.getId());
        return model;
    }

    @Override
    public BidTenderModel update(BidTenderModel model) {
        BidTenderDO entity = BidTenderDO.fromModel(model);
        AuditFieldUtil.fillForUpdate(entity);
        bidTenderMapper.updateById(entity);
        return model;
    }

    @Override
    public BidTenderModel findById(Long id) {
        BidTenderDO entity = bidTenderMapper.selectById(id);
        return entity != null ? entity.toModel() : null;
    }

    @Override
    public List<BidTenderModel> findByProjectId(Long projectId) {
        LambdaQueryWrapper<BidTenderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BidTenderDO::getProjectId, projectId)
                .orderByDesc(BidTenderDO::getCreateTime);
        return bidTenderMapper.selectList(wrapper).stream()
                .map(BidTenderDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public BidTenderModel findByProjectIdAndUserId(Long projectId, Long userId) {
        LambdaQueryWrapper<BidTenderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BidTenderDO::getProjectId, projectId)
                .eq(BidTenderDO::getUserId, userId)
                .last("LIMIT 1");
        BidTenderDO entity = bidTenderMapper.selectOne(wrapper);
        return entity != null ? entity.toModel() : null;
    }
}
