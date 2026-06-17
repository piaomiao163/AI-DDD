package com.piaomiao.service;

import com.piaomiao.model.DemoModel;

public interface DemoService {
    DemoModel hello(DemoModel demoModel);

    Boolean save(DemoModel demoModel);
}
