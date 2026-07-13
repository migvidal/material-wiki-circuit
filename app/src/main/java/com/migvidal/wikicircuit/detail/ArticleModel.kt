package com.migvidal.wikicircuit.detail

import com.migvidal.wikicircuit.core.api.common_model.Page
import com.migvidal.wikicircuit.core.api.common_model.Query
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ArticleModel(
    val `continue`: Continue? = null,
    val query: Query<PageWithImages>,
) {
    @Serializable
    data class Continue(
        val imcontinue: String,
        val `continue`: String,
    )

    @Serializable
    data class PageWithImages(
        override val title: String,
        val ns: Int,
        val pageid: Int? = null,
        @SerialName("images") val images: List<ArticleImage>? = null,
        val pageprops: PageProps,
    ) : Page {

        @Serializable
        data class ArticleImage(val title: String)

        @Serializable
        data class PageProps(
            @SerialName("page_image_free") val pageImageFree: String? = null,
            @SerialName("wikibase-shortdesc") val wikibaseShortDesc: String,
            @SerialName("wikibase_item") val wikibaseItem: String,
        )
    }
}