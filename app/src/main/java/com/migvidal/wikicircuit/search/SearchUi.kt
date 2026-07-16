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
import com.migvidal.wikicircuit.core.ui.RequestStatus
import com.migvidal.wikicircuit.core.ui.SharedElementKey
import com.migvidal.wikicircuit.core.ui.components.customSharedBounds
import com.migvidal.wikicircuit.core.ui.components.customSharedElement
import com.migvidal.wikicircuit.core.ui.components.shimmer
import com.migvidal.wikicircuit.search.SearchScreen.State.Event.ArticleClicked
import com.migvidal.wikicircuit.search.SearchScreen.State.Event.Search
import com.slack.circuit.sharedelements.SharedElementTransitionScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

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
                        delay(500.milliseconds)
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
        when (val status = response.status) {
            is RequestStatus.Failure -> Text(text = status.message)
            RequestStatus.Loading -> SkeletonResults()
            RequestStatus.Success -> {
                val data = response.data ?: return@SearchBar
                val pages = data.query?.pages ?: return@SearchBar
                Results(
                    results = pages,
                    onResultClicked = { state.eventSink(ArticleClicked(it.title)) }
                )
            }
        }
    }
}

@Composable
private fun Results(
    results: List<SearchModel.ResultPage>,
    onResultClicked: (SearchModel.ResultPage) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(results) { result ->
            ResultItem(result = result, onClick = { onResultClicked(it ?: return@ResultItem) })
        }
    }
}

@Composable
fun SkeletonResults(modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(6) {
            ResultItem(result = null, onClick = {})
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ResultItem(
    result: SearchModel.ResultPage?,
    onClick: (SearchModel.ResultPage?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val showSkeleton = result == null
    val shimmer = if (showSkeleton) Modifier.shimmer() else Modifier
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
                    modifier = Modifier
                        .customSharedBounds(
                            this@SharedElementTransitionScope,
                            SharedElementKey(
                                id = result?.title,
                                type = SharedElementKey.Type.Title,
                            )
                        )
                        .then(
                            if (showSkeleton) Modifier
                                .fillMaxWidth(1 / 3f)
                                .padding(vertical = 8.dp) else Modifier
                        )
                        .then(shimmer),
                    text = result?.title ?: "",
                    style = MaterialTheme.typography.titleSmall,
                )

                val description = result?.terms?.description?.firstOrNull() ?: ""
                if (showSkeleton) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .shimmer(),
                        text = ""
                    )
                } else {
                    Text(
                        modifier = Modifier
                            .customSharedBounds(
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
}
