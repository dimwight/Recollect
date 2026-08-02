package com.example.recollect.bits

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SlidingWipeContainer(
    targetState: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = {
            // If the new state is higher, slide in from right to left
            if (targetState > initialState) {
                (slideInHorizontally { width -> width } + fadeIn()) with
                        (slideOutHorizontally { width -> -width } + fadeOut())
            } else {
                // If the new state is lower, slide in from left to right
                (slideInHorizontally { width -> -width } + fadeIn()) with
                        (slideOutHorizontally { width -> width } + fadeOut())
            }
        }
    ) { state ->
        content(state)
    }
}


@Composable
fun WipeDemoScreen() {
    var currentScreen by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = { currentScreen = if (currentScreen == 0) 1 else 0 }) {
            Text("Trigger Wipe Transition")
        }

        SlidingWipeContainer(
            targetState = currentScreen,
            modifier = Modifier.fillMaxSize()
        ) { screenIndex ->
            when (screenIndex) {
                0 -> Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Red),
                    contentAlignment = Alignment.Center
                ) {
                    Text("First Screen", color = Color.White)
                }

                1 -> Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Blue),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Second Screen", color = Color.White)
                }
            }
        }
    }
}

