package com.migvidal.wikicircuit.feed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.migvidal.wikicircuit.core.ui.CustomAsyncImage
import com.migvidal.wikicircuit.core.ui.RequestStatus
import com.migvidal.wikicircuit.core.ui.shimmer

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
        Text(
            modifier = Modifier.then(
                if (isLoading) {
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .shimmer()
                } else {
                    Modifier
                }
            ),
            text = if (isLoading) "" else featuredArticle?.titles?.normalized ?: "",
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

        featuredArticle?.let {
            val img = it.originalImage ?: return@let
            CustomAsyncImage(width = img.width, height = img.height, sourceUrl = img.source)
        }
    }
}

