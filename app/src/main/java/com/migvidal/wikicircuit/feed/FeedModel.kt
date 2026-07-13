package com.migvidal.wikicircuit.feed

import com.migvidal.wikicircuit.core.api.common_model.ApiImage
import com.migvidal.wikicircuit.core.api.common_model.Page
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeedModel(
    @SerialName("tfa") val featuredArticle: FeaturedArticle,
) {
    @Serializable
    data class FeaturedArticle(
        override val title: String,
        val pageid: Int? = null,
        val titles: Titles,
        val thumbnail: ApiImage? = null,
        @SerialName("originalimage") val originalImage: ApiImage? = null,
        val lang: String,
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
}
