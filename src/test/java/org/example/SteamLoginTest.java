package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class SteamLoginTest extends BaseTest {

    @Test
    @DisplayName("Тест: Успешный вход в Steam (проверяет Steam Guard ИЛИ прямой логин)")
    void testSuccessfulLoginFlow() {
        SteamHomePage homePage = new SteamHomePage(driver, wait);

        Assertions.assertTrue(driver.findElement(homePage.getLoginButtonLocator()).isDisplayed(), "Ошибка: Кнопка 'Войти' не отображается на главной странице.");
        System.out.println("✓ Кнопка 'Войти' видна на главной странице.");

        SteamLoginPage loginPage = homePage.clickLoginButton();
        wait.until(ExpectedConditions.urlContains("login/"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("login/"), "Ошибка: Не удалось перейти на страницу логина.");
        System.out.println("✓ Переход на страницу логина успешен.");

        loginPage.login(Constants.TEST_USERNAME, Constants.TEST_PASSWORD);

        boolean loggedInViaSteamGuard = false;
        boolean loggedInDirectly = false;

        // Попробуем подождать, появится ли сообщение Steam Guard
        loggedInViaSteamGuard = loginPage.isSteamGuardMessageDisplayed();


        if (loggedInViaSteamGuard) {
            Assertions.assertTrue(loggedInViaSteamGuard, "Ошибка: Сообщение Steam Guard не появилось после ввода правильных данных.");
            System.out.println("✓ Сообщение Steam Guard отображается, первый этап логина пройден успешно.");

        } else {
            // Если Steam Guard не появился за короткий таймаут, возможно, вошли напрямую
            // Проверяем, вошел ли пользователь напрямую
            loggedInDirectly = homePage.isUserLoggedIn();
            Assertions.assertTrue(loggedInDirectly, "Ошибка: Не удалось войти напрямую и сообщение Steam Guard не появилось.");
            System.out.println("✓ Прямой вход успешно выполнен (Steam Guard пропущен).");

            wait.until(ExpectedConditions.urlToBe(Constants.STEAM_BASE_URL));
            Assertions.assertEquals(Constants.STEAM_BASE_URL, driver.getCurrentUrl(), "Ошибка: После успешного входа не вернулись на главную страницу.");
            System.out.println("✓ После успешного входа вернулись на главную страницу.");
        }

        // Если вошли напрямую, выходим для чистоты следующего теста
        if (loggedInDirectly) {
            driver.get(Constants.STEAM_LOGOUT_URL);
            homePage.open();
            System.out.println("Выход из аккаунта после успешного теста для изоляции.");
        }
    }

    @Test
    @DisplayName("Тест: Неудачный вход в Steam с неправильными данными (проверка сообщения об ошибке)")
    void testFailedLogin() {
        SteamHomePage homePage = new SteamHomePage(driver, wait);
        SteamLoginPage loginPage = homePage.clickLoginButton();

        wait.until(ExpectedConditions.urlContains("login/"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("login/"), "Ошибка: Не удалось перейти на страницу логина для теста неудачного входа.");
        System.out.println("✓ На странице логина для теста неудачного входа.");

        String WRONG_USERNAME = "wronguser12345";
        String WRONG_PASSWORD = "wrongpassword12345";
        loginPage.login(WRONG_USERNAME, WRONG_PASSWORD);

        // --- ГЛАВНЫЙ АССЕРТ ДЛЯ НЕУДАЧНОГО ВХОДА ---
        // Проверяем, что сообщение об ошибке появилось
        Assertions.assertTrue(loginPage.isErrorMessageDisplayed(), "Ошибка: Сообщение об ошибке входа не отобразилось.");

        // Получаем фактический текст сообщения об ошибке
        String actualErrorMessage = loginPage.getErrorMessage();

        // Ассерт: Проверяем, что текст ошибки ТОЧНО совпадает с ожидаемой константой
        Assertions.assertEquals(Constants.INVALID_CREDENTIALS_FULL_MESSAGE_RU, actualErrorMessage,
                "Ошибка: Текст сообщения об ошибке не соответствует ожидаемому. Фактический текст: " + actualErrorMessage);
        System.out.println("✓ Тест на неудачный вход успешно завершен: получено ожидаемое сообщение об ошибке: '" + actualErrorMessage + "'");

        // Дополнительная проверка: Убеждаемся, что мы остались на странице логина
        wait.until(ExpectedConditions.urlContains("login"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("login"), "Ошибка: Перешли на другую страницу после неудачного входа.");
        System.out.println("✓ Остались на странице входа после неудачной попытки, как и ожидалось.");
    }
}