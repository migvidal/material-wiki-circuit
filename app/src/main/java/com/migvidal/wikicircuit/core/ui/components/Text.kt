package com.migvidal.wikicircuit.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun TextWithSkeleton(
    textOrNull: String?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
) {
    Text(
        modifier = modifier.then(
            if (isLoading) {
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .shimmer()
            } else {
                Modifier
            }
        ),
        text = if (isLoading) "" else textOrNull ?: "",
        style = style,
    )
}