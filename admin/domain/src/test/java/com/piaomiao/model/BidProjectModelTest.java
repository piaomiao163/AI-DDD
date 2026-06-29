package com.piaomiao.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BidProjectModel 单元测试 — 状态机行为验证
 */
@DisplayName("BidProjectModel 状态机测试")
class BidProjectModelTest {

    private BidProjectModel model;

    @BeforeEach
    void setUp() {
        model = new BidProjectModel();
        model.setId(1L);
        model.setProjectName("测试招标项目");
        model.setStatus(BidProjectModel.STATUS_DRAFT);
    }

    // ======================== submit() ========================

    @Test
    @DisplayName("submit: 草稿状态 → 提交成功，状态变为待审核")
    void submit_草稿状态_应变为待审核() {
        model.submit();
        assertEquals(BidProjectModel.STATUS_PENDING_REVIEW, model.getStatus());
    }

    @Test
    @DisplayName("submit: 非草稿状态 → 抛 IllegalStateException")
    void submit_非草稿状态_应抛IllegalStateException() {
        model.setStatus(BidProjectModel.STATUS_PENDING_REVIEW);
        assertThrows(IllegalStateException.class, model::submit);
    }

    @Test
    @DisplayName("submit: 项目名为空 → 抛 IllegalArgumentException")
    void submit_项目名为空_应抛IllegalArgumentException() {
        model.setProjectName(null);
        assertThrows(IllegalArgumentException.class, model::submit);
    }

    @Test
    @DisplayName("submit: 项目名为空字符串 → 抛 IllegalArgumentException")
    void submit_项目名为空字符串_应抛IllegalArgumentException() {
        model.setProjectName("");
        assertThrows(IllegalArgumentException.class, model::submit);
    }

    // ======================== publish() ========================

    @Test
    @DisplayName("publish: 待审核状态 → 发布成功，状态变为报名中")
    void publish_待审核状态_应变为报名中() {
        model.setStatus(BidProjectModel.STATUS_PENDING_REVIEW);
        model.publish();
        assertEquals(BidProjectModel.STATUS_REGISTRATION, model.getStatus());
    }

    @Test
    @DisplayName("publish: 非待审核状态（草稿）→ 抛 IllegalStateException")
    void publish_草稿状态_应抛IllegalStateException() {
        model.setStatus(BidProjectModel.STATUS_DRAFT);
        assertThrows(IllegalStateException.class, model::publish);
    }

    @Test
    @DisplayName("publish: 非待审核状态（已发布）→ 抛 IllegalStateException")
    void publish_已发布状态_应抛IllegalStateException() {
        model.setStatus(BidProjectModel.STATUS_PUBLISHED);
        assertThrows(IllegalStateException.class, model::publish);
    }

    // ======================== openBidding() ========================

    @Test
    @DisplayName("openBidding: 报名中状态 → 开标成功，状态变为开标中")
    void openBidding_报名中状态_应变为开标中() {
        model.setStatus(BidProjectModel.STATUS_REGISTRATION);
        model.openBidding();
        assertEquals(BidProjectModel.STATUS_BIDDING_OPEN, model.getStatus());
    }

    @Test
    @DisplayName("openBidding: 非报名中状态 → 抛 IllegalStateException")
    void openBidding_非报名中状态_应抛IllegalStateException() {
        model.setStatus(BidProjectModel.STATUS_DRAFT);
        assertThrows(IllegalStateException.class, model::openBidding);
    }

    // ======================== startEvaluation() ========================

    @Test
    @DisplayName("startEvaluation: 开标中状态 → 开始评标成功，状态变为评标中")
    void startEvaluation_开标中状态_应变为评标中() {
        model.setStatus(BidProjectModel.STATUS_BIDDING_OPEN);
        model.startEvaluation();
        assertEquals(BidProjectModel.STATUS_EVALUATION, model.getStatus());
    }

    @Test
    @DisplayName("startEvaluation: 非开标中状态 → 抛 IllegalStateException")
    void startEvaluation_非开标中状态_应抛IllegalStateException() {
        model.setStatus(BidProjectModel.STATUS_REGISTRATION);
        assertThrows(IllegalStateException.class, model::startEvaluation);
    }

    // ======================== publicize() ========================

    @Test
    @DisplayName("publicize: 评标中状态 → 发起公示成功，状态变为公示中")
    void publicize_评标中状态_应变为公示中() {
        model.setStatus(BidProjectModel.STATUS_EVALUATION);
        model.publicize();
        assertEquals(BidProjectModel.STATUS_PUBLICIZING, model.getStatus());
    }

    @Test
    @DisplayName("publicize: 非评标中状态 → 抛 IllegalStateException")
    void publicize_非评标中状态_应抛IllegalStateException() {
        model.setStatus(BidProjectModel.STATUS_BIDDING_OPEN);
        assertThrows(IllegalStateException.class, model::publicize);
    }

    // ======================== award() ========================

    @Test
    @DisplayName("award: 公示中状态 → 定标成功，状态变为已定标")
    void award_公示中状态_应变为已定标() {
        model.setStatus(BidProjectModel.STATUS_PUBLICIZING);
        model.award();
        assertEquals(BidProjectModel.STATUS_AWARDED, model.getStatus());
    }

    @Test
    @DisplayName("award: 非公示中状态 → 抛 IllegalStateException")
    void award_非公示中状态_应抛IllegalStateException() {
        model.setStatus(BidProjectModel.STATUS_EVALUATION);
        assertThrows(IllegalStateException.class, model::award);
    }

    // ======================== 状态常量验证 ========================

    @Test
    @DisplayName("状态常量值正确定义")
    void 状态常量值正确() {
        assertEquals(0, BidProjectModel.STATUS_DRAFT);
        assertEquals(1, BidProjectModel.STATUS_PENDING_REVIEW);
        assertEquals(2, BidProjectModel.STATUS_PUBLISHED);
        assertEquals(3, BidProjectModel.STATUS_REGISTRATION);
        assertEquals(4, BidProjectModel.STATUS_CLOSED);
        assertEquals(5, BidProjectModel.STATUS_BIDDING_OPEN);
        assertEquals(6, BidProjectModel.STATUS_EVALUATION);
        assertEquals(7, BidProjectModel.STATUS_PUBLICIZING);
        assertEquals(8, BidProjectModel.STATUS_AWARDED);
        assertEquals(9, BidProjectModel.STATUS_ARCHIVED);
        assertEquals(10, BidProjectModel.STATUS_CANCELLED);
    }

    // ======================== 完整状态流转链路 ========================

    @Test
    @DisplayName("全流程状态机：草稿 → 待审核 → 报名中 → 开标中 → 评标中 → 公示中 → 已定标")
    void 全流程状态机_正常流转() {
        // 草稿 → 待审核
        model.submit();
        assertEquals(BidProjectModel.STATUS_PENDING_REVIEW, model.getStatus());

        // 待审核 → 报名中
        model.publish();
        assertEquals(BidProjectModel.STATUS_REGISTRATION, model.getStatus());

        // 报名中 → 开标中
        model.openBidding();
        assertEquals(BidProjectModel.STATUS_BIDDING_OPEN, model.getStatus());

        // 开标中 → 评标中
        model.startEvaluation();
        assertEquals(BidProjectModel.STATUS_EVALUATION, model.getStatus());

        // 评标中 → 公示中
        model.publicize();
        assertEquals(BidProjectModel.STATUS_PUBLICIZING, model.getStatus());

        // 公示中 → 已定标
        model.award();
        assertEquals(BidProjectModel.STATUS_AWARDED, model.getStatus());
    }
}
