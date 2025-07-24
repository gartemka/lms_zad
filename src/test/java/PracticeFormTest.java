import org.example.PracticeFormPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.Map;
import java.io.File;

public class PracticeFormTest extends BaseTest {

    @Test
    @DisplayName("Тест: Заполнение и проверка формы регистрации студента на DemoQA")
    void testStudentRegistrationForm() {
        // Breakpoint 1: Начало теста, инициализация Page Object
        PracticeFormPage formPage = new PracticeFormPage(driver, wait);

        // --- 1. Заполнение всех полей ---
        String firstName = "Иван";
        String lastName = "Иванов";
        String email = "ivan.ivanov@example.com";
        String gender = "Male";
        String mobileNumber = "1234567890";
        int birthYear = 1990;
        int birthMonth = 7; // Июль (1-based index)
        int birthDay = 22;
        String[] subjects = {"Maths", "Computer Science"};
        String[] hobbies = {"Sports", "Reading"};
        String picturePath = "/home/gamer/IdeaProjects/lms/src/resources/test_picture.png"; // Убедитесь, что этот файл существует!
        String currentAddress = "Улица Пушкина, дом Колотушкина, 5";
        String state = "NCR";
        String city = "Delhi";

        // Breakpoint 2: Перед заполнением полей
        formPage.setFirstName(firstName);
        formPage.setLastName(lastName);
        formPage.setEmail(email);
        formPage.setGender(gender);
        formPage.setMobileNumber(mobileNumber);
        formPage.setDateOfBirth(birthYear, birthMonth, birthDay);
        formPage.setSubjects(subjects);
        formPage.setHobbies(hobbies);

        File pictureFile = new File(picturePath);
        if (!pictureFile.exists()) {
            System.err.println("ВНИМАНИЕ: Файл изображения для теста не найден! Создайте " + picturePath);
            // Breakpoint 3: Файл изображения не найден, но продолжаем
        } else {
            formPage.uploadPicture(pictureFile.getAbsolutePath());
            // Breakpoint 4: Изображение загружено
        }

        formPage.setCurrentAddress(currentAddress);
        formPage.setStateAndCity(state, city);

        // --- 2. Клик на кнопку Submit ---
        // Breakpoint 5: Перед отправкой формы
        formPage.submitForm();

        // --- 3. Проверка данных в модальном окне ---
        // Breakpoint 6: После отправки формы, перед получением данных из модального окна
        Map<String, String> submittedData = formPage.getSubmissionData();

        // Проверки (ассерты) для каждой части данных
        // Breakpoint 7: Перед каждым ассертом для проверки данных
        Assertions.assertEquals(firstName + " " + lastName, submittedData.get("Student Name"), "Имя студента не совпадает.");
        Assertions.assertEquals(email, submittedData.get("Student Email"), "Email не совпадает.");
        Assertions.assertEquals(gender, submittedData.get("Gender"), "Пол не совпадает.");
        Assertions.assertEquals(mobileNumber, submittedData.get("Mobile"), "Номер телефона не совпадает.");

        String expectedDateOfBirth = String.format("%02d", birthDay) + " " +
                java.time.Month.of(birthMonth).name().substring(0, 1).toUpperCase() +
                java.time.Month.of(birthMonth).name().substring(1).toLowerCase() +
                "," + birthYear; // "22 July,1990"
        Assertions.assertEquals(expectedDateOfBirth, submittedData.get("Date of Birth"), "Дата рождения не совпадает.");

        String expectedSubjects = String.join(", ", subjects);
        Assertions.assertEquals(expectedSubjects, submittedData.get("Subjects"), "Предметы не совпадают.");

        String expectedHobbies = String.join(", ", hobbies);
        Assertions.assertEquals(expectedHobbies, submittedData.get("Hobbies"), "Хобби не совпадают.");


        Assertions.assertEquals(currentAddress, submittedData.get("Address"), "Адрес не совпадает.");
        Assertions.assertEquals(state + " " + city, submittedData.get("State and City"), "Штат и город не совпадают.");

        System.out.println("✓ Все данные в модальном окне подтверждены!");

        // Breakpoint 8: Перед закрытием модального окна
        formPage.closeSubmissionModal();
        System.out.println("Тест формы регистрации студента успешно завершен.");
        // Breakpoint 9: Конец теста
    }
}