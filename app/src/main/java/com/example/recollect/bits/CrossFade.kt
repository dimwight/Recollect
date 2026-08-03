package com.example.recollect.bits

// Import necessary Compose libraries
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CrossfadeTabs(selected: String, onSelect: (String) -> Unit) {
    val tabs = listOf("Home", "Profile")
    Column {
        Row {
            tabs.forEach { tab ->
                Text(
                    text = tab,
                    modifier = Modifier
                        .clickable { onSelect(tab) }
                        .padding(12.dp),
                    color = if (selected == tab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
        // The magic of Crossfade
        Crossfade(targetState = selected, animationSpec = tween(450)) { state ->
            when (state) {
                "Home" -> Text("🏠 Home Screen", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
                "Profile" -> Text("👤 Profile Screen", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

// A wrapper composable to hold the state for the preview
@Preview(showBackground = true)
@Composable
fun CrossfadeTabsPreview() {
    var selectedTab by remember { mutableStateOf("Home") }
    CrossfadeTabs(selected = selectedTab, onSelect = { selectedTab = it })
}