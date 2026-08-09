package com.example.recollect.bits

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun DragDirectionDetector() {
    var actionText by remember { mutableStateOf("Swipe me!") }

    var dragAccumulator by remember { mutableStateOf(Offset.Zero) }
    var startMillis by remember { mutableLongStateOf(0) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(400.dp)
                .background(Color.DarkGray)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            println("R1: onDragStart")
                            dragAccumulator = Offset.Zero
                            startMillis= System.currentTimeMillis()
                        },
                        onDrag = { change, dragAmount ->
                            println("R1: onDrag")
                            change.consume()
                            dragAccumulator += dragAmount
                        },
                        onDragEnd = {
                            val dragMillis = System.currentTimeMillis()-startMillis
                            if (dragMillis>1000){
                                actionText= "Drag took too long"
//                                actionText= "Drag ${dragMillis}ms too long"
                                return@detectDragGestures
                            }
                            println("R1: onDragEnd")
                            val minAbs = 250f
                            val totalX = dragAccumulator.x
                            val totalY = dragAccumulator.y

                            val absX = abs(totalX)
                            val absY = abs(totalY)
                            if (absX > absY) {
                                if (absX > minAbs) {
                                    actionText =
                                        if (totalX > 0) "Triggered: RIGHT" else "Triggered: LEFT"
                                }else {
                                    actionText="Below threshold"
                                }
                            } else {
                                if (absY > minAbs) {
                                    actionText = "Bad swipe axis!"
                                } else {
                                   actionText="Below threshold"
                                }
                            }
                        },
                        onDragCancel = {
                            // Handle cases where the gesture is interrupted (e.g. system dialog)
                            println("R1: onDragCancel")
                            actionText = "onDragCancel"
                            dragAccumulator = Offset.Zero
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = actionText, color = Color.White)
        }
    }
}
