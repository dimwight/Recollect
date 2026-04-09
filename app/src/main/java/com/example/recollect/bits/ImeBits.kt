package com.example.recollect.bits

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.RectRulers
import androidx.compose.ui.layout.WindowInsetsRulers
import androidx.compose.ui.layout.innermostOf
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp


private const val atTop = false

@Composable
fun Pad() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .my()
    ) {
        if (atTop) EditMe()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Bottom
        ) {
            if (!atTop) EditMe()
            Box(
                Modifier
                    .height(250.dp)
                    .background(colorBlue)
                    .navigationBarsPadding()
                    .fillMaxWidth()
            )
            Box(
                Modifier
                    .height(getImeHeight().dp)
                    .background(Color.Red)
                    .navigationBarsPadding()
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun Modifier.imeDp(imeHeight: Int): Modifier {
    val fraction =if (atTop) .4 else .31
    var heightOut = (imeHeight * fraction).toInt()
    return this then Modifier.height(heightOut.dp)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Modifier.my(): Modifier {
    println("R1: imeHeight = ${getImeHeight()}")
    return this //then Modifier.imePadding()
}

@Composable
private fun EditMe() {
    TextField("Edit me!", {})
}

@Composable
fun getImeHeight(): Int {
    val view = LocalView.current
    val observer = view.viewTreeObserver
    val height = remember { mutableIntStateOf(1) }
    DisposableEffect(observer) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val screenHeight = view.rootView.height
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val diff = screenHeight - rect.bottom
            val ratio = screenHeight.toFloat() / rect.bottom
            println("R1: screen = $screenHeight")
            println("R1: rect = ${rect.bottom}")
            println("R1: diff = $diff")
//            println("R1: rect = ${rect.height()}")
            println("R1: ratio = ${(ratio * 100).toInt()}")
            height.intValue = if (ratio < 1.5) 0
            else {
                val fraction =if (atTop) .4 else .31
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

enum class KeyboardState {
    Opened, Closed
}

@Composable
fun keyboardAsState(): MutableState<KeyboardState> {
    val keyboardState = remember { mutableStateOf(KeyboardState.Closed) }
    val view = LocalView.current

    val viewTreeObserver = view.viewTreeObserver

    DisposableEffect(viewTreeObserver) {
        val onGlobalListener = ViewTreeObserver.OnGlobalLayoutListener {
            val screenHeight = view.rootView.height
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val keypadHeight = screenHeight - rect.bottom
            keyboardState.value = if (keypadHeight > screenHeight * 0.15) {
                KeyboardState.Opened
            } else {
                KeyboardState.Closed
            }
        }
        viewTreeObserver.addOnGlobalLayoutListener(onGlobalListener)

        onDispose {
            viewTreeObserver.removeOnGlobalLayoutListener(onGlobalListener)
        }
    }

    return keyboardState
}

@Composable
fun Ime() {
    var height by remember { mutableIntStateOf(250) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .my()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
//                .imePadding()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                height.dp.toString()
            )
            TextField("Edit me!", {
                height -= 10
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
                height -= 10
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

@Composable
fun Show() {
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .imePadding()
    ) {
        var show by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Button(onClick = { show = !show }) { Text(show.toString()) }
            TextField(
                "Edit me!", {}, Modifier
                    .onFocusChanged { if (it.isFocused) show = true },
                interactionSource = remember { MutableInteractionSource() }
                    .also { interactionSource ->
                        LaunchedEffect(interactionSource) {
                            interactionSource.interactions.collect {
                                if (it is PressInteraction.Release) {
                                    show = true
                                }
                            }
                        }
                    }

            )
            Box(
                Modifier
                    .height(if (show) 250.dp else 0.dp)
                    .background(colorGreen)
                    .navigationBarsPadding()
                    .fillMaxWidth()
            )
            Box(
                Modifier
                    .height(550.dp)
                    .background(colorBlue)
                    .navigationBarsPadding()
                    .fillMaxWidth()
            )
        }
    }
}







