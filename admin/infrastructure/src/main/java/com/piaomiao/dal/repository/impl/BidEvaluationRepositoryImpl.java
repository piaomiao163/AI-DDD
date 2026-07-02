package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.piaomiao.dal.entity.BidEvaluationDO;
import com.piaomiao.dal.mapper.BidEvaluationMapper;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.model.BidEvaluationModel;
import com.piaomiao.repository.BidEvaluationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评标记录仓储实现
 */
@Repository
public class BidEvaluationRepositoryImpl implements BidEvaluationRepository {

    @Autowired
    private BidEvaluationMapper bidEvaluationMapper;

    @Override
    public BidEvaluationModel save(BidEvaluationModel model) {
        BidEvaluationDO entity = BidEvaluationDO.fromModel(model);
        AuditFieldUtil.fillForInsert(entity);
        bidEvaluationMapper.insert(entity);
        model.setId(entity.getId());
        return model;
    }

    @Override
    public BidEvaluationModel update(BidEvaluationModel model) {
        BidEvaluationDO entity = BidEvaluationDO.fromModel(model);
        AuditFieldUtil.fillForUpdate(entity);
        bidEvaluationMapper.updateById(entity);
        return model;
    }

    @Override
    public BidEvaluationModel findById(Long id) {
        BidEvaluationDO entity = bidEvaluationMapper.selectById(id);
        return entity != null ? entity.toModel() : null;
    }

    @Override
    public List<BidEvaluationModel> findByProjectId(Long projectId) {
        LambdaQueryWrapper<BidEvaluationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BidEvaluationDO::getProjectId, projectId)
                .orderByDesc(BidEvaluationDO::getCreateTime);
        return bidEvaluationMapper.selectList(wrapper).stream()
                .map(BidEvaluationDO::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public BidEvaluationModel findByTenderIdAndExpertId(Long tenderId, Long expertId) {
        LambdaQueryWrapper<BidEvaluationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BidEvaluationDO::getTenderId, tenderId)
                .eq(BidEvaluationDO::getExpertId, expertId)
                .last("LIMIT 1");
        BidEvaluationDO entity = bidEvaluationMapper.selectOne(wrapper);
        return entity != null ? entity.toModel() : null;
    }
}
