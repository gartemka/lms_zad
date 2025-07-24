package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By; // Необходим для локаторов By.xpath, By.id
import org.openqa.selenium.support.ui.ExpectedConditions; // Необходим для ExpectedConditions

public class SteamStoreTests extends BaseTest {

    @Test
    @DisplayName("Тест: Проверка поиска игры на главной странице")
    void testSearchFunctionality() {
        SteamHomePage homePage = new SteamHomePage(driver, wait);

        // Локатор searchInputFieldLocator уже объявлен в SteamHomePage, не нужно объявлять здесь снова.
        // Используем driver.findElement() напрямую с локатором из Page Object.
        By searchInputFieldLocator = By.xpath("//input[@id='store_nav_search_term']"); // Объявим локально, чтобы использовать в ассертах.

        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInputFieldLocator));
        Assertions.assertTrue(driver.findElement(searchInputFieldLocator).isDisplayed(), "Ошибка: Поле поиска не отображается.");
        Assertions.assertTrue(driver.findElement(searchInputFieldLocator).isEnabled(), "Ошибка: Поле поиска не активно.");
        System.out.println("✓ Поле поиска видимо и активно.");

        String searchTerm = "Cyberpunk 2077";
        homePage.enterSearchTerm(searchTerm); // Этот метод теперь вводит текст и нажимает ENTER.

        // Объявим expectedUrlContainsTerm локально, чтобы использовать в ассертах.
        String expectedUrlContainsTerm = searchTerm.replace(" ", "+");

        // Ждем, пока URL изменится и будет содержать искомый термин.
        wait.until(ExpectedConditions.urlContains(expectedUrlContainsTerm));
        Assertions.assertTrue(driver.getCurrentUrl().startsWith(Constants.STEAM_SEARCH_URL_PREFIX), "Ошибка: URL страницы поиска не начинается с ожидаемого префикса.");
        Assertions.assertTrue(driver.getCurrentUrl().contains(expectedUrlContainsTerm), "Ошибка: URL страницы поиска не содержит искомый термин.");
        System.out.println("✓ Перешли на страницу поиска: " + driver.getCurrentUrl());

        // Проверяем, что название игры отображается в результатах поиска
        By gameTitleOnSearchResult = By.xpath("//span[contains(@class, 'title') and text()='" + searchTerm + "']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(gameTitleOnSearchResult));
        Assertions.assertTrue(driver.findElement(gameTitleOnSearchResult).isDisplayed(),
                "Ошибка: Игра '" + searchTerm + "' не найдена в результатах поиска.");
        System.out.println("✓ Игра '" + searchTerm + "' найдена в результатах поиска.");
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Тест: Проверка навигации по меню 'Магазин' -> 'Главная страница'")
    void testStoreMenuNavigation() {
        SteamHomePage homePage = new SteamHomePage(driver, wait);

        By storeMenuButtonLocator = By.xpath("//div[@id='store_nav_area']//a[text()='Магазин']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(storeMenuButtonLocator));
        Assertions.assertTrue(driver.findElement(storeMenuButtonLocator).isDisplayed(), "Ошибка: Кнопка меню 'Магазин' не видна.");
        System.out.println("✓ Кнопка меню 'Магазин' видна.");

        homePage.hoverOverStoreMenu();

        By homePageSubMenuItemLocator = By.xpath("//div[@id='foryou_flyout']//a[text()='Главная страница']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(homePageSubMenuItemLocator));
        Assertions.assertTrue(driver.findElement(homePageSubMenuItemLocator).isDisplayed(), "Ошибка: Пункт 'Главная страница' в подменю не виден.");
        Assertions.assertTrue(driver.findElement(homePageSubMenuItemLocator).isEnabled(), "Ошибка: Пункт 'Главная страница' в подменю не активен.");
        System.out.println("✓ Пункт 'Главная страница' в подменю виден и активен.");

        homePage.clickHomePageSubMenuItem();

        wait.until(ExpectedConditions.urlToBe(Constants.STEAM_BASE_URL));
        Assertions.assertEquals(Constants.STEAM_BASE_URL, driver.getCurrentUrl(), "Ошибка: Не удалось перейти на Главную страницу из меню 'Магазин'.");
        System.out.println("✓ Успешно перешли на Главную страницу через меню.");
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Тест: Проверка заголовка и кнопки 'Скидки и мероприятия'")
    void testDiscountsSection() {
        SteamHomePage homePage = new SteamHomePage(driver, wait);

        By discountsAndEventsHeaderLocator = By.xpath("//h2[text()='Скидки и мероприятия']");
        wait.until(ExpectedConditions.visibilityOfElementLocated(discountsAndEventsHeaderLocator));
        Assertions.assertTrue(homePage.isDiscountsAndEventsHeaderDisplayed(), "Ошибка: Заголовок 'Скидки и мероприятия' не отображается.");
        System.out.println("✓ Заголовок 'Скидки и мероприятия' виден.");

        By moreDiscountsButtonLocator = By.xpath("//h2[text()='Скидки и мероприятия']//a[contains(., 'Ещё') or contains(., 'Больше продуктов')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(moreDiscountsButtonLocator));
        Assertions.assertTrue(driver.findElement(moreDiscountsButtonLocator).isDisplayed(), "Ошибка: Кнопка 'Ещё/Больше продуктов' не видна.");
        Assertions.assertTrue(driver.findElement(moreDiscountsButtonLocator).isEnabled(), "Ошибка: Кнопка 'Ещё/Больше продуктов' не активна.");
        System.out.println("✓ Кнопка 'Ещё/Больше продуктов' видна и активна.");

        homePage.clickMoreDiscountsButton();

        wait.until(ExpectedConditions.urlContains("specials"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("specials"), "Ошибка: Не удалось перейти на страницу скидок.");
        System.out.println("✓ Успешно перешли на страницу скидок.");
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Тест: Проверка названия первой игры в карусели 'Популярное и рекомендуемое'")
    void testFirstFeaturedGameTitle() {
        SteamHomePage homePage = new SteamHomePage(driver, wait);

        By featuredAndRecommendedHeaderLocator = By.id("home_featured_and_recommended");
        wait.until(ExpectedConditions.visibilityOfElementLocated(featuredAndRecommendedHeaderLocator));
        Assertions.assertTrue(driver.findElement(featuredAndRecommendedHeaderLocator).isDisplayed(), "Ошибка: Заголовок карусели 'Популярное и рекомендуемое' не виден.");
        System.out.println("✓ Заголовок карусели 'Популярное и рекомендуемое' виден.");

        String actualTitle = homePage.getFirstFeaturedGameTitle();

        Assertions.assertNotNull(actualTitle, "Ошибка: Название первой игры в карусели равно null.");
        Assertions.assertFalse(actualTitle.isEmpty(), "Ошибка: Название первой игры в карусели пустое.");
        Assertions.assertTrue(actualTitle.length() > 3, "Ошибка: Название игры слишком короткое или некорректное.");
        System.out.println("✓ Название первой игры в карусели: '" + actualTitle + "' отображается.");
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Тест: Навигация в 'Новое и интересное' -> 'Лидеры продаж' и проверка страницы")
    void testNavigateToBestsellersAndVerifyPage() {
        SteamHomePage homePage = new SteamHomePage(driver, wait);

        By noteworthyMenuButtonLocator = By.xpath("//div[@id='noteworthy_tab']//a[contains(text(), 'Новое и интересное')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(noteworthyMenuButtonLocator));
        Assertions.assertTrue(driver.findElement(noteworthyMenuButtonLocator).isDisplayed(), "Ошибка: Кнопка меню 'Новое и интересное' не видна.");
        System.out.println("✓ Кнопка меню 'Новое и интересное' видна.");

        SteamChartsPage chartsPage = homePage.navigateToBestsellers();

        wait.until(ExpectedConditions.urlContains(Constants.STEAM_TOPSALES_CHARTS_URL_PREFIX));
        Assertions.assertTrue(driver.getCurrentUrl().startsWith(Constants.STEAM_TOPSALES_CHARTS_URL_PREFIX), "Ошибка: URL страницы лидеров продаж не начинается с ожидаемого префикса.");
        System.out.println("✓ Успешно перешли на страницу 'Лидеры продаж'.");

        String pageHeader = chartsPage.getPageHeader();
        Assertions.assertTrue(pageHeader.contains("Лидеры продаж"), "Ошибка: Заголовок страницы лидеров продаж не соответствует ожидаемому.");
        System.out.println("✓ На странице 'Лидеры продаж' заголовок: '" + pageHeader + "' виден.");

        Assertions.assertTrue(chartsPage.isTopSellingTableDisplayed(), "Ошибка: Таблица лидеров продаж не отображается.");
        System.out.println("✓ Таблица лидеров продаж отображается.");

        String firstGame = chartsPage.getFirstGameTitleFromTable();
        Assertions.assertFalse(firstGame.isEmpty(), "Ошибка: Название первой игры в таблице лидеров продаж пусто.");
        System.out.println("✓ Первая игра в таблице: '" + firstGame + "' отображается.");

        chartsPage.clickViewMoreTopSellers();
        wait.until(ExpectedConditions.urlContains("topselling"));
        Assertions.assertTrue(driver.getCurrentUrl().contains("topselling"), "Ошибка: Не удалось кликнуть 'Просмотреть больше' или URL неверен.");
        System.out.println("✓ Клик по 'Просмотреть больше лидеров продаж' успешен.");
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Тест: Навигация в 'Категории' -> 'Бесплатные' и проверка страницы")
    void testNavigateToFreeToPlayAndVerifyPage() {
        SteamHomePage homePage = new SteamHomePage(driver, wait);

        By categoriesMenuButtonLocator = By.xpath("//div[@id='genre_tab']//a[contains(text(), 'Категории')]");
        wait.until(ExpectedConditions.visibilityOfElementLocated(categoriesMenuButtonLocator));
        Assertions.assertTrue(driver.findElement(categoriesMenuButtonLocator).isDisplayed(), "Ошибка: Кнопка меню 'Категории' не видна.");
        System.out.println("✓ Кнопка меню 'Категории' видна.");

        SteamFreeToPlayPage freeToPlayPage = homePage.navigateToFreeToPlay();

        wait.until(ExpectedConditions.urlContains(Constants.STEAM_FREE_TO_PLAY_URL));
        Assertions.assertTrue(driver.getCurrentUrl().startsWith(Constants.STEAM_FREE_TO_PLAY_URL), "Ошибка: Не удалось перейти на страницу 'Бесплатные игры'.");
        System.out.println("✓ Успешно перешли на страницу 'Бесплатные игры'.");

        String pageHeader = freeToPlayPage.getPageHeader();
        Assertions.assertTrue(pageHeader.contains("Бесплатные игры"), "Ошибка: Заголовок страницы бесплатных игр не соответствует ожидаемому.");
        System.out.println("✓ На странице 'Бесплатные игры' заголовок: '" + pageHeader + "' виден.");

        Assertions.assertTrue(freeToPlayPage.isMainVideoPlayerDisplayed(), "Ошибка: Основной видео-плеер на странице бесплатных игр не отображается.");
        System.out.println("✓ Основной видео-плеер отображается.");
    }

    //------------------------------------------------------------------------------------------------------------------

    @Test
    @DisplayName("Тест: Переключение вкладок на главной странице (Популярные новинки, Лидеры продаж, Скидки, Будущие новинки, Бесплатные)")
    void testHomePageTabsSwitching() {
        SteamHomePage homePage = new SteamHomePage(driver, wait);

        // 1. Проверка вкладки "Популярные новинки" (по умолчанию активна)
        homePage.clickNewReleasesTab();
        Assertions.assertEquals("Популярные новинки", homePage.getActiveTabTitle(), "Ошибка: Неправильный заголовок активной вкладки 'Популярные новинки'.");
        Assertions.assertTrue(homePage.isTabContentHeaderDisplayed("Популярные новинки"), "Ошибка: Заголовок контента вкладки 'Популярные новинки' не отображается.");
        System.out.println("✓ Вкладка 'Популярные новинки' активна и её контент отображается.");

        // 2. Переключение на вкладку "Лидеры продаж"
        homePage.clickTopSellersTab();
        Assertions.assertEquals("Лидеры продаж", homePage.getActiveTabTitle(), "Ошибка: Неправильный заголовок активной вкладки 'Лидеры продаж'.");
        Assertions.assertTrue(homePage.isTabContentHeaderDisplayed("Лидеры продаж"), "Ошибка: Заголовок контента вкладки 'Лидеры продаж' не отображается.");
        System.out.println("✓ Вкладка 'Лидеры продаж' активна и её контент отображается.");

        // 3. Переключение на вкладку "Скидки"
        homePage.clickSpecialsTab();
        Assertions.assertEquals("Скидки", homePage.getActiveTabTitle(), "Ошибка: Неправильный заголовок активной вкладки 'Скидки'.");
        Assertions.assertTrue(homePage.isTabContentHeaderDisplayed("Скидки"), "Ошибка: Заголовок контента вкладки 'Скидки' не отображается.");
        System.out.println("✓ Вкладка 'Скидки' активна и её контент отображается.");

        // 4. Переключение на вкладку "Популярные будущие новинки"
        homePage.clickUpcomingTab();
        Assertions.assertEquals("Популярные будущие новинки", homePage.getActiveTabTitle(), "Ошибка: Неправильный заголовок активной вкладки 'Популярные будущие новинки'.");
        Assertions.assertTrue(homePage.isTabContentHeaderDisplayed("Популярные будущие новинки"), "Ошибка: Заголовок контента вкладки 'Популярные будущие новинки' не отображается.");
        System.out.println("✓ Вкладка 'Популярные будущие новинки' активна и её контент отображается.");

        // 5. Переключение на вкладку "Популярные бесплатные игры"
        homePage.clickTrendingFreeTab();
        Assertions.assertEquals("Популярные бесплатные игры", homePage.getActiveTabTitle(), "Ошибка: Неправильный заголовок активной вкладки 'Популярные бесплатные игры'.");
        Assertions.assertTrue(homePage.isTabContentHeaderDisplayed("Популярные бесплатные игры"), "Ошибка: Заголовок контента вкладки 'Популярные бесплатные игры' не отображается.");
        System.out.println("✓ Вкладка 'Популярные бесплатные игры' активна и её контент отображается.");
    }
}