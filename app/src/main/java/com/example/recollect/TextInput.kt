package com.example.recollect

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun QuestionTextField(focusRequester: FocusRequester) {
    val state = (LocalActivity.current as InputActivity)
        .screenState.collectAsState().value
    val question = state.questionSpec
    Column {
        Text(
            question?.labelText?:"",
            style = myMediumStyle(true),
            fontWeight = FontWeight.Bold,
        )
        Text(
            question?.helpText?:"",
            style = mySmallStyle()
        )
        Spacer(Modifier.height(10.dp))
    }
    var indicateError = state.hasError
    val containerColor = Color(242, 242, 242)
    TextField(
        state.textFieldState,
        Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor =
                if (indicateError) Color.Companion.Red else myBlue
        ),
        labelPosition = TextFieldLabelPosition.Above(),
        keyboardOptions = KeyboardOptions(
            keyboardType = question?.keyboard?: KeyboardType.Unspecified,
            imeAction = ImeAction.Default,
            showKeyboardOnFocus = true
        ),
//        onKeyboardAction = { form.onNext() },
//        label = { }
    )


}








