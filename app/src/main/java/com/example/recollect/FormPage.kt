package com.example.recollect

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.javarosa.form.api.FormEntryController

@ExperimentalMaterial3Api
@Composable
fun TopBar(label: String = "Top app bar") {
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
fun Content(innerPadding: PaddingValues, textFieldState: TextFieldState) {
    Column(
        modifier = Modifier.padding(innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier
                .fillMaxWidth()
                .height(58.dp)
        )
        val main = LocalActivity.current as Main
        OutlinedTextField(textFieldState,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onKeyboardAction = { performDefaultAction ->
                main.onNext()
            })
    }
}

@Composable
fun BottomBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
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
                    if (true)return@Button
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
                    if (true)return@Button
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
}

@ExperimentalMaterial3Api
@Composable
fun FormPage(textFieldState: TextFieldState) {
    Scaffold(
        topBar = { TopBar("Top app bar") },
        bottomBar = { BottomBar() }
    ) { Content(it,textFieldState) }
}














