package com.migvidal.wikicircuit.search

import com.migvidal.wikicircuit.core.api.common_model.ApiImage
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object SearchScreen : Screen {
    data class State(
        val response: CachedSearchResponse,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {
        sealed interface Event {
            data class ResultClicked(val pageId: Int, val mainImage: ApiImage?) : Event
            data class Search(val term: String) : Event
        }
    }
}