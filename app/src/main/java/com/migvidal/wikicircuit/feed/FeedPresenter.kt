package com.migvidal.wikicircuit.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.migvidal.wikicircuit.article.ArticleScreen
import com.migvidal.wikicircuit.core.NetworkManager
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
    val repository: FeedRepository,
    val networkManager: NetworkManager,
) : Presenter<FeedScreen.State> {

    @Composable
    override fun present(): FeedScreen.State {
        val connected = networkManager.isConnected.collectAsStateWithLifecycle().value
        val response = repository.response.collectAsStateWithLifecycle().value

        LaunchedEffect(Unit) {
            if (connected) repository.fetchFeed()
        }

        return FeedScreen.State(connected = connected, response = response) {
            when (it) {
                is FeedScreen.State.Event.ItemClicked -> {
                    navigator.goTo(
                        ArticleScreen(
                            titles = it.titles,
                            mainImage = it.mainImage,
                            mostReadInfo = it.mostRead,
                        )
                    )
                }

                is FeedScreen.State.Event.ImageClicked -> {}
            }
        }
    }

    @CircuitInject(FeedScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(screen: FeedScreen, navigator: Navigator): FeedPresenter
    }
}