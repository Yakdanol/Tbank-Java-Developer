import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import api.NewsApiClient
import dto.News
import org.slf4j.LoggerFactory
import java.util.concurrent.Executors
import processors.Processor

fun main() = runBlocking {
    val logger = LoggerFactory.getLogger("Main")
    logger.info("Запуск приложения")

    val newsChannel = Channel<List<News>>(Channel.UNLIMITED)
    val apiClient = NewsApiClient()
    val processor = Processor(newsChannel)

    val workerCount = 15  // Количество воркеров
    val pool = Executors.newFixedThreadPool(workerCount).asCoroutineDispatcher()

    // Запускаем воркеров
    repeat(workerCount) { workerId ->
        launch(pool) {
            try {
                val newsPage = workerId + 1
                logger.debug("Worker $workerId получает новости для страницы $newsPage")
                val news = apiClient.getNews(page = newsPage)
                newsChannel.send(news)
            } catch (e: Exception) {
                logger.error("Ошибка в worker $workerId: ${e.message}")
            }
        }
    }

    // Запускаем Processor для обработки данных
    launch {
        processor.process()
    }

    // Ожидаем завершения работы всех worker'ов
    pool.close()
}
