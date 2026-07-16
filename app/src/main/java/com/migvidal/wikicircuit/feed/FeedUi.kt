package com.migvidal.wikicircuit.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.migvidal.wikicircuit.core.ui.RequestStatus
import com.migvidal.wikicircuit.core.ui.components.CustomAsyncImage
import com.migvidal.wikicircuit.core.ui.components.TextWithSkeleton
import com.migvidal.wikicircuit.core.ui.components.shimmer

@Composable
fun FeedUi(state: FeedScreen.State, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val response = state.response
        when (val status = response.status) {
            is RequestStatus.Failure -> Text(text = status.message)
            RequestStatus.Loading -> FeedBody(response = response)
            RequestStatus.Success -> FeedBody(response = response)
        }
    }
}

@Composable
private fun FeedBody(response: CachedFeedResponse, modifier: Modifier = Modifier) {
    val status = response.status
    val isLoading = status is RequestStatus.Loading
    val feed = response.data
    LazyColumn(modifier = modifier) {
        item {
            val img = feed?.image
            AnimatedVisibility(visible = img != null) {
                ImageOfTheDay(imageModel = img ?: return@AnimatedVisibility)
            }
        }
        item {
            val featured = feed?.featuredArticle
            val noData = status is RequestStatus.Failure && featured == null
            AnimatedVisibility(visible = !noData) {
                Featured(featuredArticle = featured, isLoading = isLoading)
            }
        }
        item {
            val mostRead = feed?.mostread
            val noData = status is RequestStatus.Failure && mostRead == null
            AnimatedVisibility(visible = !noData) {
                MostRead(mostRead = mostRead, isLoading = isLoading)
            }
        }
    }
}

@Composable
private fun ImageOfTheDay(imageModel: FeedModel.ImageOfTheDay, modifier: Modifier = Modifier) {
    val img = imageModel.image
    CustomAsyncImage(width = img.width, height = img.height, sourceUrl = img.source)
}

@Composable
private fun Featured(
    featuredArticle: FeedModel.FeaturedArticle?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        featuredArticle?.let {
            val img = it.originalimage ?: return@let
            CustomAsyncImage(width = img.width, height = img.height, sourceUrl = img.source)
        }

        TextWithSkeleton(
            textOrNull = featuredArticle?.titles?.normalized,
            isLoading = isLoading,
            style = MaterialTheme.typography.titleLarge,
        )

        if (isLoading) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer()
                    .padding(vertical = 8.dp),
                text = "",
            )
        } else {
            Text(text = featuredArticle?.description ?: "")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostRead(mostRead: FeedModel.MostRead?, isLoading: Boolean, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        TextWithSkeleton(
            textOrNull = mostRead?.date,
            isLoading = isLoading,
            style = MaterialTheme.typography.titleLarge,
        )
        TextWithSkeleton(
            textOrNull = mostRead?.articles?.size?.toString(),
            isLoading = isLoading,
        )
        val articles = mostRead?.articles ?: return@Card
        HorizontalCenteredHeroCarousel(
            modifier = Modifier.fillMaxWidth(),
            state = rememberCarouselState { articles.size },
        ) { index ->
            val article = articles[index]
            val img = article.originalimage ?: return@HorizontalCenteredHeroCarousel
            Column {
                Text(text = article.titles.normalized)
                CustomAsyncImage(width = img.width, height = img.height, sourceUrl = img.source)
            }
        }
    }
}

