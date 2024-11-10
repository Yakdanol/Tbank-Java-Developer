package test

import pretty_print.prettyPrint
import dto.News
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertTrue
import java.io.File

class PrettyPrintTest {

    @Test
    fun `test pretty print output to file`() {
        // Создаем тестовые данные
        val newsList = listOf(
            News(
                id = 1,
                title = "Новость 1",
                description = "Описание первой новости",
                siteUrl = "https://example.com/1",
                favoritesCount = 10,
                commentsCount = 5
            ),
            News(
                id = 2,
                title = "Новость 2",
                description = "Описание второй новости",
                siteUrl = "https://example.com/2",
                favoritesCount = 15,
                commentsCount = 8
            )
        )

        // Используем prettyPrint для создания форматированного текста
        val printer = prettyPrint {
            header(level = 1, content = "Топ Новостей")
            header(level = 2, content = "Период: ${LocalDate.now()} до ${LocalDate.now().plusDays(7)}")

            text {
                +"Ниже представлены тестовые новости:"
            }

            newsList.forEach { newsItem ->
                text {
                    bold("Заголовок: ")
                    +newsItem.title
                    +"\n"

                    +"Описание: "
                    +(newsItem.description ?: "Нет описания")
                    +"\n"

                    +"Ссылка: "
                    +(newsItem.siteUrl ?: "Нет URL")
                    +"\n"

                    +"Рейтинг: ${"%.2f".format(newsItem.rating)}"
                    +"\n"
                }
            }
        }

        // Сохраняем результат в файл
        val fileName = "test_pretty_print_${LocalDate.now()}.txt"
        printer.saveToFile(fileName)

        // Проверяем, что файл был создан
        val file = File(fileName)
        assertTrue(file.exists(), "Файл должен быть создан")

        // Читаем содержимое файла и проверяем наличие ключевых слов
        val fileContent = file.readText()
        assertTrue(fileContent.contains("Топ Новостей"), "Содержимое должно содержать заголовок")
        assertTrue(fileContent.contains("Новость 1"), "Содержимое должно содержать заголовок первой новости")
        assertTrue(fileContent.contains("Новость 2"), "Содержимое должно содержать заголовок второй новости")

        // Удаляем файл после теста
        file.delete()
    }
}
