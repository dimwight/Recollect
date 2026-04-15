@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recollect

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recollect.bits.getImeHeight
import kotlinx.coroutines.launch
import org.javarosa.form.api.FormEntryController

val myBlue = Color(62, 159, 208)

@Preview
@Composable
fun FormPage() {
    Box(
        modifier = Modifier.background(Color.White)
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            )
            InsertTextField()
            Box(
                Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            )
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
private fun InsertTextField() {
    val main = LocalActivity.current as Main
    val questionSpec = main.questionSpec
    val focusRequester = remember { FocusRequester() }
    TextField(
        questionSpec.textFieldState,
        Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.colors().copy(
            focusedIndicatorColor = myBlue
        ),
        labelPosition = TextFieldLabelPosition.Above(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next, showKeyboardOnFocus = true
        ),
        textStyle = scaleStyle(typography.bodySmall, 1.5),
        onKeyboardAction = { main.onNext() },
        label = {
            Column() {
                Text(
                    questionSpec.questionDef.labelInnerText,
                    style = scaleStyle(typography.bodyMedium, 1.5),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    questionSpec.questionDef.helpText,
                    style = scaleStyle(typography.bodySmall, 1.5)
                )
            }
        })
    focusRequester.requestFocus()
}

@Composable
private fun scaleStyle(src: TextStyle, by: Double): TextStyle =
    src.copy(fontSize = src.fontSize.times(by))

@Composable
private fun BackNextRow() {
    val buttonColors = ButtonColors(
        Color.White,
        myBlue,
        Color.White,
        Color.LightGray
    )
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val main = LocalActivity.current as Main
        var isBackEnabled by remember {
            mutableStateOf(
                main.event != FormEntryController.EVENT_BEGINNING_OF_FORM
            )
        }
        val scope = rememberCoroutineScope()
        OutlinedButton(
            colors = buttonColors,
            border = BorderStroke(1.dp,Color.LightGray),
//            modifier = Modifier.hoverable(),
            enabled = isBackEnabled, onClick = {
                main.onBack()
                isBackEnabled = main.event > 0
                if (true) return@OutlinedButton
                scope.launch {
                    main.getNumbers4().collect { value ->
                        val val4 = value
                        println("R1: val4 = $val4")
                        scope.launch {
                            getNumbers1().collect { value ->
                                val val14 = value + val4
                                println("R1: val14 = $val14")
                            }
                        }
                    }
                }
            }) {
            Text("<  Back",
                style = scaleStyle(typography.bodySmall, 1.2))
        }
        OutlinedButton(
            colors = buttonColors,
            border = BorderStroke(1.dp, Color.LightGray),
            onClick = {
                try {
                    main.onNext()
                } catch (e: Exception) {
                    TODO("Not yet implemented")
                } finally {
                }
                isBackEnabled = main.event > 0
                if (true) return@OutlinedButton
                scope.launch {
                    main.getNumbers4().collect { value ->
                        println("R1: value = $value")
                    }
                }
                scope.launch {
                    getNumbers1().collect { value ->
                        println("R1: value = $value")
                    }
                }
            }) {
            Text("Next  >",
                style = scaleStyle(typography.bodySmall, 1.2))
        }
    }
}
























