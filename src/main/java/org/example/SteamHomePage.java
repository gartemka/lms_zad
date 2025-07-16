package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SteamHomePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    // --- Локаторы для главной страницы ---
    private By loginButton = By.xpath("//a[contains(@class, 'global_action_link') and text()='войти']");
    private By loggedInAccountPulldown = By.xpath("//div[@id='account_pulldown']");

    // Локаторы для меню магазина и поиска
    private By storeMenuButton = By.xpath("//div[@id='store_nav_area']//a[text()='Магазин']");
    private By foryouFlyout = By.id("foryou_flyout"); // Контейнер для выпадающего меню "Магазин"
    private By homePageSubMenuItem = By.xpath("//div[@id='foryou_flyout']//a[text()='Главная страница']");

    private final By noteworthyMenuButton = By.xpath("//div[@id='noteworthy_tab']//a[contains(text(), 'Новое и интересное')]");
    private By noteworthyFlyout = By.id("noteworthy_flyout"); // Контейнер для выпадающего меню "Новое и интересное"
    private By bestsellersSubMenuItem = By.xpath("//div[@id='noteworthy_flyout']//a[text()='Лидеры продаж']");

    private By categoriesMenuButton = By.xpath("//div[@id='genre_tab']//a[contains(text(), 'Категории')]");
    private By genreFlyout = By.id("genre_flyout"); // Контейнер для выпадающего меню "Категории"
    private By freeToPlaySubMenuItem = By.xpath("//div[@id='genre_flyout']//a[text()='Бесплатные']");

    private By searchInputField = By.xpath("//input[@id='store_nav_search_term']");
    // Локатор кнопки поиска оставлен, но в методе enterSearchTerm используется Keys.ENTER
    private By searchButton = By.xpath("//form[@id='searchform']//a[@id='store_search_link']");

    // Локаторы для секций на главной странице
    private By discountsAndEventsHeader = By.xpath("//h2[text()='Скидки и мероприятия']");
    private By moreDiscountsButton = By.xpath("//h2[text()='Скидки и мероприятия']//a[contains(., 'Ещё') or contains(., 'Больше продуктов')]");
    private By firstFeaturedGameTitle = By.xpath("//div[@id='home_maincap_v7']//div[@class='app_name']/div");

    // Локаторы для вкладок "Популярные новинки", "Лидеры продаж" и т.д.
    private By newReleasesTab = By.xpath("//button[@id='tab_newreleases_content_trigger']");
    private By topSellersTab = By.xpath("//button[@id='tab_topsellers_content_trigger']");
    private By upcomingTab = By.xpath("//button[@id='tab_upcoming_content_trigger']");
    private By specialsTab = By.xpath("//button[@id='tab_specials_content_trigger']");
    private By trendingFreeTab = By.xpath("//button[@id='tab_trendingfree_content_trigger']");

    private By tabContentContainer = By.id("home_tabs_content");


    public SteamHomePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        this.actions = new Actions(driver);
    }

    public By getLoginButtonLocator() {
        return loginButton;
    }

    public void open() {
        driver.get(Constants.STEAM_BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        System.out.println("Открыта домашняя страница Steam.");
    }

    public SteamLoginPage clickLoginButton() {
        System.out.println("Нажимаем кнопку 'Войти' на домашней странице.");
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        wait.until(ExpectedConditions.urlContains("login"));
        return new SteamLoginPage(driver, wait);
    }

    public boolean isUserLoggedIn() {
        try {
            boolean loginButtonInvisible = wait.until(ExpectedConditions.invisibilityOfElementLocated(loginButton));
            boolean loggedInElementVisible = wait.until(ExpectedConditions.visibilityOfElementLocated(loggedInAccountPulldown)).isDisplayed();
            return loginButtonInvisible && loggedInElementVisible;
        } catch (Exception e) {
            System.out.println("Не удалось подтвердить вход: " + e.getMessage());
            return false;
        }
    }

    /**
     * Наводит курсор на кнопку "Магазин" для активации выпадающего меню.
     */
    public void hoverOverStoreMenu() {
        WebElement storeMenu = wait.until(ExpectedConditions.elementToBeClickable(storeMenuButton));
        actions.moveToElement(storeMenu).perform();
        System.out.println("Наведен курсор на меню 'Магазин'.");
        // Ждем, пока пункт меню станет кликабельным (подразумевает, что и flyout виден)
        wait.until(ExpectedConditions.elementToBeClickable(homePageSubMenuItem));
    }

    /**
     * Кликает по пункту "Главная страница" в выпадающем меню "Магазин".
     */
    public void clickHomePageSubMenuItem() {
        wait.until(ExpectedConditions.elementToBeClickable(homePageSubMenuItem)).click();
        System.out.println("Клик по пункту 'Главная страница' в подменю 'Магазин'.");
        wait.until(ExpectedConditions.urlToBe(Constants.STEAM_BASE_URL));
    }

    /**
     * Вводит текст в поле поиска и нажимает Enter.
     */
    public void enterSearchTerm(String term) {
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(searchInputField)); // Ждем, пока поле будет кликабельным
        searchInput.clear();
        searchInput.sendKeys(term);
        // Небольшая пауза может помочь, если Steam динамически загружает подсказки или обрабатывает ввод
        try { Thread.sleep(Constants.TINY_PAUSE_MILLISECONDS); } catch (InterruptedException e) { e.printStackTrace(); }
        searchInput.sendKeys(Keys.ENTER); // Нажимаем Enter
        System.out.println("Введен текст поиска: " + term + " и нажата Enter.");
    }

    /**
     * Проверяет видимость заголовка "Скидки и мероприятия".
     */
    public boolean isDiscountsAndEventsHeaderDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(discountsAndEventsHeader)).isDisplayed();
    }

    /**
     * Кликает по кнопке "Ещё" или "Больше продуктов" в секции "Скидки и мероприятия".
     */
    public void clickMoreDiscountsButton() {
        wait.until(ExpectedConditions.elementToBeClickable(moreDiscountsButton)).click();
        wait.until(ExpectedConditions.urlContains("specials"));
        System.out.println("Нажата кнопка 'Ещё/Больше продуктов' в секции скидок.");
    }

    /**
     * Получает название первой игры в карусели "Популярное и рекомендуемое".
     */
    public String getFirstFeaturedGameTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(firstFeaturedGameTitle)).getText();
    }

    /**
     * Наводит курсор на меню "Новое и интересное" и кликает на "Лидеры продаж".
     */
    public SteamChartsPage navigateToBestsellers() {
        WebElement noteworthyMenu = wait.until(ExpectedConditions.elementToBeClickable(noteworthyMenuButton));
        actions.moveToElement(noteworthyMenu).perform();
        System.out.println("Наведен курсор на меню 'Новое и интересное'.");
        // Ждем, пока пункт подменю станет кликабельным
        wait.until(ExpectedConditions.elementToBeClickable(bestsellersSubMenuItem)).click();
        System.out.println("Клик по пункту 'Лидеры продаж'.");
        return new SteamChartsPage(driver, wait);
    }

    /**
     * Наводит курсор на меню "Категории" и кликает на "Бесплатные".
     */
    public SteamFreeToPlayPage navigateToFreeToPlay() {
        WebElement categoriesMenu = wait.until(ExpectedConditions.elementToBeClickable(categoriesMenuButton));
        actions.moveToElement(categoriesMenu).perform();
        System.out.println("Наведен курсор на меню 'Категории'.");
        // Ждем, пока пункт подменю станет кликабельным
        wait.until(ExpectedConditions.elementToBeClickable(freeToPlaySubMenuItem)).click();
        System.out.println("Клик по пункту 'Бесплатные'.");
        return new SteamFreeToPlayPage(driver, wait);
    }

    /**
     * Кликает по вкладке "Популярные новинки" и проверяет её активность.
     */
    public void clickNewReleasesTab() {
        wait.until(ExpectedConditions.elementToBeClickable(newReleasesTab)).click();
        wait.until(ExpectedConditions.attributeContains(newReleasesTab, "class", "active"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabContentContainer));
        System.out.println("Клик по вкладке 'Популярные новинки'.");
    }

    /**
     * Кликает по вкладке "Лидеры продаж" и проверяет её активность.
     */
    public void clickTopSellersTab() {
        wait.until(ExpectedConditions.elementToBeClickable(topSellersTab)).click();
        wait.until(ExpectedConditions.attributeContains(topSellersTab, "class", "active"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabContentContainer));
        System.out.println("Клик по вкладке 'Лидеры продаж'.");
    }

    /**
     * Кликает по вкладке "Популярные будущие новинки" и проверяет её активность.
     */
    public void clickUpcomingTab() {
        wait.until(ExpectedConditions.elementToBeClickable(upcomingTab)).click();
        wait.until(ExpectedConditions.attributeContains(upcomingTab, "class", "active"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabContentContainer));
        System.out.println("Клик по вкладке 'Популярные будущие новинки'.");
    }

    /**
     * Кликает по вкладке "Скидки" и проверяет её активность.
     */
    public void clickSpecialsTab() {
        wait.until(ExpectedConditions.elementToBeClickable(specialsTab)).click();
        wait.until(ExpectedConditions.attributeContains(specialsTab, "class", "active"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabContentContainer));
        System.out.println("Клик по вкладке 'Скидки'.");
    }

    /**
     * Кликает по вкладке "Популярные бесплатные игры" и проверяет её активность.
     */
    public void clickTrendingFreeTab() {
        wait.until(ExpectedConditions.elementToBeClickable(trendingFreeTab)).click();
        wait.until(ExpectedConditions.attributeContains(trendingFreeTab, "class", "active"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabContentContainer));
        System.out.println("Клик по вкладке 'Популярные бесплатные игры'.");
    }

    /**
     * Получает заголовок активной вкладки.
     */
    public String getActiveTabTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='home_tabs_row']//button[contains(@class, 'active')]//div[@class='tab_content']"))).getText();
    }

    /**
     * Проверяет, что заголовок содержимого вкладки соответствует ожидаемому.
     */
    public boolean isTabContentHeaderDisplayed(String expectedHeader) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabContentContainer));
        By headerLocator = By.xpath("//div[@id='home_tabs_content']//h2[contains(@class, 'tab_content_title') and text()='" + expectedHeader + "']");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(headerLocator)).isDisplayed();
    }
}