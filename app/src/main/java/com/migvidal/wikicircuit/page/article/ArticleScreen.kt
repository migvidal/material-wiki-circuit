package com.migvidal.wikicircuit.page.article

import com.migvidal.wikicircuit.core.network.api.common_model.ApiImage
import com.migvidal.wikicircuit.core.network.api.common_model.ImageDto
import com.migvidal.wikicircuit.core.ui.components.MostRead
import com.migvidal.wikicircuit.feed.Titles
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleScreen(
    val pageId: Int? = null,
    val titles: Titles? = null,
    val mainImage: ApiImage? = null,
    val mostReadInfo: MostRead? = null,
) : Screen {

    data class State(
        val connected: Boolean,
        val title: String,
        val mainImage: ImageDto?,
        val response: CachedArticleResponse,
        val isFavorite: Boolean,
        val mostReadInfo: MostRead?,
        val allImages: List<ApiImage> = emptyList(),
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {
        sealed interface Event : CircuitUiEvent {
            data object BackClicked : Event
        }
    }
}

