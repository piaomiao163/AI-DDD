package com.piaomiao.command.impl;

import com.piaomiao.command.BidComplaintCommand;
import com.piaomiao.dto.BidComplaintDTO;
import com.piaomiao.model.BidComplaintModel;
import com.piaomiao.query.BidComplaintPageQuery;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.BidComplaintService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

/**
 * 质疑投诉命令实现
 */
@Service
public class BidComplaintCommandImpl implements BidComplaintCommand {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BidComplaintService bidComplaintService;

    @Override
    public BidComplaintDTO save(BidComplaintDTO dto) {
        return toDTO(bidComplaintService.save(toModel(dto)));
    }

    @Override
    public PageResult<BidComplaintDTO> findByPage(BidComplaintPageQuery query) {
        PageResult<BidComplaintModel> result = bidComplaintService.findByPage(
                query != null ? query : new BidComplaintPageQuery());
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::toDTO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }

    @Override
    public BidComplaintDTO reply(Long id, String replyContent) {
        return toDTO(bidComplaintService.reply(id, replyContent));
    }

    @Override
    public BidComplaintDTO close(Long id) {
        return toDTO(bidComplaintService.close(id));
    }

    @Override
    public BidComplaintDTO escalate(Long id) {
        return toDTO(bidComplaintService.escalate(id));
    }

    private BidComplaintModel toModel(BidComplaintDTO dto) {
        if (dto == null) return null;
        BidComplaintModel model = new BidComplaintModel();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    private BidComplaintDTO toDTO(BidComplaintModel model) {
        if (model == null) return null;
        BidComplaintDTO dto = new BidComplaintDTO();
        BeanUtils.copyProperties(model, dto);
        dto.setCreateTime(formatDateTime(model.getCreateTime()));
        dto.setUpdateTime(formatDateTime(model.getUpdateTime()));
        return dto;
    }

    private String formatDateTime(LocalDateTime dt) {
        return dt != null ? dt.format(FORMATTER) : null;
    }
}
