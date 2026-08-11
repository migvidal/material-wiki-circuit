package com.migvidal.wikicircuit.feature.page.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.migvidal.wikicircuit.core.network.api.common_model.ImageDto
import com.migvidal.wikicircuit.core.ui.RequestStatus
import com.migvidal.wikicircuit.feature.page.common.PageRepository
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

class ImagePresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    @Assisted val imageScreen: ImageScreen,
) : Presenter<ImageScreen.State> {
    @Composable
    override fun present(): ImageScreen.State {
        return ImageScreen.State(image = imageScreen.image)
    }

    @CircuitInject(screen = ImageScreen::class, scope = SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(navigator: Navigator, imageScreen: ImageScreen): ImagePresenter
    }
}