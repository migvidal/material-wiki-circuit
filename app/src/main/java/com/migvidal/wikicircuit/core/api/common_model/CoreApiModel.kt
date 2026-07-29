package com.migvidal.wikicircuit.core.api.common_model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface Page {
    val title: String
    val pageid: Int?
}

@Serializable
data class Query<out T : Page>(
    val normalized: List<Normalized> = emptyList(),
    val pages: List<T> = emptyList(),
    @SerialName("allimages") val allImages: List<ApiExtendedImage> = emptyList(),
) {
    @Serializable
    data class Normalized(
        val fromencoded: Boolean? = null,
        val from: String? = null,
        val to: String? = null,
    )

    @Serializable
    data class ApiExtendedImage(
        val title: String,
        val name: String,
        @SerialName("mediatype") val mediaType: String,
        override val height: Int,
        override val width: Int,
        override val source: String? = null,
        override val url: String? = null,
    ) : ApiImage
}

sealed interface ApiImage {
    val source: String?
    val url: String?
    val height: Int
    val width: Int
}

@Serializable
@Parcelize
data class ApiSimpleImage(
    override val height: Int,
    override val width: Int,
    override val source: String? = null,
    override val url: String? = null,
) : Parcelable, ApiImage