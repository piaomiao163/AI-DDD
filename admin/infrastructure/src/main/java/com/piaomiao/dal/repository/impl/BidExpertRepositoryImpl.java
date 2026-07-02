package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piaomiao.dal.entity.BidExpertDO;
import com.piaomiao.dal.mapper.BidExpertMapper;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.model.BidExpertModel;
import com.piaomiao.query.BidExpertPageQuery;
import com.piaomiao.repository.BidExpertRepository;
import com.piaomiao.response.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 评标专家仓储实现
 */
@Repository
public class BidExpertRepositoryImpl implements BidExpertRepository {

    @Autowired
    private BidExpertMapper bidExpertMapper;

    @Override
    public BidExpertModel save(BidExpertModel model) {
        BidExpertDO entity = BidExpertDO.fromModel(model);
        AuditFieldUtil.fillForInsert(entity);
        bidExpertMapper.insert(entity);
        model.setId(entity.getId());
        return model;
    }

    @Override
    public BidExpertModel update(BidExpertModel model) {
        BidExpertDO entity = BidExpertDO.fromModel(model);
        AuditFieldUtil.fillForUpdate(entity);
        bidExpertMapper.updateById(entity);
        return model;
    }

    @Override
    public BidExpertModel findById(Long id) {
        BidExpertDO entity = bidExpertMapper.selectById(id);
        return entity != null ? entity.toModel() : null;
    }

    @Override
    public void deleteById(Long id) {
        bidExpertMapper.deleteById(id);
    }

    @Override
    public PageResult<BidExpertModel> findByPage(BidExpertPageQuery query) {
        LambdaQueryWrapper<BidExpertDO> countWrapper = buildQueryWrapper(query);
        long total = bidExpertMapper.selectCount(countWrapper);

        LambdaQueryWrapper<BidExpertDO> pageWrapper = buildQueryWrapper(query);
        pageWrapper.orderByDesc(BidExpertDO::getCreateTime);

        int pageNum = query.getPageNum() != null ? query.getPageNum() : 1;
        int pageSize = query.getPageSize() != null ? query.getPageSize() : 10;
        Page<BidExpertDO> pageParam = new Page<>(pageNum, pageSize, false);
        IPage<BidExpertDO> result = bidExpertMapper.selectPage(pageParam, pageWrapper);
        List<BidExpertModel> models = result.getRecords().stream()
                .map(BidExpertDO::toModel)
                .collect(Collectors.toList());
        return new PageResult<>(total, models, pageNum, pageSize);
    }

    private LambdaQueryWrapper<BidExpertDO> buildQueryWrapper(BidExpertPageQuery query) {
        LambdaQueryWrapper<BidExpertDO> wrapper = new LambdaQueryWrapper<>();
        if (query.getName() != null && !query.getName().isEmpty()) {
            wrapper.like(BidExpertDO::getName, query.getName());
        }
        if (query.getSpecialty() != null && !query.getSpecialty().isEmpty()) {
            wrapper.like(BidExpertDO::getSpecialty, query.getSpecialty());
        }
        return wrapper;
    }
}
