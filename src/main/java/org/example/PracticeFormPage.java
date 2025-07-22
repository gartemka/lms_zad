package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Keys;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.Select; // Импорт Select class

import java.io.File;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.Month; // Импорт Month enum

public class PracticeFormPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- Локаторы формы ---
    private By firstNameField = By.id("firstName");
    private By lastNameField = By.id("lastName");
    private By userEmailField = By.id("userEmail");

    // Радио-кнопки для пола - кликаем по LABEL, это надежнее
    private By genderMaleRadioLabel = By.cssSelector("label[for='gender-radio-1']");
    private By genderFemaleRadioLabel = By.cssSelector("label[for='gender-radio-2']");
    private By genderOtherRadioLabel = By.cssSelector("label[for='gender-radio-3']");

    private By userNumberField = By.id("userNumber");
    private By dateOfBirthInputField = By.id("dateOfBirthInput"); // Поле ввода для даты рождения

    // Subjects - используем input внутри контейнера subjectsContainer
    private By subjectsContainer = By.id("subjectsContainer"); // Контейнер для Select
    private By subjectsInput = By.id("subjectsInput"); // Поле ввода для предметов (внутри Select)

    // Хобби - кликаем по LABEL, это надежнее
    private By hobbiesSportsCheckboxLabel = By.cssSelector("label[for='hobbies-checkbox-1']");
    private By hobbiesReadingCheckboxLabel = By.cssSelector("label[for='hobbies-checkbox-2']");
    private By hobbiesMusicCheckboxLabel = By.cssSelector("label[for='hobbies-checkbox-3']");

    private By uploadPictureInput = By.id("uploadPicture"); // Поле для загрузки файла (type="file")
    private By currentAddressField = By.id("currentAddress");

    // State и City - используем input внутри их контейнеров
    private By stateDropdownContainer = By.id("state"); // Контейнер для State Select
    private By stateInput = By.id("react-select-3-input"); // Поле ввода для State

    private By cityDropdownContainer = By.id("city"); // Контейнер для City Select
    private By cityInput = By.id("react-select-4-input"); // Поле ввода для City

    private By submitButton = By.id("submit");

    // --- Локаторы модального окна подтверждения ---
    private By modalTitle = By.id("example-modal-sizes-title-lg");
    private By modalTableRows = By.xpath("//div[@class='table-responsive']//tbody/tr");
    private By closeSubmitModalButton = By.id("closeLargeModal");


    public PracticeFormPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        // Убедимся, что форма загружена, ожидая видимости первого поля
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));
        System.out.println("Инициализирован Page Object: Practice Form Page.");
    }

    // --- Приватные методы для динамического создания локаторов ---
    // ИСПРАВЛЕНИЕ: Методы возвращают By, принимают String
    private By getSubjectOptionByText(String text) {
        return By.xpath(String.format("//div[contains(@id, 'react-select') and contains(@id, 'option') and text()='%s']", text));
    }

    // ИСПРАВЛЕНИЕ: Методы возвращают By, принимают String
    private By getStateOptionByText(String text) {
        return By.xpath(String.format("//div[contains(@id, 'react-select-3-option') and text()='%s']", text));
    }

    // ИСПРАВЛЕНИЕ: Методы возвращают By, принимают String
    private By getCityOptionByText(String text) {
        return By.xpath(String.format("//div[contains(@id, 'react-select-4-option') and text()='%s']", text));
    }


    public void setFirstName(String firstName) {
        wait.until(ExpectedConditions.elementToBeClickable(firstNameField)).sendKeys(firstName);
        System.out.println("Заполнено имя: " + firstName);
    }

    public void setLastName(String lastName) {
        wait.until(ExpectedConditions.elementToBeClickable(lastNameField)).sendKeys(lastName);
        System.out.println("Заполнена фамилия: " + lastName);
    }

    public void setEmail(String email) {
        wait.until(ExpectedConditions.elementToBeClickable(userEmailField)).sendKeys(email);
        System.out.println("Заполнен Email: " + email);
    }

    public void setGender(String gender) {
        By genderLocator;
        switch (gender.toLowerCase()) {
            case "male":
                genderLocator = genderMaleRadioLabel;
                break;
            case "female":
                genderLocator = genderFemaleRadioLabel;
                break;
            case "other":
                genderLocator = genderOtherRadioLabel;
                break;
            default:
                throw new IllegalArgumentException("Неверный пол: " + gender);
        }
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(genderLocator));
        System.out.println("Выбран пол: " + gender);
    }

    public void setMobileNumber(String mobileNumber) {
        wait.until(ExpectedConditions.elementToBeClickable(userNumberField)).sendKeys(mobileNumber);
        System.out.println("Заполнен номер телефона: " + mobileNumber);
    }

    public void setDateOfBirth(int year, int month, int day) {
        wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthInputField)).click();

        WebElement monthDropdownElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".react-datepicker__month-select")));
        Select monthSelect = new Select(monthDropdownElement);
        String monthName = Month.of(month).toString().charAt(0) + Month.of(month).toString().substring(1).toLowerCase();
        monthSelect.selectByVisibleText(monthName);

        WebElement yearDropdownElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".react-datepicker__year-select")));
        Select yearSelect = new Select(yearDropdownElement);
        yearSelect.selectByVisibleText(String.valueOf(year));

        By dayLocator = By.xpath(String.format("//div[contains(@class, 'react-datepicker__day') and not(contains(@class, 'outside-month')) and text()='%d']", day));
        wait.until(ExpectedConditions.elementToBeClickable(dayLocator)).click();
        System.out.println("Заполнена дата рождения: " + day + "/" + month + "/" + year);
    }

    public void setSubjects(String... subjects) {
        for (String subject : subjects) {
            WebElement subjectInput = wait.until(ExpectedConditions.elementToBeClickable(subjectsInput));
            subjectInput.sendKeys(subject);
            // ИСПРАВЛЕНИЕ: Вызываем приватный метод getSubjectOptionByText
            wait.until(ExpectedConditions.elementToBeClickable(getSubjectOptionByText(subject))).click();
            System.out.println("Добавлен предмет: " + subject);
        }
    }

    public void setHobbies(String... hobbies) {
        for (String hobby : hobbies) {
            By hobbyLocator;
            switch (hobby.toLowerCase()) {
                case "sports":
                    hobbyLocator = hobbiesSportsCheckboxLabel;
                    break;
                case "reading":
                    hobbyLocator = hobbiesReadingCheckboxLabel;
                    break;
                case "music":
                    hobbyLocator = hobbiesMusicCheckboxLabel;
                    break;
                default:
                    throw new IllegalArgumentException("Неверное хобби: " + hobby);
            }
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", driver.findElement(hobbyLocator));
            System.out.println("Выбрано хобби: " + hobby);
        }
    }

    public void uploadPicture(String filePath) {
        File uploadFile = new File(filePath);
        if (!uploadFile.exists()) {
            throw new IllegalArgumentException("Файл для загрузки не найден по пути: " + filePath);
        }
        driver.findElement(uploadPictureInput).sendKeys(uploadFile.getAbsolutePath());
        System.out.println("Загружено изображение: " + uploadFile.getName());
    }

    public void setCurrentAddress(String address) {
        wait.until(ExpectedConditions.elementToBeClickable(currentAddressField)).sendKeys(address);
        System.out.println("Заполнен текущий адрес.");
    }

    public void setStateAndCity(String state, String city) {
        // Выбираем State
        WebElement stateInputElem = wait.until(ExpectedConditions.elementToBeClickable(stateInput));
        stateInputElem.sendKeys(state);
        // ИСПРАВЛЕНИЕ: Вызываем приватный метод getStateOptionByText
        wait.until(ExpectedConditions.elementToBeClickable(getStateOptionByText(state))).click();
        System.out.println("Выбрана область: " + state);

        // Выбираем City
        WebElement cityInputElem = wait.until(ExpectedConditions.elementToBeClickable(cityInput));
        cityInputElem.sendKeys(city);
        // ИСПРАВЛЕНИЕ: Вызываем приватный метод getCityOptionByText
        wait.until(ExpectedConditions.elementToBeClickable(getCityOptionByText(city))).click();
        System.out.println("Выбран город: " + city);
    }

    public void submitForm() {
        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", submitBtn);
        System.out.println("Нажата кнопка 'Submit'.");
    }

    public Map<String, String> getSubmissionData() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
        System.out.println("Модальное окно подтверждения отображается.");

        Map<String, String> submittedData = new HashMap<>();
        List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(modalTableRows));
        for (WebElement row : rows) {
            String label = row.findElement(By.xpath("./td[1]")).getText();
            String value = row.findElement(By.xpath("./td[2]")).getText();
            submittedData.put(label, value);
        }
        System.out.println("Получены данные из модального окна.");
        return submittedData;
    }

    public void closeSubmissionModal() {
        wait.until(ExpectedConditions.elementToBeClickable(closeSubmitModalButton)).click();
        System.out.println("Модальное окно подтверждения закрыто.");
        wait.until(ExpectedConditions.invisibilityOfElementLocated(modalTitle));
    }
}