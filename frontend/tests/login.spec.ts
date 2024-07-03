import { expect, test } from '@playwright/test';
import { testUser } from './resources/user.data.ts';

test.describe('Login page', () => {
  test('should redirect to login page if not logged in', async ({ page }) => {
    await page.goto('/');

    await expect(page.getByRole('heading', { name: 'Log in' })).toBeVisible();
    expect(page.url()).toContain('/login');

    await page.screenshot({ path: 'lost-pixel/login-page.png', fullPage: true });
  });

  test('login as admin user', async ({ page }) => {
    await page.goto('/login');

    await page.getByPlaceholder('E-Mail').fill(testUser.admin.email);
    await page.getByPlaceholder('Passwort').fill(testUser.admin.password);
    await page.getByRole('button', { name: 'Anmelden' }).click();

    await expect(page.getByRole('link', { name: 'Learn Track' })).toBeVisible();
    await page.mouse.move(0, 0);
    await page.waitForTimeout(200);

    await page.screenshot({ path: 'lost-pixel/admin-landing-page.png', fullPage: true });
  });

  test('should redirect a user to landing page if logged in', async ({ page }) => {
    await page.goto('/login');

    await page.getByPlaceholder('E-Mail').fill(testUser.admin.email);
    await page.getByPlaceholder('Passwort').fill(testUser.admin.password);
    await page.getByRole('button', { name: 'Anmelden' }).click();

    await expect(page.getByRole('link', { name: 'Learn Track' })).toBeVisible();

    await page.goto('/login');

    await expect(page.getByRole('link', { name: 'Learn Track' })).toBeVisible();
    expect(page.url()).toContain('/');
  });
});
