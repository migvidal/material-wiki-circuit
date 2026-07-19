package com.migvidal.wikicircuit.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
private fun Modifier.loading() = this
    .fillMaxWidth()
    .padding(vertical = 8.dp)
    .shimmer()

@Composable
fun CustomText(
    textOrNull: String?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
) {
    Text(
        modifier = modifier.then(
            if (isLoading) Modifier.loading() else Modifier
        ),
        text = if (isLoading) "" else textOrNull ?: "",
        style = style,
        color = color,
        fontWeight = fontWeight,
    )
}

@Composable
fun CustomText(
    annotatedStringOrNull: AnnotatedString?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle.Default,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
) {
    val placeholder = buildAnnotatedString {}
    Text(
        modifier = modifier.then(
            if (isLoading) Modifier.loading() else Modifier
        ),
        text = if (isLoading) placeholder else annotatedStringOrNull ?: placeholder,
        style = style,
        color = color,
        fontWeight = fontWeight,
    )
}

@Composable
fun CustomPreHeading(
    textOrNull: String?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    CustomText(
        textOrNull = textOrNull?.uppercase(),
        isLoading = isLoading,
        modifier = modifier.padding(top = 8.dp, bottom = 4.dp),
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f),
    )
}