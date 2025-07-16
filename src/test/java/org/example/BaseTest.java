package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Локаторы для Cookie-баннера
    private By acceptAllCookiesButton = By.id("acceptAllButton");
    private By cookiePrefPopup = By.id("cookiePrefPopup");

    @BeforeEach
    public void setUp() {
        String browserType = System.getProperty("browser", Constants.BROWSER_TYPE);
        System.out.println("Запускаем тесты в браузере: " + browserType);

        if (browserType.equalsIgnoreCase("chrome")) {
            // Selenium Manager автоматически скачает и настроит Chromedriver.
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito"); // Запускаем Chrome в режиме инкогнито
            // options.addArguments("--headless"); // Раскомментируйте для безголового режима
            driver = new ChromeDriver(options);
        } else if (browserType.equalsIgnoreCase("firefox")) {
            // System.setProperty("webdriver.gecko.driver", Constants.GECKODRIVER_PATH); // Уже не нужно с Selenium Manager
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("-safe-mode"); // Запускает Firefox в безопасном режиме
            // options.addArguments("--private"); // Закомментируйте
            // options.addArguments("--headless"); // Закомментируйте
            driver = new FirefoxDriver(options);
            System.out.println("Запускаем тесты в Firefox (безопасный режим).");
        } else {
            throw new IllegalArgumentException("Неподдерживаемый тип браузера: " + browserType);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // Неявное ожидание
        wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_WAIT_TIMEOUT_SECONDS)); // Явное ожидание

        driver.get(Constants.STEAM_BASE_URL);
        // Ждем, пока URL содержит часть базового адреса, чтобы убедиться, что страница загружается
        wait.until(ExpectedConditions.urlContains(Constants.STEAM_BASE_URL.substring(8, Constants.STEAM_BASE_URL.length() - 1)));
        System.out.println("WebDriver и WebDriverWait инициализированы. Браузер открыт на: " + driver.getCurrentUrl());

        // Обработка Cookie-баннера
        handleCookieConsent();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Закрываем браузер после каждого теста
            System.out.println("WebDriver закрыт.");
        }
    }

    /**
     * Обрабатывает баннер с согласием на использование файлов cookie, если он появляется.
     * Кликает "Принять все", если кнопка видна.
     */
    private void handleCookieConsent() {
        try {
            // Используем короткое ожидание, чтобы не тратить время, если баннера нет
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(Constants.SHORT_WAIT_TIMEOUT_SECONDS));
            WebElement cookiePopup = shortWait.until(ExpectedConditions.visibilityOfElementLocated(cookiePrefPopup));

            if (cookiePopup.isDisplayed()) {
                System.out.println("Cookie-баннер отображается.");
                shortWait.until(ExpectedConditions.elementToBeClickable(acceptAllCookiesButton)).click();
                System.out.println("Нажата кнопка 'Принять все' на Cookie-баннере.");
                shortWait.until(ExpectedConditions.invisibilityOfElementLocated(cookiePrefPopup)); // Ждем исчезновения баннера
            }
        } catch (Exception e) {
            System.out.println("Cookie-баннер не отображается или не удалось с ним взаимодействовать за " + Constants.SHORT_WAIT_TIMEOUT_SECONDS + " секунд.");
            // Если баннера нет или он исчез, просто продолжаем
        }
    }
}