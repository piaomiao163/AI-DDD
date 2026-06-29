package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piaomiao.dal.entity.BidProjectDO;
import com.piaomiao.dal.mapper.BidProjectMapper;
import com.piaomiao.dal.util.AuditFieldUtil;
import com.piaomiao.model.BidProjectModel;
import com.piaomiao.query.BidProjectPageQuery;
import com.piaomiao.repository.BidProjectRepository;
import com.piaomiao.response.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BidProjectRepositoryImpl implements BidProjectRepository {

    @Autowired
    private BidProjectMapper bidProjectMapper;

    @Override
    public BidProjectModel save(BidProjectModel model) {
        BidProjectDO entity = BidProjectDO.fromModel(model);
        if (entity.getProjectNo() == null || entity.getProjectNo().isEmpty()) {
            String no = "BID" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                    + String.format("%04d", (int)(Math.random() * 10000));
            entity.setProjectNo(no);
        }
        AuditFieldUtil.fillForInsert(entity);
        bidProjectMapper.insert(entity);
        model.setId(entity.getId());
        return model;
    }

    @Override
    public BidProjectModel update(BidProjectModel model) {
        BidProjectDO entity = BidProjectDO.fromModel(model);
        AuditFieldUtil.fillForUpdate(entity);
        bidProjectMapper.updateById(entity);
        return model;
    }

    @Override
    public BidProjectModel findById(Long id) {
        BidProjectDO entity = bidProjectMapper.selectById(id);
        return entity != null ? entity.toModel() : null;
    }

    @Override
    public void deleteById(Long id) {
        bidProjectMapper.deleteById(id);
    }

    @Override
    public PageResult<BidProjectModel> findByPage(BidProjectPageQuery query) {
        LambdaQueryWrapper<BidProjectDO> countWrapper = new LambdaQueryWrapper<>();
        if (query.getProjectName() != null && !query.getProjectName().isEmpty()) {
            countWrapper.like(BidProjectDO::getProjectName, query.getProjectName());
        }
        if (query.getPurchaseType() != null) {
            countWrapper.eq(BidProjectDO::getPurchaseType, query.getPurchaseType());
        }
        if (query.getStatus() != null) {
            countWrapper.eq(BidProjectDO::getStatus, query.getStatus());
        }
        long total = bidProjectMapper.selectCount(countWrapper);

        LambdaQueryWrapper<BidProjectDO> pageWrapper = new LambdaQueryWrapper<>();
        if (query.getProjectName() != null && !query.getProjectName().isEmpty()) {
            pageWrapper.like(BidProjectDO::getProjectName, query.getProjectName());
        }
        if (query.getPurchaseType() != null) {
            pageWrapper.eq(BidProjectDO::getPurchaseType, query.getPurchaseType());
        }
        if (query.getStatus() != null) {
            pageWrapper.eq(BidProjectDO::getStatus, query.getStatus());
        }
        pageWrapper.orderByDesc(BidProjectDO::getCreateTime);

        int pageNum = query.getPageNum() != null ? query.getPageNum() : 1;
        int pageSize = query.getPageSize() != null ? query.getPageSize() : 10;
        Page<BidProjectDO> pageParam = new Page<>(pageNum, pageSize, false);
        IPage<BidProjectDO> result = bidProjectMapper.selectPage(pageParam, pageWrapper);
        List<BidProjectModel> models = result.getRecords().stream()
                .map(BidProjectDO::toModel)
                .collect(Collectors.toList());
        return new PageResult<>(total, models, pageNum, pageSize);
    }
}
