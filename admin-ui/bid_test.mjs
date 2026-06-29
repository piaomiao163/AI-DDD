import { chromium } from 'playwright';
import { writeFileSync } from 'fs';

const B = chromium;
const browser = await B.launch({ headless: false, slowMo: 600 });
const page = await browser.newPage();
const shots = [];

const shot = async (name) => {
  const p = `D:/screenshots/${name}.png`;
  await page.screenshot({ path: p, fullPage: false });
  shots.push(p);
  console.log(`[screenshot] ${p}`);
};

try {
  // 1. 登录
  console.log('\n=== 1. 登录 ===');
  await page.goto('http://localhost:3000/login');
  await page.waitForLoadState('networkidle');
  await shot('01_login_page');

  await page.fill('input[type="text"], input[placeholder*="用户名"], input[placeholder*="username"]', 'admin');
  await page.fill('input[type="password"]', '123456');
  await page.click('button[type="submit"], .el-button--primary');
  await page.waitForLoadState('networkidle');
  await page.waitForTimeout(1500);
  await shot('02_after_login');
  console.log('登录成功，当前URL:', page.url());

  // 2. 导航到招标项目页面
  console.log('\n=== 2. 导航到招标项目 ===');
  // 先找菜单
  const menuItems = await page.$$eval('a, .el-menu-item', els =>
    els.map(el => ({ text: el.textContent?.trim(), href: el.getAttribute('href') }))
       .filter(x => x.text && (x.text.includes('招标') || x.text.includes('bid') || x.text.includes('项目')))
  );
  console.log('找到菜单项:', JSON.stringify(menuItems));

  await page.goto('http://localhost:3000/bid/project');
  await page.waitForLoadState('networkidle');
  await page.waitForTimeout(1500);
  await shot('03_bid_project_list');
  console.log('招标项目列表页，URL:', page.url());

  // 检查是否有错误
  const errText = await page.$eval('.el-message--error, .error-msg', el => el.textContent).catch(() => null);
  if (errText) console.log('页面错误:', errText);

  // 3. 点击新增
  console.log('\n=== 3. 新增招标项目 ===');
  const addBtn = await page.$('button:has-text("新增"), button:has-text("创建"), .el-button:has-text("新增")');
  if (addBtn) {
    await addBtn.click();
    await page.waitForTimeout(1500);
    await shot('04_create_form');
    console.log('新增表单已打开');

    // 填写表单
    await page.fill('input[placeholder*="项目名称"], input[placeholder*="名称"]', '测试招标项目-UI测试').catch(() => {});
    await page.waitForTimeout(500);
    await shot('05_filled_form');

    // 提交
    const submitBtn = await page.$('button:has-text("确定"), button:has-text("保存"), button:has-text("提交")');
    if (submitBtn) {
      await submitBtn.click();
      await page.waitForTimeout(2000);
      await shot('06_after_save');
      console.log('保存后页面截图完成');
    }
  } else {
    console.log('未找到新增按钮，截当前页面');
    await shot('04_no_add_btn');
  }

  // 4. 查看列表
  await page.goto('http://localhost:3000/bid/project');
  await page.waitForLoadState('networkidle');
  await page.waitForTimeout(1500);
  await shot('07_list_final');

  const rows = await page.$$eval('.el-table__row, tbody tr', rows =>
    rows.map(r => r.textContent?.trim().substring(0, 80))
  ).catch(() => []);
  console.log('\n列表数据:', rows);

} catch(e) {
  console.error('测试出错:', e.message);
  await shot('error_state');
} finally {
  await page.waitForTimeout(3000);
  await browser.close();
  console.log('\n=== 测试完成 ===');
  console.log('截图文件:', shots);
}
