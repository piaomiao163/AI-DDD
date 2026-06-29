package com.piaomiao.query;

import com.piaomiao.base.BasePageDTO;

/**
 * 评标专家分页查询条件
 */
public class BidExpertPageQuery extends BasePageDTO {

    /**
     * 专家姓名
     */
    private String name;

    /**
     * 专业方向
     */
    private String specialty;

    public BidExpertPageQuery() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
