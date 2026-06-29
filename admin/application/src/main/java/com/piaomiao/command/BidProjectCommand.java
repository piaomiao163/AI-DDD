package com.piaomiao.command;

import com.piaomiao.dto.BidProjectDTO;
import com.piaomiao.query.BidProjectPageQuery;
import com.piaomiao.response.PageResult;

public interface BidProjectCommand {

    BidProjectDTO save(BidProjectDTO dto);

    BidProjectDTO update(BidProjectDTO dto);

    BidProjectDTO findById(Long id);

    void deleteById(Long id);

    PageResult<BidProjectDTO> findByPage(BidProjectPageQuery query);

    BidProjectDTO submit(Long id);

    BidProjectDTO publish(Long id);

    BidProjectDTO openBidding(Long id);

    BidProjectDTO startEvaluation(Long id);

    BidProjectDTO publicize(Long id, Integer days);

    BidProjectDTO award(Long projectId, Long winnerTenderId);
}
