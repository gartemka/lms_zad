package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmissionModalPage {
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    // Локатор для модального окна
    private final By modalDialog = By.className("modal-dialog");
    // Локатор для таблицы с данными
    private final By studentDetailsTable = By.xpath("//div[@class='modal-body']//table//tbody");
    private final By closeButton = By.id("closeLargeModal");

    public SubmissionModalPage(WebDriver driver) {
        this.webDriver = driver;
        this.webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public boolean isModalDisplayed() {
        try {
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(modalDialog));
            return webDriver.findElement(modalDialog).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public Map<String, String> getSubmittedData() {
        Map<String, String> data = new HashMap<>();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(studentDetailsTable));
        WebElement tableBody = webDriver.findElement(studentDetailsTable);
        List<WebElement> rows = tableBody.findElements(By.tagName("tr"));

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() == 2) {
                String key = cells.get(0).getText().trim();
                String value = cells.get(1).getText().trim();
                data.put(key, value);
            }
        }
        return data;
    }

    public void closeSubmissionModal() {
        webDriver.findElement(closeButton).click();
    }
}

