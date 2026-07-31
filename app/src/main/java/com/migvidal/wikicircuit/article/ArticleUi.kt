package com.migvidal.wikicircuit.article

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.migvidal.wikicircuit.R
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
            val connected = state.connected
            val response = state.response
            val status = response.status
            when {
                !connected -> Text(text = stringResource(R.string.no_internet))
                status is RequestStatus.Failure -> Text(text = status.message)
                else -> ArticleBody(state = state)
            }
        }
    }
}

@Composable
private fun SharedElementTransitionScope.ArticleBody(
    state: ArticleScreen.State,
    modifier: Modifier = Modifier
) {
    var offsetY by remember { mutableFloatStateOf(0f) }
    Column(modifier = modifier.pointerInput(Unit) {
        awaitEachGesture {
            do {
                val event = awaitPointerEvent()
                event.changes.firstOrNull { it.pressed }?.let {
                    offsetY = it.position.y
                }
            } while (event.changes.any { it.pressed })
        }
    }) {
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

        val mainImg = state.mainImage

        val gridState = rememberLazyStaggeredGridState()

        val mainImgHeight by animateFloatAsState(
            run {
                val max = if (gridState.canScrollBackward) 50f else 280f
                offsetY.coerceIn(
                    minimumValue = 0f,
                    maximumValue = max,
                )
            })


        CustomAsyncImage(
            modifier = Modifier
                .heightIn(max = mainImgHeight.dp)
                .customSharedElement(
                    scope = this@ArticleBody,
                    key = SharedElementKey(type = SharedElementKey.Type.Image, id = mainImg?.source)
                ),
            imageOrNull = mainImg,
            isDataLoading = isLoading,
        )

        val images = article?.query?.allImages
        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = StaggeredGridCells.Fixed(2),
            state = gridState,
        ) {
            val default = 5
            items(count = images?.size ?: default) { index ->
                val img = images?.get(index)
                CustomAsyncImage(imageOrNull = img, isDataLoading = isLoading)
            }
        }
    }
}