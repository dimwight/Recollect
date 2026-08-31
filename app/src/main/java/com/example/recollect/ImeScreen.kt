package com.example.recollect

import android.graphics.Rect
import android.view.ViewTreeObserver
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.recollect.bits.AddRepeatDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
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
fun SwipeBox(
    inputActivity: InputActivity,
    content: @Composable BoxScope.() -> Unit
) {
    var actionText by remember { mutableStateOf("Swipe me!") }
    var dragAccumulator by remember { mutableStateOf(Offset.Zero) }
    var startMillis by remember { mutableLongStateOf(0) }
    Box(
        modifier = Modifier
            .background(Color.White)
            .fillMaxSize()
            .padding(horizontal = 15.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        dragAccumulator = Offset.Zero
                        startMillis = System.currentTimeMillis()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        dragAccumulator += dragAmount
                    },
                    onDragEnd = {
                        val dragMillis = System.currentTimeMillis() - startMillis
                        if (dragMillis > 1000) {
                            actionText = "Drag took too long"
                            return@detectDragGestures
                        }
                        val minAbs = 250f
                        val totalX = dragAccumulator.x
                        val totalY = dragAccumulator.y

                        val absX = abs(totalX)
                        val absY = abs(totalY)
                        if (absX > absY) {
                            if (absX > minAbs) {
                                if (totalX > 0) inputActivity.onBack()
                                else inputActivity.onNext()
                            } else {
                                actionText = "Below threshold"
                            }
                        } else {
                            actionText = if (absY > minAbs) {
                                "Bad swipe axis!"
                            } else {
                                "Below threshold"
                            }
                        }
                    },
                    onDragCancel = {
                        println("R1: onDragCancel")
                        actionText = "onDragCancel"
                        dragAccumulator = Offset.Zero
                    }
                )
            }, content = { content() })
}

@Composable
fun ImeScreen(inputActivity: InputActivity) {
    SwipeBox(inputActivity) {
        val screenState = inputActivity.screenState.collectAsState().value
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            FormTitleRow(screenState)
            if (DoWipe && screenState.forWipe) {
                val wipeDuration = 300
                val wipeWait = 200
                var wipeState by remember {
                    mutableIntStateOf(
                        screenState.thenState?.questionAt ?: -1
                    )
                }
                println("R1: wipeState = $wipeState")
                AnimatedContent(
                    targetState = wipeState,
                    transitionSpec = {
                        val slideTween = tween<IntOffset>(
                            durationMillis = wipeDuration,
                            easing = LinearEasing
                        )
                        println("R1: ${initialState-targetState}")
                        if (targetState > initialState) {
                            slideInHorizontally(slideTween) { it: Int -> it } togetherWith
                                    slideOutHorizontally(slideTween) { -it }
                        } else {
                            slideInHorizontally(slideTween) { -it } togetherWith
                                    slideOutHorizontally(slideTween) { it }
                        }
                    }
                ) { at ->
                    ChooseFormBox(screenState, inputActivity, at)
                }
                LaunchedEffect(wipeWait) {
                    wipeState = screenState.questionAt
                    println("R1: wipeState~ = $wipeState")
                    delay(wipeDuration.milliseconds)
                    inputActivity.clearForWipe()
                }
            } else ChooseFormBox(screenState, inputActivity)
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom
        ) {
            BackNextRow(inputActivity, screenState)
            Box(
                Modifier
                    .height((45 + getImeHeight()).dp)
                    .fillMaxWidth()
                    .background(Color.White)
            )
        }
    }
}

@Composable
private fun ChooseFormBox(
    screenState: ScreenState,
    inputActivity: InputActivity,
    at: Int = -1
) {
    val forWipe = at != -1
    val formTitle = screenState.formTitle
    if (!forWipe) {
        if (screenState.endOfForm) {
            FormEndBox(inputActivity, formTitle)
        } else FormWidgetEditBox(screenState, inputActivity)
        return
    }
    val wipeState = if (at == screenState.questionAt) screenState
    else screenState.thenState!!
    if (wipeState.endOfForm) FormEndBox(inputActivity, formTitle)
    else FormWidgetWipeBox(wipeState)
}

@Composable
private fun FormWidgetEditBox(
    screenState: ScreenState,
    inputActivity: InputActivity
) {
    Box {
        Column {
            FlowRow(Modifier.padding(vertical = 0.dp)) {
                val labels = screenState.questionSpec.captions
                    .mapTo(ArrayList()) {
                        it.formElement.labelInnerText
                    }
                for ((at: Int, next) in labels.withIndex())
                    if (at < labels.size - 1)
                        Text("$next >", style = mySmallStyle())
                Spacer(Modifier.height(15.dp))
            }
            if (false || screenState.addRepeat)
                AddRepeatDialog(
                    onDismissRequest = { inputActivity.clearAddRepeat() }
                ) { }
            val focusRequester = remember { FocusRequester() }
            val focusManager = LocalFocusManager.current
            QuestionTextField(focusRequester)
            if (!screenState.forWipe)
                LaunchedEffect(screenState) {
                    if (screenState.newWidget_) {
                        inputActivity.clearNewWidget_()
                    } else {
                        if (DoWipe) delay(200.milliseconds)
                        focusRequester.requestFocus()
                    }
                }
            else
                LaunchedEffect(screenState) {
                    focusManager.clearFocus(true)
                }
        }
    }
}

@Composable
private fun FormEndBox(inputActivity: InputActivity, formTitle: String) {
    Box {
        Column {
            Spacer(modifier = Modifier.height(140.dp))
            Text(
                text = "You are at the end of $formTitle.",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(24.dp))
            NoticeCard()
            Spacer(modifier = Modifier.height(60.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SaveDraftButton(inputActivity)
                FinalizeButton()
            }
        }
    }
}


@Composable
private fun FormWidgetWipeBox(screenState: ScreenState) {
    Box() {
        Column {
            val question = screenState.questionSpec
            FlowRow(Modifier.padding(vertical = 0.dp)) {
                val labels = question.captions
                    .mapTo(ArrayList()) {
                        it.formElement.labelInnerText
                    }
                for ((at: Int, next) in labels.withIndex())
                    if (at < labels.size - 1)
                        Text("$next >+", style = mySmallStyle())
                Spacer(Modifier.height(15.dp))
            }
            Text(
                question.labelText,
                style = myMediumStyle(true),
                fontWeight = FontWeight.Bold,
            )
            Text(
                question.helpText,
                style = mySmallStyle()
            )
            Spacer(Modifier.height(10.dp))
            val containerColor = Color(242, 242, 242)
            TextField(
                screenState.textFieldState,
                Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors().copy(
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor
                )
            )
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
            if (false) {
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

























