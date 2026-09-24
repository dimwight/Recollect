package com.example.recollect.bits

import androidx.compose.animation.core.tween
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

enum class SliderDetent {
    Min,
    Quarter,
    Half,
    ThreeQuarters,
    Max
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetentSlider(
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    BoxWithConstraints(modifier.fillMaxWidth()) {
        val widthPx = with(density) { maxWidth.toPx() }

        val anchors = remember(widthPx) {
            DraggableAnchors {
                SliderDetent.Min at 0f
                SliderDetent.Quarter at widthPx * 0.25f
                SliderDetent.Half at widthPx * 0.5f
                SliderDetent.ThreeQuarters at widthPx * 0.75f
                SliderDetent.Max at widthPx
            }
        }

        val state: AnchoredDraggableState<SliderDetent> = remember {
            AnchoredDraggableState(
                initialValue = SliderDetent.Half,
                positionalThreshold = { distance -> distance * 0.5f },
                velocityThreshold = { with(density) { 100.dp.toPx() } },
                snapAnimationSpec = tween(),
                decayAnimationSpec = splineBasedDecay(density)
            )
        }

        LaunchedEffect(anchors) {
            state.updateAnchors(anchors)
        }

        Box(
            Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            // Track
            Box(
                Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(Color.LightGray)
            )

            val thumbOffset =
                if (state.offset.isNaN()) 0f
                else state.requireOffset()

            // Thumb
            Box(
                Modifier
                    .offset {
                        IntOffset(
                            x = thumbOffset.roundToInt(),
                            y = 0
                        )
                    }
                    .size(32.dp)
                    .background(
                        Color.Blue,
                        CircleShape
                    )
                    .anchoredDraggable(
                        state = state,
                        orientation = Orientation.Horizontal
                    )
            )
        }

        Text(
            text = "Selected: ${state.currentValue}",
            modifier = Modifier.padding(top = 56.dp)
        )
    }
}

@Composable
fun LogSlider(
    enabled: Boolean,
    minValue: Float = 1f,
    maxValue: Float = 10000f,
    initialValue: Float = 100f,
    onValueChanged: (Float) -> Unit = {},
) {
    val minLog = log10(minValue)
    val maxLog = log10(maxValue)

    var sliderPosition by remember {
        mutableFloatStateOf(
            ((log10(initialValue) - minLog) / (maxLog - minLog))
                .coerceIn(0f, 1f)
        )
    }

    val actualValue = 10f.pow(
        minLog + sliderPosition * (maxLog - minLog)
    )

    Column {
        Text(
            text = "Value: ${actualValue.roundToInt()}"
        )

        Slider(
            enabled = enabled,
            value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                onValueChanged(
                    10f.pow(
                        minLog + it * (maxLog - minLog)
                    )
                )
            },
            valueRange = 0f..1f
        )
    }
}












