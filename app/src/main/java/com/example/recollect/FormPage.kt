@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.recollect

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.recollect.bits.getImeHeight
import kotlinx.coroutines.launch
import org.javarosa.form.api.FormEntryController


@ExperimentalMaterial3Api
@Composable
fun FormPage() {
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
//                    .background(Color.Cyan)
                    .fillMaxWidth()
            )
            val main = LocalActivity.current as Main
            val questionSpec = main.questionSpec
            OutlinedTextField(
                questionSpec.textFieldState,
                labelPosition = TextFieldLabelPosition.Above(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    showKeyboardOnFocus = true
                ),
                onKeyboardAction = { main.onNext() },
                label = { Text(questionSpec.questionDef?.labelInnerText?:"") })
            Box(
                Modifier
                    .height(50.dp)
//                    .background(Color.Cyan)
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
                    BackNextRow()
                    Box(
                        Modifier
                            .height(getImeHeight().dp)
//                            .background(Color.Red)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}
@Composable
fun FormPage_(questionSpec: QuestionSpec) {
    val my = false
    if (my) Surface(){
        TopBar()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier
                .fillMaxWidth()
                .height(18.dp))
            TextField(value = "Testing...", onValueChange = {})
        }
        BottomBar()
    }
    else Scaffold(
        modifier = Modifier,
        topBar = { TopBar(textTop) },
        bottomBar = { BottomBar() }
    ) { Content(it, questionSpec) }
}

private const val textTop = "Top app bar"

@ExperimentalMaterial3Api
@Composable
fun TopBar(label: String = textTop) {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(label)
        }
    )
}

@Composable
fun Content(innerPadding: PaddingValues, questionSpec: QuestionSpec) {
    Column(
        modifier = Modifier.padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier
            .fillMaxWidth()
            .height(58.dp))
        val main = LocalActivity.current as Main
/*
        OutlinedTextField(
            questionSpec.textFieldState,
            label = { Text(questionSpec.questionDef) },
            labelPosition = TextFieldLabelPosition.Above(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                showKeyboardOnFocus = true
            ),
            onKeyboardAction = { performDefaultAction ->
                main.onNext()
            })
*/
    }
}

@Composable
fun BottomBar() {
    BottomAppBar(
//        modifier = Modifier.imePadding(),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
        content = { BackNextRow() })
}

@Composable
fun BackNextRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val main = LocalActivity.current as Main
        var isBackEnabled by remember {
            mutableStateOf(
                main.event !=
                        FormEntryController.EVENT_BEGINNING_OF_FORM
            )
        }
        val scope = rememberCoroutineScope()
        Button(
            enabled = isBackEnabled,
            onClick = {
                main.onBack()
                isBackEnabled = main.event > 0
                if (true) return@Button
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
            }
        ) {
            Text("Back")
        }
        Button(
            onClick = {
                main.onNext()
                isBackEnabled = main.event > 0
                if (true) return@Button
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
            }
        ) {
            Text("Next")
        }
    }
}
























