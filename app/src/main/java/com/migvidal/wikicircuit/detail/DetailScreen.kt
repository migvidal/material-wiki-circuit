package com.migvidal.wikicircuit.detail

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.migvidal.wikicircuit.SharedElementKey
import com.migvidal.wikicircuit.core.customSharedBounds
import com.migvidal.wikicircuit.core.customSharedElement
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.sharedelements.SharedElementTransitionScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

data class DetailScreen(val title: String) : Screen {
    override fun describeContents() = 0

    override fun writeToParcel(p0: Parcel, p1: Int) {
        p0.writeString(title)
    }

    companion object CREATOR : Parcelable.Creator<DetailScreen> {
        override fun createFromParcel(p0: Parcel?) = DetailScreen(p0?.readString() ?: "")
        override fun newArray(p0: Int): Array<out DetailScreen?> = arrayOfNulls(p0)
    }

    data class State(
        val response: Result<FullArticle?>,
        val isFavorite: Boolean,
        val eventSink: (Event) -> Unit,
    ) : CircuitUiState {
        sealed interface Event {
            data object BackClicked : Event
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Detail(state: DetailScreen.State, modifier: Modifier = Modifier) {
    SharedElementTransitionScope {
        Card(
            modifier = modifier
                .padding(16.dp)
                .customSharedElement(
                    scope = this@SharedElementTransitionScope,
                    key = SharedElementKey(type = SharedElementKey.Type.Card),
                ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val response = state.response
                response.onSuccess { articleResponse ->
                    if (articleResponse == null) {
                        Text(text = "Nothing selected")
                    } else {
                        DetailContent(article = articleResponse)
                    }

                }

            }
        }
    }
}

@Composable
private fun SharedElementTransitionScope.DetailContent(
    article: FullArticle,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.customSharedBounds(
                this@DetailContent,
                SharedElementKey(
                    id = article.title,
                    type = SharedElementKey.Type.Title,
                )
            ), text = article.title, style = MaterialTheme.typography.displayMedium
        )
        Text(
            modifier = Modifier.customSharedBounds(
                this@DetailContent,
                SharedElementKey(
                    id = article.summary,
                    type = SharedElementKey.Type.Description,
                )
            ), text = article.summary, fontWeight = FontWeight.Bold
        )
        Text(text = article.body)
    }
}

class DetailPresenter @AssistedInject constructor(
    @Assisted val navigator: Navigator,
    @Assisted val detailScreen: DetailScreen,
    val articleRepository: ArticleRepository,
) : Presenter<DetailScreen.State> {

    @Composable
    override fun present(): DetailScreen.State {
        val article = articleRepository.article.collectAsStateWithLifecycle().value
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