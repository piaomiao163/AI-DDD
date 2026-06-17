package com.piaomiao.query;

import com.piaomiao.base.BasePageDTO;

/**
 * 我的请假分页查询条件
 */
public class MyLeavePageQuery extends BasePageDTO {

    public MyLeavePageQuery() {}

    public MyLeavePageQuery(Integer pageNum, Integer pageSize, String sortField, String sortOrder) {
        super(pageNum, pageSize, sortField, sortOrder);
    }
}
