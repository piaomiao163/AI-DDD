package com.piaomiao.service;

import com.piaomiao.model.BidProjectModel;
import com.piaomiao.query.BidProjectPageQuery;
import com.piaomiao.response.PageResult;

public interface BidProjectService {

    BidProjectModel save(BidProjectModel model);

    BidProjectModel update(BidProjectModel model);

    BidProjectModel findById(Long id);

    void deleteById(Long id);

    PageResult<BidProjectModel> findByPage(BidProjectPageQuery query);

    BidProjectModel submit(Long id);

    BidProjectModel publish(Long id);

    BidProjectModel openBidding(Long id);

    BidProjectModel startEvaluation(Long id);

    BidProjectModel publicize(Long id, Integer days);

    BidProjectModel award(Long projectId, Long winnerTenderId);
}
