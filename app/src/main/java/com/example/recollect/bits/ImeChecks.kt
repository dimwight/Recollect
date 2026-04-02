package com.example.recollect.bits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.RectRulers
import androidx.compose.ui.layout.WindowInsetsRulers
import androidx.compose.ui.layout.innermostOf
import androidx.compose.ui.unit.dp

@Composable
fun ImeCheck(insideNotPadding: Boolean) {
    val boxModifier = if (insideNotPadding)
        Modifier
            .fillMaxSize()
            .fitInside(
                RectRulers.innermostOf(
                    WindowInsetsRulers.NavigationBars.current,
                    WindowInsetsRulers.Ime.current
                )
            )
    else
        Modifier
            .fillMaxSize()

    Box(
        modifier = boxModifier
    ) {
        val alignAtBottom = if (insideNotPadding)
            Modifier
                .align(Alignment.BottomStart)
        else
            Modifier
                .align(Alignment.BottomStart)
                .navigationBarsPadding()
                .imePadding()

        val caption = if (insideNotPadding) "Inside" else "Padding"
        if (false) {
            // Trigger and good check
            TextField(caption, {}, alignAtBottom)
        } else {
            // Trigger
            TextField("Click here!", {})
            // Bad checks?
            if (false) Text(caption, alignAtBottom)
            else if (false)
                Button(onClick = {}, alignAtBottom) {
                Text(caption)
            }
            else
                Box(alignAtBottom
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color(0xFF53D9A1))
                )
        }
    }
}







