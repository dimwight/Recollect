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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Screens(activity: InputActivity) {
    val atFormEnd = activity.pageState.collectAsState().value.atFormEnd
    if (atFormEnd){
        FormEndScreen(activity)
    }
    else ImeScreen()
}

@Composable
private fun FormEndScreen(activity: InputActivity){
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
            Spacer(Modifier.height(22.dp))
            val question = (LocalActivity.current as InputActivity)
                .pageState.collectAsState().value.questionSpec
            FormTitleRow(question)
            Spacer(Modifier.height(5.dp))
            BackNextButton("   Back   ") {
                activity.setFormEnd(false)
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    BackNextRow()
                    Spacer(Modifier.height(10.dp))
                    Box(
                        Modifier
                            .height(getImeHeight().dp)
                            .fillMaxWidth()
                            .background(Color.White)
                    )
                    Spacer(Modifier.height(25.dp))
                }
            }
        }
    }
}












