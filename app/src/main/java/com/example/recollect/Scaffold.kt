package com.example.recollect

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTopBar() {
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text("Top app bar")
        }
    )
}
@Composable
fun NewContent(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier.padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(56.dp),
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
      //          This is an example of a scaffold. It uses the Scaffold composable's parameters to create a screen with a simple top app bar, bottom app bar, and floating action button.

            text = """
               It also contains some basic inner content, such as this text.
                
                You have pressed the floating action button presses times.
                   
                """.trimIndent(),
        )
        Text(
            modifier = Modifier.padding(8.dp),
            text =
                """
                This is an example of a scaffold. It uses the Scaffold composable's parameters to create a screen with a simple top app bar, bottom app bar, and floating action button.
                
                It also contains some basic inner content, such as this text.
                
                You have pressed the floating action button presses times.
                     
                """.trimIndent(),
        )

    }
}
@Composable
fun NewBottomBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ) {
        Row(
            modifier = Modifier.Companion.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val main = LocalActivity.current as Main
            Button(
                onClick = { main.onBack() }) {
                Text("Back")
            }

            Button(
                onClick = { main.onNext() },
            ) {
                Text("Next")
            }
        }
    }


}

@Preview()
@Composable
fun ScaffoldSet() {
    Scaffold(
        topBar = { NewTopBar() },
        bottomBar = { NewBottomBar() }
    ) { innerPadding -> NewContent(innerPadding) }
}


