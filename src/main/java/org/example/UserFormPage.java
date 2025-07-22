package org.example;


import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class UserFormPage {
    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    // Локаторы элементов формы
    private final By firstNameField = By.id("firstName");
    private final By lastNameField = By.id("lastName");
    private final By userEmailField = By.id("userEmail");
    private final By genderMaleRadio = By.id("gender-radio-1");
    private final By genderFemaleRadio = By.id("gender-radio-2");
    private final By genderOtherRadio = By.id("gender-radio-3");
    private final By userNumberField = By.id("userNumber");
    private final By dateOfBirthInput = By.id("dateOfBirthInput");
    private final By subjectsInput = By.id("subjectsInput");
    private final By hobbiesSportsCheckbox = By.id("hobbies-checkbox-1");
    private final By hobbiesReadingCheckbox = By.id("hobbies-checkbox-2");
    private final By hobbiesMusicCheckbox = By.id("hobbies-checkbox-3");
    private final By uploadPictureInput = By.id("uploadPicture");
    private final By currentAddressTextarea = By.id("currentAddress");
    private final By stateDropdown = By.id("react-select-3-input");
    private final By cityDropdown = By.id("react-select-4-input");
    private final By submitButton = By.id("submit");

    public UserFormPage(WebDriver driver) {
        this.webDriver = driver;
        this.webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterFirstName(String firstName) {
        webDriver.findElement(firstNameField).sendKeys(firstName);
    }

    public void enterLastName(String lastName) {
        webDriver.findElement(lastNameField).sendKeys(lastName);
    }

    public void enterEmail(String email) {
        webDriver.findElement(userEmailField).sendKeys(email);
    }

    public void selectGender(String gender) {
        switch (gender.toLowerCase()) {
            case "male":
                webDriver.findElement(genderMaleRadio).click();
                break;
            case "female":
                webDriver.findElement(genderFemaleRadio).click();
                break;
            case "other":
                webDriver.findElement(genderOtherRadio).click();
                break;
            default:
                throw new IllegalArgumentException("Неверное значение пола: " + gender);
        }
    }

    public void enterMobileNumber(String mobileNumber) {
        webDriver.findElement(userNumberField).sendKeys(mobileNumber);
    }

    public void enterDateOfBirth(String date) {
        WebElement dobElement = webDriver.findElement(dateOfBirthInput);
        dobElement.click(); // Открываем календарь
        dobElement.sendKeys(Keys.CONTROL + "a"); // Выделяем весь текст
        dobElement.sendKeys(Keys.DELETE); // Удаляем текст
        dobElement.sendKeys(date); // Вводим новую дату
        dobElement.sendKeys(Keys.ENTER); // Закрываем календарь
    }

    public void addSubjects(String... subjects) {
        WebElement subjectsElement = webDriver.findElement(subjectsInput);
        for (String subject : subjects) {
            subjectsElement.sendKeys(subject);
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("react-select-2-option-0")));
            subjectsElement.sendKeys(Keys.ENTER);
        }
    }

    public void selectHobbies(String... hobbies) {
        for (String hobby : hobbies) {
            switch (hobby.toLowerCase()) {
                case "sports":
                    webDriver.findElement(hobbiesSportsCheckbox).click();
                    break;
                case "reading":
                    webDriver.findElement(hobbiesReadingCheckbox).click();
                    break;
                case "music":
                    webDriver.findElement(hobbiesMusicCheckbox).click();
                    break;
                default:
                    System.out.println("Хобби " + hobby + " не найдено.");
            }
        }
    }

    public void uploadPicture(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IllegalArgumentException("Файл не найден: " + filePath);
        }
        webDriver.findElement(uploadPictureInput).sendKeys(file.getAbsolutePath());
    }

    public void enterCurrentAddress(String address) {
        webDriver.findElement(currentAddressTextarea).sendKeys(address);
    }

    public void selectState(String state) {
        WebElement stateElement = webDriver.findElement(stateDropdown);
        stateElement.sendKeys(state);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id^='react-select-3-option']")));
        stateElement.sendKeys(Keys.ENTER);
    }

    public void selectCity(String city) {
        WebElement cityElement = webDriver.findElement(cityDropdown);
        webDriverWait.until(ExpectedConditions.elementToBeClickable(cityElement));
        cityElement.sendKeys(city);
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[id^='react-select-4-option']")));
        cityElement.sendKeys(Keys.ENTER);
    }

    public void submitForm() {
        webDriver.findElement(submitButton).sendKeys(Keys.ENTER); // Использование Keys.ENTER для обхода возможных перекрытий кнопкой
    }
}
