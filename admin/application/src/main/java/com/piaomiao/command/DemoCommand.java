package com.piaomiao.command;

import com.piaomiao.dto.DemoDTO;

public interface DemoCommand {
    String hello(DemoDTO demoDTO);

    Boolean save(DemoDTO demo);
}
