package com.example.recollect.bits

import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInBack
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInExpo
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutBack
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.EaseInOutCirc
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.EaseInOutElastic
import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.EaseInOutQuint
import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseInQuad
import androidx.compose.animation.core.EaseInQuart
import androidx.compose.animation.core.EaseInQuint
import androidx.compose.animation.core.EaseInSine
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutBounce
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.EaseOutElastic
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.EaseOutQuad
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.EaseOutQuint
import androidx.compose.animation.core.EaseOutSine
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

data class EasingOption(
    val name: String,
    val easing: Easing
)

val Easings: List<EasingOption>
    get() = listOf(
        EasingOption("Linear....", LinearEasing),
        EasingOption("FastOutSlowIn....", FastOutSlowInEasing),
        EasingOption("LinearOutSlowIn....", LinearOutSlowInEasing),
        EasingOption("FastOutLinearIn....", FastOutLinearInEasing),

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
val picks: MutableList<EasingOption> = mutableListOf()

var itemHeightPx = 38
var pickerHeightPx = 0
var pickerHeightDp = 450
fun getPickerRows(): Int {
    val rows = pickerHeightPx / itemHeightPx
    return rows
}

@Composable
fun EasingPicker(
    list: List<EasingOption>,
    gettingValues: Boolean = false,
    scrollAt: Int = 0,
    easingAt: Int,
    onSelected: (Int) -> Unit,
) {
    val scrollState = rememberScrollState()

    if (list == Easings && !gettingValues) {
        LaunchedEffect(scrollAt) {
//            println("R1: value = ${scrollState.value}")
//            println("R1: scrollAt = $scrollAt")
            scrollState.scrollTo(scrollAt * itemHeightPx)
//            println("R1: value~ = ${scrollState.value}")
        }
    }

    val dpToPx = with(LocalDensity.current) { 1.dp.toPx() }
    val pxToDp = 1.0 / dpToPx
    Column(
        modifier = Modifier
            .then(
                if (gettingValues)
                    Modifier.onSizeChanged {
                        pickerHeightPx = it.height
                        pickerHeightDp = (pickerHeightPx*pxToDp).roundToInt()
                    } else Modifier.Companion
            )
            .width(170.dp)
            .border(
                width = 2.dp,
                color = Color.LightGray,
            )
//            .heightIn(max = pickerHeightDp.dp/(if (list == Easings) 1 else 3))
            .requiredHeight((pickerHeightDp/if (list == Easings) 1 else 3).dp)
            .verticalScroll(scrollState)
    ) {
        list.forEachIndexed { at, easing ->
            Text(
                text = if (false) "$at " else "" + easing.name,
                modifier = Modifier
                    .then(
                        if (gettingValues && at == 0)
                            Modifier.onSizeChanged {
                                itemHeightPx = it.height
                            }
                        else
                            Modifier.Companion
                    )
                    .background(
                        if (list == Easings && easingAt >= 0 &&
                            easing == list[easingAt]
                        )
                            Color.Gray else Color.White
                    )
                    .fillMaxWidth()
                    .clickable {
                        onSelected(at)
                    }
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}