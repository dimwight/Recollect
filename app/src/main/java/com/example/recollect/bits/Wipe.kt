package com.example.recollect.bits

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recollect.timeMillis
import kotlin.random.Random
import androidx.compose.animation.core.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

data class EasingOption(
    val name: String,
    val easing: Easing
)

val AllEasings = listOf(
    EasingOption("LinearEasing", LinearEasing),
    EasingOption("FastOutSlowInEasing", FastOutSlowInEasing),
    EasingOption("LinearOutSlowInEasing", LinearOutSlowInEasing),
    EasingOption("FastOutLinearInEasing", FastOutLinearInEasing),

    EasingOption("Ease", Ease),
    EasingOption("EaseIn", EaseIn),
    EasingOption("EaseOut", EaseOut),
    EasingOption("EaseInOut", EaseInOut),

    EasingOption("EaseInSine", EaseInSine),
    EasingOption("EaseOutSine", EaseOutSine),
    EasingOption("EaseInOutSine", EaseInOutSine),

    EasingOption("EaseInQuad", EaseInQuad),
    EasingOption("EaseOutQuad", EaseOutQuad),
    EasingOption("EaseInOutQuad", EaseInOutQuad),

    EasingOption("EaseInCubic", EaseInCubic),
    EasingOption("EaseOutCubic", EaseOutCubic),
    EasingOption("EaseInOutCubic", EaseInOutCubic),

    EasingOption("EaseInQuart", EaseInQuart),
    EasingOption("EaseOutQuart", EaseOutQuart),
    EasingOption("EaseInOutQuart", EaseInOutQuart),

    EasingOption("EaseInQuint", EaseInQuint),
    EasingOption("EaseOutQuint", EaseOutQuint),
    EasingOption("EaseInOutQuint", EaseInOutQuint),

    EasingOption("EaseInExpo", EaseInExpo),
    EasingOption("EaseOutExpo", EaseOutExpo),
    EasingOption("EaseInOutExpo", EaseInOutExpo),

    EasingOption("EaseInCirc", EaseInCirc),
    EasingOption("EaseOutCirc", EaseOutCirc),
    EasingOption("EaseInOutCirc", EaseInOutCirc),

    EasingOption("EaseInBack", EaseInBack),
    EasingOption("EaseOutBack", EaseOutBack),
    EasingOption("EaseInOutBack", EaseInOutBack),

    EasingOption("EaseInElastic", EaseInElastic),
    EasingOption("EaseOutElastic", EaseOutElastic),
    EasingOption("EaseInOutElastic", EaseInOutElastic),

    EasingOption("EaseInBounce", EaseInBounce),
    EasingOption("EaseOutBounce", EaseOutBounce),
    EasingOption("EaseInOutBounce", EaseInOutBounce),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasingPicker(
    selectedAt: Int,
    onSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        val selected = AllEasings[selectedAt]
        OutlinedTextField(
            value = selected.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Easing") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            AllEasings.forEachIndexed { at, option ->
                DropdownMenuItem(
                    text = { Text(option.name) },
                    onClick = {
                        onSelected(at)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun WipeDemoScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Spacer(Modifier.height(50.dp))
        var selectedEasing by remember {
            mutableStateOf(AllEasings.first())
        }
        var selectedAt by remember { mutableIntStateOf(0) }
        val selectEasing = AllEasings[selectedAt]

        val scope = rememberCoroutineScope()
        var wipeState by remember { mutableIntStateOf(0) }

        EasingPicker(
            selectedAt = selectedAt,
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
                text = AllEasings[selectedAt].name,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Button(
                enabled = selectedAt < AllEasings.lastIndex,
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
                            AllEasings.lastIndex
                        else
                            selectedAt - 1
                }
            ) {
                Text("Previous")
            }

            Button(
                onClick = {
                    selectedAt =
                        if (selectedAt == AllEasings.lastIndex)
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
                    easing = AllEasings[selectedAt].easing
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
fun CustomWipeContainer(
    targetState: Int,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    // Define shared animation specs for perfect synchronization
    val duration = 600 // Time in milliseconds
    val curve = FastOutSlowInEasing // Natural deceleration curve

    AnimatedContent(
        targetState = targetState,
        modifier = modifier,
        transitionSpec = {
            if (targetState > initialState) {
                // Moving forward: Slide right-to-left
                val enterTransition = slideInHorizontally(
                    animationSpec = tween(durationMillis = duration, easing = curve)
                ) { width -> width } + fadeIn(
                    animationSpec = tween(durationMillis = duration)
                )

                val exitTransition = slideOutHorizontally(
                    animationSpec = tween(durationMillis = duration, easing = curve)
                ) { width -> -width } + fadeOut(
                    animationSpec = tween(durationMillis = duration)
                )

                enterTransition.togetherWith(exitTransition)
            } else {
                // Moving backward: Slide left-to-right
                val enterTransition = slideInHorizontally(
                    animationSpec = tween(durationMillis = duration, easing = curve)
                ) { width -> -width } + fadeIn(
                    animationSpec = tween(durationMillis = duration)
                )

                val exitTransition = slideOutHorizontally(
                    animationSpec = tween(durationMillis = duration, easing = curve)
                ) { width -> width } + fadeOut(
                    animationSpec = tween(durationMillis = duration)
                )

                enterTransition.togetherWith(exitTransition)
            }
        }
    ) { state ->
        content(state)
    }
}

@Composable
fun AnimateIncrementDecrement(
    count: Int,
    content: @Composable (Int) -> Unit
) {
    AnimatedContent(
        targetState = count,
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            } else {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            }.using(SizeTransform(clip = false))
        },
    ) { state ->
        content(state)
    }
}

@Composable
fun AnimateIncrementDecrementSample() {
    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        var count by remember { mutableIntStateOf(0) }
        // The `AnimatedContent` below uses an integer count as its target state. So when the
        // count changes, it will animate out the content associated with the previous count, and
        // animate in the content associated with the target state.
        AnimatedContent(
            targetState = count,
            transitionSpec = {
                // We can define how the new target content comes in and how initial content
                // leaves in the ContentTransform. Here we want to create the impression that the
                // different numbers have a spatial relationship - larger numbers are
                // positioned (Horizontally) below smaller numbers.
                if (targetState > initialState) {
                    // If the incoming number is larger, new number slides up and fades in while
                    // the previous (smaller) number slides up to make room and fades out.
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                } else {
                    // If the incoming number is smaller, new number slides down and fades in
                    // while
                    // the previous number slides down and fades out.
                    slideInHorizontally { -it } + fadeIn() togetherWith
                            slideOutHorizontally { it } + fadeOut()
                    // Disable clipping since the faded slide-out is desired out of bounds, but
                    // the size transform is still needed from number getting longer
                }
                    .using(SizeTransform(clip = false)) // Using default spring for the size change.
            },
        ) { targetCount ->
            // This establishes a mapping between the target state and the content in the form of a
            // Composable function. IMPORTANT: The parameter of this content lambda should
            // *always* be used. During the content transform, the old content will be looked up
            // using this lambda with the old state, until it's fully animated out.

            // Since AnimatedContent differentiates the contents using their target states as the
            // key, the same composable function returned by the content lambda like below will be
            // invoked under different keys and therefore treated as different entities.
            Text("$targetCount", fontSize = 20.sp)
        }
        Spacer(Modifier.size(20.dp))
        Row(horizontalArrangement = Arrangement.SpaceAround) {
            Button(onClick = { count-- }) { Text("Minus") }
            Spacer(Modifier.size(60.dp))
            Button(onClick = { count++ }) { Text("Plus ") }
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
fun FadeInExample() {
    var isVisible by remember { mutableStateOf(false) }

    Column {
        Button(onClick = { isVisible = !isVisible }) {
            Text("Toggle Fade")
        }

        // AnimatedVisibility manages the appearance and disappearance
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 1000)),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000))
        ) {
            Text(text = "Hello, I faded in!")
        }
    }
}














