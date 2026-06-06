package com.migvidal.wikicircuit.search

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.migvidal.wikicircuit.SharedElementKey
import com.migvidal.wikicircuit.core.customSharedBounds
import com.migvidal.wikicircuit.core.customSharedElement
import com.migvidal.wikicircuit.search.SearchScreen.State.Event.ArticleClicked
import com.slack.circuit.sharedelements.SharedElementTransitionScope

@Composable
fun Search(state: SearchScreen.State, modifier: Modifier = Modifier) {
    val response = state.response
    val isLoading = response != null && !response.isFailure && !response.isSuccess
    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = isLoading,
        onRefresh = {
            state.eventSink(
                SearchScreen.State.Event.Refresh
            )
        },
    ) {
        response ?: run {
            Text(text = "Nothing selected")
            return@PullToRefreshBox
        }

        response.onSuccess { searchResponse ->
            val pages = searchResponse.query?.pages ?: return@onSuccess
            Results(
                results = pages,
                onResultClicked = { state.eventSink(ArticleClicked(it.title)) }
            )
        }

        response.onFailure {
            Text(text = it.message ?: "Error")
        }
    }
}

@Composable
private fun Results(
    results: List<SearchResponse.ResultPage>,
    onResultClicked: (SearchResponse.ResultPage) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(results) { result ->
            ResultItem(result = result, onClick = onResultClicked)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ResultItem(
    result: SearchResponse.ResultPage,
    onClick: (SearchResponse.ResultPage) -> Unit,
    modifier: Modifier = Modifier,
) {
    SharedElementTransitionScope {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .customSharedElement(
                    scope = this@SharedElementTransitionScope,
                    key = SharedElementKey(type = SharedElementKey.Type.Card),
                ),
            onClick = { onClick(result) }) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    modifier = Modifier.customSharedBounds(
                        this@SharedElementTransitionScope,
                        SharedElementKey(
                            id = result.title,
                            type = SharedElementKey.Type.Title,
                        )
                    ),
                    text = result.title,
                    style = MaterialTheme.typography.titleSmall,
                )

                val description = result.terms?.description?.firstOrNull() ?: "-"
                Text(
                    modifier = Modifier.customSharedBounds(
                        this@SharedElementTransitionScope,
                        SharedElementKey(
                            id = description,
                            type = SharedElementKey.Type.Description,
                        )
                    ),
                    text = description,
                )
            }
        }
    }
}

