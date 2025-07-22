
import org.example.UserFormPage;
import org.example.SubmissionModalPage;
import org.example.TestPropHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FormFillTest {

    private WebDriver driver;
    private UserFormPage userFormPage;
    private SubmissionModalPage submissionModalPage;

    // Данные для заполнения формы
    private final String firstName = "Иван";
    private final String lastName = "Петров";
    private final String email = "ivan.petrov@example.com";
    private final String gender = "Male";
    private final String mobileNumber = "1234567890";
    private final String dateOfBirth = "15 Jun 1990";
    private final String[] subjects = {"Computer Science", "Physics"};
    private final String[] hobbies = {"Sports", "Music"};
    private final String currentAddress = "Улица Пушкина, дом Колотушкина, 10";
    private final String state = "NCR";
    private final String city = "Delhi";

    @BeforeEach
    void setupSuite() {
        // --- Ручная установка пути к ChromeDriver ---
        // Укажите путь к вашему ChromeDriver.exe
        // Замените "C:\\path\\to\\your\\chromedriver.exe" на фактический путь на вашей машине.
        System.setProperty("webdriver.chrome.driver", "/home/gamer/IdeaProjects/lms/src/resources/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        driver = new ChromeDriver(options);
        driver.get(TestPropHolder.BASE_URL);
        userFormPage = new UserFormPage(driver);
        submissionModalPage = new SubmissionModalPage(driver);
    }

    @Test
    @DisplayName("Проверка заполнения и отправки формы регистрации студента")
    void verifyStudentRegistrationFormSubmission() {
        // Заполнение всех полей
        userFormPage.enterFirstName(firstName);
        userFormPage.enterLastName(lastName);
        userFormPage.enterEmail(email);
        userFormPage.selectGender(gender);
        userFormPage.enterMobileNumber(mobileNumber);
        userFormPage.enterDateOfBirth(dateOfBirth);
        userFormPage.addSubjects(subjects);
        userFormPage.selectHobbies(hobbies);

        userFormPage.enterCurrentAddress(currentAddress);
        userFormPage.selectState(state);
        userFormPage.selectCity(city);

        // Нажимаем кнопку Submit
        userFormPage.submitForm();

        // Проверяем, что модальное окно появилось
        assertTrue(submissionModalPage.isModalDisplayed(), "Модальное окно не появилось после отправки формы.");

        // Получаем данные из модального окна
        Map<String, String> submittedData = submissionModalPage.getSubmittedData();

        // Проверяем, что все данные совпадают
        assertEquals(firstName + " " + lastName, submittedData.get("Student Name"), "Имя студента не совпадает.");
        assertEquals(email, submittedData.get("Student Email"), "Email студента не совпадает.");
        assertEquals(gender, submittedData.get("Gender"), "Пол не совпадает.");
        assertEquals(mobileNumber, submittedData.get("Mobile"), "Мобильный номер не совпадает.");
        // Формат даты в модальном окне может отличаться
        assertEquals("15 June,1990", submittedData.get("Date of Birth"), "Дата рождения не совпадает.");
        assertEquals(String.join(", ", subjects), submittedData.get("Subjects"), "Предметы не совпадают.");
        assertEquals(String.join(", ", hobbies), submittedData.get("Hobbies"), "Хобби не совпадают.");
        assertTrue(submittedData.get("Picture").contains("test_image.jpg"), "Имя файла изображения не совпадает.");
        assertEquals(currentAddress, submittedData.get("Address"), "Адрес не совпадает.");
        assertEquals(state + " " + city, submittedData.get("State and City"), "Штат и город не совпадают.");

        // Код для скриншотов с помощью Ashot удален
        // try { ... } catch (IOException e) { ... }

        // Закрываем модальное окно
        submissionModalPage.closeSubmissionModal();
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}