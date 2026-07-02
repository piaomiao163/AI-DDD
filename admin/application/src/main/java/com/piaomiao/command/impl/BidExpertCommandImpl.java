package com.piaomiao.command.impl;

import com.piaomiao.command.BidExpertCommand;
import com.piaomiao.dto.BidExpertDTO;
import com.piaomiao.model.BidExpertModel;
import com.piaomiao.query.BidExpertPageQuery;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.BidExpertService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * 评标专家命令实现
 */
@Service
public class BidExpertCommandImpl implements BidExpertCommand {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BidExpertService bidExpertService;

    @Override
    public BidExpertDTO save(BidExpertDTO dto) {
        return toDTO(bidExpertService.save(toModel(dto)));
    }

    @Override
    public BidExpertDTO update(BidExpertDTO dto) {
        return toDTO(bidExpertService.update(toModel(dto)));
    }

    @Override
    public BidExpertDTO findById(Long id) {
        return toDTO(bidExpertService.findById(id));
    }

    @Override
    public void deleteById(Long id) {
        bidExpertService.deleteById(id);
    }

    @Override
    public PageResult<BidExpertDTO> findByPage(BidExpertPageQuery query) {
        PageResult<BidExpertModel> result = bidExpertService.findByPage(
                query != null ? query : new BidExpertPageQuery());
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::toDTO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }

    @Override
    public BidExpertDTO blacklist(Long id, String reason) {
        return toDTO(bidExpertService.blacklist(id, reason));
    }

    @Override
    public BidExpertDTO unblacklist(Long id) {
        return toDTO(bidExpertService.unblacklist(id));
    }

    private BidExpertModel toModel(BidExpertDTO dto) {
        if (dto == null) return null;
        BidExpertModel model = new BidExpertModel();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    private BidExpertDTO toDTO(BidExpertModel model) {
        if (model == null) return null;
        BidExpertDTO dto = new BidExpertDTO();
        BeanUtils.copyProperties(model, dto);
        dto.setCreateTime(formatDateTime(model.getCreateTime()));
        dto.setUpdateTime(formatDateTime(model.getUpdateTime()));
        return dto;
    }

    private String formatDateTime(LocalDateTime dt) {
        return dt != null ? dt.format(FORMATTER) : null;
    }
}
