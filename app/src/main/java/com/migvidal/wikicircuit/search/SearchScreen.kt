package com.migvidal.wikicircuit.search

import com.migvidal.wikicircuit.core.api.common_model.ApiSimpleImage
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data object SearchScreen : Screen {
    data class State(
        val connected: Boolean,
        val response: CachedSearchResponse,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {
        sealed interface Event: CircuitUiEvent {
            data class ResultClicked(val pageId: Int, val mainImage: ApiSimpleImage?) : Event
            data class Search(val term: String) : Event
        }
    }
}