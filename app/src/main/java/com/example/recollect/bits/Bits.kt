package com.example.recollect.bits

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.seconds

@Composable
fun AddRepeatDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true)
    ) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                // Dialog Title
                Text(
                    text = "Add ?"+"Inner",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF1A1C1E)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // "Don't add" Button
                    OutlinedButton(
                        onClick = onDismissRequest,
                        shape = RoundedCornerShape(100.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFC4C6D0))
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF3B82F6) // Accent text color
                        ),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = Color(0xFF3B82F6)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Don't add", fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // "Add" Button
                    Button(
                        onClick = onConfirm,
                        shape = RoundedCornerShape(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3498DB), // Matches the blue color fill
                            contentColor = Color.White
                        ),
                        modifier = Modifier.height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Confirm"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Add", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
@Composable
fun ConfirmationDialog_(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        // The properties below ensure native dimming behavior works properly
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = true)
        ) {
            Card(
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 28.dp)
                ) {
                    // Dialog Title
                    Text(
                        text = "Add \"Inner\"?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF1A1C1E)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Action Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // "Don't add" Button
                        OutlinedButton(
                            onClick = onDismissRequest,
                            shape = RoundedCornerShape(100.dp),
                            border = ButtonDefaults.outlinedButtonBorder.copy(
                                brush = SolidColor(Color(0xFFC4C6D0))
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF3B82F6) // Accent text color
                            ),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancel",
                                tint = Color(0xFF3B82F6)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Don't add", fontSize = 14.sp)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // "Add" Button
                        Button(
                            onClick = onConfirm,
                            shape = RoundedCornerShape(100.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF3498DB), // Matches the blue color fill
                                contentColor = Color.White
                            ),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Confirm"
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Add", fontSize = 14.sp)
                        }
                    }
                }
            }
        }
}

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