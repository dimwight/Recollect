package com.example.recollect.bits

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DurationSlider(
    enabled: Boolean,
    durationThen: Int,
    updateDuration: ((Int) -> Unit),
) {
    val dpToPx = with(LocalDensity.current) { 1.dp.toPx() }
    val pxToDp = 1.0 / dpToPx
    var useWidth by remember { mutableIntStateOf(0) }
    Column(
        Modifier.onSizeChanged {
            useWidth = (it.width * pxToDp).roundToInt()
        }
    ) {
        Spacer(Modifier.height(10.dp))
        Text(
            "Duration",
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        var step by remember { mutableFloatStateOf(50f) }
        var durationNow by remember { mutableIntStateOf(durationThen) }
        Slider(
            enabled = enabled,
            modifier = Modifier
                .width((useWidth - 15).dp)
                .height(30.dp),
            value = step,
            onValueChange = {
                step = it
                durationNow = when (step) {
                    0f -> 200
                    25f -> 500
                    50f -> 1000
                    75f -> 1500
                    100f -> 2000
                    else -> 1000
                }
                updateDuration(durationNow)
            },
            valueRange = 0f..100f,
            steps = 3 // results in 5 positions: 0, 25, 50, 75, 100
        )
        Row(
            modifier = Modifier.height(25.dp)
        ) {
            val labels = listOf<String>("200", "500", "1000", "1500", "2000")
            val useWidth_ = useWidth
            // 173 276 354 391
            // 171 271 340
            labels.forEachIndexed { index, s: String ->
                Text(s)
                val gap = useWidth / labels.lastIndex * .6 - (s.length * 3 / 2)
                Spacer(Modifier.width(gap.dp))
            }
        }
    }

}













