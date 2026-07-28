package com.migvidal.wikicircuit.article

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.migvidal.wikicircuit.core.ui.RequestStatus
import com.migvidal.wikicircuit.core.ui.SharedElementKey
import com.migvidal.wikicircuit.core.ui.components.CustomAsyncImage
import com.migvidal.wikicircuit.core.ui.components.CustomText
import com.migvidal.wikicircuit.core.ui.components.MostReadInfo
import com.migvidal.wikicircuit.core.ui.components.customSharedBounds
import com.migvidal.wikicircuit.core.ui.components.customSharedElement
import com.slack.circuit.sharedelements.SharedElementTransitionScope

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ArticleUi(state: ArticleScreen.State, modifier: Modifier = Modifier) {
    SharedElementTransitionScope {
        Surface(
            modifier = modifier
                .customSharedBounds(
                    scope = this@SharedElementTransitionScope,
                    key = SharedElementKey(type = SharedElementKey.Type.Card, id = state.title),
                ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                val response = state.response
                when (val status = response.status) {
                    is RequestStatus.Failure -> Text(text = status.message)
                    else -> ArticleBody(state = state)
                }
            }
        }
    }
}

@Composable
private fun SharedElementTransitionScope.ArticleBody(
    state: ArticleScreen.State,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val response = state.response
        val status = response.status

        val isLoading = status is RequestStatus.Loading
        state.mostReadInfo?.let {
            MostReadInfo(mostRead = it, isLoading = false)
        }

        val article = response.data
        val page = article?.query?.pages?.firstOrNull()
        val title = state.title
        CustomText(
            modifier = Modifier
                .customSharedBounds(
                    scope = this@ArticleBody,
                    key = SharedElementKey(
                        id = title,
                        type = SharedElementKey.Type.Title,
                    )
                ),
            textOrNull = title,
            isLoading = isLoading,
            style = MaterialTheme.typography.displaySmall,
        )

        val summary = page?.pageprops?.wikibaseShortDesc

        CustomText(
            modifier = Modifier.customSharedBounds(
                scope = this@ArticleBody,
                key = SharedElementKey(
                    id = summary,
                    type = SharedElementKey.Type.Description,
                )
            ),
            textOrNull = summary,
            isLoading = isLoading,
            fontWeight = FontWeight.Bold,
        )

        val img = state.mainImage
        CustomAsyncImage(
            modifier = Modifier.customSharedElement(
                scope = this@ArticleBody,
                key = SharedElementKey(type = SharedElementKey.Type.Image, id = img?.source)
            ),
            imageOrNull = img,
            isDataLoading = isLoading,
        )
    }
}