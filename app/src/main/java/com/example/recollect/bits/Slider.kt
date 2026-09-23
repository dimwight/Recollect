package com.example.recollect.bits

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun LogSlider(
    minValue: Float = 1f,
    maxValue: Float = 10000f,
    initialValue: Float = 100f,
    onValueChanged: (Float) -> Unit = {}
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












