package com.migvidal.wikicircuit.feed

import com.migvidal.wikicircuit.core.api.common_model.ApiImage
import com.migvidal.wikicircuit.core.api.common_model.Page
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedModel(
    @SerialName("tfa") val featuredArticle: FeaturedArticle,
    val image: ImageOfTheDay,
    val mostread: MostRead,
) {
    @Serializable
    data class FeaturedArticle(
        override val title: String,
        override val pageid: Int? = null,
        override val titles: Titles,
        override val originalimage: ApiImage? = null,
        override val description: String? = null,
        override val extract: String,
        @SerialName("extract_html") override val extractHtml: String,
    ) : Page, Article

    @Serializable
    data class ImageOfTheDay(
        val title: String,
        val image: ApiImage,
        @SerialName("filepage") val filePage: String? = null,
        val artist: TextWrapper,
        val credit: TextWrapper,
        val description: TextWrapper,
    ) {
        @Serializable
        data class TextWrapper(val html: String, val text: String)
    }

    @Serializable
    data class MostRead(val date: String, val articles: List<MostReadArticle>) {
        @Serializable
        data class MostReadArticle(
            override val title: String,
            override val pageid: Int?= null,
            override val titles: Titles,
            override val originalimage: ApiImage? = null,
            override val description: String? = null,
            override val extract: String,
            @SerialName("extract_html") override val extractHtml: String,
            val views: Int,
            val rank: Int,
        ) : Page, Article
    }
}

@Serializable
sealed interface Article {
    val titles: Titles
    val originalimage: ApiImage?
    val description: String?
    val extract: String
    val extractHtml: String
}

@Serializable
data class Titles(
    val canonical: String,
    val normalized: String,
)

