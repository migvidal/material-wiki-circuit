package com.migvidal.wikicircuit.search

import com.migvidal.wikicircuit.core.api.common_model.Page
import com.migvidal.wikicircuit.core.api.common_model.Query
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val `continue`: Continue? = null,
    val query: Query<ResultPage>? = null,
) {
    @Serializable
    data class Continue(val picontinue: Int? = null, val `continue`: String? = null)

    @Serializable
    data class ResultPage(
        override val title: String,
        val pageid: Int? = null,
        val ns: Int,
        val index: Int,
        val thumbnail: Thumbnail? = null,
        val terms: Terms? = null,
    ) : Page {
        @Serializable
        data class Thumbnail(
            val source: String,
            val width: Int,
            val height: Int,
        )

        @Serializable
        data class Terms(
            val description: List<String>
        )
    }
}