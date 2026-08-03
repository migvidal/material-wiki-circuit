package com.migvidal.wikicircuit.core.api.common_model

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

@Serializable
@Parcelize
data class ApiImage(
    val height: Int,
    val width: Int,
    val source: String? = null,
    val url: String? = null,
) : Parcelable