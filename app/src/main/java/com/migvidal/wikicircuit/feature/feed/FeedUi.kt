package com.migvidal.wikicircuit.feature.feed

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.migvidal.wikicircuit.R
import com.migvidal.wikicircuit.core.network.api.common_model.ImageDto
import com.migvidal.wikicircuit.core.ui.RequestStatus
import com.migvidal.wikicircuit.core.ui.SharedElementKey
import com.migvidal.wikicircuit.core.ui.components.CardWithImage
import com.migvidal.wikicircuit.core.ui.components.CustomAsyncImage
import com.migvidal.wikicircuit.core.ui.components.CustomCarouselLayoutCard
import com.migvidal.wikicircuit.core.ui.components.CustomElevatedCard
import com.migvidal.wikicircuit.core.ui.components.CustomPreHeading
import com.migvidal.wikicircuit.core.ui.components.CustomText
import com.migvidal.wikicircuit.core.ui.components.MostRead
import com.migvidal.wikicircuit.core.ui.components.MostReadInfo
import com.migvidal.wikicircuit.core.ui.components.customSharedBounds
import com.migvidal.wikicircuit.core.ui.components.customSharedElement
import com.migvidal.wikicircuit.feature.feed.FeedModel.MostRead.MostReadArticle
import com.migvidal.wikicircuit.feature.feed.FeedModel.OnThisDay.OnThisDayPage
import com.migvidal.wikicircuit.feature.feed.FeedScreen.State.Event.ImageClicked
import com.migvidal.wikicircuit.feature.feed.FeedScreen.State.Event.ItemClicked
import com.slack.circuit.sharedelements.SharedElementTransitionScope

