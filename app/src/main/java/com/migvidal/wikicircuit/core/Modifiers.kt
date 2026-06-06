package com.migvidal.wikicircuit.core

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.migvidal.wikicircuit.SharedElementKey
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