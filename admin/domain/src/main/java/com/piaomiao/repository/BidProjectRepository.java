package com.piaomiao.repository;

import com.piaomiao.model.BidProjectModel;
import com.piaomiao.query.BidProjectPageQuery;
import com.piaomiao.response.PageResult;

public interface BidProjectRepository {

    BidProjectModel save(BidProjectModel model);

    BidProjectModel update(BidProjectModel model);

    BidProjectModel findById(Long id);

    void deleteById(Long id);

    PageResult<BidProjectModel> findByPage(BidProjectPageQuery query);
}
