package com.piaomiao.service;

import com.piaomiao.model.BidProjectModel;
import com.piaomiao.query.BidProjectPageQuery;
import com.piaomiao.repository.BidProjectRepository;
import com.piaomiao.response.PageResult;
import com.piaomiao.service.impl.BidProjectServiceImpl;
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
import static org.mockito.Mockito.*;

/**
 * BidProjectService 单元测试 — Mock Repository，验证业务编排逻辑
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BidProjectService 单元测试")
class BidProjectServiceTest {

    @Mock
    private BidProjectRepository bidProjectRepository;

    @InjectMocks
    private BidProjectServiceImpl bidProjectService;

    private BidProjectModel draftModel;

    @BeforeEach
    void setUp() {
        draftModel = new BidProjectModel();
        draftModel.setId(1L);
        draftModel.setProjectName("测试项目");
        draftModel.setStatus(BidProjectModel.STATUS_DRAFT);
    }

    // ======================== save() ========================

    @Test
    @DisplayName("save: 正常保存 → 调用 Repository.save 并返回模型")
    void save_正常输入_应调用Repository并返回() {
        when(bidProjectRepository.save(any(BidProjectModel.class))).thenReturn(draftModel);

        BidProjectModel result = bidProjectService.save(draftModel);

        assertNotNull(result);
        verify(bidProjectRepository, times(1)).save(draftModel);
    }

    // ======================== findById() ========================

    @Test
    @DisplayName("findById: 存在的ID → 返回模型")
    void findById_存在ID_返回模型() {
        when(bidProjectRepository.findById(1L)).thenReturn(draftModel);

        BidProjectModel result = bidProjectService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    @DisplayName("findById: 不存在的ID → 抛 IllegalArgumentException")
    void findById_不存在ID_抛异常() {
        when(bidProjectRepository.findById(999L)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> bidProjectService.findById(999L));
    }

    // ======================== findByPage() ========================

    @Test
    @DisplayName("findByPage: 正常查询 → 返回 PageResult")
    void findByPage_正常查询_返回PageResult() {
        PageResult<BidProjectModel> mockPage = new PageResult<>(1L,
                Collections.singletonList(draftModel), 1, 10);
        when(bidProjectRepository.findByPage(any(BidProjectPageQuery.class))).thenReturn(mockPage);

        BidProjectPageQuery query = new BidProjectPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        PageResult<BidProjectModel> result = bidProjectService.findByPage(query);

        assertNotNull(result);
        assertEquals(1L, result.getTotal());
        assertEquals(1, result.getList().size());
    }

    // ======================== submit() ========================

    @Test
    @DisplayName("submit: 草稿项目提交 → 调用 Model.submit() 并持久化")
    void submit_草稿项目_应调用update() {
        BidProjectModel updatedModel = new BidProjectModel();
        updatedModel.setId(1L);
        updatedModel.setStatus(BidProjectModel.STATUS_PENDING_REVIEW);
        when(bidProjectRepository.findById(1L)).thenReturn(draftModel);
        when(bidProjectRepository.update(any(BidProjectModel.class))).thenReturn(updatedModel);

        BidProjectModel result = bidProjectService.submit(1L);

        assertNotNull(result);
        verify(bidProjectRepository, times(1)).update(any(BidProjectModel.class));
    }

    @Test
    @DisplayName("submit: 项目不存在 → 抛 RuntimeException")
    void submit_项目不存在_抛异常() {
        when(bidProjectRepository.findById(999L)).thenReturn(null);
        assertThrows(RuntimeException.class, () -> bidProjectService.submit(999L));
    }

    // ======================== deleteById() ========================

    @Test
    @DisplayName("deleteById: 草稿状态项目 → 调用 Repository.deleteById")
    void deleteById_草稿项目_正常删除() {
        when(bidProjectRepository.findById(1L)).thenReturn(draftModel);
        doNothing().when(bidProjectRepository).deleteById(1L);
        bidProjectService.deleteById(1L);
        verify(bidProjectRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("deleteById: 非草稿状态项目 → 抛 IllegalStateException")
    void deleteById_非草稿项目_抛异常() {
        draftModel.setStatus(BidProjectModel.STATUS_PENDING_REVIEW);
        when(bidProjectRepository.findById(1L)).thenReturn(draftModel);
        assertThrows(IllegalStateException.class, () -> bidProjectService.deleteById(1L));
    }
}
