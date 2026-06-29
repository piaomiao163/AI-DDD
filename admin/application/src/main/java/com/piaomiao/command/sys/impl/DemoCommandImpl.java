package com.piaomiao.command.sys.impl;

import com.piaomiao.command.sys.DemoCommand;
import com.piaomiao.dto.sys.DemoDTO;
import com.piaomiao.model.sys.DemoModel;
import com.piaomiao.service.sys.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DemoCommandImpl implements DemoCommand {
    @Autowired
    private DemoService demoService;
    @Override
    public String hello(DemoDTO demoDTO) {
        DemoModel demoModel = new DemoModel();
        demoModel.setName(demoDTO.getName());
        demoModel.setAge(demoDTO.getAge());
        DemoModel result = demoService.hello(demoModel);
        return "hello " + result.getName();
    }

    @Override
    public Boolean save(DemoDTO demo) {
        DemoModel demoModel = new DemoModel();
        demoModel.setName(demo.getName());
        demoModel.setAge(demo.getAge());
        return demoService.save(demoModel);
    }
}
