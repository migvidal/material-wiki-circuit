package com.migvidal.wikicircuit.page.common

import com.migvidal.wikicircuit.core.api.common_model.ApiImage
import com.migvidal.wikicircuit.core.api.common_model.Page
import com.migvidal.wikicircuit.core.api.common_model.Query
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PageModel(
    val `continue`: Continue? = null,
    val query: Query<PageWithPropsAndImages>,
) {
    @Serializable
    data class Continue(
        val `continue`: String,
    )

    @Serializable
    data class PageWithPropsAndImages(
        override val title: String,
        override val pageid: Int? = null,
        val ns: Int,
        val pageprops: PageProps? = null,
        @SerialName("canonicalurl") val canonicalUrl: String? = null,
        val images: List<ImageReference>? = emptyList(),
        @SerialName("imageinfo") val imageInfo: List<ApiImage> = emptyList(),
    ) : Page {

        @Serializable
        data class PageProps(
            @SerialName("page_image_free") val pageImageFree: String? = null,
            @SerialName("wikibase-shortdesc") val wikibaseShortDesc: String,
            @SerialName("wikibase_item") val wikibaseItem: String,
        )

        @Serializable
        data class ImageReference(val ns: Int, val title: String)
    }
}