package com.example.recollect.bits

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recollect.BackNextRow
import com.example.recollect.FormTitleRow
import com.example.recollect.ImeScreen
import com.example.recollect.InputActivity
import com.example.recollect.R
import com.example.recollect.SwipeBox

// Define the custom primary blue color from the image
val FormBlue = Color(0xFF3FA3D2)
val CardBackground = Color(0xFFF4F6F7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormCompletionScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "All question types",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                actions = {
                    IconButton(onClick = { /* Save draft */ }) {
//                        Icon(imageVector = Icons.Default.Save, contentDescription = "Save")
                    }
                    IconButton(onClick = { /* View list */ }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.List, contentDescription = "List")
                    }
                    IconButton(onClick = { /* More choices */ }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "More options")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            // Back Button Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Go back */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = FormBlue),
                    modifier = Modifier.width(140.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Back", fontSize = 16.sp)
                }
            }
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Heading Text
            AtEnd()

            Spacer(modifier = Modifier.height(24.dp))

            // Notice Card
            NoticeCard()

            Spacer(modifier = Modifier.height(40.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Save as draft Button
                SaveDraftButton()

                // Finalize Button
                FinalizeButton()
            }
        }
    }
}

@Composable
private fun AtEnd() {
    Text(
        text = "You are at the end of All question types.",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp,
        color = Color.Black
    )
}

@Composable
private fun NoticeCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stacked icons to replicate the "no edit" or crossed pencil look
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = FormBlue,
                        modifier = Modifier.size(24.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = FormBlue,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Edits can't be made after finalizing.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "If you need to make edits to your form, \"Save as draft\" until you're ready to send.",
                fontSize = 15.sp,
                lineHeight = 22.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
private fun RowScope.SaveDraftButton() {
    OutlinedButton(
        onClick = { /* Handle draft save */ },
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .weight(1f)
            .height(54.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(50)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = FormBlue)
    ) {
//                    Icon(imageVector = Icons.Default.Save, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Save as draft", fontSize = 16.sp)
    }
}

@Composable
private fun RowScope.FinalizeButton() {
    Button(
        onClick = { /* Handle finalize submission */ },
        shape = RoundedCornerShape(50),
        colors = ButtonDefaults.buttonColors(containerColor = FormBlue),
        modifier = Modifier
            .weight(1f)
            .height(54.dp)
    ) {
        Icon(imageVector = Icons.Default.Send, contentDescription = null, tint = Color.White)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Finalize", fontSize = 16.sp, color = Color.White)
    }
}

@Composable
fun Screens(activity: InputActivity) {
    val forceEndOfForm = false
    val endOfForm = forceEndOfForm ||
            activity.screenState.collectAsState().value.endOfForm
    if (false) {
        Box {
        }
    } else if (endOfForm) {
        EndOfFormScreen(activity)
    } else ImeScreen(activity)
}

@Composable
private fun EndOfFormScreen(inputActivity: InputActivity) {
    SwipeBox(inputActivity) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            val screenState = inputActivity.screenState.collectAsState().value
            FormTitleRow(screenState)
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    // Heading Text
                    AtEnd()

                    Spacer(modifier = Modifier.height(24.dp))

                    // Notice Card
                    NoticeCard()

                    Spacer(modifier = Modifier.height(40.dp))

                    // Action Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Save as draft Button
                        SaveDraftButton()

                        // Finalize Button
                        FinalizeButton()
                    }
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.form_end),
                        contentDescription = ""
                    )
                    BackNextRow(inputActivity, screenState)
                    Spacer(Modifier.height(45.dp))
                }
            }
        }
    }
}












