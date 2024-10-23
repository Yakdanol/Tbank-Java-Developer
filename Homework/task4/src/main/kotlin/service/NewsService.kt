package service

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import dto.News

class NewsService {

    fun List<News>.getMostRatedNews(count: Int, period: ClosedRange<LocalDate>): List<News> {
        return this.filter { news ->
            news.publicationDate?.let {
                val publicationDate = Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalDate()
                publicationDate in period
            } == true
        }.sortedByDescending { it.rating }
            .take(count)
    }
}
