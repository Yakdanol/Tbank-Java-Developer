package org.yakdanol

import api.NewsApiClient
import org.slf4j.LoggerFactory
import pretty_print.prettyPrint
import service.NewsService
import utils.CsvUtils
import java.time.LocalDate

fun main() {
    val logger = LoggerFactory.getLogger("Main")
    logger.info("Запуск приложения")

    val apiClient = NewsApiClient()
    val newsService = NewsService()

    // Получаем список новостей через API
    logger.debug("Получение списка новостей через API")
    val news = apiClient.getNews(100)

    // Получаем топ новости за определенный период
    logger.debug("Фильтрация топ новостей за период")
    val topRatedNews = newsService.run {
        news.getMostRatedNews(10, LocalDate.now()..LocalDate.now().plusDays(30))
    }

    // Сохраняем новости в .csv
    logger.debug("Сохранение топ новостей в CSV файл")
    CsvUtils.saveNews(topRatedNews)

    // Получаем детали новостей с фильтрацией полей
    logger.debug("Получение деталей новостей")
    val newsDetails = apiClient.getNewsDetails(1, "favorites_count")
    println(newsDetails)

    // Используем DSL для Pretty Print
    logger.debug("Использование DSL для Pretty Print")
    val printer = prettyPrint {
        header(level = 1, content = "Топ Новостей")
        header(level = 2, content = "Период: ${LocalDate.now()} до ${LocalDate.now().plusDays(30)}")

        text {
            +"Ниже представлены топовые новости, отфильтрованные по рейтингу."
        }

        topRatedNews.forEach { newsItem ->
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

                link("https://kudago.com/news/${newsItem.slug}", "Читать далее")
                +"\n"
            }
        }

        text {
            +"Для получения более подробной информации посетите официальный сайт."
        }
    }

    printer.printToConsole()
    printer.saveToFile("pretty_printed_news_${LocalDate.now()}.txt")
    logger.info("Приложение завершило работу")
}
