package com.example.recollect.bits

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    // Tracks the total accumulated distance during a single drag session
    var dragAccumulator by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.DarkGray)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            // Reset accumulator when a new touch begins
                            dragAccumulator = Offset.Zero
                        },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            // Add up horizontal and vertical changes
                            dragAccumulator += dragAmount
                        },
                        onDragEnd = {
                            // 1. Minimum threshold to avoid accidental micro-drags (e.g. 50 pixels)
                            val threshold = 50f
                            val totalX = dragAccumulator.x
                            val totalY = dragAccumulator.y

                            // 2. Evaluate direction based on which axis has a larger movement
                            if (abs(totalX) > abs(totalY)) {
                                // Horizontal movement was dominant
                                if (abs(totalX) > threshold) {
                                    actionText = if (totalX > 0) "Triggered: RIGHT" else "Triggered: LEFT"
                                }
                            } else {
                                // Vertical movement was dominant
                                if (abs(totalY) > threshold) {
                                    actionText = if (totalY > 0) "Triggered: DOWN" else "Triggered: UP"
                                }
                            }
                        },
                        onDragCancel = {
                            // Handle cases where the gesture is interrupted (e.g. system dialog)
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
