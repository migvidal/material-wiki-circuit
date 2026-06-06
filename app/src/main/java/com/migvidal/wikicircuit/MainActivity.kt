package com.migvidal.wikicircuit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.migvidal.wikicircuit.detail.DetailScreen
import com.migvidal.wikicircuit.search.SearchScreen
import com.migvidal.wikicircuit.ui.theme.WikiCircuitTheme
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.runtime.navigation.canGoBack
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
        enableEdgeToEdge()
        setContent {
            CircuitCompositionLocals(circuit) {
                val backStack = rememberSaveableBackStack(root = SearchScreen)
                val navigator = rememberCircuitNavigator(backStack)
                WikiCircuitTheme {
                    Scaffold(topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = when (backStack.currentRecord?.screen) {
                                        SearchScreen -> "Feed"
                                        DetailScreen -> "Detail"
                                        else -> ""
                                    }
                                )
                            },
                            navigationIcon = {
                                if (backStack.canGoBack) {
                                    IconButton(onClick = {navigator.pop()}) {
                                        Icon(
                                            painter = painterResource(R.drawable.arrow_back),
                                            contentDescription = "Back",
                                        )
                                    }
                                }
                            }
                        )
                    }) { padding ->
                        Box(modifier = Modifier.padding(padding)) {
                            SharedElementTransitionLayout {
                                NavigableCircuitContent(
                                    navigator = navigator,
                                    backStack = backStack
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}