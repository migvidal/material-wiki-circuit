package com.migvidal.wikicircuit.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.migvidal.wikicircuit.R
import com.migvidal.wikicircuit.core.ui.RequestStatus
import com.migvidal.wikicircuit.core.ui.components.CardWithImage
import com.migvidal.wikicircuit.core.ui.components.CustomAsyncImage
import com.migvidal.wikicircuit.core.ui.components.CustomCarouselLayoutCard
import com.migvidal.wikicircuit.core.ui.components.CustomElevatedCard
import com.migvidal.wikicircuit.core.ui.components.CustomPreHeading
import com.migvidal.wikicircuit.core.ui.components.CustomText

@Composable
fun FeedUi(state: FeedScreen.State, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val response = state.response
        when (val status = response.status) {
            is RequestStatus.Failure -> Text(text = status.message)
            RequestStatus.Loading -> FeedBody(response = response, onItemClicked = {})
            RequestStatus.Success -> FeedBody(
                response = response,
                onItemClicked = { state.eventSink(FeedScreen.State.Event.ItemClicked(it)) },
            )
        }
    }
}

@Composable
private fun FeedBody(
    response: CachedFeedResponse,
    onItemClicked: (title: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val status = response.status
    val isLoading = status is RequestStatus.Loading
    val feed = response.data
    LazyColumn(modifier = modifier) {
        item {
            val img = feed?.image
            ImageOfTheDay(imageModel = img, isLoading = isLoading, onItemClicked = onItemClicked)
        }
        item {
            val featured = feed?.featuredArticle
            Featured(featuredArticle = featured, isLoading = isLoading)
        }
        item {
            val mostRead = feed?.mostread
            MostRead(mostRead = mostRead, isLoading = isLoading, onItemClicked = onItemClicked)
        }
    }
}

@Composable
private fun ImageOfTheDay(
    imageModel: FeedModel.ImageOfTheDay?,
    isLoading: Boolean,
    onItemClicked: (title: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    CustomElevatedCard(
        modifier = modifier,
        shape = RectangleShape,
        onClick = {
            val title = imageModel?.title ?: return@CustomElevatedCard
            onItemClicked(title)
        },
    ) {
        CustomAsyncImage(
            imageOrNull = imageModel?.image,
            isDataLoading = isLoading,
            cropped = false
        )
        Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp)) {
            CustomPreHeading(textOrNull = "Image of the day", isLoading = isLoading)
            CustomText(
                textOrNull = imageModel?.description?.text,
                isLoading = isLoading,
                style = MaterialTheme.typography.titleLarge,
            )
            val attribution = buildString {
                append(imageModel?.artist?.text)
                append(" · ")
                append(imageModel?.credit?.text)
            }
            CustomText(textOrNull = attribution, isLoading = isLoading)
        }
    }
}

@Composable
private fun Featured(
    featuredArticle: FeedModel.FeaturedArticle?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    CardWithImage(
        modifier = modifier.padding(horizontal = 8.dp, vertical = 16.dp),
        image = {
            CustomAsyncImage(
                imageOrNull = featuredArticle?.originalimage,
                isDataLoading = isLoading,
            )
        },
        onClick = {},
    ) {
        CustomPreHeading(textOrNull = "Featured", isLoading = isLoading)
        CustomText(
            textOrNull = featuredArticle?.titles?.normalized,
            isLoading = isLoading,
            style = MaterialTheme.typography.titleLarge,
        )

        CustomText(
            textOrNull = featuredArticle?.description,
            isLoading = isLoading,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MostRead(
    mostRead: FeedModel.MostRead?,
    isLoading: Boolean,
    onItemClicked: (title: String) -> Unit,
    modifier: Modifier = Modifier
) {
    CustomCarouselLayoutCard(
        modifier = modifier,
        isLoading = isLoading,
        header = {
            CustomPreHeading(
                modifier = Modifier.padding(vertical = 8.dp),
                textOrNull = "Most read · ${mostRead?.date}",
                isLoading = isLoading,
            )
        },
        carouselItems = mostRead?.articles ?: emptyList(),
        carouselItem = { item ->
            val img = item?.originalimage
            val titles = item?.titles
            CardWithImage(
                modifier = Modifier.padding(horizontal = 8.dp),
                onClick = {
                    val title = titles?.canonical ?: return@CardWithImage
                    onItemClicked(title)
                },
                image = {
                    CustomAsyncImage(
                        imageOrNull = img,
                        isDataLoading = isLoading,
                    )
                }
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        CustomText(
                            textOrNull = "#${item?.rank}",
                            isLoading = isLoading,
                            fontWeight = FontWeight.Bold,
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        if (!isLoading) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(R.drawable.bar_chart),
                                contentDescription = "Views",
                            )
                        }
                        CustomText(
                            textOrNull = item?.views.toString(),
                            isLoading = isLoading,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    CustomText(
                        textOrNull = titles?.normalized,
                        isLoading = isLoading,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }

        }
    )
}

