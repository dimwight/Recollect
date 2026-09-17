package com.example.recollect.bits

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.recollect.timeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

private const val scrollJump = 5

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun WipeDemoScreen() {
    fun adjustPicks(selected: EasingOption) {
        var now = picks
        if (now.contains(selected))
            now.remove(selected)
        now.add(0, selected)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        var easingAt by remember { mutableIntStateOf(0) }
        var scrollAt by remember { mutableIntStateOf(0) }
        // These are used by animation later in composition
        var wipeState by remember { mutableIntStateOf(0) }
        val scope = rememberCoroutineScope()
        var gettingValues by remember { mutableStateOf(true) }

        Spacer(Modifier.height(50.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EasingPicker(
                list = Easings,
                gettingValues = gettingValues,
                scrollAt = scrollAt,
                easingAt = easingAt,
                onSelected = { listAt: Int ->
                    easingAt = listAt
                    val selected = Easings[easingAt]
                    adjustPicks(selected)
                    scope.launch {
                        delay(500.milliseconds)
                        if (Random.nextFloat() < .5)
                            wipeState++
                        else
                            wipeState--
                    }
                }
            )
            if (gettingValues) {
                LaunchedEffect(gettingValues) {
                    gettingValues = false
                    if (false){
                        scrollAt=Easings.lastIndex-getPickerRows()
                        easingAt= Easings.lastIndex-1
                     }
                    else if (true) easingAt=3
                }
                return
            }
            if (picks.isEmpty()) picks.add(Easings[easingAt])
            EasingPicker(
                list = picks,
                easingAt=easingAt,
                onSelected = { listAt: Int ->
                    easingAt=Easings.indexOf(picks[listAt])
                    val selected = Easings.get(easingAt)
                    adjustPicks(selected)
                    if(easingAt < scrollAt)
                        scrollAt=easingAt
                    else {
                        val fromScroll = easingAt - getPickerRows()
                        if (fromScroll > scrollAt)
                            scrollAt+=fromScroll
                    }
                    scope.launch {
                        delay(500.milliseconds)
                        if (Random.nextFloat() < .5)
                            wipeState++
                        else
                            wipeState--
                    }
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val lastIndex = Easings.lastIndex
            Button(
                enabled = easingAt > 0,
                onClick = {
                    easingAt--
                    if (easingAt < scrollAt)
                        scrollAt--
                }
            ) { Text("Back") }

            Button(
                enabled = easingAt < lastIndex,
                onClick = {
                    easingAt++
                    if (easingAt - getPickerRows() >= scrollAt)
                        scrollAt++
                }
            ) { Text("Next") }
            Button(
                enabled = scrollAt > 0,
                onClick = {
                    scrollAt -= min(scrollJump, scrollAt)
                }
            ) { Text("Up") }

            Button(
                enabled = scrollAt + getPickerRows() <= lastIndex,
                onClick = {
                    scrollAt += min(scrollJump, lastIndex - scrollAt)
                }
            ) { Text("Down") }
        }
// } Relevant portion of usage ends here

        Button(onClick = {
            timeMillis("click")
            if (Random.nextFloat() < .5)
                wipeState++
            else
                wipeState--
        }) {
            Text("Wipe")
        }

        AnimatedContent(
            targetState = wipeState,
            transitionSpec = {
                val slideTween = tween<IntOffset>(
                    durationMillis = 1500,
                    easing = Easings[easingAt].easing
                )
                if (targetState > initialState) {
                    slideInHorizontally(slideTween) { it } togetherWith
                            slideOutHorizontally(slideTween) { -it }
                } else {
                    slideInHorizontally(slideTween) { -it } togetherWith
                            slideOutHorizontally(slideTween) { it }
                }
            }
        ) { state -> AtBox(state) }
    }
}

@Composable
fun WipeDemoScreen__() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(Modifier.height(50.dp))

        var selectedAt by remember { mutableIntStateOf(0) }
        val scope = rememberCoroutineScope()
        var wipeState by remember { mutableIntStateOf(0) }

        EasingPicker(
            scrollAt = 0,
            easingAt = selectedAt,
            onSelected = {
                selectedAt = it
                timeMillis("click")
                scope.launch {
                    delay(500.milliseconds)
                    if (Random.nextFloat() < .5)
                        wipeState++
                    else
                        wipeState--
                }
            }
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                enabled = selectedAt > 0,
                onClick = { selectedAt-- }
            ) {
                Text("Previous")
            }

            Text(
                text = Easings[selectedAt].name,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Button(
                enabled = selectedAt < Easings.lastIndex,
                onClick = { selectedAt++ }
            ) {
                Text("Next")
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    selectedAt =
                        if (selectedAt == 0)
                            Easings.lastIndex
                        else
                            selectedAt - 1
                }
            ) {
                Text("Previous")
            }

            Button(
                onClick = {
                    selectedAt =
                        if (selectedAt == Easings.lastIndex)
                            0
                        else
                            selectedAt + 1
                }
            ) {
                Text("Next")
            }
        }


        Spacer(Modifier.height(50.dp))
        Button(onClick = {
            timeMillis("click")
            if (Random.nextFloat() < .5)
                wipeState++
            else
                wipeState--
        }) {
            Text("Wipe")
        }

        AnimatedContent(
            targetState = wipeState,
            transitionSpec = {
                val slideTween = tween<IntOffset>(
                    durationMillis = 1500,
                    easing = Easings[selectedAt].easing
                )
                if (targetState > initialState) {
                    slideInHorizontally(slideTween) { it } togetherWith
                            slideOutHorizontally(slideTween) { -it }
                } else {
                    slideInHorizontally(slideTween) { -it } togetherWith
                            slideOutHorizontally(slideTween) { it }
                }
            }
        ) { state -> AtBox(state) }
    }
}

