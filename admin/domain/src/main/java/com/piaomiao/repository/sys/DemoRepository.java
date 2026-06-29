package com.piaomiao.repository.sys;

import com.piaomiao.model.sys.DemoModel;

import java.util.List;

// domain 层 - 仓储接口
public interface DemoRepository {

    DemoModel findById(Long id);

    int insert(DemoModel model);

    List<DemoModel> findAllByName(String name);
}