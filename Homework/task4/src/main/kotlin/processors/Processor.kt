package processors

import dto.News
import kotlinx.coroutines.channels.Channel
import org.slf4j.LoggerFactory
import java.io.File

class Processor(private val newsChannel: Channel<List<News>>) {
    private val logger = LoggerFactory.getLogger(Processor::class.java)

    suspend fun process() {
        val file = File("news_${System.currentTimeMillis()}.csv").bufferedWriter()

        for (newsList in newsChannel) {
            logger.info("Процессор получил ${newsList.size} новостей для обработки.")
            newsList.forEach { news ->
                file.write("${news.id},${news.title},${news.place?.title ?: "N/A"}\n")
            }
        }

        file.close()
        logger.info("Файл новостей успешно сохранен и закрыт.")
    }
}
