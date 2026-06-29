package com.piaomiao.service.impl;

import com.piaomiao.model.BidRegistrationModel;
import com.piaomiao.query.BidRegistrationPageQuery;
import com.piaomiao.repository.BidRegistrationRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.BidRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商报名领域服务实现
 */
@Service
public class BidRegistrationServiceImpl implements BidRegistrationService {

    @Autowired
    private BidRegistrationRepository bidRegistrationRepository;

    @Override
    public BidRegistrationModel save(BidRegistrationModel model) {
        model.submit();
        return bidRegistrationRepository.save(model);
    }

    @Override
    public BidRegistrationModel findById(Long id) {
        return requireRegistration(id);
    }

    @Override
    public List<BidRegistrationModel> findByProjectId(Long projectId) {
        return bidRegistrationRepository.findByProjectId(projectId);
    }

    @Override
    public BidRegistrationModel findByProjectIdAndUserId(Long projectId, Long userId) {
        return bidRegistrationRepository.findByProjectIdAndUserId(projectId, userId);
    }

    @Override
    public PageResult<BidRegistrationModel> findByPage(BidRegistrationPageQuery query) {
        return bidRegistrationRepository.findByPage(query);
    }

    @Override
    public BidRegistrationModel approve(Long id) {
        BidRegistrationModel model = requireRegistration(id);
        model.approve();
        return bidRegistrationRepository.update(model);
    }

    @Override
    public BidRegistrationModel reject(Long id, String reason) {
        BidRegistrationModel model = requireRegistration(id);
        model.reject(reason);
        return bidRegistrationRepository.update(model);
    }

    private BidRegistrationModel requireRegistration(Long id) {
        BidRegistrationModel model = bidRegistrationRepository.findById(id);
        if (model == null) {
            throw new IllegalArgumentException("报名记录不存在，ID=" + id);
        }
        return model;
    }
}
