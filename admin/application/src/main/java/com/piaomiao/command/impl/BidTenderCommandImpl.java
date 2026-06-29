package com.piaomiao.command.impl;

import com.piaomiao.command.BidTenderCommand;
import com.piaomiao.dto.BidTenderDTO;
import com.piaomiao.model.BidTenderModel;
import com.piaomiao.service.BidTenderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 投标书命令实现
 */
@Service
public class BidTenderCommandImpl implements BidTenderCommand {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BidTenderService bidTenderService;

    @Override
    public BidTenderDTO save(BidTenderDTO dto) {
        return toDTO(bidTenderService.save(toModel(dto)));
    }

    @Override
    public BidTenderDTO submit(Long id) {
        return toDTO(bidTenderService.submit(id));
    }

    @Override
    public BidTenderDTO findById(Long id) {
        return toDTO(bidTenderService.findById(id));
    }

    @Override
    public List<BidTenderDTO> findByProjectId(Long projectId) {
        return bidTenderService.findByProjectId(projectId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public BidTenderDTO findMyTender(Long projectId, Long userId) {
        BidTenderModel model = bidTenderService.findByProjectIdAndUserId(projectId, userId);
        return model != null ? toDTO(model) : null;
    }

    private BidTenderModel toModel(BidTenderDTO dto) {
        if (dto == null) return null;
        BidTenderModel model = new BidTenderModel();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    private BidTenderDTO toDTO(BidTenderModel model) {
        if (model == null) return null;
        BidTenderDTO dto = new BidTenderDTO();
        BeanUtils.copyProperties(model, dto);
        dto.setCreateTime(formatDateTime(model.getCreateTime()));
        dto.setUpdateTime(formatDateTime(model.getUpdateTime()));
        return dto;
    }

    private String formatDateTime(LocalDateTime dt) {
        return dt != null ? dt.format(FORMATTER) : null;
    }
}
