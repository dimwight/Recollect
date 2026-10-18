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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.time.Duration.Companion.milliseconds

private const val scrollJump = 5

private fun adjustPicksWithWipe(
    selected: EasingOption,
    scope: CoroutineScope
) {
    val picks_ = picks
    if (picks_.contains(selected))
        picks_.remove(selected)
    picks_.add(0, selected)
    scope.launch {
        delay(100.milliseconds)
        wipeEasing()
    }
}

private fun wipeEasing() {
    wipeState.intValue = if (wipeState.intValue == 1) 0 else 1
}

private val wipeState = mutableIntStateOf(0)
var scrollTo = mutableIntStateOf(-1)

@Preview
@Composable
fun WipeViewer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        var firstPass by remember { mutableStateOf(true) }
//        var gettingValues by remember { mutableStateOf(!firstPass) }
        var easingAt by remember { mutableIntStateOf(-1) }
        var durationMillis by remember { mutableIntStateOf(1000) }
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
                getValues = firstPass,
                easingAt = easingAt,
                onSelected = { listAt ->
                    easingAt = listAt
                    adjustPicksWithWipe(Easings[easingAt], scope)
                })
            if (firstPass) {
                if (true) LaunchedEffect(firstPass) {// No effect?
                    var then = scrollTo.intValue
                    scrollTo.intValue=0
                }
            firstPass = false
            }

            Spacer(Modifier.width(10.dp))
            PicksCol(
                easingAt = easingAt,
                onUp = {
                    scrollTo.intValue -= min(scrollJump, scrollTo.intValue)
                },
                onDown = {
                    val jump = scrollJump
                    val lastIndex = Easings.lastIndex
                    val then = scrollTo.intValue
                    val now = then + min(jump, lastIndex - then)
                    scrollTo.intValue = now
                },
                onBack = {
                    easingAt--
                    if (easingAt < scrollTo.intValue)
                        scrollTo.intValue = easingAt--
                    adjustPicksWithWipe(Easings[easingAt], scope)
                },
                onNext = {
                    easingAt++
                    if (easingAt - getPickerRows() >= scrollTo.intValue)
                        scrollTo.intValue++
                    adjustPicksWithWipe(Easings[easingAt], scope)
                },
                onClear = {
                    picks.clear()
                    easingAt = -1
                    scrollTo.intValue = 0
                },
                onSelected = { listAt ->
                    easingAt = Easings.indexOf(picks[listAt])
                    if (false || easingAt < scrollTo.intValue)
                        scrollTo.intValue = easingAt
                    else {
                        val fromScroll = easingAt - getPickerRows()
                        if (false || fromScroll > scrollTo.intValue)
                            scrollTo.intValue += fromScroll
                    }
                    println("R1: \rlistAt = ${listAt}" +
                            " \reasingAt = ${easingAt}" +
                            " \rscrollTo = ${scrollTo.intValue}")
                    adjustPicksWithWipe(Easings[easingAt], scope)
                }
            )
            Spacer(Modifier.width(10.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(Modifier.width(20.dp))
            DurationSlider(
                true || easingSet,
                durationMillis,
            ) {
                durationMillis = it
                wipeEasing()
            }
        }

        AnimatedContent(
            targetState = wipeState.intValue,
            transitionSpec = {
                val slideTween = tween<IntOffset>(
                    durationMillis = durationMillis,
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
        ) { at ->
            AtBox(at)
        }
    }
}

@Composable
private fun PicksCol(
    easingAt: Int,
    onUp: () -> Unit,
    onDown: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onClear: () -> Unit,
    saveOn: () -> Unit = {},
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
                onClick = onClear
            ) { Text("Clear") }

            if (false) Button(
                enabled = picks.size > 5,
                onClick = saveOn
            ) { Text("Save") }
        }
        Spacer(Modifier.height(20.dp))
        val lastEasing = Easings.lastIndex
        val easingSet = easingAt >= 0
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                enabled = scrollTo.intValue > 0,
                onClick = onUp
            ) { Text("Up") }
            Button(
                enabled = scrollTo.intValue + getPickerRows() < lastEasing,
//                enabled = false,
                onClick = onDown
            ) {
                Column {
                    Text("Down")
                    Text("scrollTo: ${scrollTo.intValue}")
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                enabled = easingSet,
                onClick = onBack
            ) { Text("Back") }
            Button(
                enabled = easingSet && easingAt < lastEasing,
                onClick = onNext
            ) { Text("Next") }
        }
        Spacer(Modifier.height(20.dp))
        Row {
            Button(
                enabled = true || easingSet,
                onClick = {
                    wipeEasing()
                }
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
            "W$at", color = Color.Red, style = MaterialTheme.typography.headlineLarge
        )
    }
}















