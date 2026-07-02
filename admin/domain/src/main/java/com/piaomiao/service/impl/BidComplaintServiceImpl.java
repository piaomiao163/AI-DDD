package com.piaomiao.service.impl;

import com.piaomiao.model.BidComplaintModel;
import com.piaomiao.query.BidComplaintPageQuery;
import com.piaomiao.repository.BidComplaintRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.BidComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 质疑投诉领域服务实现
 */
@Service
public class BidComplaintServiceImpl implements BidComplaintService {

    @Autowired
    private BidComplaintRepository bidComplaintRepository;

    @Override
    public BidComplaintModel save(BidComplaintModel model) {
        model.submit();
        return bidComplaintRepository.save(model);
    }

    @Override
    public PageResult<BidComplaintModel> findByPage(BidComplaintPageQuery query) {
        return bidComplaintRepository.findByPage(query);
    }

    @Override
    public BidComplaintModel reply(Long id, String replyContent) {
        BidComplaintModel model = requireComplaint(id);
        model.reply(replyContent);
        return bidComplaintRepository.update(model);
    }

    @Override
    public BidComplaintModel close(Long id) {
        BidComplaintModel model = requireComplaint(id);
        model.close();
        return bidComplaintRepository.update(model);
    }

    @Override
    public BidComplaintModel escalate(Long id) {
        BidComplaintModel model = requireComplaint(id);
        model.escalate();
        return bidComplaintRepository.update(model);
    }

    private BidComplaintModel requireComplaint(Long id) {
        BidComplaintModel model = bidComplaintRepository.findById(id);
        if (model == null) {
            throw new IllegalArgumentException("质疑投诉不存在，ID=" + id);
        }
        return model;
    }
}
