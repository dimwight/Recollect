package com.example.recollect

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

val myBlue = Color(62, 159, 208)

@Composable
fun HeaderRows(inputActivity: InputActivity) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        val screenState = inputActivity.screenState.collectAsState().value
        FormTitleRow(screenState)
        FlowRow(Modifier.padding(vertical = 0.dp)) {
            val labels = screenState.questionSpec.captions
                .mapTo(ArrayList<String>()) {
                it.formElement.labelInnerText
            }
            for ((at: Int, next) in labels.withIndex()) {
                if (at < labels.size - 1) Text("$next >", style = mySmallStyle())
            }
        }
        Spacer(Modifier.height(15.dp))
    }
}

@Composable
fun FormTitleRow(screenState: ScreenState) {
    Row(
        Modifier.padding(vertical = 35.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = screenState.formTitle, style = myMediumStyle(true))
    }
}

@Composable
fun ImeScreen(inputActivity: InputActivity) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        val screenState = inputActivity.screenState.collectAsState().value
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(22.dp))
            HeaderRows(inputActivity)
            val focusRequester = remember { FocusRequester() }
            QuestionTextField(focusRequester)
            LaunchedEffect(Unit) {
                delay(2000.milliseconds)
                focusRequester.requestFocus()
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    BackNextRow(screenState)
                    Spacer(Modifier.height(10.dp))
                    Box(
                        Modifier
                            .height(getImeHeight().dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    )
                    Spacer(Modifier.height(25.dp))
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
            val rectY = if (false) rect.height() else rect.bottom
            val diff = screenHeight - rectY
            val ratio = screenHeight.toFloat() / rectY
            if (false) {
                println("R1: rect = $rect")
                println("R1: screen = $screenHeight")
                println("R1: diff = $diff")
                println("R1: ratio = ${(ratio * 100).toInt()}")
            }
            height.intValue = if (ratio < 1.5) 0
            else {
                val fraction = if (false) .34 else remember*.9
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

@Composable
fun BackNextRow(screenState: ScreenState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val inputActivity = LocalActivity.current as InputActivity
        BackNextButton("<  Back", screenState.showBack) {
            inputActivity.onBack()
        }
        val scope = rememberCoroutineScope()
        BackNextButton("Next  >", screenState.showNext) {
            inputActivity.onNext()
            if (false) {
                scope.launch {
                    inputActivity.getNumbers4_().collect { value ->
                        println("R1: value = $value")
                    }
                }
                scope.launch {
                    getNumbers1_().collect { value ->
                        println("R1: value = $value")
                    }
                }
            }
        }
    }
}

@Composable
fun BackNextButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val disabled = Color.LightGray
    OutlinedButton(
        colors = ButtonColors(
            Color.White,
            myBlue,
            Color.White,
            disabled
        ),
        border = BorderStroke(1.dp, disabled),
        contentPadding = PaddingValues(55.dp, 13.dp),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            text,
            style = if (enabled) {
                mySmallStyle().copy(myBlue)
            } else {
                mySmallStyle().copy(disabled)
            }
        )
    }
}

























