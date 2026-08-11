package com.migvidal.wikicircuit.feature.page.image

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.migvidal.wikicircuit.core.ui.SharedElementKey
import com.migvidal.wikicircuit.core.ui.components.CustomAsyncImage
import com.migvidal.wikicircuit.core.ui.components.customSharedElement
import com.slack.circuit.sharedelements.SharedElementTransitionScope

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ImageUi(state: ImageScreen.State, modifier: Modifier = Modifier) {
    SharedElementTransitionScope() {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            val imgUrl = state.image?.url
            CustomAsyncImage(
                modifier = Modifier.customSharedElement(
                    scope = this@SharedElementTransitionScope,
                    key = SharedElementKey(type = SharedElementKey.Type.Image, id = imgUrl,)
                ),
                urlOrNull = imgUrl,
                isDataLoading = false,
            )
        }
    }
}