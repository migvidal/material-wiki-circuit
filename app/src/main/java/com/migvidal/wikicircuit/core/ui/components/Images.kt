package com.migvidal.wikicircuit.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import com.migvidal.wikicircuit.core.USER_AGENT_KEY
import com.migvidal.wikicircuit.core.getUserAgent
import com.migvidal.wikicircuit.core.ui.RequestStatus

@Composable
fun CustomAsyncImage(width: Int, height: Int, sourceUrl: String, modifier: Modifier = Modifier) {
    var status by remember { mutableStateOf<RequestStatus>(RequestStatus.Success) }
    val loadingModifier = when (status) {
        is RequestStatus.Failure -> Modifier
        RequestStatus.Loading -> Modifier.height(300.dp).shimmer()
        RequestStatus.Success -> Modifier.aspectRatio(width / height.toFloat())
    }
    val context = LocalContext.current
    val headers = NetworkHeaders.Builder().add(USER_AGENT_KEY, getUserAgent(context)).build()
    val request = ImageRequest.Builder(context)
        .data(sourceUrl)
        .httpHeaders(headers)
        .build()

    AnimatedVisibility(modifier = modifier, visible = status !is RequestStatus.Failure) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .then(loadingModifier),
            model = request,
            contentDescription = null,
            onLoading = { status = RequestStatus.Loading },
            onSuccess = { status = RequestStatus.Success },
            onError = { status = RequestStatus.Failure() },
        )
    }
}