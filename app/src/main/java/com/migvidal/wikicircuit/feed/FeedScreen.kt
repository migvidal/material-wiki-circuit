package com.migvidal.wikicircuit.feed

import com.migvidal.wikicircuit.core.api.common_model.ApiSimpleImage
import com.migvidal.wikicircuit.core.ui.components.MostRead
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
        sealed interface Event : CircuitUiEvent {
            data class ItemClicked(
                val titles: Titles,
                val mainImage: ApiSimpleImage?,
                val mostRead: MostRead? = null,
            ) : Event

            data class ImageClicked(val filePage: String) : Event
        }
    }
}