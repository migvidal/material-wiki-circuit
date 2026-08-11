package com.migvidal.wikicircuit.feature.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.migvidal.wikicircuit.feature.page.article.ArticleScreen
import com.migvidal.wikicircuit.core.network.NetworkManager
import com.migvidal.wikicircuit.core.network.api.common_model.ImageDto
import com.migvidal.wikicircuit.feature.page.image.ImageScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

class FeedPresenter @AssistedInject constructor(
    @Assisted val screen: FeedScreen,
    @Assisted val navigator: Navigator,
    val repository: FeedProvider,
    val networkManager: NetworkManager,
) : Presenter<FeedScreen.State> {

    @Composable
    override fun present(): FeedScreen.State {
        val connected = networkManager.isConnected.collectAsStateWithLifecycle().value
        val response = repository.response.collectAsStateWithLifecycle().value

        LaunchedEffect(Unit) {
            if (connected) repository.fetchFeed()
        }

        return FeedScreen.State(connected = connected, response = response) { event ->
            when (event) {
                is FeedScreen.State.Event.ItemClicked -> {
                    val mainImageDto = event.mainImage?.run {
                        ImageDto(
                            height = height,
                            width = width,
                            url = url ?: source,
                        )
                    }
                    navigator.goTo(
                        ArticleScreen(
                            titles = event.titles,
                            mainImage = mainImageDto,
                            mostReadInfo = event.mostRead,
                        )
                    )
                }

                is FeedScreen.State.Event.ImageClicked -> {
                    val screen = ImageScreen(event.image ?: return@State)
                    navigator.goTo(screen)
                }
            }
        }
    }

    @CircuitInject(FeedScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(screen: FeedScreen, navigator: Navigator): FeedPresenter
    }
}