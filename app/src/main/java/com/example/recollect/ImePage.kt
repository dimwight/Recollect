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
import kotlinx.coroutines.launch

val myBlue = Color(62, 159, 208)

@Composable
private fun HeaderRows() {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        val question = (LocalActivity.current as InputActivity)
            .pageState.collectAsState().value.questionSpec
        Row(
            Modifier.padding(vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = question.formTitle, style = myMediumStyle(true))
        }
        Spacer(Modifier.height(5.dp))
        FlowRow(Modifier.padding(vertical = 0.dp)) {
            val labels = question.captions.mapTo(ArrayList<String>()) {
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
fun ImePage() {
   val activity = LocalActivity.current as InputActivity
    val atFormEnd = activity.pageState.collectAsState().value.atFormEnd
    if (atFormEnd){
        BackNextButton("Go") {
            activity.setFormEnd(false)
        }
        return
    }
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(Modifier.height(22.dp))
            HeaderRows()
            val focusRequester = remember { FocusRequester() }
            QuestionTextField(focusRequester)
            focusRequester.requestFocus()
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    BackNextRow()
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
fun BackNextRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val inputActivity = LocalActivity.current as InputActivity
        BackNextButton("<  Back",
            inputActivity.pageState.collectAsState().value.isBackEnabled
        ) {
            inputActivity.onBack()
        }
        val scope = rememberCoroutineScope()
        BackNextButton("Next  >") {
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
private fun BackNextButton(
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

























