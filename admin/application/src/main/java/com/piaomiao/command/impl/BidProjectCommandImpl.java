package com.piaomiao.command.impl;

import com.piaomiao.command.BidProjectCommand;
import com.piaomiao.dto.BidProjectDTO;
import com.piaomiao.model.BidProjectModel;
import com.piaomiao.query.BidProjectPageQuery;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.BidProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
public class BidProjectCommandImpl implements BidProjectCommand {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private BidProjectService bidProjectService;

    @Override
    public BidProjectDTO save(BidProjectDTO dto) {
        return toDTO(bidProjectService.save(toModel(dto)));
    }

    @Override
    public BidProjectDTO update(BidProjectDTO dto) {
        return toDTO(bidProjectService.update(toModel(dto)));
    }

    @Override
    public BidProjectDTO findById(Long id) {
        return toDTO(bidProjectService.findById(id));
    }

    @Override
    public void deleteById(Long id) {
        bidProjectService.deleteById(id);
    }

    @Override
    public PageResult<BidProjectDTO> findByPage(BidProjectPageQuery query) {
        PageResult<BidProjectModel> result = bidProjectService.findByPage(
                query != null ? query : new BidProjectPageQuery());
        return new PageResult<>(result.getTotal(),
                result.getList().stream().map(this::toDTO).collect(Collectors.toList()),
                result.getPageNum(), result.getPageSize());
    }

    @Override
    public BidProjectDTO submit(Long id) {
        return toDTO(bidProjectService.submit(id));
    }

    @Override
    public BidProjectDTO publish(Long id) {
        return toDTO(bidProjectService.publish(id));
    }

    @Override
    public BidProjectDTO openBidding(Long id) {
        return toDTO(bidProjectService.openBidding(id));
    }

    @Override
    public BidProjectDTO startEvaluation(Long id) {
        return toDTO(bidProjectService.startEvaluation(id));
    }

    @Override
    public BidProjectDTO publicize(Long id, Integer days) {
        return toDTO(bidProjectService.publicize(id, days));
    }

    @Override
    public BidProjectDTO award(Long projectId, Long winnerTenderId) {
        return toDTO(bidProjectService.award(projectId, winnerTenderId));
    }

    private BidProjectModel toModel(BidProjectDTO dto) {
        if (dto == null) return null;
        BidProjectModel model = new BidProjectModel();
        BeanUtils.copyProperties(dto, model);
        model.setAnnounceStartTime(parseDateTime(dto.getAnnounceStartTime()));
        model.setRegisterDeadline(parseDateTime(dto.getRegisterDeadline()));
        model.setQaDeadline(parseDateTime(dto.getQaDeadline()));
        model.setBidDeadline(parseDateTime(dto.getBidDeadline()));
        model.setEvalExpectedEnd(parseDateTime(dto.getEvalExpectedEnd()));
        return model;
    }

    private BidProjectDTO toDTO(BidProjectModel model) {
        if (model == null) return null;
        BidProjectDTO dto = new BidProjectDTO();
        BeanUtils.copyProperties(model, dto);
        dto.setAnnounceStartTime(formatDateTime(model.getAnnounceStartTime()));
        dto.setRegisterDeadline(formatDateTime(model.getRegisterDeadline()));
        dto.setQaDeadline(formatDateTime(model.getQaDeadline()));
        dto.setBidDeadline(formatDateTime(model.getBidDeadline()));
        dto.setEvalExpectedEnd(formatDateTime(model.getEvalExpectedEnd()));
        dto.setCreateTime(formatDateTime(model.getCreateTime()));
        dto.setUpdateTime(formatDateTime(model.getUpdateTime()));
        return dto;
    }

    private LocalDateTime parseDateTime(String s) {
        if (s == null || s.isEmpty()) return null;
        try {
            return LocalDateTime.parse(s, FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    private String formatDateTime(LocalDateTime dt) {
        return dt != null ? dt.format(FORMATTER) : null;
    }
}
