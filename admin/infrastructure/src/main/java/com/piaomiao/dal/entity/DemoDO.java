package com.piaomiao.dal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.piaomiao.model.DemoModel;

@TableName("demo")
public class DemoDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 转换为领域模型
     */
    public static DemoModel toModel(DemoDO demoDO) {
        DemoModel demoModel = new DemoModel();
        demoModel.setId(demoDO.getId());
        demoModel.setName(demoDO.getName());
        demoModel.setAge(demoDO.getAge());
        return demoModel;
    }

    /**
     * 从领域模型转换
     */
    public static DemoDO fromModel(DemoModel demoModel) {
        DemoDO demoDO = new DemoDO();
        demoDO.setId(demoModel.getId());
        demoDO.setName(demoModel.getName());
        demoDO.setAge(demoModel.getAge());
        return demoDO;
    }
}
