
import org.example.SteamGamePage;
import org.example.SteamHomePage;
import org.example.SteamSearchResultsPage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Map;

public class SteamChartsScenarioTest extends BaseTest { // Наследуем от BaseTest

    @Test
    @DisplayName("Сценарий: Проверка топ-10 игр с страницы результатов поиска и их деталей")
    void testTop10GamesFromSearchResultsScenario() { // ИЗМЕНЕНИЕ: Название теста
        SteamHomePage homePage = new SteamHomePage(driver, wait);

        // Шаг 1: Открываем домашнюю страницу (уже сделано в BaseTest.setUp())
        // Навигация в "Новое и интересное" -> "Лидеры продаж"
        // Теперь этот метод возвращает страницу результатов поиска из-за редиректа Steam
        SteamSearchResultsPage searchResultsPage = homePage.navigateToBestsellers(); // ИЗМЕНЕНИЕ: Тип возвращаемого PO

        // Ассерты для Шага 1: Проверяем, что попали на страницу результатов поиска
        wait.until(ExpectedConditions.urlContains("search/?filter=topsellers")); // ИЗМЕНЕНИЕ: Ожидаемый URL
        Assertions.assertTrue(driver.getCurrentUrl().contains("search/?filter=topsellers"), "Ошибка: Не удалось перейти на страницу результатов поиска по лидерам продаж.");
        System.out.println("✓ Успешно перешли на страницу результатов поиска по лидерам продаж: " + driver.getCurrentUrl());

        // Шаг 2 (УДАЛЕНО ИЗ ЭТОГО СЦЕНАРИЯ): Изменение страны на "По всему миру"
        // На странице результатов поиска (search/?filter=topsellers) нет такого же выпадающего списка стран.
        // Если нужно тестировать изменение страны именно на этой странице, потребуются новые локаторы и метод в SteamSearchResultsPage.

        // Шаг 3 (по вашему запросу): Получение названий и цен первых 10 игр
        int numberOfGamesToGet = 10;
        // ИЗМЕНЕНИЕ: Используем searchResultsPage для получения данных
        List<Map<String, String>> top10Games = searchResultsPage.getTopNGamesData(numberOfGamesToGet);

        // Ассерты для Шага 3
        Assertions.assertFalse(top10Games.isEmpty(), "Ошибка: Список игр пуст.");
        Assertions.assertTrue(top10Games.size() >= numberOfGamesToGet, "Ошибка: Получено менее " + numberOfGamesToGet + " игр из списка (получено: " + top10Games.size() + ")."); // ИЗМЕНЕНИЕ: >= на случай, если меньше доступно
        System.out.println("✓ Получены данные для первых " + top10Games.size() + " игр:");
        top10Games.forEach(game -> System.out.println("  Название: " + game.get("title") + ", Цена: " + game.get("price")));

        System.out.println("Сценарий 'Проверка топ-10 игр с страницы результатов поиска и их деталей' успешно завершен.");
    }
}