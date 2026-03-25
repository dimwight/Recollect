package com.example.recollect

import android.graphics.Color
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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
fun BitsScaffold() {
    Scaffold(
        topBar = { TopBar("") },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {Pulse() }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.Companion.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(56.dp),
        ) {
            if (false) UserProfileFetch()
            else if (false) Pulse()
        }
    }
}
@Composable
fun Pulse() {
    // Allow the pulse rate to be configured, so it can be sped up if the user is running
    // out of time
    var pulseRateMs by remember { mutableLongStateOf(3000L) }
    val alpha = remember { Animatable(1f) }
    LaunchedEffect(pulseRateMs) { // Restart the effect when the pulse rate changes
        while (isActive) {
            println("R1: pulseRateMs = ${pulseRateMs}")
            delay(pulseRateMs) // Pulse the alpha every pulseRateMs to alert the user
            alpha.animateTo(0f)
            alpha.animateTo(1f)
            println("R1: pulseRateMs~ = ${pulseRateMs}")
        }
    }
}










