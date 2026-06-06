package com.migvidal.wikicircuit.search

import com.migvidal.wikicircuit.core.api.common_model.Page
import com.migvidal.wikicircuit.core.api.common_model.Query
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

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
        val ns: Int,
        val pageid: Int? = null,
        val index: Int,
        val terms: Terms? = null,
        @Transient val allImages: List<String> = emptyList()
    ) : Page {

        @Serializable
        data class Terms(
            val description: List<String>
        )
    }
}