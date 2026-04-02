package com.example.recollect.bits

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.RectRulers
import androidx.compose.ui.layout.WindowInsetsRulers
import androidx.compose.ui.layout.innermostOf
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Gist() {
    Scaffold(
        topBar = {
            Row(Modifier.fillMaxWidth(), Arrangement.Center) {
                var query by rememberSaveable { mutableStateOf("") }
                SearchBar(query, { query = it }, {}, false, {}) {}
            }
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .consumeWindowInsets(innerPadding)
                    .imePadding(),
                contentPadding = innerPadding,
                content = { items(30) { ListItem({ Text("Item #$it") }) } },
            )
        },
    )
}



@Preview
@Composable
fun UserProfileFetch() {
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }

    LaunchedEffect("") {
        delay(5.seconds)
        userProfile = fetchUserProfile()
    }

    userProfile?.let {
        Text("User name: ${it.name}")
    } ?: CircularProgressIndicator()
}

private fun fetchUserProfile(): UserProfile =
    UserProfile()

private data class UserProfile(val name: String = "Fred")

@ExperimentalMaterial3Api
@Preview
@Composable
fun BitsScaffold() {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) { Pulse() }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(56.dp),
        ) {
            if (false) UserProfileFetch()
            else AnimatedVisibilityCookbook()
            //Pulse()
        }
    }
}

@Composable
fun Pulse() {
    val pulseRateMs by remember { mutableLongStateOf(3000L) }
    val alpha = remember { Animatable(1f) }
    LaunchedEffect(pulseRateMs) { // Restart the effect when the pulse rate changes
        while (isActive) {
            println("R1: pulseRateMs = $pulseRateMs")
            delay(pulseRateMs) // Pulse the alpha every pulseRateMs to alert the user
            alpha.animateTo(0f)
            alpha.animateTo(1f)
            println("R1: pulseRateMs~ = $pulseRateMs")
        }
    }
}