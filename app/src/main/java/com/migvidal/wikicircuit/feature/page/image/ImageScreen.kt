package com.migvidal.wikicircuit.feature.page.image

import com.migvidal.wikicircuit.core.network.api.common_model.ImageDto
import com.migvidal.wikicircuit.core.ui.RequestStatus
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageScreen(
    val image: ImageDto,
) : Screen {
    data class State(
        val image: ImageDto?,
    ) : CircuitUiState
}