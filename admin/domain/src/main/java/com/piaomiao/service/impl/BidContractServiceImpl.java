package com.piaomiao.service.impl;

import com.piaomiao.model.BidContractModel;
import com.piaomiao.query.BidContractPageQuery;
import com.piaomiao.repository.BidContractRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.BidContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 合同领域服务实现
 */
@Service
public class BidContractServiceImpl implements BidContractService {

    @Autowired
    private BidContractRepository bidContractRepository;

    @Override
    public BidContractModel save(BidContractModel model) {
        model.create();
        return bidContractRepository.save(model);
    }

    @Override
    public BidContractModel update(BidContractModel model) {
        requireContract(model.getId());
        return bidContractRepository.update(model);
    }

    @Override
    public BidContractModel findById(Long id) {
        return requireContract(id);
    }

    @Override
    public PageResult<BidContractModel> findByPage(BidContractPageQuery query) {
        return bidContractRepository.findByPage(query);
    }

    @Override
    public BidContractModel sign(Long id, LocalDate signDate) {
        BidContractModel model = requireContract(id);
        model.sign(signDate);
        return bidContractRepository.update(model);
    }

    @Override
    public BidContractModel execute(Long id) {
        BidContractModel model = requireContract(id);
        model.execute();
        return bidContractRepository.update(model);
    }

    @Override
    public BidContractModel complete(Long id) {
        BidContractModel model = requireContract(id);
        model.complete();
        return bidContractRepository.update(model);
    }

    @Override
    public BidContractModel terminate(Long id) {
        BidContractModel model = requireContract(id);
        model.terminate();
        return bidContractRepository.update(model);
    }

    private BidContractModel requireContract(Long id) {
        BidContractModel model = bidContractRepository.findById(id);
        if (model == null) {
            throw new IllegalArgumentException("合同不存在，ID=" + id);
        }
        return model;
    }
}
