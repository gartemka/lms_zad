package org.example;

public class TestPropHolder {
    // URL
    public static final String BASE_URL = Constants.STEAM_BASE_URL;
    public static final String LOGIN_URL = Constants.STEAM_LOGIN_URL;

    // Учетные данные
    public static final String USERNAME = Constants.TEST_USERNAME;
    public static final String PASSWORD = Constants.TEST_PASSWORD;

    // Тайм-ауты
    public static final int DEFAULT_WAIT_TIMEOUT_SECONDS = Constants.DEFAULT_WAIT_TIMEOUT_SECONDS;
    public static final int SHORT_WAIT_TIMEOUT_SECONDS = Constants.SHORT_WAIT_TIMEOUT_SECONDS;

    // Ошибки
    public static final String ERROR_MESSAGE_PART_RU = Constants.ERROR_MESSAGE_PART_RU;
    public static final String ERROR_MESSAGE_PART_EN = Constants.ERROR_MESSAGE_PART_EN;
    public static final String INVALID_CREDENTIALS_FULL_MESSAGE_RU = Constants.INVALID_CREDENTIALS_FULL_MESSAGE_RU;

    // Дополнительные свойства, если нужны (например, путь к файлу для загрузки, если это актуально для Steam)
    // public static final String UPLOAD_FILE_PATH = "src/test/resources/test_image.jpg";
}