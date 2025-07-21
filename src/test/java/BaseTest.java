
import org.example.Constants;
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

    // Локаторы для Cookie-баннера (актуальны для главной страницы Steam)
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
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("--private"); // Запускаем Firefox в приватном режиме
            // options.addArguments("--headless"); // Раскомментируйте для безголового режима
            driver = new FirefoxDriver(options);
        } else {
            throw new IllegalArgumentException("Неподдерживаемый тип браузера: " + browserType);
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); // Неявное ожидание
        wait = new WebDriverWait(driver, Duration.ofSeconds(Constants.DEFAULT_WAIT_TIMEOUT_SECONDS)); // Явное ожидание

        driver.get(Constants.STEAM_BASE_URL);
        wait.until(ExpectedConditions.urlContains(Constants.STEAM_BASE_URL.substring(8, Constants.STEAM_BASE_URL.length() - 1)));
        System.out.println("WebDriver и WebDriverWait инициализированы. Браузер открыт на: " + driver.getCurrentUrl());

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
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(Constants.SHORT_WAIT_TIMEOUT_SECONDS));
            WebElement cookiePopup = shortWait.until(ExpectedConditions.visibilityOfElementLocated(cookiePrefPopup));

            if (cookiePopup.isDisplayed()) {
                System.out.println("Cookie-баннер отображается.");
                shortWait.until(ExpectedConditions.elementToBeClickable(acceptAllCookiesButton)).click();
                System.out.println("Нажата кнопка 'Принять все' на Cookie-баннере.");
                shortWait.until(ExpectedConditions.invisibilityOfElementLocated(cookiePrefPopup));
            }
        } catch (Exception e) {
            System.out.println("Cookie-баннер не отображается или не удалось с ним взаимодействовать за " + Constants.SHORT_WAIT_TIMEOUT_SECONDS + " секунд.");
        }
    }

    // Методы для скриншотов удалены, так как они не используются в ассертах по коду
    // protected String takeFullPageScreenshot(String fileName) { /* ... */ return null; }
    // protected boolean compareScreenshots(String baseImageFilePath, String currentImageFilePath, String diffImageFileName) { /* ... */ return false; }
    // protected String takeViewportScreenshot(String fileName) { /* ... */ return null; }
}