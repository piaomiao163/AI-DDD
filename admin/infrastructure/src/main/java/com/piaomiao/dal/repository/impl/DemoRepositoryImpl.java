package com.piaomiao.dal.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.piaomiao.dal.entity.DemoDO;
import com.piaomiao.dal.mapper.DemoMapper;
import com.piaomiao.model.DemoModel;
import com.piaomiao.repository.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.stream.Collectors;

// infrastructure 层 - 仓储实现
@Repository
public class DemoRepositoryImpl implements DemoRepository {

    @Autowired
    private DemoMapper demoMapper;

    @Override
    public DemoModel findById(Long id) {
        return DemoDO.toModel(demoMapper.selectById(id));
    }

    @Override
    public int insert(DemoModel model) {
        DemoDO demoDO = DemoDO.fromModel(model);
        return demoMapper.insert(demoDO);
    }

    @Override
    public List<DemoModel> findAllByName(String name) {
        return demoMapper.selectList(new LambdaQueryWrapper<DemoDO>()
                        .eq(DemoDO::getName, name))
                .stream()
                .map(DemoDO::toModel)
                .collect(Collectors.toList());
    }

}