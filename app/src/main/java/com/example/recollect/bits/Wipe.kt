package com.example.recollect.bits

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.recollect.timeMillis
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

private const val scrollJump = 5

private fun adjustPicksWithWipe(
    selected: EasingOption,
    scope: CoroutineScope
) {
    if (picks.contains(selected))
        picks.remove(selected)
    picks.add(0, selected)
    scope.launch {
        delay(100.milliseconds)
        wipeEasing()
    }
}

private fun wipeEasing() {
    wipeState.intValue = if (wipeState.intValue == 1) 0 else 1
}

private fun wipeEasing_() {
    if (Random.nextFloat() < .5)
        wipeState.intValue++
    else
        wipeState.intValue--
}

private val wipeState = mutableIntStateOf(0)

@Preview
@Composable
fun EasingsViewer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        var gettingValues by remember { mutableStateOf(true) }
        var easingAt by remember { mutableIntStateOf(-1) }
        var scrollAt by remember { mutableIntStateOf(0) }
        Spacer(Modifier.height(50.dp))
        val easingSet = easingAt >= 0
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.width(10.dp))
            val scope = rememberCoroutineScope()
            EasingPicker(
                list = Easings,
                gettingValues = gettingValues,
                scrollAt = scrollAt,
                easingAt = easingAt
            ) { listAt ->
                easingAt = listAt
                adjustPicksWithWipe(Easings[easingAt], scope)
            }
            if (gettingValues) {
                LaunchedEffect(gettingValues) {
                    gettingValues = false
                }
                return
            }

            Spacer(Modifier.width(10.dp))
            val lastEasing = Easings.lastIndex
            PicksCol(
                easingAt = easingAt,
                scrollAt = scrollAt,
                onUpClick = {
                    scrollAt -= min(scrollJump, scrollAt)
                },
                onDownClick = {
                    scrollAt += min(scrollJump, lastEasing - scrollAt)
                },
                onBackClick = {
                    easingAt--
                    if (easingAt < scrollAt)
                        scrollAt--
                    adjustPicksWithWipe(Easings[easingAt], scope)
                },
                onNextClick = {
                    easingAt++
                    if (easingAt - getPickerRows() >= scrollAt)
                        scrollAt++
                    adjustPicksWithWipe(Easings[easingAt], scope)
                },
                onClearClick = {
                    picks.clear()
                    easingAt = -1
                },
                onSelected = { listAt ->
                    easingAt = Easings.indexOf(picks[listAt])
                    if (easingAt < scrollAt)
                        scrollAt = easingAt
                    else {
                        val fromScroll = easingAt - getPickerRows()
                        if (fromScroll > scrollAt)
                            scrollAt += fromScroll
                    }
                    adjustPicksWithWipe(Easings[easingAt], scope)
                },
            )
            Spacer(Modifier.width(10.dp))
        }

        AnimatedContent(
            targetState = wipeState.intValue,
            transitionSpec = {
                val slideTween = tween<IntOffset>(
                    durationMillis = 1500,
                    easing = Easings[
                        if (easingAt < 0) 0 else easingAt
                    ].easing
                )
                if (targetState > initialState) {
                    slideInHorizontally(slideTween) { it } togetherWith
                            slideOutHorizontally(slideTween) { -it }
                } else {
                    slideInHorizontally(slideTween) { -it } togetherWith
                            slideOutHorizontally(slideTween) { it }
                }
            }
        ) { at -> AtBox(at) }
    }
}

@Composable
private fun PicksCol(
    easingAt: Int,
    scrollAt: Int,
    onUpClick: () -> Unit,
    onDownClick: () -> Unit,
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
    onClearClick: () -> Unit,
    saveOnClick: () -> Unit = {},
    onSelected: (Int) -> Unit,
) {
    Column {
        EasingPicker(
            list = picks,
            easingAt = easingAt,
            onSelected = onSelected
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                enabled = picks.isNotEmpty(),
                onClick = onClearClick
            ) { Text("Clear") }

            if (false) Button(
                enabled = picks.size > 5,
                onClick = saveOnClick
            ) { Text("Save") }
        }
        Spacer(Modifier.height(20.dp))
        val lastEasing = Easings.lastIndex
        val easingSet = easingAt>=0
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                enabled = scrollAt > 0,
                onClick = onUpClick
            ) { Text("Up") }
            Button(
                enabled = scrollAt + getPickerRows() <= lastEasing,
                onClick = onDownClick
            ) { Text("Down") }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                enabled = easingSet,
                onClick = onBackClick
            ) { Text("Back") }
            Button(
                enabled = easingSet && easingAt < lastEasing,
                onClick = onNextClick
            ) { Text("Next") }
        }
        Spacer(Modifier.height(20.dp))
        Row {
            Button(
                enabled = easingSet,
                onClick = { wipeEasing() }
            ) {
                Text("Wipe")
            }
        }
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
        var wipeAt by remember { mutableIntStateOf(0) }

        EasingPicker(
            list = Easings,
            scrollAt = 0,
            easingAt = selectedAt,
            onSelected = {
                selectedAt = it
                timeMillis("click")
                scope.launch {
                    delay(500.milliseconds)
                    if (Random.nextFloat() < .5)
                        wipeAt++
                    else
                        wipeAt--
                }
            },
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
                wipeAt++
            else
                wipeAt--
        }) {
            Text("Wipe")
        }

        AnimatedContent(
            targetState = wipeAt,
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
        ) { at -> AtBox(at) }
    }
}














