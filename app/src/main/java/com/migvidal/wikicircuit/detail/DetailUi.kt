package com.migvidal.wikicircuit.detail

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.migvidal.wikicircuit.core.ui.RequestStatus
import com.migvidal.wikicircuit.core.ui.SharedElementKey
import com.migvidal.wikicircuit.core.ui.components.customSharedBounds
import com.migvidal.wikicircuit.core.ui.components.customSharedElement
import com.migvidal.wikicircuit.core.ui.components.shimmer
import com.slack.circuit.sharedelements.SharedElementTransitionScope

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun DetailUi(state: DetailScreen.State, modifier: Modifier = Modifier) {
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
                when (val status = response.status) {
                    is RequestStatus.Failure -> Text(text = status.message)
                    RequestStatus.Loading -> DetailContent(articleResponse = null)
                    RequestStatus.Success -> DetailContent(articleResponse = response.data)
                }
            }
        }
    }
}

@Composable
private fun SharedElementTransitionScope.DetailContent(
    articleResponse: ArticleModel?,
    modifier: Modifier = Modifier
) {
    val showSkeleton = articleResponse == null

    Column(modifier = modifier) {
        val page = articleResponse?.query?.pages?.firstOrNull()
        val title = page?.title ?: ""
        Text(
            modifier = Modifier
                .customSharedBounds(
                    this@DetailContent,
                    SharedElementKey(
                        id = title,
                        type = SharedElementKey.Type.Title,
                    )
                )
                .then(
                    if (showSkeleton) {
                        Modifier
                            .fillMaxWidth(1 / 3f)
                            .padding(vertical = 8.dp)
                            .shimmer()
                    } else {
                        Modifier
                    }
                ), text = title, style = MaterialTheme.typography.displayMedium
        )
        val summary = page?.pageprops?.wikibaseShortDesc ?: ""
        if (showSkeleton) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shimmer(), text = "", minLines = 5
            )
        } else {
            Text(
                modifier = Modifier.customSharedBounds(
                    this@DetailContent,
                    SharedElementKey(
                        id = summary,
                        type = SharedElementKey.Type.Description,
                    )
                ), text = summary, fontWeight = FontWeight.Bold
            )
        }
    }
}