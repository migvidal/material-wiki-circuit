package com.migvidal.wikicircuit.core.ui

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.migvidal.wikicircuit.R
import com.migvidal.wikicircuit.core.ui.theme.WikiCircuitTheme
import com.migvidal.wikicircuit.detail.DetailScreen
import com.migvidal.wikicircuit.feed.FeedScreen
import com.migvidal.wikicircuit.search.SearchScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.navigation.canGoBack
import com.slack.circuit.runtime.navigation.currentScreen
import com.slack.circuit.runtime.screen.Screen
import com.slack.circuit.sharedelements.SharedElementTransitionLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var circuit: Circuit

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim = Color.TRANSPARENT,
                darkScrim = Color.TRANSPARENT,
            )
        )
        setContent {
            MainContent()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
    @Composable
    private fun MainContent(modifier: Modifier = Modifier) {
        CircuitCompositionLocals(circuit) {
            val topLevelScreens = topLevelDestinations.map { it.screen }
            val backStack =
                rememberSaveableBackStack(initialScreens = topLevelScreens)
            val navigator = rememberCircuitNavigator(backStack)
            WikiCircuitTheme {
                val currentScreen = backStack.currentScreen
                Scaffold(
                    modifier = modifier,
                    topBar = {
                        TopBar(
                            currentScreen = currentScreen,
                            canGoBack = backStack.canGoBack && backStack.currentScreen !in topLevelScreens,
                            goBack = { navigator.pop() },
                        )
                    },
                    bottomBar = {
                        val bottomBarVisible = currentScreen !is DetailScreen
                        AnimatedVisibility(visible = bottomBarVisible) {
                            BottomBar(
                                currentScreen = currentScreen,
                                onItemClicked = { navigator.goTo(it.screen) },
                            )
                        }
                    }
                ) { padding ->
                    Box(modifier = Modifier.padding(padding)) {
                        SharedElementTransitionLayout {
                            NavigableCircuitContent(
                                navigator = navigator,
                                backStack = backStack,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    currentScreen: Screen?,
    canGoBack: Boolean,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = when (currentScreen) {
                    SearchScreen -> stringResource(R.string.search)
                    DetailScreen -> stringResource(R.string.detail)
                    else -> ""
                }
            )
        },
        navigationIcon = {
            if (canGoBack) {
                IconButton(onClick = goBack) {
                    Icon(
                        painter = painterResource(R.drawable.arrow_back),
                        contentDescription = stringResource(R.string.back),
                    )
                }
            }
        }
    )
}

@Composable
fun BottomBar(
    currentScreen: Screen?,
    onItemClicked: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier) {
        topLevelDestinations.forEach { destination ->
            NavigationBarItem(
                selected = destination.screen == currentScreen,
                onClick = { onItemClicked(destination) },
                icon = {
                    Icon(
                        painter = painterResource(destination.iconRes),
                        contentDescription = stringResource(destination.labelRes),
                    )
                },
                label = {
                    Text(text = stringResource(destination.labelRes))
                }
            )
        }
    }
}


private val topLevelDestinations = listOf(
    TopLevelDestination(
        screen = SearchScreen,
        labelRes = R.string.search,
        iconRes = R.drawable.search,
    ),
    TopLevelDestination(
        screen = FeedScreen,
        labelRes = R.string.feed,
        iconRes = R.drawable.feed,
    ),
)

data class TopLevelDestination(
    val screen: Screen,
    @param:StringRes val labelRes: Int,
    @param:DrawableRes val iconRes: Int,
)