package com.migvidal.wikicircuit.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> CustomCarouselLayoutCard(
    isLoading: Boolean,
    header: @Composable RowScope.() -> Unit,
    carouselItems: List<T>,
    carouselItem: @Composable (itemOrNull: T?) -> Unit,
    modifier: Modifier = Modifier,
    leadIn: @Composable ColumnScope.() -> Unit = {},
) {
    CustomCard(modifier = modifier) {
        Column {
            val defaultItems = 3
            val size = if (carouselItems.isEmpty()) defaultItems else carouselItems.size
            val carouselState = rememberCarouselState { size }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                header()

                if (!isLoading && carouselState.currentItem > 0) {
                    val scope = rememberCoroutineScope()
                    TextButton(
                        onClick = { scope.launch { carouselState.animateScrollToItem(item = 0) } }
                    ) {
                        Text("Back to first")
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                leadIn()
            }

            if (carouselItems.size > 1) {
                HorizontalCenteredHeroCarousel(
                    modifier = Modifier.fillMaxWidth(),
                    state = carouselState,
                ) { index ->
                    val item = carouselItems.getOrElse(index) { null }
                    carouselItem(item)
                }
            } else {
                carouselItem(carouselItems.firstOrNull())
            }

        }
    }
}