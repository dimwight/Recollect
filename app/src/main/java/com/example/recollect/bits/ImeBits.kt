package com.example.recollect.bits

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp



@Preview
@Composable
fun Pad() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .height(50.dp)
                    .imePadding()
                    .background(Color.Cyan)
                    .fillMaxWidth()
            )
            TextField("Edit me!", {})
            Box(
                Modifier
                    .height(50.dp)
                    .background(Color.Cyan)
                    .imePadding()
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .imePadding()
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .imePadding()
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        Modifier
                            .height(300.dp)
                            .imePadding()
                            .background(Color.Blue)
                            .fillMaxWidth()
                    )
                    Box(
                        Modifier
                            .height(getImeHeight().dp)
                            .background(Color.Red)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun getImeHeight(): Int {
    val view = LocalView.current
    val observer = view.viewTreeObserver
    val height = remember { mutableIntStateOf(1) }
    val pxToDp = with(LocalDensity.current) { 1.0 / (1.dp.toPx()) }
    val remember = remember { pxToDp }
    DisposableEffect(observer) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val screenHeight = view.rootView.height
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            println("R1: rect = $rect")
            val rectY = if (false) rect.height() else rect.bottom
            val diff = screenHeight - rectY
            val ratio = screenHeight.toFloat() / rectY
            println("R1: screen = $screenHeight")
            println("R1: diff = $diff")
            println("R1: ratio = ${(ratio * 100).toInt()}")
            height.intValue = if (ratio < 1.5) 0
            else {
                val fraction =if (true) .34 else remember
                (diff * fraction).toInt()
            }
        }
        observer.addOnGlobalLayoutListener(listener)

        onDispose {
            observer.removeOnGlobalLayoutListener(listener)
        }
    }

    return height.intValue
}







