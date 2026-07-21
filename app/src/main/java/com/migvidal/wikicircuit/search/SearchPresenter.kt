package com.migvidal.wikicircuit.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.migvidal.wikicircuit.article.ArticleScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.launch

class SearchPresenter @AssistedInject constructor(
    @Assisted val screen: SearchScreen,
    @Assisted val navigator: Navigator,
    val repository: SearchRepository,
) :
    Presenter<SearchScreen.State> {

    @Composable
    override fun present(): SearchScreen.State {
        val scope = rememberCoroutineScope()
        val response = repository.response.collectAsStateWithLifecycle().value

        return SearchScreen.State(response = response) {
            when (it) {
                is SearchScreen.State.Event.ResultClicked -> {
                    navigator.goTo(ArticleScreen(pageId = it.pageId, mainImage = it.mainImage))
                }

                is SearchScreen.State.Event.Search -> {
                    scope.launch { repository.fetchSearch(it.term) }
                }
            }
        }
    }

    @CircuitInject(SearchScreen::class, SingletonComponent::class)
    @AssistedFactory
    fun interface Factory {
        fun create(
            feedScreen: SearchScreen,
            navigator: Navigator,
        ): SearchPresenter
    }
}