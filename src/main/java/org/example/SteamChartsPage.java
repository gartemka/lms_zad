package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Duration;

public class SteamChartsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- Локаторы для страницы чартов (https://store.steampowered.com/charts/topselling/) ---
    // ИСПРАВЛЕНИЕ: topsellingTableXPath теперь просто String, а не By
    private String topsellingTableXPath = "//table[contains(@class, '_3arZn0BMPzyhcYNADe193m')]";
    private By pageHeader = By.xpath("//div[@id='steam_charts_root_content']//h1[text()='Лидеры продаж']");

    // Локаторы для выпадающего списка страны
    private By countryDropdownButton = By.xpath("//div[contains(@class, 'cuSpV3QmWAyfy5jEL_lAD')]//button[contains(@class, 'DialogDropDown') and ./div[contains(@class, 'DialogDropDown_CurrentDisplay')]]");
    private By countryGlobalOption = By.xpath("//div[contains(@class, 'DialogDropDownMenu')]//div[text()='По всему миру']");
    private By currentCountryDisplay = By.xpath("//div[contains(@class, 'cuSpV3QmWAyfy5jEL_lAD')]//div[contains(@class, 'DialogDropDown_CurrentDisplay')]");

    // ИСПРАВЛЕНИЕ: topSellingTable создается из строкового topsellingTableXPath
    private By topSellingTable = By.xpath(topsellingTableXPath);
    // ИСПРАВЛЕНИЕ: tableRows использует строковый topsellingTableXPath
    private By tableRows = By.xpath(topsellingTableXPath + "//tbody//tr"); // Все строки таблицы (без заголовка)

    private By gameTitleRelativeLocator = By.xpath(".//a[contains(@class, '_2C5PJOUH6RqyuBNEwaCE9X')]//div[contains(@class, '_1n_4-zvf0n4aqGEksbgW9N')]");
    private By gamePriceRelativeLocator = By.xpath(".//div[contains(@class, 'StoreSalePriceWidgetContainer')]//div[contains(@class, '_3j4dI1yA7cRfCvK8h406OB')]");

    private By viewMoreTopSellersButton = By.xpath("//button[text()='Просмотреть больше лидеров продаж']");


    public SteamChartsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        // Убедимся, что мы на странице чартов
        wait.until(ExpectedConditions.urlContains(Constants.STEAM_CHARTS_TOPSALES_URL));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader));
        System.out.println("Создан Page Object SteamChartsPage. Текущий URL: " + driver.getCurrentUrl());
    }

    /**
     * Получает заголовок страницы чартов.
     */
    public String getPageHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader)).getText();
    }

    /**
     * Проверяет, отображается ли таблица лидеров продаж.
     */
    public boolean isTopSellingTableDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(topSellingTable)).isDisplayed();
    }

    /**
     * Получает название игры из указанной строки таблицы.
     * @param rowElement WebElement строки таблицы.
     * @return Название игры.
     */
    public String getGameTitleFromRow(WebElement rowElement) {
        // ИСПРАВЛЕНИЕ: Используем findElement на rowElement
        return wait.until(ExpectedConditions.visibilityOf(rowElement.findElement(gameTitleRelativeLocator))).getText();
    }

    /**
     * Получает цену игры из указанной строки таблицы.
     * @param rowElement WebElement строки таблицы.
     * @return Цена игры (текст).
     */
    public String getGamePriceFromRow(WebElement rowElement) {
        // ИСПРАВЛЕНИЕ: Используем findElement на rowElement
        return wait.until(ExpectedConditions.visibilityOf(rowElement.findElement(gamePriceRelativeLocator))).getText();
    }

    /**
     * Кликает по кнопке "Просмотреть больше лидеров продаж".
     */
    public void clickViewMoreTopSellers() {
        wait.until(ExpectedConditions.elementToBeClickable(viewMoreTopSellersButton)).click();
        System.out.println("Клик по кнопке 'Просмотреть больше лидеров продаж'.");
        wait.until(ExpectedConditions.urlContains("topselling/PL#tab=TopSellers")); // URL для Польши
    }

    /**
     * Кликает по первой игре в списке.
     * @return Название кликнутой игры для проверки на следующей странице.
     */
    public String clickFirstGameInList() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(topSellingTable)); // Убедимся, что таблица видна
        // ИСПРАВЛЕНИЕ: Используем topsellingTableXPath для получения первой строки
        WebElement firstRow = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(topsellingTableXPath + "//tr[1]")));

        // Получаем элемент ссылки внутри первой строки
        // ИСПРАВЛЕНИЕ: FindElement вызывается на firstRow
        WebElement firstGameLink = wait.until(ExpectedConditions.elementToBeClickable(firstRow.findElement(By.xpath(".//a[contains(@class, '_2C5PJOUH6RqyuBNEwaCE9X')]"))));

        String gameTitle = getGameTitleFromRow(firstRow); // Получаем название перед кликом

        firstGameLink.click();
        System.out.println("Клик по первой игре в списке: " + gameTitle);
        return gameTitle;
    }

    /**
     * Открывает выпадающий список стран.
     */
    public void openCountryDropdown() {
        wait.until(ExpectedConditions.elementToBeClickable(countryDropdownButton)).click();
        System.out.println("Клик по выпадающему списку стран.");
        wait.until(ExpectedConditions.visibilityOfElementLocated(countryGlobalOption));
    }

    /**
     * Выбирает опцию "По всему миру" в выпадающем списке стран.
     */
    public void selectGlobalCountryOption() {
        wait.until(ExpectedConditions.elementToBeClickable(countryGlobalOption)).click();
        System.out.println("Выбрана опция 'По всему миру'.");
        wait.until(ExpectedConditions.textToBePresentInElementLocated(currentCountryDisplay, "По всему миру"));
        wait.until(ExpectedConditions.urlContains("topselling/global"));
    }

    /**
     * Получает названия и цены первых N игр.
     * @param count Количество игр для получения.
     * @return Список Map<String, String>, где каждый Map содержит "title" и "price".
     */
    public List<Map<String, String>> getTopNGamesData(int count) {
        List<Map<String, String>> gamesData = new ArrayList<>();
        wait.until(ExpectedConditions.visibilityOfElementLocated(topSellingTable));
        List<WebElement> rows = driver.findElements(tableRows);

        int actualCount = Math.min(count, rows.size());

        for (int i = 0; i < actualCount; i++) {
            WebElement rowElement = rows.get(i);
            String title = getGameTitleFromRow(rowElement);
            String price = getGamePriceFromRow(rowElement);
            Map<String, String> gameInfo = new HashMap<>();
            gameInfo.put("title", title);
            gameInfo.put("price", price);
            gamesData.add(gameInfo);
        }
        return gamesData;
    }
}