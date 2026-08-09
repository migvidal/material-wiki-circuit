package com.migvidal.wikicircuit.page.article

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.migvidal.wikicircuit.core.network.NetworkManager
import com.migvidal.wikicircuit.core.network.api.common_model.ImageDto
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

class ArticlePresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    @Assisted val articleScreen: ArticleScreen,
    val articleProvider: ArticleProvider,
    val networkManager: NetworkManager,
) : Presenter<ArticleScreen.State> {

    @Composable
    override fun present(): ArticleScreen.State {
        val titlesFromScreen = articleScreen.titles

        LaunchedEffect(Unit) {
            titlesFromScreen?.canonical?.let {
                articleProvider.fetchArticle(it)
                return@LaunchedEffect
            }
            articleScreen.pageId?.let {
                articleProvider.fetchArticle(it)
            }
        }

        val connected = networkManager.isConnected.collectAsStateWithLifecycle().value
        val article = articleProvider.response.collectAsStateWithLifecycle().value
        val titleFromArticle = article.data?.title
        val imageDto = articleScreen.mainImage?.let {
            ImageDto(
                height = it.height,
                width = it.width,
                url = it.source ?: it.url,
            )
        }

        return ArticleScreen.State(
            connected = connected,
            title = titlesFromScreen?.normalized ?: titleFromArticle ?: "Article",
            mainImage = imageDto,
            response = article,
            isFavorite = false,
            mostReadInfo = articleScreen.mostReadInfo,
        ) {
            when (it) {
                is ArticleScreen.State.Event.BackClicked -> navigator.pop()
            }
        }
    }

    @CircuitInject(ArticleScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            navigator: Navigator,
            articleScreen: ArticleScreen
        ): ArticlePresenter
    }
}