@Composable
fun FeedUi(state: FeedScreen.State, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val response = state.response
        val status = response.status
        val connected = state.connected
        when {
            !connected -> Text(stringResource(R.string.no_internet))
            status is RequestStatus.Failure -> Text(text = status.message)
            else -> FeedBody(state = state)
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun FeedBody(
    state: FeedScreen.State,
    modifier: Modifier = Modifier
) {
    val response = state.response
    val status = response.status
    val isLoading = status is RequestStatus.Loading
    val feed = response.data
    val surfaceColor = MaterialTheme.colorScheme.surface
    LazyColumn(modifier = modifier) {
        val stickyModifiers = Modifier
            .padding(horizontal = 16.dp)
            .dropShadow(shape = RectangleShape) {
                color = surfaceColor
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
                onClick = {
                    val imgDto = img?.image?.run {
                        ImageDto(
                            height = height,
                            width = width,
                            url = source ?: url,
                        )
                    }
                    state.eventSink(ImageClicked(imgDto))
                }
            )
        }
        item {
            val featured = feed?.featuredArticle
            Featured(
                modifier = Modifier
                    .then(cardPadding)
                    .padding(horizontal = 16.dp),
                featuredArticle = featured,
                isLoading = isLoading,
                onClick = {
                    state.eventSink(
                        ItemClicked(
                            titles = featured?.titles ?: return@Featured,
                            mainImage = featured.originalimage,
                        )
                    )
                }
            )
        }
        item {
            val mostRead = feed?.mostread
            MostReadSection(
                modifier = Modifier.then(cardPadding),
                mostRead = mostRead,
                isLoading = isLoading,
                onItemClicked = { item ->
                    state.eventSink(
                        ItemClicked(
                            titles = item.titles,
                            mainImage = item.originalimage,
                            mostRead = MostRead(
                                rank = item.rank,
                                views = item.views,
                            )
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun ImageOfTheDay(
    imageModel: FeedModel.ImageOfTheDay?,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CustomElevatedCard(
        modifier = modifier,
        shape = RectangleShape,
        onClick = onClick,
    ) {
        val img = imageModel?.image
        val url = img?.run { url ?: source }
        SharedElementTransitionScope {
            CustomAsyncImage(
                modifier = Modifier.customSharedElement(
                    scope = this,
                    key = SharedElementKey(type = SharedElementKey.Type.Image, id = url)
                ),
                urlOrNull = url,
                isDataLoading = isLoading,
                aspectRatio = img?.run { width / height.toFloat() },
            )
        }
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun Featured(
    featuredArticle: FeedModel.FeaturedArticle?,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SharedElementTransitionScope {
        val title = featuredArticle?.titles?.normalized
        CardWithImage(
            modifier = modifier,
            id = title,
            image = {
                val img = featuredArticle?.originalimage
                CustomAsyncImage(
                    modifier = Modifier.customSharedElement(
                        scope = this@SharedElementTransitionScope,
                        key = SharedElementKey(
                            type = SharedElementKey.Type.Image,
                            id = img?.source
                        ),
                    ),
                    urlOrNull = img?.source,
                    isDataLoading = isLoading,
                )
            },
            onClick = onClick,
        ) {
            CustomPreHeading(textOrNull = "Featured", isLoading = isLoading)
            CustomText(
                modifier = Modifier.customSharedElement(
                    scope = this@SharedElementTransitionScope,
                    key = SharedElementKey(type = SharedElementKey.Type.Title, id = title)
                ),
                textOrNull = title,
                isLoading = isLoading,
                style = MaterialTheme.typography.titleLarge,
            )

            val description = featuredArticle?.description
            CustomText(
                modifier = Modifier.customSharedElement(
                    scope = this@SharedElementTransitionScope,
                    key = SharedElementKey(
                        type = SharedElementKey.Type.Description,
                        id = description,
                    )
                ),
                textOrNull = description,
                isLoading = isLoading,
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun MostReadSection(
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
                textOrNull = "Most read · ${mostRead?.date}",
                isLoading = isLoading,
            )
        },
        carouselItems = mostRead?.articles ?: emptyList(),
        carouselItem = { item ->
            SharedElementTransitionScope {
                val img = item?.originalimage
                val titles = item?.titles
                val title = titles?.normalized
                CardWithImage(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    id = title,
                    onClick = {
                        item?.let { onItemClicked(it) }
                    },
                    image = {
                        CustomAsyncImage(
                            modifier = Modifier.customSharedElement(
                                scope = this@SharedElementTransitionScope,
                                key = SharedElementKey(
                                    type = SharedElementKey.Type.Image,
                                    id = img?.source,
                                )
                            ),
                            urlOrNull = img?.source,
                            isDataLoading = isLoading,
                        )
                    }
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        val mostRead = item?.run { MostRead(rank, views) }

                        mostRead?.let { MostReadInfo(mostRead = it, isLoading = isLoading) }

                        CustomText(
                            modifier = Modifier.customSharedElement(
                                scope = this@SharedElementTransitionScope,
                                key = SharedElementKey(
                                    type = SharedElementKey.Type.Title,
                                    id = title,
                                )
                            ),
                            textOrNull = title,
                            isLoading = isLoading,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
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
            SharedElementTransitionScope {
                val title = page?.titles?.normalized
                CardWithImage(
                    modifier = Modifier
                        .padding(horizontal = 8.dp),
                    id = title,
                    onClick = { page?.let { onitemClicked(it) } },
                    image = {
                        val img = page?.originalimage
                        CustomAsyncImage(
                            modifier = Modifier.customSharedBounds(
                                scope = this@SharedElementTransitionScope,
                                key = SharedElementKey(
                                    type = SharedElementKey.Type.Image,
                                    id = img?.source,
                                )
                            ),
                            urlOrNull = img?.source,
                            isDataLoading = isLoading,
                        )
                    }
                ) {
                    CustomText(
                        modifier = Modifier.customSharedBounds(
                            scope = this@SharedElementTransitionScope,
                            key = SharedElementKey(
                                type = SharedElementKey.Type.Title,
                                id = title,
                            )
                        ),
                        textOrNull = title,
                        isLoading = isLoading,
                        style = MaterialTheme.typography.titleLarge,
                    )
                }
            }
        }
    )

}

