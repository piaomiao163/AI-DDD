package com.piaomiao.service.impl;

import com.piaomiao.model.BidProjectModel;
import com.piaomiao.query.BidProjectPageQuery;
import com.piaomiao.repository.BidProjectRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.BidProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidProjectServiceImpl implements BidProjectService {

    @Autowired
    private BidProjectRepository bidProjectRepository;

    @Override
    public BidProjectModel save(BidProjectModel model) {
        model.setStatus(BidProjectModel.STATUS_DRAFT);
        return bidProjectRepository.save(model);
    }

    @Override
    public BidProjectModel update(BidProjectModel model) {
        BidProjectModel existing = requireProject(model.getId());
        if (existing.getStatus() != null && existing.getStatus() != BidProjectModel.STATUS_DRAFT) {
            throw new IllegalStateException("只有草稿状态的项目才能编辑");
        }
        return bidProjectRepository.update(model);
    }

    @Override
    public BidProjectModel findById(Long id) {
        return requireProject(id);
    }

    @Override
    public void deleteById(Long id) {
        BidProjectModel existing = requireProject(id);
        if (existing.getStatus() != null && existing.getStatus() != BidProjectModel.STATUS_DRAFT) {
            throw new IllegalStateException("只有草稿状态的项目才能删除");
        }
        bidProjectRepository.deleteById(id);
    }

    @Override
    public PageResult<BidProjectModel> findByPage(BidProjectPageQuery query) {
        return bidProjectRepository.findByPage(query);
    }

    @Override
    public BidProjectModel submit(Long id) {
        BidProjectModel model = requireProject(id);
        model.submit();
        return bidProjectRepository.update(model);
    }

    @Override
    public BidProjectModel publish(Long id) {
        BidProjectModel model = requireProject(id);
        model.publish();
        return bidProjectRepository.update(model);
    }

    @Override
    public BidProjectModel openBidding(Long id) {
        BidProjectModel model = requireProject(id);
        model.openBidding();
        return bidProjectRepository.update(model);
    }

    @Override
    public BidProjectModel startEvaluation(Long id) {
        BidProjectModel model = requireProject(id);
        model.startEvaluation();
        return bidProjectRepository.update(model);
    }

    @Override
    public BidProjectModel publicize(Long id, Integer days) {
        BidProjectModel model = requireProject(id);
        model.publicize();
        return bidProjectRepository.update(model);
    }

    @Override
    public BidProjectModel award(Long projectId, Long winnerTenderId) {
        BidProjectModel model = requireProject(projectId);
        model.award();
        return bidProjectRepository.update(model);
    }

    private BidProjectModel requireProject(Long id) {
        BidProjectModel model = bidProjectRepository.findById(id);
        if (model == null) {
            throw new IllegalArgumentException("招标项目不存在，ID=" + id);
        }
        return model;
    }
}
