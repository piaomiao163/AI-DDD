package com.piaomiao.web.vo;

import com.piaomiao.dto.sys.PostDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class PostVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String code;
    private String name;
    private Integer sort;
    private Integer status;
    private String remark;

    public static PostDTO toDTO(PostVO vo) {
        PostDTO dto = new PostDTO();
        dto.setId(vo.getId());
        dto.setCode(vo.getCode());
        dto.setName(vo.getName());
        dto.setSort(vo.getSort());
        dto.setStatus(vo.getStatus());
        dto.setRemark(vo.getRemark());
        return dto;
    }
}
