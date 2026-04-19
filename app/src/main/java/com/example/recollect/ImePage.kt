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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.javarosa.form.api.FormEntryController

val myBlue = Color(62, 159, 208)

@Composable
private fun HeaderRows() {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
    ) {
        val spec = (LocalActivity.current as FormControl).questionSpec
        Row(
            Modifier.height(65.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(spec.formDef.title, style = myMediumStyle(true))
        }
        FlowRow(
            Modifier.height(50.dp),
//            verticalArrangement = Arrangement.Center
        ) {
            val labels = spec.captions.mapTo(ArrayList<String>()) {
                it.formElement.labelInnerText
            }
            for ((at: Int, next) in labels.withIndex()) {
                if (at < labels.size - 1) Text("$next >", style = mySmallStyle())
            }
        }
    }
}

@Composable
fun ImePage() {
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
            HeaderRows()
            val focusRequester = remember { FocusRequester() }
            FocusedTextField(focusRequester)
            focusRequester.requestFocus()
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    BackNextRow()
                    Box(
                        Modifier
                            .height(getImeHeight().dp)
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
                val fraction = if (true) .34 else remember
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
        val formControl = LocalActivity.current as FormControl
        var isBackEnabled by remember {
            mutableStateOf(
                formControl.event != FormEntryController.EVENT_BEGINNING_OF_FORM
            )
        }
        val scope = rememberCoroutineScope()
        var onClick: () -> Unit = {
            formControl.onBack()
            isBackEnabled = formControl.event > 0
            if (false) scope.launch {
                formControl.getNumbers4_().collect { value ->
                    val val4 = value
                    println("R1: val4 = $val4")
                    scope.launch {
                        getNumbers1_().collect { value ->
                            val val14 = value + val4
                            println("R1: val14 = $val14")
                        }
                    }
                }
            }
        }
        BackNextButton("<  Back", isBackEnabled, onClick)
        var onClick1: () -> Unit = {
            formControl.onNext()
            isBackEnabled = formControl.event > 0
            if (false) {
                scope.launch {
                    formControl.getNumbers4_().collect { value ->
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
        BackNextButton("Next  >", isBackEnabled, onClick1)
    }
}

@Composable
private fun BackNextButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        colors = ButtonColors(
            Color.White,
            myBlue,
            Color.White,
            Color.LightGray
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        contentPadding = PaddingValues(50.dp, 15.dp),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            text,
            style = if (enabled) {
                mySmallStyle().copy(myBlue)
            } else {
                mySmallStyle()
            }
        )
    }
}

























