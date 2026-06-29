package com.piaomiao.service.sys;

import com.piaomiao.model.sys.DemoModel;

public interface DemoService {
    DemoModel hello(DemoModel demoModel);

    Boolean save(DemoModel demoModel);
}