@Composable
fun WipeDemoScreen_() {
    var wipeState by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(Modifier.height(50.dp))
        Button(onClick = {
            timeMillis("click")
            if (Random.nextFloat() < .5)
                wipeState++
            else
                wipeState--
        }) {
            Text("Wipe")
        }

        SlidingWipeContainer(
            targetState = wipeState,
            modifier = Modifier.fillMaxSize()
        ) { at ->
            if (false) AtBox(at)
            else when (at) {
                0 -> AtBox(at)
                1 -> AtBox(at)
            }
        }
    }

}

@Composable
fun SlidingWipeContainer(
    targetState: Int,
    modifier: Modifier,
    content: @Composable (Int) -> Unit,
) {
    if (true) {
        AnimatedContent(
            targetState = targetState,
            transitionSpec = {
                val slideTween = tween<IntOffset>(
                    1200,
                    easing = LinearEasing
                )
                val initial = initialState
                if (targetState > initial) {
                    slideInHorizontally(slideTween) { it } /*+ fadeIn()*/ togetherWith
                            slideOutHorizontally(slideTween) { -it }/* + fadeOut()*/
                } else {
                    slideInHorizontally(slideTween) { -it }/* + fadeIn()*/ togetherWith
                            slideOutHorizontally(slideTween) { it }/* + fadeOut()*/
                }
            }
        ) { state ->
            content(state)
        }
    } else if (false) AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = {
            // Adjust duration (e.g., 400ms) and add a smooth cubic easing curve
            val slideSpec: FiniteAnimationSpec<IntOffset> =
                tween(durationMillis = 400, easing = EaseInOutCubic)
            val fadeSpec: TweenSpec<Float> =
                tween(durationMillis = 400, easing = EaseInOutCubic)

            if (targetState > initialState) {
                // Wipe Right to Left: New content slides in from the right boundary
                slideInHorizontally(slideSpec) { width -> width } +
                        fadeIn(fadeSpec) togetherWith
                        slideOutHorizontally(slideSpec) { width -> -width } +
                        fadeOut(fadeSpec)
            } else {
                // Wipe Left to Right: New content slides in from the left boundary
                slideInHorizontally(slideSpec) { width -> -width } +
                        fadeIn(fadeSpec) togetherWith
                        slideOutHorizontally(slideSpec) { width -> width } +
                        fadeOut(fadeSpec)
            }
        },
        label = "DirectionalWipe"
    ) { state ->
        content(state)
    }
    else AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = {
            val fadeSpec = if (false)
                tween(durationMillis = 10000)
            else tween<Float>(easing = LinearOutSlowInEasing)
            // right to left?
            if (targetState > initialState) {
                (slideInHorizontally { width -> width }
                        + fadeIn(fadeSpec)) togetherWith
                        (slideOutHorizontally { width -> -width }
                                + fadeOut(fadeSpec))
            } else {
                (slideInHorizontally { width -> -width }
                        + fadeIn(fadeSpec)) togetherWith
                        (slideOutHorizontally { width -> width }
                                + fadeOut(fadeSpec))
            }
        }
    ) { state ->
        content(state)
    }
}


@Composable
fun AtBox(at: Int) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.Center
    ) {
        Text(
            "$at", color = Color.Red, style = MaterialTheme.typography.headlineLarge
        )
    }
}














