package com.example.recollect

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

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

@Preview
@Composable
fun UserProfileScaffold() {
    Scaffold(
        topBar = { TopBar("") },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.Companion.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(56.dp),
        ) {
            UserProfileFetch()
        }
    }
}