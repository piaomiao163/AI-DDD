package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piaomiao.dal.entity.BidContractDO;
import com.piaomiao.dal.mapper.BidContractMapper;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.model.BidContractModel;
import com.piaomiao.query.BidContractPageQuery;
import com.piaomiao.repository.BidContractRepository;
import com.piaomiao.response.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 合同仓储实现
 */
@Repository
public class BidContractRepositoryImpl implements BidContractRepository {

    @Autowired
    private BidContractMapper bidContractMapper;

    @Override
    public BidContractModel save(BidContractModel model) {
        BidContractDO entity = BidContractDO.fromModel(model);
        AuditFieldUtil.fillForInsert(entity);
        bidContractMapper.insert(entity);
        model.setId(entity.getId());
        return model;
    }

    @Override
    public BidContractModel update(BidContractModel model) {
        BidContractDO entity = BidContractDO.fromModel(model);
        AuditFieldUtil.fillForUpdate(entity);
        bidContractMapper.updateById(entity);
        return model;
    }

    @Override
    public BidContractModel findById(Long id) {
        BidContractDO entity = bidContractMapper.selectById(id);
        return entity != null ? entity.toModel() : null;
    }

    @Override
    public PageResult<BidContractModel> findByPage(BidContractPageQuery query) {
        LambdaQueryWrapper<BidContractDO> countWrapper = buildQueryWrapper(query);
        long total = bidContractMapper.selectCount(countWrapper);

        LambdaQueryWrapper<BidContractDO> pageWrapper = buildQueryWrapper(query);
        pageWrapper.orderByDesc(BidContractDO::getCreateTime);

        int pageNum = query.getPageNum() != null ? query.getPageNum() : 1;
        int pageSize = query.getPageSize() != null ? query.getPageSize() : 10;
        Page<BidContractDO> pageParam = new Page<>(pageNum, pageSize, false);
        IPage<BidContractDO> result = bidContractMapper.selectPage(pageParam, pageWrapper);
        List<BidContractModel> models = result.getRecords().stream()
                .map(BidContractDO::toModel)
                .collect(Collectors.toList());
        return new PageResult<>(total, models, pageNum, pageSize);
    }

    private LambdaQueryWrapper<BidContractDO> buildQueryWrapper(BidContractPageQuery query) {
        LambdaQueryWrapper<BidContractDO> wrapper = new LambdaQueryWrapper<>();
        if (query.getProjectId() != null) {
            wrapper.eq(BidContractDO::getProjectId, query.getProjectId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(BidContractDO::getStatus, query.getStatus());
        }
        return wrapper;
    }
}
