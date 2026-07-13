package com.migvidal.wikicircuit.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
fun FeedBody(response: CachedFeedResponse, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        item {
            ImageOfTheDay()
        }
        item {
            Featured(featuredArticle = response.data?.featuredArticle)
        }
    }
}

@Composable
private fun ImageOfTheDay(modifier: Modifier = Modifier) {
}

@Composable
private fun Featured(featuredArticle: FeedModel.FeaturedArticle?, modifier: Modifier = Modifier) {
    val showSkeleton = featuredArticle == null

    Column(modifier = modifier) {
        Text(
            modifier = Modifier.then(
                if (showSkeleton) {
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .shimmer()
                } else {
                    Modifier
                }
            ),
            text = featuredArticle?.titles?.normalized ?: "",
            style = MaterialTheme.typography.titleLarge,
        )

        if (showSkeleton) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .shimmer()
                    .padding(vertical = 8.dp),
                text = "",
            )
        }

        Text(text = featuredArticle?.description ?: "")
    }
}

