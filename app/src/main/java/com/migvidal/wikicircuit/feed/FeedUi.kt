package com.migvidal.wikicircuit.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FeedUi(state: FeedScreen.State, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        val response = state.response
        val isLoading = response != null && !response.isFailure && !response.isSuccess
        if (isLoading) {
            Text(text = "Loading")
            return
        }

        response ?: run {
            Text(text = "No items")
            return
        }

        response.onSuccess {
            Text(text = it)
        }

        response.onFailure {
            Text(text = it.message.toString())
        }
    }
}