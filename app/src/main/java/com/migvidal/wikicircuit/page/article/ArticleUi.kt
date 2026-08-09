package com.migvidal.wikicircuit.page.article

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    Column(modifier = modifier) {
        val response = state.response
        val status = response.status

        val isLoading = status is RequestStatus.Loading
        state.mostReadInfo?.let {
            MostReadInfo(mostRead = it, isLoading = false)
        }

        val article = response.data
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
            isLoading = false,
            style = MaterialTheme.typography.displaySmall,
        )

        val summary = article?.summary

        CustomText(
            modifier = Modifier.customSharedBounds(
                scope = this@ArticleBody,
                key = SharedElementKey(
                    id = summary,
                    type = SharedElementKey.Type.Description,
                )
            ),
            textOrNull = summary,
            isLoading = false,
            fontWeight = FontWeight.Bold,
        )

        val mainImg = state.mainImage

        val gridState = rememberLazyStaggeredGridState()

        val mainImgAspectRatio by animateFloatAsState(
            targetValue = when {
                gridState.canScrollBackward -> 8f
                !gridState.canScrollBackward && gridState.isScrollInProgress -> 16 / 10f
                else -> 16 / 9f
            }
        )

        val mainImageRadius by animateDpAsState(
            targetValue = when {
                gridState.canScrollBackward -> 800.dp
                else -> 0.dp
            }
        )

        CustomAsyncImage(
            modifier = Modifier
                .clip(RoundedCornerShape(mainImageRadius))
                .customSharedElement(
                    scope = this@ArticleBody,
                    key = SharedElementKey(type = SharedElementKey.Type.Image, id = mainImg?.url)
                ),
            urlOrNull = mainImg?.url,
            isDataLoading = false,
            aspectRatio = mainImgAspectRatio,
        )

        val images = article?.images
        LazyVerticalStaggeredGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = StaggeredGridCells.Fixed(2),
            state = gridState,
        ) {
            val default = 5
            items(count = images?.size ?: default) { index ->
                val img = images?.get(index) ?: return@items
                CustomAsyncImage(urlOrNull = img.url, isDataLoading = isLoading)
            }
        }
    }
}