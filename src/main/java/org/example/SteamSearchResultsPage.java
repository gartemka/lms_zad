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
import java.time.Duration; // Импорт для Duration

public class SteamSearchResultsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- Локаторы для страницы результатов поиска (filter=topsellers) ---
    // Контейнер, содержащий все строки результатов поиска
    private By searchResultsRowsContainer = By.id("search_resultsRows");
    // Локатор для отдельной строки результата поиска (элемент <a>)
    private By searchResultRow = By.xpath("//div[@id='search_resultsRows']/a[contains(@class, 'search_result_row')]");

    // Локаторы для получения названия и цены внутри ОДНОЙ строки результата поиска (используются относительно родительской строки)
    private By gameTitleRelativeLocator = By.xpath(".//span[@class='title']");
    // Локатор для цены: может быть 'free' или числовое значение
    private By gamePriceRelativeLocator = By.xpath(".//div[contains(@class, 'search_price_discount_combined')]//div[contains(@class, 'discount_final_price') or contains(@class, 'free')]");


    public SteamSearchResultsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        // Ожидаем, что мы находимся на странице результатов поиска и контейнер с ними видим
        wait.until(ExpectedConditions.urlContains("search/?filter=topsellers"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchResultsRowsContainer));
        System.out.println("Создан Page Object SteamSearchResultsPage. Текущий URL: " + driver.getCurrentUrl());
    }

    /**
     * Получает названия и цены первых N игр со страницы результатов поиска.
     * @param count Количество игр для получения.
     * @return Список Map<String, String>, где каждый Map содержит "title" и "price".
     */
    public List<Map<String, String>> getTopNGamesData(int count) {
        List<Map<String, String>> gamesData = new ArrayList<>();
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchResultsRowsContainer));

        // Находим все строки результатов поиска
        List<WebElement> rows = driver.findElements(searchResultRow);

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

    /**
     * Получает название игры из указанной строки результата поиска.
     * @param rowElement WebElement строки результата поиска (элемент <a>).
     * @return Название игры.
     */
    public String getGameTitleFromRow(WebElement rowElement) {
        // Ожидаем видимости элемента с названием внутри строки
        return wait.until(ExpectedConditions.visibilityOf(rowElement.findElement(gameTitleRelativeLocator))).getText();
    }

    /**
     * Получает цену игры из указанной строки результата поиска.
     * @param rowElement WebElement строки результата поиска (элемент <a>).
     * @return Цена игры (текст).
     */
    public String getGamePriceFromRow(WebElement rowElement) {
        // Ожидаем видимости элемента с ценой внутри строки
        return wait.until(ExpectedConditions.visibilityOf(rowElement.findElement(gamePriceRelativeLocator))).getText();
    }


}