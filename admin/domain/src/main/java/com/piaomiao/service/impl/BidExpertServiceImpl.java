package com.piaomiao.service.impl;

import com.piaomiao.model.BidExpertModel;
import com.piaomiao.query.BidExpertPageQuery;
import com.piaomiao.repository.BidExpertRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.BidExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 评标专家领域服务实现
 */
@Service
public class BidExpertServiceImpl implements BidExpertService {

    @Autowired
    private BidExpertRepository bidExpertRepository;

    @Override
    public BidExpertModel save(BidExpertModel model) {
        model.validate();
        return bidExpertRepository.save(model);
    }

    @Override
    public BidExpertModel update(BidExpertModel model) {
        requireExpert(model.getId());
        model.validate();
        return bidExpertRepository.update(model);
    }

    @Override
    public BidExpertModel findById(Long id) {
        return requireExpert(id);
    }

    @Override
    public void deleteById(Long id) {
        requireExpert(id);
        bidExpertRepository.deleteById(id);
    }

    @Override
    public PageResult<BidExpertModel> findByPage(BidExpertPageQuery query) {
        return bidExpertRepository.findByPage(query);
    }

    @Override
    public BidExpertModel blacklist(Long id, String reason) {
        BidExpertModel model = requireExpert(id);
        model.blacklist(reason);
        return bidExpertRepository.update(model);
    }

    @Override
    public BidExpertModel unblacklist(Long id) {
        BidExpertModel model = requireExpert(id);
        model.unblacklist();
        return bidExpertRepository.update(model);
    }

    private BidExpertModel requireExpert(Long id) {
        BidExpertModel model = bidExpertRepository.findById(id);
        if (model == null) {
            throw new IllegalArgumentException("专家不存在，ID=" + id);
        }
        return model;
    }
}
