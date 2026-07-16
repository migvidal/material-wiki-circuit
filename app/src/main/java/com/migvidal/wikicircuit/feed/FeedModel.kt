package com.migvidal.wikicircuit.feed

import com.migvidal.wikicircuit.core.api.common_model.ApiImage
import com.migvidal.wikicircuit.core.api.common_model.Page
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedModel(
    @SerialName("tfa") val featuredArticle: FeaturedArticle,
    val image: ImageOfTheDay,
) {
    @Serializable
    data class FeaturedArticle(
        override val title: String,
        val pageid: Int? = null,
        val titles: Titles,
        @SerialName("originalimage") val originalImage: ApiImage? = null,
        val description: String? = null,
        val extract: String,
        @SerialName("extract_html") val extractHtml: String,
    ): Page {
        @Serializable
        data class Titles(
            val canonical: String,
            val normalized: String,
        )
    }

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
}
