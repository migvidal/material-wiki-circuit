package com.migvidal.wikicircuit.feed

import com.migvidal.wikicircuit.core.api.common_model.ApiImage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
object FeedScreen : Screen {
    data class State(
        val response: CachedFeedResponse,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {
        sealed interface Event: CircuitUiEvent {
            data class ItemClicked(val titles: Titles, val mainImage: ApiImage?) : Event
            data class ImageClicked(val filePage: String) : Event
        }
    }
}