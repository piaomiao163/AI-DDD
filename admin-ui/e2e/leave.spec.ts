import { test, expect, Page } from '@playwright/test';

/**
 * 请假模块 E2E 测试
 */

async function login(page: Page) {
  await page.goto('/login');
  await page.locator('input[placeholder*="用户名"], input[name="username"]').fill('admin');
  await page.locator('input[placeholder*="密码"], input[name="password"]').fill('123456');
  // DEV 模式下 onMounted 自动填充验证码，等待填充完成
  await page.waitForFunction(
    () => (document.querySelector('input[placeholder*="验证码"]') as HTMLInputElement)?.value?.length >= 4,
    { timeout: 8000 }
  );
  await page.locator('.login-button').click();
  await page.waitForURL(url => !url.toString().includes('/login'), { timeout: 10000 });
}

test.describe('请假列表', () => {

  test.beforeEach(async ({ page }) => {
    await login(page);
  });

  test('导航到我的请假 → 列表加载正常', async ({ page }) => {
    await page.goto('/leave/my');
    await page.waitForTimeout(1500);
    const table = page.locator('.el-table, table');
    await expect(table.first()).toBeVisible({ timeout: 6000 });
  });
});

test.describe('请假 - 申请', () => {

  test.beforeEach(async ({ page }) => {
    await login(page);
  });

  test('申请请假 → 提交成功，列表有新记录', async ({ page }) => {
    await page.goto('/leave/apply');
    await page.waitForTimeout(800);

    // 填写标题
    const titleInput = page.locator('input[placeholder*="如：年假申请"]');
    await expect(titleInput).toBeVisible({ timeout: 5000 });
    await titleInput.fill(`E2E年假申请_${Date.now()}`);

    // 选择请假类型
    await page.locator('.el-select').first().click();
    await page.locator('.el-select-dropdown__item').first().click();

    // 填写开始日期（el-date-picker：clear + fill + Enter）
    const startDateInput = page.locator('input[placeholder*="开始日期"]');
    await startDateInput.click();
    await page.keyboard.press('Control+A');
    await page.keyboard.press('Delete');
    await startDateInput.fill('2026-08-01');
    await page.keyboard.press('Enter');
    await page.waitForTimeout(300);

    // 填写结束日期
    const endDateInput = page.locator('input[placeholder*="结束日期"]');
    await endDateInput.click();
    await page.keyboard.press('Control+A');
    await page.keyboard.press('Delete');
    await endDateInput.fill('2026-08-03');
    await page.keyboard.press('Enter');
    await page.waitForTimeout(300);

    // 填写请假原因
    const reasonInput = page.locator('textarea[placeholder*="请输入请假原因"]');
    await reasonInput.fill('家廷事务，需要请假');

    // 点击提交申请按钮
    const submitBtn = page.locator('button:has-text("提交申请")');
    await expect(submitBtn).toBeVisible({ timeout: 5000 });
    await submitBtn.click();

    const successMsg = page.locator('.el-message--success');
    await expect(successMsg).toBeVisible({ timeout: 15000 });
  });
});

test.describe('请假 - 撤回', () => {

  test.beforeEach(async ({ page }) => {
    await login(page);
  });

  test('待审批的请假单 → 点击撤回 → 状态变为已撤回', async ({ page }) => {
    await page.goto('/leave/my');
    await page.waitForTimeout(1500);

    const pendingRow = page.locator('.el-table__row').filter({ hasText: '待审批' }).first();
    if (await pendingRow.isVisible()) {
      const withdrawBtn = pendingRow.locator('button:has-text("撤回"), a:has-text("撤回")').first();
      if (await withdrawBtn.isVisible()) {
        await withdrawBtn.click();
        // ElMessageBox.prompt 对话框：填写備注后确定
        const promptInput = page.locator('.el-message-box__input input');
        if (await promptInput.isVisible({ timeout: 2000 })) {
          await promptInput.fill('自动化测试撤回');
        }
        const confirmBtn = page.locator('.el-message-box .el-button--primary').first();
        if (await confirmBtn.isVisible({ timeout: 2000 })) {
          await confirmBtn.click();
        }
        const successMsg = page.locator('.el-message--success');
        await expect(successMsg).toBeVisible({ timeout: 5000 });
      }
    }
  });
});
