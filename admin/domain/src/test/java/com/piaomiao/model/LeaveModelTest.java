package com.piaomiao.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LeaveModel 单元测试 — 请假单领域行为验证
 */
@DisplayName("LeaveModel 领域行为测试")
class LeaveModelTest {

    private LeaveModel model;

    @BeforeEach
    void setUp() {
        model = new LeaveModel();
        model.setId(1L);
        model.setApplicantId(100L);
        model.setTitle("年假申请");
        model.setLeaveType(1);
        model.setStartDate(LocalDate.of(2026, 7, 1));
        model.setEndDate(LocalDate.of(2026, 7, 3));
        model.setDays(new BigDecimal("3"));
        model.setReason("家庭事务");
        model.setStatus(LeaveModel.STATUS_PENDING);
    }

    // ======================== submit() ========================

    @Test
    @DisplayName("submit: 正常输入 → 状态变为待审批")
    void submit_正常输入_状态变为待审批() {
        model.setStatus(null);
        model.submit();
        assertEquals(LeaveModel.STATUS_PENDING, model.getStatus());
    }

    @Test
    @DisplayName("submit: 标题为空 → 抛 IllegalArgumentException")
    void submit_标题为空_抛异常() {
        model.setTitle(null);
        assertThrows(IllegalArgumentException.class, model::submit);
    }

    @Test
    @DisplayName("submit: 标题为空字符串 → 抛 IllegalArgumentException")
    void submit_标题为空字符串_抛异常() {
        model.setTitle("  ");
        assertThrows(IllegalArgumentException.class, model::submit);
    }

    @Test
    @DisplayName("submit: 请假类型为空 → 抛 IllegalArgumentException")
    void submit_请假类型为空_抛异常() {
        model.setLeaveType(null);
        assertThrows(IllegalArgumentException.class, model::submit);
    }

    @Test
    @DisplayName("submit: 请假天数为0 → 抛 IllegalArgumentException")
    void submit_请假天数为零_抛异常() {
        model.setDays(BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class, model::submit);
    }

    @Test
    @DisplayName("submit: 请假天数为负数 → 抛 IllegalArgumentException")
    void submit_请假天数为负数_抛异常() {
        model.setDays(new BigDecimal("-1"));
        assertThrows(IllegalArgumentException.class, model::submit);
    }

    @Test
    @DisplayName("submit: 开始日期晚于结束日期 → 抛 IllegalArgumentException")
    void submit_开始日期晚于结束日期_抛异常() {
        model.setStartDate(LocalDate.of(2026, 7, 5));
        model.setEndDate(LocalDate.of(2026, 7, 1));
        assertThrows(IllegalArgumentException.class, model::submit);
    }

    @Test
    @DisplayName("submit: 请假原因为空 → 抛 IllegalArgumentException")
    void submit_请假原因为空_抛异常() {
        model.setReason(null);
        assertThrows(IllegalArgumentException.class, model::submit);
    }

    // ======================== withdraw() ========================

    @Test
    @DisplayName("withdraw: 申请人撤回待审批请假单 → 成功")
    void withdraw_申请人_待审批_成功() {
        model.setStatus(LeaveModel.STATUS_PENDING);
        model.withdraw(100L, "临时取消");
        assertEquals(LeaveModel.STATUS_WITHDRAWN, model.getStatus());
    }

    @Test
    @DisplayName("withdraw: 申请人撤回审批中请假单 → 成功")
    void withdraw_申请人_审批中_成功() {
        model.setStatus(LeaveModel.STATUS_APPROVING);
        model.withdraw(100L, "临时取消");
        assertEquals(LeaveModel.STATUS_WITHDRAWN, model.getStatus());
    }

    @Test
    @DisplayName("withdraw: 非申请人撤回 → 抛 IllegalStateException")
    void withdraw_非申请人_抛异常() {
        model.setStatus(LeaveModel.STATUS_PENDING);
        assertThrows(IllegalStateException.class, () -> model.withdraw(999L, "他人不得撤回"));
    }

    @Test
    @DisplayName("withdraw: 已通过状态不可撤回 → 抛 IllegalStateException")
    void withdraw_已通过状态_抛异常() {
        model.setStatus(LeaveModel.STATUS_APPROVED);
        assertThrows(IllegalStateException.class, () -> model.withdraw(100L, "无法撤回"));
    }

    @Test
    @DisplayName("withdraw: 已撤回状态不可再次撤回 → 抛 IllegalStateException")
    void withdraw_已撤回状态_抛异常() {
        model.setStatus(LeaveModel.STATUS_WITHDRAWN);
        assertThrows(IllegalStateException.class, () -> model.withdraw(100L, "重复撤回"));
    }

    // ======================== 状态常量验证 ========================

    @Test
    @DisplayName("LeaveModel 状态常量值正确")
    void 状态常量值正确() {
        assertEquals(0, LeaveModel.STATUS_PENDING);
        assertEquals(1, LeaveModel.STATUS_APPROVING);
        assertEquals(2, LeaveModel.STATUS_APPROVED);
        assertEquals(3, LeaveModel.STATUS_REJECTED);
        assertEquals(4, LeaveModel.STATUS_WITHDRAWN);
    }
}
