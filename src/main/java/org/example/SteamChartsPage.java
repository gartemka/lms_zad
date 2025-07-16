package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SteamChartsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- Локаторы для страницы чартов ---
    private By pageHeader = By.xpath("//h1[text()='Лидеры продаж']");
    private By countryDropdown = By.xpath("//button[./div[text()='Польша']]"); // Кнопка выбора страны
    private By topSellingTable = By.xpath("//table[contains(@class, '_3arZn0BMPzyhcYNADe193m')]");
    private By firstGameTitleInTable = By.xpath("//table[contains(@class, '_3arZn0BMPzyhcYNADe193m')]//tr[1]//div[@class='_1n_4-zvf0n4aqGEksbgW9N']");
    private By viewMoreTopSellersButton = By.xpath("//button[text()='Просмотреть больше лидеров продаж']");


    public SteamChartsPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        // Убедимся, что мы на странице чартов
        wait.until(ExpectedConditions.urlContains(Constants.STEAM_TOPSALES_CHARTS_URL_PREFIX));
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
     * Получает название первой игры в таблице лидеров продаж.
     */
    public String getFirstGameTitleFromTable() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(firstGameTitleInTable)).getText();
    }

    /**
     * Кликает по кнопке "Просмотреть больше лидеров продаж".
     */
    public void clickViewMoreTopSellers() {
        wait.until(ExpectedConditions.elementToBeClickable(viewMoreTopSellersButton)).click();
        System.out.println("Клик по кнопке 'Просмотреть больше лидеров продаж'.");
        // После клика ожидаем, что URL изменится или страница обновится
        // Например, url должен стать https://store.steampowered.com/charts/topselling/PL#tab=TopSellers
        wait.until(ExpectedConditions.urlContains("topselling/PL#tab=TopSellers"));
    }
}