package utils

import dto.News
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CsvUtils {

    fun saveNews(news: Collection<News>) {
        val directory = File("news")
        if (!directory.exists()) {
            directory.mkdir()
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")
        val timestamp = LocalDateTime.now().format(formatter)
        val fileName = "news_$timestamp.csv"
        val file = File(directory, fileName)

        file.bufferedWriter().use { writer ->
            writer.write("id,title,place,description,siteUrl,favoritesCount,commentsCount,rating\n")
            news.forEach {
                writer.write("${it.id},${it.title},${it.place?.title ?: "N/A"}," +
                        "${it.description ?: "N/A"},${it.siteUrl ?: "N/A"}," +
                        "${it.favoritesCount},${it.commentsCount},${it.rating}\n")
            }
        }
    }
}
