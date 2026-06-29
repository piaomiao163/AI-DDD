package com.piaomiao.service.impl;

import com.piaomiao.model.BidTenderModel;
import com.piaomiao.repository.BidTenderRepository;
import com.piaomiao.service.BidTenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 投标书领域服务实现
 */
@Service
public class BidTenderServiceImpl implements BidTenderService {

    @Autowired
    private BidTenderRepository bidTenderRepository;

    @Override
    public BidTenderModel save(BidTenderModel model) {
        model.saveDraft();
        if (model.getId() != null) {
            return bidTenderRepository.update(model);
        }
        return bidTenderRepository.save(model);
    }

    @Override
    public BidTenderModel submit(Long id) {
        BidTenderModel model = requireTender(id);
        model.submit();
        return bidTenderRepository.update(model);
    }

    @Override
    public BidTenderModel findById(Long id) {
        return requireTender(id);
    }

    @Override
    public List<BidTenderModel> findByProjectId(Long projectId) {
        return bidTenderRepository.findByProjectId(projectId);
    }

    @Override
    public BidTenderModel findByProjectIdAndUserId(Long projectId, Long userId) {
        return bidTenderRepository.findByProjectIdAndUserId(projectId, userId);
    }

    private BidTenderModel requireTender(Long id) {
        BidTenderModel model = bidTenderRepository.findById(id);
        if (model == null) {
            throw new IllegalArgumentException("投标书不存在，ID=" + id);
        }
        return model;
    }
}
