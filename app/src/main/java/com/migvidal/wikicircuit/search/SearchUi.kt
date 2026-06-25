package com.migvidal.wikicircuit.search

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.migvidal.wikicircuit.core.ui.SharedElementKey
import com.migvidal.wikicircuit.core.ui.customSharedBounds
import com.migvidal.wikicircuit.core.ui.customSharedElement
import com.migvidal.wikicircuit.search.SearchScreen.State.Event.ArticleClicked
import com.migvidal.wikicircuit.search.SearchScreen.State.Event.Search
import com.slack.circuit.sharedelements.SharedElementTransitionScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUi(state: SearchScreen.State, modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val queryState = rememberTextFieldState()
    var searchJob: Job? = null
    var searchBarExpanded by rememberSaveable { mutableStateOf(false) }
    val animatedPadding by animateDpAsState(targetValue = if (searchBarExpanded) 0.dp else 24.dp)

    SearchBar(
        modifier = modifier
            .consumeWindowInsets(WindowInsets.statusBars)
            .fillMaxWidth()
            .padding(animatedPadding),
        expanded = searchBarExpanded,
        onExpandedChange = { searchBarExpanded = it },
        inputField = {
            SearchBarDefaults.InputField(
                query = queryState.text.toString(),
                onQueryChange = { query ->
                    queryState.edit { replace(0, length, query) }
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        delay(500)
                        if (query.isNotBlank()) state.eventSink(Search(query))
                    }
                },
                onSearch = {
                    val query = queryState.text.toString()
                    if (query.isNotBlank()) state.eventSink(Search(query))
                },
                expanded = searchBarExpanded,
                onExpandedChange = { searchBarExpanded = it }
            )
        }
    ) {
        val response = state.response
        val isLoading = response != null && !response.isFailure && !response.isSuccess
        if (isLoading) {
            Text(text = "Loading")
            return@SearchBar
        }

        response ?: run {
            Text(text = "No results")
            return@SearchBar
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

