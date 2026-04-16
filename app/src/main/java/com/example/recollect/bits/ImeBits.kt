package com.example.recollect.bits

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.recollect.getImeHeight


@Preview
@Composable
fun Pad() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .height(50.dp)
                    .imePadding()
                    .background(Color.Cyan)
                    .fillMaxWidth()
            )
            TextField("Edit me!", {})
            Box(
                Modifier
                    .height(50.dp)
                    .background(Color.Cyan)
                    .imePadding()
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .imePadding()
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .imePadding()
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        Modifier
                            .height(300.dp)
                            .imePadding()
                            .background(Color.Blue)
                            .fillMaxWidth()
                    )
                    Box(
                        Modifier
                            .height(getImeHeight().dp)
                            .background(Color.Red)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}







