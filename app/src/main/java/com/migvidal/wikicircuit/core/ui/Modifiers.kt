package com.migvidal.wikicircuit.core.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.migvidal.wikicircuit.core.ui.SharedElementKey
import com.slack.circuit.sharedelements.SharedElementTransitionScope

@Composable
fun Modifier.customSharedBounds(
    scope: SharedElementTransitionScope,
    key: SharedElementKey,
): Modifier {
    with(scope) {
        return sharedBounds(
            animatedVisibilityScope = requireAnimatedScope(
                SharedElementTransitionScope.AnimatedScope.Navigation,
            ),
            sharedContentState = rememberSharedContentState(key = key),
            enter = fadeIn(spring(stiffness = Spring.StiffnessLow)),
            exit = fadeOut(spring(stiffness = Spring.StiffnessLow)),
        )
    }
}

@Composable
fun Modifier.customSharedElement(
    scope: SharedElementTransitionScope,
    key: SharedElementKey,
): Modifier {
    with(scope) {
        return sharedElement(
            animatedVisibilityScope = requireAnimatedScope(
                SharedElementTransitionScope.AnimatedScope.Navigation,
            ),
            sharedContentState = rememberSharedContentState(key = key),
        )
    }
}

@Composable
fun Modifier.shimmer(
    durationMillis: Int = 1000,
): Modifier {
    val transition = rememberInfiniteTransition()

    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 500f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = durationMillis,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
    )

    return drawBehind {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.LightGray.copy(alpha = 0.2f),
                    Color.LightGray.copy(alpha = 1.0f),
                    Color.LightGray.copy(alpha = 0.2f),
                ),
                start = Offset(x = translateAnimation, y = translateAnimation),
                end = Offset(x = translateAnimation + 100f, y = translateAnimation + 100f),
            )
        )
    }
}