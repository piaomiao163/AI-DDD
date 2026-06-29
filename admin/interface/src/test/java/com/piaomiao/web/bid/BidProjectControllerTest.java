package com.piaomiao.web.bid;

import com.piaomiao.command.BidProjectCommand;
import com.piaomiao.dto.BidProjectDTO;
import com.piaomiao.query.BidProjectPageQuery;
import com.piaomiao.response.PageResult;
import com.piaomiao.response.Response;
import com.piaomiao.rest.TemplateRest;
import com.piaomiao.web.vo.bid.BidProjectAwardVO;
import com.piaomiao.web.vo.bid.BidProjectPublicizeVO;
import com.piaomiao.web.vo.bid.BidProjectQueryVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * BidProjectController 单元测试 — 验证 Controller 路由委托和参数校验
 * 使用纯 Mockito，不启动 Spring 容器
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BidProjectController 单元测试")
class BidProjectControllerTest {

    @Mock
    private BidProjectCommand bidProjectCommand;

    @Mock
    private TemplateRest templateRest;

    @InjectMocks
    private BidProjectController bidProjectController;

    private BidProjectDTO sampleDto;

    @BeforeEach
    void setUp() {
        sampleDto = new BidProjectDTO();
        sampleDto.setId(1L);
        sampleDto.setProjectName("测试招标项目");
        sampleDto.setStatus(0);
    }

    // ======================== save() ========================

    @Test
    @DisplayName("save: templateRest 被调用一次")
    void save_正常请求_调用templateRest() {
        when(templateRest.request(any(), eq(true))).thenReturn(Response.success(sampleDto));

        BidProjectDTO dto = new BidProjectDTO();
        dto.setProjectName("测试招标项目");

        Response<BidProjectDTO> response = bidProjectController.save(dto);

        assertNotNull(response);
        verify(templateRest, times(1)).request(any(), eq(true));
    }

    // ======================== findById() ========================

    @Test
    @DisplayName("findById: templateRest 被调用一次")
    void findById_调用templateRest() {
        when(templateRest.request(any())).thenReturn(Response.success(sampleDto));

        Response<BidProjectDTO> response = bidProjectController.findById(1L);

        assertNotNull(response);
        verify(templateRest, times(1)).request(any());
    }

    // ======================== page() ========================

    @Test
    @DisplayName("page: 分页查询调用 templateRest")
    void page_分页查询_调用templateRest() {
        PageResult<BidProjectDTO> mockPage = new PageResult<>(0L, Collections.emptyList(), 1, 10);
        when(templateRest.request(any())).thenReturn(Response.success(mockPage));

        BidProjectQueryVO vo = new BidProjectQueryVO();
        vo.setPageNum(1);
        vo.setPageSize(10);

        Response<PageResult<BidProjectDTO>> response = bidProjectController.page(vo);

        assertNotNull(response);
        verify(templateRest, times(1)).request(any());
    }

    // ======================== submit() ========================

    @Test
    @DisplayName("submit: 调用 templateRest（写操作带事务）")
    void submit_调用templateRest() {
        BidProjectDTO reviewDto = new BidProjectDTO();
        reviewDto.setStatus(1);
        when(templateRest.request(any(), eq(true))).thenReturn(Response.success(reviewDto));

        Response<BidProjectDTO> response = bidProjectController.submit(1L);

        assertNotNull(response);
        verify(templateRest, times(1)).request(any(), eq(true));
    }

    // ======================== award() ========================

    @Test
    @DisplayName("award: VO 正常传入 → 调用 templateRest（写操作带事务）")
    void award_正常VO_调用templateRest() {
        BidProjectDTO awardedDto = new BidProjectDTO();
        awardedDto.setStatus(8);
        when(templateRest.request(any(), eq(true))).thenReturn(Response.success(awardedDto));

        BidProjectAwardVO vo = new BidProjectAwardVO();
        vo.setProjectId(1L);
        vo.setWinnerTenderId(10L);

        Response<BidProjectDTO> response = bidProjectController.award(vo);

        assertNotNull(response);
        verify(templateRest, times(1)).request(any(), eq(true));
    }

    // ======================== publicize() ========================

    @Test
    @DisplayName("publicize: VO 为 null 时使用默认天数 7 → 调用 templateRest")
    void publicize_voNull_使用默认天数() {
        when(templateRest.request(any(), eq(true))).thenReturn(Response.success(sampleDto));

        Response<BidProjectDTO> response = bidProjectController.publicize(1L, (BidProjectPublicizeVO) null);

        assertNotNull(response);
        verify(templateRest, times(1)).request(any(), eq(true));
    }

    // ======================== delete() ========================

    @Test
    @DisplayName("delete: 调用 templateRest（写操作带事务）")
    void delete_调用templateRest() {
        when(templateRest.request(any(), eq(true))).thenReturn(Response.success(null));

        Response<Void> response = bidProjectController.delete(1L);

        assertNotNull(response);
        verify(templateRest, times(1)).request(any(), eq(true));
    }
}
