package com.example.recollect

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.visible
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

val myBlue = Color(62, 159, 208)

@Composable
fun FormTitleRow(screenState: ScreenState) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.height(110.dp))
        Text(text = screenState.formTitle, style = myMediumStyle(true))
    }
}

@Composable
fun ImeScreen(inputActivity: InputActivity) {
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
            val oldContent = false
            if (oldContent) FlowRow(Modifier.padding(vertical = 0.dp)) {
                val labels = screenState.questionSpec.captions
                    .mapTo(ArrayList()) {
                        it.formElement.labelInnerText
                    }
                for ((at: Int, next) in labels.withIndex()) {
                    if (at < labels.size - 1) Text("$next >", style = mySmallStyle())
                }
                Spacer(Modifier.height(15.dp))
            }
            if (oldContent) {
                val focusRequester = remember { FocusRequester() }
                val focusManager = LocalFocusManager.current
                QuestionTextField(focusRequester)
                LaunchedEffect(screenState) {
                    if (screenState.newWidget) {
                        focusManager.clearFocus(true)
                        delay(500.milliseconds)
                        inputActivity.clearNewWidget()
                    } else if (false) {
                        delay(500.milliseconds)
                        focusRequester.requestFocus()
                    }
                }
            } else {
                val tabs = listOf("Home", "Explore", "Profile")
                val pagerState = rememberPagerState(pageCount = { tabs.size })
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxWidth()
                ) { page -> SwipeBox(tabs, page) }
            }
            Box {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    if (true||
                        !screenState.newWidget) {
                        BackNextRow(inputActivity, screenState)
                    }
                    Spacer(Modifier.height(20.dp))
                    if (true||oldContent) {
                        Box(
                            Modifier
                                .height(getImeHeight().dp)
                                .fillMaxWidth()
                                .background(Color.White)
                        )
                    }
                    Spacer(Modifier.height(25.dp))
                }
            }
        }
    }
}

@Composable
fun getImeHeight(): Int {
    val view = LocalView.current
    val observer = view.viewTreeObserver
    val height = remember { mutableIntStateOf(1) }
    val pxToDp = with(LocalDensity.current) { 1.0 / (1.dp.toPx()) }
    val remember = remember { pxToDp }
    DisposableEffect(observer) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val screenHeight = view.rootView.height
            val rect = Rect()
            view.getWindowVisibleDisplayFrame(rect)
            val rectY = if (false) rect.height() else rect.bottom
            val diff = screenHeight - rectY
            val ratio = screenHeight.toFloat() / rectY
            if (true) {
//                println("R1: rect = $rect")
//                println("R1: screen = $screenHeight")
//                println("R1: diff = $diff")
                println("R1: ratio = ${(ratio * 100).toInt()}")
            }
            height.intValue = if (ratio < 1.5) 0
            else {
                val fraction = if (false) .34 else remember * .9
                (diff * fraction).toInt()
            }
        }
        observer.addOnGlobalLayoutListener(listener)

        onDispose {
            observer.removeOnGlobalLayoutListener(listener)
        }
    }

    return height.intValue
}

@Composable
fun BackNextRow(inputActivity: InputActivity, screenState: ScreenState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        BackNextButton("<  Back", screenState.showBack) {
            inputActivity.onBack()
        }
        val scope = rememberCoroutineScope()
        BackNextButton("Next  >", screenState.showNext) {
            inputActivity.onNext()
            if (false) {
                scope.launch {
                    inputActivity.getNumbers4_().collect { value ->
                        println("R1: value = $value")
                    }
                }
                scope.launch {
                    getNumbers1_().collect { value ->
                        println("R1: value = $value")
                    }
                }
            }
        }
    }
}

@Composable
fun BackNextButton(
    text: String,
    show: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = Modifier.visible(show),
        colors = ButtonColors(
            Color.White,
            myBlue,
            Color.White,
            Color.LightGray
        ),
        border = BorderStroke(1.dp, Color.LightGray),
        contentPadding = PaddingValues(55.dp, 13.dp),
        onClick = onClick
    ) {
        Text(text, style = mySmallStyle().copy(myBlue))
    }
}

























