package dto

import kotlinx.serialization.Serializable
import kotlin.math.exp

@Serializable
data class News(
    val id: Int,
    val title: String,
    val slug: String? = null,
    val publicationDate: Long? = null,
    val place: Place? = null,
    val description: String? = null,
    val bodyText: String? = null,
    val images: List<Image>? = null,
    val siteUrl: String? = null,
    val favoritesCount: Int = 0,
    val commentsCount: Int = 0
) {
    val rating: Double by lazy {
        1 / (1 + exp(-(favoritesCount / (commentsCount + 1.0))))
    }
}

@Serializable
data class Place(
    val id: Int,
    val title: String? = null,
    val address: String? = null,
    val siteUrl: String? = null,
    val location: String? = null
)

@Serializable
data class Image(
    val image: String,
    val thumbnails: Thumbnails? = null
)

@Serializable
data class Thumbnails(
    val small: String? = null,
    val large: String? = null
)
