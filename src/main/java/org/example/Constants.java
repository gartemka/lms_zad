package org.example;

public final class Constants {
    private Constants() {
        // Приватный конструктор, чтобы нельзя было создать экземпляры этого класса.
    }

    // --- Константы вашего проекта ---

    // URL для Steam
    public static final String STEAM_BASE_URL = "https://store.steampowered.com/";
    public static final String STEAM_LOGIN_URL = STEAM_BASE_URL + "login/";
    public static final String STEAM_LOGOUT_URL = STEAM_BASE_URL + "logout/";
    public static final String STEAM_SEARCH_URL_PREFIX = STEAM_BASE_URL + "search/?term=";
    public static final String STEAM_TOPSALES_CHARTS_URL_PREFIX = STEAM_BASE_URL + "charts/topselling/"; // <-- ДОБАВЛЕНА
    public static final String STEAM_FREE_TO_PLAY_URL = STEAM_BASE_URL + "genre/Free%20to%20Play/"; // <-- Добавлен
    // ВНИМАНИЕ: ЗАМЕНИТЕ ЭТИ ЗАГЛУШКИ НА РЕАЛЬНЫЕ УЧЕТНЫЕ ДАННЫЕ ВАШЕГО ТЕСТОВОГО АККАУНТА STEAM
    public static final String TEST_USERNAME = "artemgg21022102"; //
    public static final String TEST_PASSWORD = "21022102aA"; //

    // Пути к драйверам браузеров (Selenium Manager сделает их ненужными, но оставим для справки)
    public static final String CHROMEDRIVER_PATH = "/usr/local/bin/chromedriver";
    public static final String GECKODRIVER_PATH = "/usr/local/bin/geckodriver";

    // Тип браузера для запуска тестов по умолчанию
    public static final String BROWSER_TYPE = "chrome"; // <-- По умолчанию Chrome

    // Таймауты ожидания (в секундах)
    public static final int TINY_PAUSE_MILLISECONDS = 500; // Для отладки наведений (0.5 секунды)
    public static final int DEFAULT_WAIT_TIMEOUT_SECONDS = 15;
    public static final int SHORT_WAIT_TIMEOUT_SECONDS = 5; // <-- ДОБАВЛЕНА ЭТА КОНСТАНТА

    // Сообщения об ошибках логина (Steam может выдавать их на разных языках)
    public static final String ERROR_MESSAGE_PART_RU = "неверное имя аккаунта или пароль";
    public static final String ERROR_MESSAGE_PART_EN = "Incorrect account name or password";

    // Можно добавить другие константы
    public static final String REPORT_FILE_NAME = "TestReport.html";

}