package com.migvidal.wikicircuit.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

class DetailPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    @Assisted val detailScreen: DetailScreen,
    val articleRepository: ArticleRepository,
) : Presenter<DetailScreen.State> {

    @Composable
    override fun present(): DetailScreen.State {
        val article = articleRepository.response.collectAsStateWithLifecycle().value
        val title = detailScreen.title

        LaunchedEffect(title) {
            articleRepository.fetchArticle(title)
        }

        return DetailScreen.State(response = article, isFavorite = false) {
            when (it) {
                is DetailScreen.State.Event.BackClicked -> navigator.pop()
            }
        }
    }

    @CircuitInject(DetailScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            navigator: Navigator,
            detailScreen: DetailScreen
        ): DetailPresenter
    }
}