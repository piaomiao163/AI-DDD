package com.piaomiao.web;


import com.piaomiao.command.sys.DemoCommand;
import com.piaomiao.dto.sys.DemoDTO;
import com.piaomiao.response.Response;
import com.piaomiao.rest.CallbackRest;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.DemoVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private DemoCommand demoCommand;
    @Autowired
    private TemplateRest templateRest;

    @GetMapping("/hello")
    public Response<String> hello() {
        return templateRest.request(new CallbackRest<String>() {
            @Override
            public void check() {
                // 校验参数是否为空
            }
            @Override
            public String execute() {
                // 直接返回基本类型或数据对象，不需要包装为Response
                DemoDTO demoDTO = new DemoDTO();
                demoDTO.setName("1111");
                demoDTO.setAge(18);
                return demoCommand.hello(demoDTO);
            }
        });
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public Response<Boolean> save(@RequestBody DemoVO vo) {
        String lockKey = "demo:lock:" + System.currentTimeMillis();
        long lockTimeout = 30; // 锁超时时间（秒）
        return templateRest.requestWithLock(new CallbackRest<Boolean>() {
            @Override
            public void check() {
                // 校验参数是否为空
                if (vo == null) {
                    throw new IllegalArgumentException("vo is null");
                }
                if (StringUtils.isBlank(vo.getName())) {
                    throw new IllegalArgumentException("vo.name is blank");
                }
                if (vo.getAge() == null) {
                    throw new IllegalArgumentException("vo.age is null");
                }
            }
            @Override
            public Boolean execute() {
                // 直接返回基本类型或数据对象，不需要包装为Response
                return demoCommand.save(DemoVO.toDTO(vo));
            }
        },lockKey,lockTimeout,true);
    }

}