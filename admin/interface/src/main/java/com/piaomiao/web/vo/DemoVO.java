package com.piaomiao.web.vo;

import com.piaomiao.dto.DemoDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class DemoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private Integer age;

    public static DemoDTO toDTO(DemoVO vo) {
        DemoDTO dto = new DemoDTO();
        dto.setName(vo.getName());
        dto.setAge(vo.getAge());
        return dto;
    }
}
