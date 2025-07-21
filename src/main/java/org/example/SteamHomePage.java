package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor; // Импорт для JavaScriptExecutor


public class SteamHomePage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;

    // --- Локаторы для главной страницы ---
    private By loginButton = By.xpath("//a[contains(@class, 'global_action_link') and text()='войти']");
    private By loggedInAccountPulldown = By.xpath("//div[@id='account_pulldown']");

    // Локаторы для меню магазина и поиска
    private By storeMenuButton = By.xpath("//div[@id='store_nav_area']//a[text()='Магазин']");
    private By homePageSubMenuItem = By.xpath("//div[@id='foryou_flyout']//a[text()='Главная страница']");

    // Уточненный локатор для кнопки меню "Новое и интересное" (desktop-версия)
    private By noteworthyMenuButton = By.xpath("//div[@id='noteworthy_tab']/span[@class='pulldown']/a[@class='pulldown_desktop' and text()='Новое и интересное']");
    // Более гибкий локатор для "Лидеры продаж" (ищет по тексту в любой popup_menu_item)
    private By bestsellersSubMenuItem = By.xpath("//a[text()='Лидеры продаж']");


    // Уточненный локатор для кнопки меню "Категории" (desktop-версия)
    private By categoriesMenuButton = By.xpath("//div[@id='genre_tab']/span[@class='pulldown']/a[@class='pulldown_desktop' and text()='Категории']");
    // Более гибкий локатор для "Бесплатные"
    private By freeToPlaySubMenuItem = By.xpath("//a[contains(@class, 'popup_menu_item') and text()='Бесплатные']");

    private By searchInputField = By.xpath("//input[@id='store_nav_search_term']");
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

    /**
     * Открывает главную страницу Steam. (Хотя обычно вызывается в BaseTest)
     */
    public void open() {
        driver.get(Constants.STEAM_BASE_URL);
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButton));
        System.out.println("Открыта домашняя страница Steam.");
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
        // Ждем, что пункт подменю станет кликабельным
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
        WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(searchInputField));
        searchInput.clear();
        searchInput.sendKeys(term);
        searchInput.sendKeys(Keys.ENTER);
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
     * Теперь возвращает SteamSearchResultsPage, так как Steam перенаправляет туда.
     */
    public SteamSearchResultsPage navigateToBestsellers() { // ИЗМЕНЕНИЕ: Возвращает SteamSearchResultsPage
        WebElement noteworthyMenu = wait.until(ExpectedConditions.elementToBeClickable(noteworthyMenuButton)); // Ждем кликабельности кнопки меню
        actions.moveToElement(noteworthyMenu).perform();
        System.out.println("Наведен курсор на меню 'Новое и интересное'.");

        // Попытаемся кликнуть обычным способом, если не получится - используем JS-клик
        try {
            wait.until(ExpectedConditions.elementToBeClickable(bestsellersSubMenuItem)).click();
        } catch (Exception e) {
            System.out.println("Не удалось кликнуть по 'Лидеры продаж' обычным способом, пробуем JS-клик. Ошибка: " + e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(bestsellersSubMenuItem));
        }
        System.out.println("Клик по пункту 'Лидеры продаж'.");
        // После клика, ожидаем, что попадем на страницу поиска с фильтром topsellers
        wait.until(ExpectedConditions.urlContains("search/?filter=topsellers"));
        return new SteamSearchResultsPage(driver, wait); // ИЗМЕНЕНИЕ: Возвращаем SteamSearchResultsPage
    }

    /**
     * Наводит курсор на меню "Категории" и кликает на "Бесплатные".
     */
    public SteamFreeToPlayPage navigateToFreeToPlay() {
        WebElement categoriesMenu = wait.until(ExpectedConditions.elementToBeClickable(categoriesMenuButton)); // Ждем кликабельности кнопки меню
        actions.moveToElement(categoriesMenu).perform();
        System.out.println("Наведен курсор на меню 'Категории'.");
        try {
            wait.until(ExpectedConditions.elementToBeClickable(freeToPlaySubMenuItem)).click();
        } catch (Exception e) {
            System.out.println("Не удалось кликнуть по 'Бесплатные' обычным способом, пробуем JS-клик. Ошибка: " + e.getMessage());
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(freeToPlaySubMenuItem));
        }
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