package com.migvidal.wikicircuit.article

import com.migvidal.wikicircuit.core.api.common_model.Page
import com.migvidal.wikicircuit.core.api.common_model.Query
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleModel(
    val `continue`: Continue? = null,
    val query: Query<PageWithProps>,
) {
    @Serializable
    data class Continue(
        val `continue`: String,
    )

    @Serializable
    data class PageWithProps(
        override val title: String,
        override val pageid: Int? = null,
        val ns: Int,
        val pageprops: PageProps,
        @SerialName("canonicalurl") val canonicalUrl: String,
    ) : Page {

        @Serializable
        data class PageProps(
            @SerialName("page_image_free") val pageImageFree: String? = null,
            @SerialName("wikibase-shortdesc") val wikibaseShortDesc: String,
            @SerialName("wikibase_item") val wikibaseItem: String,
        )
    }
}