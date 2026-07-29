package com.migvidal.wikicircuit.article

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.coerceIn
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
            val response = state.response
            when (val status = response.status) {
                is RequestStatus.Failure -> Text(text = status.message)
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

        val mainImg = state.mainImage

        val gridState = rememberLazyStaggeredGridState()
        var offsetX by remember { mutableFloatStateOf(0f) }

        CustomAsyncImage(
            modifier = Modifier.customSharedElement(
                scope = this@ArticleBody,
                key = SharedElementKey(type = SharedElementKey.Type.Image, id = mainImg?.source)
            ).pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    offsetX=dragAmount
                    change.consume()
                }
            },
            imageOrNull = mainImg,
            isDataLoading = isLoading,
        )

        Text(offsetX.toString())

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