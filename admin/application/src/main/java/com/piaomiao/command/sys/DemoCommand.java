package com.piaomiao.command.sys;

import com.piaomiao.dto.sys.DemoDTO;

public interface DemoCommand {
    String hello(DemoDTO demoDTO);

    Boolean save(DemoDTO demo);
}
