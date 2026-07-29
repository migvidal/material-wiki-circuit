package com.migvidal.wikicircuit.feed

import android.os.Parcelable
import com.migvidal.wikicircuit.core.api.common_model.ApiSimpleImage
import com.migvidal.wikicircuit.core.api.common_model.Page
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

private const val EXTRACT_HTML_SERIAL_NAME = "extract_html"

@Serializable
data class FeedModel(
    @SerialName("tfa") val featuredArticle: FeaturedArticle,
    val image: ImageOfTheDay,
    val mostread: MostRead? = null,
    @SerialName("onthisday") val onThisDay: List<OnThisDay>,
) {
    @Serializable
    data class FeaturedArticle(
        override val title: String,
        override val pageid: Int? = null,
        override val titles: Titles,
        override val originalimage: ApiSimpleImage? = null,
        override val description: String? = null,
        override val extract: String,
        @SerialName(EXTRACT_HTML_SERIAL_NAME) override val extractHtml: String? = null,
    ) : Page, Article

    @Serializable
    data class ImageOfTheDay(
        val title: String,
        val image: ApiSimpleImage,
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
            override val pageid: Int? = null,
            override val titles: Titles,
            override val originalimage: ApiSimpleImage? = null,
            override val description: String? = null,
            override val extract: String,
            @SerialName(EXTRACT_HTML_SERIAL_NAME) override val extractHtml: String? = null,
            val views: Int,
            val rank: Int,
        ) : Page, Article
    }

    @Serializable
    data class OnThisDay(
        val text: String,
        val pages: List<OnThisDayPage>,
        val year: Int,
    ) {
        @Serializable
        data class OnThisDayPage(
            override val title: String,
            override val pageid: Int? = null,
            override val titles: Titles,
            override val originalimage: ApiSimpleImage? = null,
            override val description: String? = null,
            override val extract: String,
            @SerialName(EXTRACT_HTML_SERIAL_NAME) override val extractHtml: String? = null,
            val coordinates: Coordinates? = null,
        ) : Page, Article {
            @Serializable
            data class Coordinates(val lat: Double, val lon: Double)
        }
    }
}


@Serializable
sealed interface Article {
    val titles: Titles
    val originalimage: ApiSimpleImage?
    val description: String?
    val extract: String
    val extractHtml: String?
}

@Serializable
@Parcelize
data class Titles(
    val canonical: String,
    val normalized: String,
): Parcelable

