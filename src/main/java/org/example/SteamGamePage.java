package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SteamGamePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- Локаторы для страницы конкретной игры ---
    private By gameTitleLocator = By.id("appHubAppName"); // Заголовок игры
    private By releaseDateLocator = By.xpath("//div[@class='release_date']/div[@class='date']"); // Дата выхода
    private By developerLocator = By.xpath("//div[@id='developers_and_publishers']//a[contains(@href, '/developer/')]"); // Разработчик
    private By mainGenreLocator = By.xpath("//div[@class='glance_tags popular_tags']//a[contains(@href, '/tags/')][1]"); // Основной жанр (первый в списке популярных тегов)
    private By gamePriceLocator = By.xpath("//div[contains(@class, 'game_area_purchase_price')]"); // Локатор для цены на странице игры (если есть)


    public SteamGamePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        // Ожидаем, что страница игры загрузилась (URL содержит "app/")
        wait.until(ExpectedConditions.urlContains("/app/"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(gameTitleLocator)); // Ждем появления заголовка игры
        System.out.println("Создан Page Object SteamGamePage. Текущий URL: " + driver.getCurrentUrl());
    }

    /**
     * Получает заголовок (название) игры.
     */
    public String getGameTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(gameTitleLocator)).getText();
    }

    /**
     * Получает дату выхода игры.
     */
    public String getReleaseDate() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(releaseDateLocator)).getText();
    }

    /**
     * Получает имя разработчика игры.
     */
    public String getDeveloper() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(developerLocator)).getText();
    }

    /**
     * Получает основной жанр игры.
     */
    public String getMainGenre() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(mainGenreLocator)).getText();
    }

    /**
     * Получает цену игры со страницы.
     */
    public String getGamePrice() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(gamePriceLocator)).getText();
        } catch (Exception e) {
            System.out.println("Цена на странице игры не найдена: " + e.getMessage());
            return "Цена не найдена"; // Возвращаем, если цена не найдена
        }
    }
}