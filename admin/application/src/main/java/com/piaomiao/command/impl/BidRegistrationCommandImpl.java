package com.piaomiao.command.impl;

import com.piaomiao.command.BidRegistrationCommand;
import com.piaomiao.dto.BidRegistrationDTO;
import com.piaomiao.model.BidRegistrationModel;
import com.piaomiao.query.BidRegistrationPageQuery;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.BidRegistrationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 供应商报名命令实现
 */
@Service
public class BidRegistrationCommandImpl implements BidRegistrationCommand {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BidRegistrationService bidRegistrationService;

    @Override
    public BidRegistrationDTO save(BidRegistrationDTO dto) {
        return toDTO(bidRegistrationService.save(toModel(dto)));
    }

    @Override
    public PageResult<BidRegistrationDTO> findByPage(BidRegistrationPageQuery query) {
        PageResult<BidRegistrationModel> result = bidRegistrationService.findByPage(
                query != null ? query : new BidRegistrationPageQuery());
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::toDTO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }

    @Override
    public List<BidRegistrationDTO> findByProjectId(Long projectId) {
        return bidRegistrationService.findByProjectId(projectId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public BidRegistrationDTO findMyRegistration(Long projectId, Long userId) {
        BidRegistrationModel model = bidRegistrationService.findByProjectIdAndUserId(projectId, userId);
        return model != null ? toDTO(model) : null;
    }

    @Override
    public BidRegistrationDTO approve(Long id) {
        return toDTO(bidRegistrationService.approve(id));
    }

    @Override
    public BidRegistrationDTO reject(Long id, String reason) {
        return toDTO(bidRegistrationService.reject(id, reason));
    }

    private BidRegistrationModel toModel(BidRegistrationDTO dto) {
        if (dto == null) return null;
        BidRegistrationModel model = new BidRegistrationModel();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    private BidRegistrationDTO toDTO(BidRegistrationModel model) {
        if (model == null) return null;
        BidRegistrationDTO dto = new BidRegistrationDTO();
        BeanUtils.copyProperties(model, dto);
        dto.setCreateTime(formatDateTime(model.getCreateTime()));
        dto.setUpdateTime(formatDateTime(model.getUpdateTime()));
        return dto;
    }

    private String formatDateTime(LocalDateTime dt) {
        return dt != null ? dt.format(FORMATTER) : null;
    }
}
