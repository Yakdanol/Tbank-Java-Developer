package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import dto.News
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

@Serializable
data class NewsResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<News>
)

class NewsApiClient {
    private val logger = LoggerFactory.getLogger(NewsApiClient::class.java)

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }) // Игнорированием неизвестных ключей
        }
        install(Logging) {
            level = LogLevel.INFO
        }
        defaultRequest {
            accept(ContentType.Application.Json)
        }
    }

    fun getNews(count: Int = 100, location: String = "spb"): List<News> = runBlocking {
        val newsList = mutableListOf<News>()
        var nextUrl: String? = "https://kudago.com/public-api/v1.4/news/?order_by=-publication_date&page_size=$count&location=$location&actual_only=true"

        try {
            while (nextUrl != null) {
                val response: NewsResponse = client.get(nextUrl).body()
                newsList.addAll(response.results)
                nextUrl = response.next
                logger.info("Загружено ${response.results.size} новостей, следующая страница: $nextUrl")
            }
        } catch (e: ClientRequestException) {
            logger.error("Ошибка при запросе: ${e.response.status}", e)
        } catch (e: Exception) {
            logger.error("Произошла ошибка: ${e.localizedMessage}", e)
        }

        return@runBlocking newsList
    }

    fun getNewsDetails(newsId: Int, fields: String = ""): News? = runBlocking {
        try {
            logger.info("Запрос деталей новостей с id $newsId с полями $fields")
            return@runBlocking client.get {
                url("https://kudago.com/public-api/v1.4/news/$newsId/")
                parameter("fields", fields)
            }.body()
        } catch (e: ClientRequestException) {
            if (e.response.status == HttpStatusCode.NotFound) {
                logger.warn("Новость с id $newsId не найдена (404)")
            }
            null
        } catch (e: Exception) {
            logger.error("Произошла ошибка: ${e.localizedMessage}", e)
            null
        }
    }
}
