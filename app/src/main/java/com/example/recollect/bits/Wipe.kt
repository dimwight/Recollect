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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
    var atNow by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()
        .background(Color.White)) {
        Spacer(Modifier.height(50.dp))
        Button(onClick = {
            atNow = if (atNow == 0) 1 else 0
        }) {
            Text("Wipe")
        }

        SlidingWipeContainer(
            targetState = atNow,
            modifier = Modifier.fillMaxSize()
        ) { at ->
            when (at) {
                0 -> AtBox(at)

                1 -> AtBox(at)
            }
        }
    }

}

@Composable
private fun AtBox(at: Int) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text("$at",
            color = Color.Red,
            style = MaterialTheme.typography.headlineLarge
        )
    }
}













