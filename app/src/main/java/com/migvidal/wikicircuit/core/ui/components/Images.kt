package com.migvidal.wikicircuit.core.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.migvidal.wikicircuit.R
import com.migvidal.wikicircuit.core.USER_AGENT_KEY
import com.migvidal.wikicircuit.core.api.common_model.ApiImage
import com.migvidal.wikicircuit.core.api.common_model.ApiSimpleImage
import com.migvidal.wikicircuit.core.getUserAgent
import com.migvidal.wikicircuit.core.ui.RequestStatus

@Composable
fun CustomAsyncImage(
    imageOrNull: ApiImage?,
    isDataLoading: Boolean,
    modifier: Modifier = Modifier,
    cropped: Boolean = true,
) {
    var imageStatus by remember { mutableStateOf<RequestStatus>(RequestStatus.Loading) }
    val defaultRatio = 3 / 2f
    val boxModifier = if (imageOrNull == null && isDataLoading) {
        Modifier
            .aspectRatio(defaultRatio)
            .shimmer()
    } else {
        when (imageStatus) {
            is RequestStatus.Failure -> {
                Modifier
            }

            RequestStatus.Success -> {
                if (imageOrNull != null) {
                    val imageRatio = imageOrNull.width / imageOrNull.height.toFloat()
                    Modifier.aspectRatio(if (cropped) defaultRatio else imageRatio)
                } else {
                    Modifier
                }
            }

            RequestStatus.Loading -> {
                Modifier
                    .aspectRatio(defaultRatio)
                    .shimmer()
            }
        }
    }

    val context = LocalContext.current
    val headers = NetworkHeaders.Builder().add(USER_AGENT_KEY, getUserAgent(context)).build()

    val isVisible = isDataLoading || (imageStatus !is RequestStatus.Failure && imageOrNull != null)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(boxModifier)
            .animateContentSize()
    ) {
        val request = ImageRequest.Builder(context)
            .data(imageOrNull?.source ?: imageOrNull?.url)
            .httpHeaders(headers)
            .build()

        if (isVisible) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth(),
                model = request,
                contentScale = if (cropped) ContentScale.Crop else ContentScale.Fit,
                contentDescription = null,
                onLoading = { imageStatus = RequestStatus.Loading },
                onSuccess = { imageStatus = RequestStatus.Success },
                onError = { imageStatus = RequestStatus.Failure() },
            )
        } else {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(defaultRatio),
                color = MaterialTheme.colorScheme.error,
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(R.drawable.broken_image),
                        contentDescription = "No image found",
                        tint = MaterialTheme.colorScheme.onError,
                    )
                }
            }
        }
    }
}