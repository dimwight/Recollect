package com.example.recollect

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.recollect.bits.TabbedPagerScreen
import com.example.recollect.bits.WipeDemoScreen

@Composable
fun Screens(activity: InputActivity) {
    val forceEndOfForm = false
    val endOfForm = forceEndOfForm ||
            activity.screenState.collectAsState().value.endOfForm
    if (false) {
        Box() {
            WipeDemoScreen()
        }
    } else if (endOfForm) {
        EndOfFormScreen(activity)
    } else ImeScreen(activity)
}

@Composable
private fun EndOfFormScreen(inputActivity: InputActivity) {
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            val screenState = inputActivity.screenState.collectAsState().value
            FormTitleRow(screenState)
            Box {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    BackNextRow(inputActivity, screenState)
                    Spacer(Modifier.height(45.dp))
                }
            }
        }
    }
}












