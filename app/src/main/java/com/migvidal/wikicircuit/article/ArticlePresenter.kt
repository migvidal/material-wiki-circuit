package com.migvidal.wikicircuit.article

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

class ArticlePresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    @Assisted val articleScreen: ArticleScreen,
    val articleRepository: ArticleRepository,
) : Presenter<ArticleScreen.State> {

    @Composable
    override fun present(): ArticleScreen.State {
        val article = articleRepository.response.collectAsStateWithLifecycle().value
        val titleFromArticle = article.data?.query?.pages?.firstOrNull()?.title
        val titlesFromScreen = articleScreen.titles

        LaunchedEffect(Unit) {
            titlesFromScreen?.canonical?.let {
                articleRepository.fetchArticle(it)
                return@LaunchedEffect
            }
            articleScreen.pageId?.let {
                articleRepository.fetchArticle(it)
            }
        }

        return ArticleScreen.State(
            title = titlesFromScreen?.normalized ?: titleFromArticle ?: "Article",
            mainImage = articleScreen.mainImage,
            response = article,
            isFavorite = false,
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