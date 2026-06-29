package com.piaomiao.service.impl;

import com.piaomiao.model.BidEvaluationModel;
import com.piaomiao.repository.BidEvaluationRepository;
import com.piaomiao.service.BidEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评标记录领域服务实现
 */
@Service
public class BidEvaluationServiceImpl implements BidEvaluationService {

    @Autowired
    private BidEvaluationRepository bidEvaluationRepository;

    @Override
    public BidEvaluationModel save(BidEvaluationModel model) {
        model.saveDraft();
        if (model.getId() != null) {
            return bidEvaluationRepository.update(model);
        }
        return bidEvaluationRepository.save(model);
    }

    @Override
    public BidEvaluationModel submit(Long id) {
        BidEvaluationModel model = requireEvaluation(id);
        model.submit();
        return bidEvaluationRepository.update(model);
    }

    @Override
    public List<BidEvaluationModel> findByProjectId(Long projectId) {
        return bidEvaluationRepository.findByProjectId(projectId);
    }

    @Override
    public BidEvaluationModel findByTenderIdAndExpertId(Long tenderId, Long expertId) {
        return bidEvaluationRepository.findByTenderIdAndExpertId(tenderId, expertId);
    }

    private BidEvaluationModel requireEvaluation(Long id) {
        BidEvaluationModel model = bidEvaluationRepository.findById(id);
        if (model == null) {
            throw new IllegalArgumentException("评标记录不存在，ID=" + id);
        }
        return model;
    }
}
