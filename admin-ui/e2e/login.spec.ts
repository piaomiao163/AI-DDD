import { test, expect } from '@playwright/test';

/**
 * 登录功能 E2E 测试
 */
test.describe('登录页面', () => {

  test('正确账密登录 → 跳转到首页/dashboard', async ({ page }) => {
    await page.goto('/login');
    await page.locator('input[placeholder*="用户名"], input[name="username"]').fill('admin');
    await page.locator('input[placeholder*="密码"], input[name="password"]').fill('123456');

    // DEV 模式下 onMounted 自动调用 getCaptcha()，等待验证码自动填充
    await page.waitForFunction(
      () => (document.querySelector('input[placeholder*="验证码"]') as HTMLInputElement)?.value?.length >= 4,
      { timeout: 8000 }
    );

    await page.locator('.login-button').click();
    await page.waitForURL(/dashboard|index|home|\//, { timeout: 10000 });
    await expect(page).not.toHaveURL(/login/);
  });

  test('错误密码 → 显示错误提示', async ({ page }) => {
    await page.goto('/login');
    await page.locator('input[placeholder*="用户名"], input[name="username"]').fill('admin');
    await page.locator('input[placeholder*="密码"], input[name="password"]').fill('wrongpassword');

    // 等待验证码自动填充后清空，填入错误验证码触发校验失败
    await page.waitForFunction(
      () => (document.querySelector('input[placeholder*="验证码"]') as HTMLInputElement)?.value?.length >= 4,
      { timeout: 8000 }
    );

    await page.locator('.login-button').click();

    // 等待错误提示出现（ElMessage 或表单校验错误）
    const errorMsg = page.locator('.el-message--error, .el-form-item__error');
    await expect(errorMsg.first()).toBeVisible({ timeout: 8000 });
  });

  test('用户名为空 → 按钮点击后停留在登录页', async ({ page }) => {
    await page.goto('/login');
    await page.locator('.login-button').click();
    await expect(page).toHaveURL(/login/);
  });
});
