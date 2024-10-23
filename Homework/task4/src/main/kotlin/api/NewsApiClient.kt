package api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.serialization.json.Json
import dto.News
import kotlinx.serialization.Serializable
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
    private val semaphore = Semaphore(2)  // Rate-limiting для двух одновременных запросов

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    suspend fun getNews(page: Int = 1, pageSize: Int = 100): List<News> {
        semaphore.withPermit {
            return try {
                val url = "https://kudago.com/public-api/v1.4/news/?order_by=-publication_date&page=$page&page_size=$pageSize"
                val response: NewsResponse = client.get(url).body()
                logger.info("Загружены новости с ${response.results.size} элементов.")
                response.results
            } catch (e: Exception) {
                logger.error("Ошибка при запросе новостей: ${e.message}")
                emptyList()
            }
        }
    }
}
