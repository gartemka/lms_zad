
import org.example.Constants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo; // Импорт для получения информации о тесте
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;

    // Локаторы для Cookie-баннера на Steam
    private By acceptAllCookiesButton = By.id("acceptAllButton");
    private By cookiePrefPopup = By.id("cookiePrefPopup");

    @BeforeEach
    public void setUp(TestInfo testInfo) { // ИСПРАВЛЕНИЕ: Добавлен TestInfo
        String browserType = System.getProperty("browser", Constants.BROWSER_TYPE);
        String startUrl;

        // Динамическое определение URL на основе имени запускаемого тестового класса
        if (testInfo.getTestClass().isPresent() && testInfo.getTestClass().get().equals(PracticeFormTest.class)) {
            startUrl = Constants.DEMOQA_FORM_URL;
            System.out.println("Обнаружен PracticeFormTest, открываем URL: " + startUrl);
        } else {
            startUrl = Constants.STEAM_BASE_URL;
            System.out.println("Обнаружен Steam-тест или неизвестный тест, открываем URL: " + startUrl);
        }

        System.out.println("Запускаем тесты в браузере: " + browserType);

        if (browserType.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            // options.addArguments("--incognito");
             options.addArguments("--headless");
            driver = new ChromeDriver(options);
        } else if (browserType.equalsIgnoreCase("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            // options.addArguments("--private");
             options.addArguments("--headless");
            driver = new FirefoxDriver(options);
        } else {
            throw new IllegalArgumentException("Неподдерживаемый тип браузера: " + browserType);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(Constants.SHORT_WAIT_TIMEOUT_SECONDS));
        wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_WAIT_TIMEOUT_SECONDS));

        driver.get(startUrl); // Открываем нужный URL

        // В зависимости от открываемого URL, выполняем специфические ожидания
        if (startUrl.startsWith(Constants.STEAM_BASE_URL)) { // Если это Steam сайт
            wait.until(ExpectedConditions.urlContains(Constants.STEAM_BASE_URL.substring(8, Constants.STEAM_BASE_URL.length() - 1)));
            handleCookieConsent(); // Обработка Cookie-баннера только для Steam
        } else if (startUrl.equals(Constants.DEMOQA_FORM_URL)) { // Если это форма DemoQA
            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Practice Form']")));
        }

        System.out.println("WebDriver и WebDriverWait инициализированы. Браузер открыт на: " + driver.getCurrentUrl());
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("WebDriver закрыт.");
        }
    }

    /**
     * Обрабатывает баннер с согласием на использование файлов cookie на Steam.
     * Не актуален для DemoQA, но необходим для тестов Steam.
     */
    private void handleCookieConsent() {
        try {
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(Constants.SHORT_WAIT_TIMEOUT_SECONDS));
            WebElement cookiePopup = shortWait.until(ExpectedConditions.visibilityOfElementLocated(cookiePrefPopup));

            if (cookiePopup.isDisplayed()) {
                System.out.println("Cookie-баннер отображается.");
                shortWait.until(ExpectedConditions.elementToBeClickable(acceptAllCookiesButton)).click();
                System.out.println("Нажата кнопка 'Принять все' на Cookie-баннере.");
                shortWait.until(ExpectedConditions.invisibilityOfElementLocated(cookiePrefPopup));
            }
        } catch (Exception e) {
            // Игнорируем ошибку, если баннера нет или не удалось с ним взаимодействовать
        }
    }
}