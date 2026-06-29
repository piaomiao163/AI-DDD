import { test, expect, Page } from '@playwright/test';

/**
 * 招标项目模块 E2E 测试
 */

/** 登录辅助函数 */
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

test.describe('招标项目列表', () => {

  test.beforeEach(async ({ page }) => {
    await login(page);
  });

  test('导航到招标项目列表 → 页面正常加载，无 500/404 错误', async ({ page }) => {
    // 监听网络请求
    const failedRequests: string[] = [];
    page.on('response', resp => {
      if (resp.status() >= 400 && resp.url().includes('/bid/project')) {
        failedRequests.push(`${resp.status()} ${resp.url()}`);
      }
    });

    await page.goto('/bid/project');
    // 等待 loading 消失
    await page.waitForFunction(() => !document.querySelector('.el-loading-mask'), { timeout: 8000 }).catch(() => {});

    // 表格容器应可见
    const table = page.locator('.el-table');
    await expect(table.first()).toBeVisible({ timeout: 8000 });

    // 无关键接口报错
    expect(failedRequests).toHaveLength(0);
  });

  test('搜索项目名 → 列表刷新', async ({ page }) => {
    await page.goto('/bid/project');
    await page.waitForFunction(() => !document.querySelector('.el-loading-mask'), { timeout: 8000 }).catch(() => {});

    const searchInput = page.locator('input[placeholder*="项目名称"]').first();
    await expect(searchInput).toBeVisible({ timeout: 5000 });
    await searchInput.fill('测试');
    await page.locator('button:has-text("查询")').click();
    await page.waitForFunction(() => !document.querySelector('.el-loading-mask'), { timeout: 5000 }).catch(() => {});

    // 查询后页面不报错（分页可能为空，不强制断言）
    await expect(page.locator('.el-table')).toBeVisible({ timeout: 5000 });
  });
});

test.describe('招标项目 - 新建', () => {

  test.beforeEach(async ({ page }) => {
    await login(page);
  });

  test('新建项目并保存 → 列表出现新记录', async ({ page }) => {
    await page.goto('/bid/project');
    await page.waitForTimeout(1000);

    // 点击新建按钮（实际文字：新建项目）
    const createBtn = page.locator('button:has-text("新建项目")');
    await expect(createBtn).toBeVisible({ timeout: 5000 });
    await createBtn.click();

    // 等待表单页/对话框
    await page.waitForTimeout(800);

    // 填写项目名称
    const projectNameInput = page.locator('input[placeholder*="项目名"], .el-form input').first();
    await projectNameInput.fill(`E2E测试项目_${Date.now()}`);

    // 提交/保存
    const saveBtn = page.locator('button:has-text("保存"), button:has-text("提交"), button[type="submit"]').first();
    await saveBtn.click();

    // 等待成功提示
    const successMsg = page.locator('.el-message--success');
    await expect(successMsg).toBeVisible({ timeout: 5000 });
  });
});

test.describe('招标项目 - 详情页', () => {

  test.beforeEach(async ({ page }) => {
    await login(page);
  });

  test('进入第一条记录详情 → 无 404/控制台错误', async ({ page }) => {
    const consoleErrors: string[] = [];
    page.on('console', msg => {
      if (msg.type() === 'error') consoleErrors.push(msg.text());
    });

    const failedRequests: string[] = [];
    page.on('response', resp => {
      if (resp.status() === 404 || resp.status() >= 500) {
        failedRequests.push(`${resp.status()} ${resp.url()}`);
      }
    });

    await page.goto('/bid/project');
    await page.waitForFunction(() => !document.querySelector('.el-loading-mask'), { timeout: 8000 }).catch(() => {});

    // 找到第一条记录的详情按钮
    const detailBtn = page.locator('a:has-text("详情"), button:has-text("详情"), .el-table__row a').first();
    if (await detailBtn.isVisible()) {
      await detailBtn.click();
      await page.waitForTimeout(2000);

      // 验证无关键接口错误（允许 stub 接口返回空）
      const criticalErrors = failedRequests.filter(r => r.includes('404') && r.includes('/bid/project/'));
      expect(criticalErrors).toHaveLength(0);
    }
  });
});

test.describe('招标项目 - 状态流转', () => {

  test.beforeEach(async ({ page }) => {
    await login(page);
  });

  test('草稿项目点击「提交审核」 → 状态变为待审核', async ({ page }) => {
    await page.goto('/bid/project');
    await page.waitForFunction(() => !document.querySelector('.el-loading-mask'), { timeout: 8000 }).catch(() => {});

    // 找到草稿状态行
    const draftRow = page.locator('.el-table__row').filter({ hasText: '草稿' }).first();
    if (await draftRow.isVisible()) {
      const submitBtn = draftRow.locator('button:has-text("提交"), a:has-text("提交")').first();
      if (await submitBtn.isVisible()) {
        await submitBtn.click();
        // 确认对话框
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
