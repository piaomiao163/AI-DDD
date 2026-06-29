package com.piaomiao.service.sys.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.piaomiao.model.sys.DemoModel;
import com.piaomiao.repository.sys.DemoRepository;
import com.piaomiao.service.sys.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    private DemoRepository demoRepository;
    @Override
    public DemoModel hello(DemoModel demoModel) {
        return demoRepository.findById(1L);
    }

    @Override
    public Boolean save(DemoModel demoModel) {
        List<DemoModel> models = demoRepository.findAllByName(demoModel.getName());
        if (CollectionUtil.isNotEmpty(models)){
            throw new IllegalArgumentException("姓名已存在");
        }
        return demoRepository.insert(demoModel) > 0;
    }
}
