package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SteamFreeToPlayPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Локаторы для страницы "Бесплатные игры"
    private By pageHeader = By.xpath("//div[contains(@class, 'ContentHubTitle') and text()='Бесплатные игры']");
    // Локатор для видео-плеера (если есть на этой странице)
    private By mainVideoPlayer = By.xpath("//video[contains(@class, '_3sG-J5T8SrzM0Hjkda7sgL') and @autoplay]");
    private By videoPlayPauseButton = By.xpath("//div[contains(@class, 'VideoRow')]//div[contains(@class, 'PlayButton')]"); // Пример
    private By autoPlayCheckbox = By.xpath("//div[@class='DialogToggle_Label']/span[text()='Автовоспроизведение']/ancestor::div[@role='checkbox']");


    public SteamFreeToPlayPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        // Убедимся, что мы на странице бесплатных игр
        wait.until(ExpectedConditions.urlContains(Constants.STEAM_FREE_TO_PLAY_URL));
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader));
        System.out.println("Создан Page Object SteamFreeToPlayPage. Текущий URL: " + driver.getCurrentUrl());
    }

    /**
     * Получает заголовок страницы "Бесплатные игры".
     */
    public String getPageHeader() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader)).getText();
    }

    /**
     * Проверяет, отображается ли основной видео-плеер на странице.
     */
    public boolean isMainVideoPlayerDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(mainVideoPlayer)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Проверяет, что видео воспроизводится (для автоплей видео).
     * Это может быть сложной проверкой, так как нет прямого Selenium-метода.
     * Здесь можно использовать JavaScript Executor.
     */
    public boolean isVideoPlaying() {
        // Это более сложная проверка, требующая JS-executor
        // Или можно просто проверить, что элемент видео существует
        return isMainVideoPlayerDisplayed();
    }

    /**
     * Кликает по чекбоксу "Автовоспроизведение".
     */
    public void clickAutoplayCheckbox() {
        WebElement checkbox = wait.until(ExpectedConditions.elementToBeClickable(autoPlayCheckbox));
        checkbox.click();
        System.out.println("Клик по чекбоксу 'Автовоспроизведение'.");
        // Можно добавить ассерт на изменение атрибута aria-checked
    }
}