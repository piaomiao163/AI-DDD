package com.piaomiao.command.impl;

import com.piaomiao.command.BidContractCommand;
import com.piaomiao.dto.BidContractDTO;
import com.piaomiao.model.BidContractModel;
import com.piaomiao.query.BidContractPageQuery;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.BidContractService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * 合同命令实现
 */
@Service
public class BidContractCommandImpl implements BidContractCommand {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Autowired
    private BidContractService bidContractService;

    @Override
    public BidContractDTO save(BidContractDTO dto) {
        return toDTO(bidContractService.save(toModel(dto)));
    }

    @Override
    public BidContractDTO update(BidContractDTO dto) {
        return toDTO(bidContractService.update(toModel(dto)));
    }

    @Override
    public BidContractDTO findById(Long id) {
        return toDTO(bidContractService.findById(id));
    }

    @Override
    public PageResult<BidContractDTO> findByPage(BidContractPageQuery query) {
        PageResult<BidContractModel> result = bidContractService.findByPage(
                query != null ? query : new BidContractPageQuery());
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::toDTO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }

    @Override
    public BidContractDTO sign(Long id, String signDate) {
        LocalDate date = parseDate(signDate);
        return toDTO(bidContractService.sign(id, date));
    }

    @Override
    public BidContractDTO execute(Long id) {
        return toDTO(bidContractService.execute(id));
    }

    @Override
    public BidContractDTO complete(Long id) {
        return toDTO(bidContractService.complete(id));
    }

    @Override
    public BidContractDTO terminate(Long id) {
        return toDTO(bidContractService.terminate(id));
    }

    private BidContractModel toModel(BidContractDTO dto) {
        if (dto == null) return null;
        BidContractModel model = new BidContractModel();
        BeanUtils.copyProperties(dto, model);
        model.setStartDate(parseDate(dto.getStartDate()));
        model.setEndDate(parseDate(dto.getEndDate()));
        model.setSignDate(parseDate(dto.getSignDate()));
        return model;
    }

    private BidContractDTO toDTO(BidContractModel model) {
        if (model == null) return null;
        BidContractDTO dto = new BidContractDTO();
        BeanUtils.copyProperties(model, dto);
        dto.setStartDate(formatDate(model.getStartDate()));
        dto.setEndDate(formatDate(model.getEndDate()));
        dto.setSignDate(formatDate(model.getSignDate()));
        dto.setCreateTime(formatDateTime(model.getCreateTime()));
        dto.setUpdateTime(formatDateTime(model.getUpdateTime()));
        return dto;
    }

    private LocalDate parseDate(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return LocalDate.parse(s, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private String formatDate(LocalDate d) {
        return d != null ? d.format(DATE_FORMATTER) : null;
    }

    private String formatDateTime(LocalDateTime dt) {
        return dt != null ? dt.format(FORMATTER) : null;
    }
}
