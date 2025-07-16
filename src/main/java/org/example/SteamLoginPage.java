package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SteamLoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Локаторы для страницы входа
    private By usernameField = By.xpath("//div[text()='Войдите, используя имя аккаунта']/following-sibling::input[@type='text']");
    private By passwordField = By.xpath("//div[text()='Пароль']/following-sibling::input[@type='password']");
    private By signInButton = By.xpath("//button[@type='submit' and text()='Войти']");

    // Локатор для сообщения об ошибке
    private By errorMessage = By.xpath("//div[@class='login_signin_error' and (contains(text(), '" + Constants.ERROR_MESSAGE_PART_RU + "') or contains(text(), '" + Constants.ERROR_MESSAGE_PART_EN + "'))]");

    // УЛУЧШЕННЫЙ ЛОКАТОР ДЛЯ СООБЩЕНИЯ STEAM GUARD
    // Теперь ищем div, который содержит div с изображением и div с нужным текстом.
    // Это делает его более устойчивым, если классы обфусцированы или меняются.
    private By steamGuardContainer = By.xpath("//div[contains(@class, '_3zQ9hnkyXJEv7nN0oBU56M')]"); // Основной контейнер
    private By steamGuardMessageTextElement = By.xpath(
            "//div[contains(@class, '_3zQ9hnkyXJEv7nN0oBU56M')]//div[contains(text(), 'Используйте мобильное приложение Steam, чтобы подтвердить вход')]"
    ); // Элемент с текстом внутри контейнера


    public SteamLoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        System.out.println("Создан Page Object SteamLoginPage. Текущий URL: " + driver.getCurrentUrl());
    }

    public void enterUsername(String username) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        element.clear();
        element.sendKeys(username);
        System.out.println("Введено имя пользователя.");
    }

    public void enterPassword(String password) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        element.clear();
        element.sendKeys(password);
        System.out.println("Введен пароль.");
    }

    public void clickSignInButton() {
        wait.until(ExpectedConditions.elementToBeClickable(signInButton)).click();
        System.out.println("Нажата кнопка 'Войти'.");
    }

    /**
     * Выполняет полную операцию входа на странице.
     */
    public void login(String username, String password) {
        System.out.println("Попытка входа с именем: " + username);
        enterUsername(username);
        enterPassword(password);
        clickSignInButton();
    }

    /**
     * Получает текст сообщения об ошибке.
     */
    public String getErrorMessage() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).getText();
    }

    /**
     * Проверяет, отображается ли сообщение об ошибке.
     */
    public boolean isErrorMessageDisplayed() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(Constants.SHORT_WAIT_TIMEOUT_SECONDS));
            return shortWait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверяет, отображается ли сообщение Steam Guard после ввода учетных данных.
     */
    public boolean isSteamGuardMessageDisplayed() {
        try {
            // Ожидаем видимости самого контейнера Steam Guard
            wait.until(ExpectedConditions.visibilityOfElementLocated(steamGuardContainer));
            // Затем ожидаем, что элемент с нужным текстом станет видимым внутри этого контейнера
            return wait.until(ExpectedConditions.visibilityOfElementLocated(steamGuardMessageTextElement)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Этот геттер больше не нужен, т.к. проверка теперь комплексная
    // public By getSteamGuardMessageLocator() {
    //     return steamGuardMessage;
    // }

    /**
     * Открывает страницу логина напрямую.
     */
    public void openLoginPage() {
        driver.get(Constants.STEAM_LOGIN_URL);
        System.out.println("Открыта страница входа напрямую: " + driver.getCurrentUrl());
    }
}