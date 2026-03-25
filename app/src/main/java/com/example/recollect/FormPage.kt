package com.example.recollect

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.javarosa.form.api.FormEntryController

@OptIn(ExperimentalMaterial3Api::class)
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
fun Content(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier.padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(56.dp),
    ) {
        Text(
            text = "[label]",
            style = MaterialTheme.typography.titleMedium,
/*            modifier = Modifier.a
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp)*/ // Space to 1st bubble
        )
//        Spacer(modifier = Modifier.width(8.dp))
        var textFieldState by remember { mutableStateOf("[A string]") }
        TextField(
            value = textFieldState,
            onValueChange = {
                println("R1: onValueChange = $it")
                textFieldState=it
            },
        )
    }
}

@Composable
fun BottomBar() {
    val scope = rememberCoroutineScope()
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
            Button(
                enabled = isBackEnabled,
                onClick = {
                    main.onBack()
                    isBackEnabled = main.event > 0
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

@Preview
@Composable
fun FormPage() {
    Scaffold(
        topBar = { TopBar("Top app bar") },
        bottomBar = { BottomBar() }
    ) { innerPadding -> Content(innerPadding) }
}














