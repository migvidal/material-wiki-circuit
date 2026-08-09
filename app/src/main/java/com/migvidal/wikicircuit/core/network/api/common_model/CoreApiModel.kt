package com.migvidal.wikicircuit.core.network.api.common_model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

interface Page {
    val title: String
    val pageid: Int?
}

@Serializable
data class Query<out T : Page>(
    val normalized: List<Normalized> = emptyList(),
    val pages: List<T> = emptyList(),
) {
    @Serializable
    data class Normalized(
        val fromencoded: Boolean? = null,
        val from: String? = null,
        val to: String? = null,
    )

}

sealed interface Sizeable{
    val height: Int
    val width: Int
}

@Serializable
@Parcelize
data class ApiImage(
    override val height: Int,
    override val width: Int,
    val source: String? = null,
    val url: String? = null,
) : Parcelable, Sizeable

@Parcelize
data class ImageDto(
    override val height: Int,
    override val width: Int,
    val url: String?,
): Sizeable, Parcelable
