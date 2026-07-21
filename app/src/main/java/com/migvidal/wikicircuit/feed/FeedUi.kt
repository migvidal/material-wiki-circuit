package com.migvidal.wikicircuit.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.draw.dropShadow
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
import com.migvidal.wikicircuit.feed.FeedModel.MostRead.MostReadArticle
import com.migvidal.wikicircuit.feed.FeedModel.OnThisDay.OnThisDayPage
import com.migvidal.wikicircuit.feed.FeedScreen.State.Event.ImageClicked
import com.migvidal.wikicircuit.feed.FeedScreen.State.Event.ItemClicked

@Composable
fun FeedUi(state: FeedScreen.State, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val response = state.response
        when (val status = response.status) {
            is RequestStatus.Failure -> Text(text = status.message)
            RequestStatus.Loading -> FeedBody(state = state)
            RequestStatus.Success -> FeedBody(state = state)
        }
    }
}

@Composable
private fun FeedBody(
    state: FeedScreen.State,
    modifier: Modifier = Modifier
) {
    val response = state.response
    val status = response.status
    val isLoading = status is RequestStatus.Loading
    val feed = response.data
    LazyColumn(modifier = modifier) {
        val stickyModifiers = Modifier
            .padding(horizontal = 16.dp)
            .dropShadow(shape = RectangleShape) {
                alpha = .4f
                radius = 32f
            }

        val cardPadding = Modifier.padding(vertical = 8.dp)

        stickyHeader {
            Text(
                modifier = Modifier.then(stickyModifiers),
                text = "Today",
                style = MaterialTheme.typography.displaySmall,
            )
        }
        item {
            val img = feed?.image
            ImageOfTheDay(
                modifier = Modifier.then(cardPadding),
                imageModel = img,
                isLoading = isLoading,
                onClick = { image ->
                    image.filePage?.let {
                        state.eventSink(
                            ImageClicked(it)
                        )
                    }
                }
            )
        }
        item {
            val featured = feed?.featuredArticle
            Featured(
                modifier = Modifier
                    .then(cardPadding)
                    .padding(horizontal = 16.dp), featuredArticle = featured, isLoading = isLoading
            )
        }
        item {
            val mostRead = feed?.mostread
            MostRead(
                modifier = Modifier.then(cardPadding),
                mostRead = mostRead,
                isLoading = isLoading,
                onItemClicked = { item ->
                    state.eventSink(
                        ItemClicked(
                            titles = item.titles,
                            mainImage = item.originalimage,
                        )
                    )
                }
            )
        }
        stickyHeader {
            Text(
                modifier = Modifier.then(stickyModifiers),
                text = "On this day",
                style = MaterialTheme.typography.displaySmall,
            )
        }
        val onThisDay = feed?.onThisDay
        items(count = onThisDay?.size ?: 3) {
            val item = onThisDay?.get(it)
            OnThisDayItem(
                modifier = Modifier.then(cardPadding),
                onThisDay = item,
                isLoading = isLoading,
                onitemClicked = { item ->
                    state.eventSink(
                        ItemClicked(
                            titles = item.titles,
                            mainImage = item.originalimage,
                        )
                    )
                },
            )
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center,
            ) { Text(text = "You've reached the end!") }
        }
    }
}

@Composable
private fun ImageOfTheDay(
    imageModel: FeedModel.ImageOfTheDay?,
    isLoading: Boolean,
    onClick: (imageModel: FeedModel.ImageOfTheDay) -> Unit,
    modifier: Modifier = Modifier,
) {
    CustomElevatedCard(
        modifier = modifier,
        shape = RectangleShape,
        onClick = {
            imageModel?.let { onClick(it) }
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
        modifier = modifier,
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
private fun MostRead(
    mostRead: FeedModel.MostRead?,
    isLoading: Boolean,
    onItemClicked: (MostReadArticle) -> Unit,
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
                    item?.let { onItemClicked(it) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnThisDayItem(
    onThisDay: FeedModel.OnThisDay?,
    isLoading: Boolean,
    onitemClicked: (OnThisDayPage) -> Unit,
    modifier: Modifier = Modifier,
) {
    CustomCarouselLayoutCard(
        modifier = modifier,
        isLoading = isLoading,
        header = {
            Column {
                CustomText(
                    textOrNull = onThisDay?.year?.toString(),
                    isLoading = isLoading,
                    fontWeight = FontWeight.Bold,
                )
            }
        },
        leadIn = {
            CustomText(textOrNull = onThisDay?.text, isLoading = isLoading)
        },
        carouselItems = onThisDay?.pages ?: emptyList(),
        carouselItem = { page ->
            CardWithImage(
                modifier = Modifier.padding(horizontal = 8.dp),
                onClick = { page?.let { onitemClicked(it) } },
                image = {
                    CustomAsyncImage(
                        imageOrNull = page?.originalimage,
                        isDataLoading = isLoading,
                    )
                }
            ) {
                CustomText(
                    textOrNull = page?.titles?.normalized,
                    isLoading = isLoading,
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    )

}

