package com.example.recollect.bits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.RectRulers
import androidx.compose.ui.layout.WindowInsetsRulers
import androidx.compose.ui.layout.innermostOf
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Ime() {
    var height by remember { mutableIntStateOf(250) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        val imeVisible = WindowInsets.isImeVisible
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .imePadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                height.dp.toString()+" "+imeVisible.toString()
            )
            TextField("Edit me!", {
                height-=10
            })
            Box(
                Modifier
                    .height(height.dp)
                    .background(Color(0xFF53D9A1))
                    .navigationBarsPadding()
//                   .imePadding()
                    .fillMaxWidth()
            )
            TextField("Edit me?", {
                height-=10
            })
        }
    }
}

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
            // Trigger and good behavior
            TextField(caption, {}, alignAtBottom)
        } else {
            // Trigger
            TextField("Click here!", {})
            // Bad behaviors
            val option = 2
            when (option) {
                0 -> Box(
                    alignAtBottom
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(Color(0xFF53D9A1))
                )

                1 -> Text(caption, alignAtBottom)
                2 -> Button(onClick = {}, alignAtBottom) {
                    Text(caption)
                }
            }

        }

    }
}








