package com.migvidal.wikicircuit.article

import android.os.Parcelable
import com.migvidal.wikicircuit.core.api.common_model.ApiImage
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
        val title: String,
        val mainImage: ApiImage?,
        val response: CachedArticleResponse,
        val isFavorite: Boolean,
        val mostReadInfo: MostRead?,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {
        sealed interface Event: CircuitUiEvent {
            data object BackClicked : Event
        }
    }
}

