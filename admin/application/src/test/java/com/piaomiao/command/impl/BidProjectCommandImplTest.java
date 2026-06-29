package com.piaomiao.command.impl;

import com.piaomiao.dto.BidProjectDTO;
import com.piaomiao.model.BidProjectModel;
import com.piaomiao.query.BidProjectPageQuery;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.BidProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * BidProjectCommandImpl 单元测试 — 验证 DTO↔Model 转换和编排逻辑
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BidProjectCommandImpl 单元测试")
class BidProjectCommandImplTest {

    @Mock
    private BidProjectService bidProjectService;

    @InjectMocks
    private BidProjectCommandImpl bidProjectCommand;

    private BidProjectModel sampleModel;

    @BeforeEach
    void setUp() {
        sampleModel = new BidProjectModel();
        sampleModel.setId(1L);
        sampleModel.setProjectName("测试招标项目");
        sampleModel.setPurchaseType(1);
        sampleModel.setBidMethod(1);
        sampleModel.setBudgetAmount(new BigDecimal("100.00"));
        sampleModel.setStatus(BidProjectModel.STATUS_DRAFT);
        sampleModel.setCreateTime(LocalDateTime.of(2026, 6, 1, 10, 0, 0));
        sampleModel.setUpdateTime(LocalDateTime.of(2026, 6, 1, 10, 0, 0));
    }

    // ======================== save() ========================

    @Test
    @DisplayName("save: DTO 转换为 Model 后调用 Service，返回 DTO")
    void save_正常DTO_调用Service并返回DTO() {
        when(bidProjectService.save(any(BidProjectModel.class))).thenReturn(sampleModel);

        BidProjectDTO dto = new BidProjectDTO();
        dto.setProjectName("测试招标项目");
        dto.setPurchaseType(1);

        BidProjectDTO result = bidProjectCommand.save(dto);

        assertNotNull(result);
        assertEquals("测试招标项目", result.getProjectName());
        verify(bidProjectService, times(1)).save(any(BidProjectModel.class));
    }

    // ======================== findById() ========================

    @Test
    @DisplayName("findById: 找到项目 → 返回 DTO")
    void findById_存在项目_返回DTO() {
        when(bidProjectService.findById(1L)).thenReturn(sampleModel);

        BidProjectDTO result = bidProjectCommand.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试招标项目", result.getProjectName());
    }

    @Test
    @DisplayName("findById: 项目不存在 → 返回 null")
    void findById_不存在项目_返回null() {
        when(bidProjectService.findById(999L)).thenReturn(null);

        BidProjectDTO result = bidProjectCommand.findById(999L);

        assertNull(result);
    }

    // ======================== findByPage() ========================

    @Test
    @DisplayName("findByPage: 分页查询 → 返回正确的 PageResult<DTO>")
    void findByPage_正常查询_返回PageResult() {
        PageResult<BidProjectModel> mockPage = new PageResult<>(1L,
                Collections.singletonList(sampleModel), 1, 10);
        when(bidProjectService.findByPage(any(BidProjectPageQuery.class))).thenReturn(mockPage);

        BidProjectPageQuery query = new BidProjectPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        PageResult<BidProjectDTO> result = bidProjectCommand.findByPage(query);

        assertNotNull(result);
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getList().size());
        assertEquals("测试招标项目", result.getList().get(0).getProjectName());
    }

    // ======================== toDTO 时间字段格式化 ========================

    @Test
    @DisplayName("toDTO: createTime/updateTime 正确格式化为字符串")
    void toDTO_时间字段_正确格式化() {
        when(bidProjectService.findById(1L)).thenReturn(sampleModel);

        BidProjectDTO result = bidProjectCommand.findById(1L);

        assertNotNull(result.getCreateTime());
        assertEquals("2026-06-01 10:00:00", result.getCreateTime());
        assertEquals("2026-06-01 10:00:00", result.getUpdateTime());
    }

    @Test
    @DisplayName("toDTO: 时间字段为 null → 返回 null 字符串")
    void toDTO_时间字段为null_返回null() {
        sampleModel.setCreateTime(null);
        sampleModel.setUpdateTime(null);
        when(bidProjectService.findById(1L)).thenReturn(sampleModel);

        BidProjectDTO result = bidProjectCommand.findById(1L);

        assertNull(result.getCreateTime());
        assertNull(result.getUpdateTime());
    }

    // ======================== submit/publish 等状态转换 ========================

    @Test
    @DisplayName("submit: 调用 Service.submit 并返回 DTO")
    void submit_正常调用() {
        BidProjectModel reviewModel = new BidProjectModel();
        reviewModel.setId(1L);
        reviewModel.setStatus(BidProjectModel.STATUS_PENDING_REVIEW);
        when(bidProjectService.submit(1L)).thenReturn(reviewModel);

        BidProjectDTO result = bidProjectCommand.submit(1L);

        assertNotNull(result);
        assertEquals(BidProjectModel.STATUS_PENDING_REVIEW, result.getStatus());
    }

    @Test
    @DisplayName("award: 调用 Service.award 并返回 DTO")
    void award_正常调用() {
        BidProjectModel awardedModel = new BidProjectModel();
        awardedModel.setId(1L);
        awardedModel.setStatus(BidProjectModel.STATUS_AWARDED);
        when(bidProjectService.award(1L, 10L)).thenReturn(awardedModel);

        BidProjectDTO result = bidProjectCommand.award(1L, 10L);

        assertNotNull(result);
        assertEquals(BidProjectModel.STATUS_AWARDED, result.getStatus());
    }
}
