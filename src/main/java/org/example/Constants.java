package org.example;

public final class Constants {
    private Constants() {
        // Приватный конструктор
    }

    // --- Константы вашего проекта ---

    // URL для Steam
    public static final String STEAM_BASE_URL = "https://store.steampowered.com/";
    public static final String STEAM_LOGIN_URL = STEAM_BASE_URL + "login/";
    public static final String STEAM_LOGOUT_URL = STEAM_BASE_URL + "logout/";
    public static final String STEAM_SEARCH_URL_PREFIX = STEAM_BASE_URL + "search/?term=";
    public static final String STEAM_CHARTS_TOPSALES_URL = STEAM_BASE_URL + "charts/topselling/";
    public static final String STEAM_FREE_TO_PLAY_URL = STEAM_BASE_URL + "genre/Free%20to%20Play/";

    // Учетные данные для тестов (для тестового аккаунта, НЕ для реального!)
    // ВНИМАНИЕ: ЗАМЕНИТЕ ЭТИ ЗАГЛУШКИ НА РЕАЛЬНЫЕ УЧЕТНЫЕ ДАННЫЕ ВАШЕГО ТЕСТОВОГО АККАУНТА STEAM
    public static final String TEST_USERNAME = "ВАШ_ТЕСТОВЫЙ_ЛОГИН_STEAM"; // <-- ОБНОВИТЕ
    public static final String TEST_PASSWORD = "ВАШ_ТЕСТОВЫЙ_ПАРОЛЬ_STEAM"; // <-- ОБНОВИТЕ

    // Пути к драйверам браузеров (Selenium Manager сделает их ненужными, но оставим для справки)
    public static final String CHROMEDRIVER_PATH = "/usr/local/bin/chromedriver"; // На самом деле не используется с Selenium Manager
    public static final String GECKODRIVER_PATH = "/usr/local/bin/geckodriver";   // На самом деле не используется с Selenium Manager

    // Тип браузера для запуска тестов по умолчанию
    public static final String BROWSER_TYPE = "chrome"; // <-- По умолчанию Chrome (можно изменить на "firefox")

    // Таймауты ожидания (в секундах)
    public static final int DEFAULT_WAIT_TIMEOUT_SECONDS = 15;
    public static final int SHORT_WAIT_TIMEOUT_SECONDS = 5;
    public static final int TINY_PAUSE_MILLISECONDS = 500; // Для отладки наведений (0.5 секунды)

    // Сообщения об ошибках логина (Steam может выдавать их на разных языках)
    public static final String ERROR_MESSAGE_PART_RU = "неверное имя аккаунта или пароль";
    public static final String ERROR_MESSAGE_PART_EN = "Incorrect account name or password";
    public static final String INVALID_CREDENTIALS_FULL_MESSAGE_RU = "Пожалуйста, проверьте свой пароль и имя аккаунта и попробуйте снова."; // Точное сообщение для невалидных данных
}