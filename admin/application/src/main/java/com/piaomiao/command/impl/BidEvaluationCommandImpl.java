package com.piaomiao.command.impl;

import com.piaomiao.command.BidEvaluationCommand;
import com.piaomiao.dto.BidEvaluationDTO;
import com.piaomiao.model.BidEvaluationModel;
import com.piaomiao.service.BidEvaluationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 评标记录命令实现
 */
@Service
public class BidEvaluationCommandImpl implements BidEvaluationCommand {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BidEvaluationService bidEvaluationService;

    @Override
    public BidEvaluationDTO save(BidEvaluationDTO dto) {
        return toDTO(bidEvaluationService.save(toModel(dto)));
    }

    @Override
    public BidEvaluationDTO submit(Long id) {
        return toDTO(bidEvaluationService.submit(id));
    }

    @Override
    public List<BidEvaluationDTO> findByProjectId(Long projectId) {
        return bidEvaluationService.findByProjectId(projectId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public BidEvaluationDTO findMyEvaluation(Long tenderId, Long expertId) {
        BidEvaluationModel model = bidEvaluationService.findByTenderIdAndExpertId(tenderId, expertId);
        return model != null ? toDTO(model) : null;
    }

    private BidEvaluationModel toModel(BidEvaluationDTO dto) {
        if (dto == null) return null;
        BidEvaluationModel model = new BidEvaluationModel();
        BeanUtils.copyProperties(dto, model);
        return model;
    }

    private BidEvaluationDTO toDTO(BidEvaluationModel model) {
        if (model == null) return null;
        BidEvaluationDTO dto = new BidEvaluationDTO();
        BeanUtils.copyProperties(model, dto);
        dto.setCreateTime(formatDateTime(model.getCreateTime()));
        dto.setUpdateTime(formatDateTime(model.getUpdateTime()));
        return dto;
    }

    private String formatDateTime(LocalDateTime dt) {
        return dt != null ? dt.format(FORMATTER) : null;
    }
}
