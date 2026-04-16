package com.example.recollect

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import org.javarosa.form.api.FormEntryController

@Composable
fun FocusedTextField(focusRequester: FocusRequester) {
    val formInput = LocalActivity.current as FormInput
    val questionSpec = formInput.questionSpec
    var indicateBad by remember { mutableStateOf(false) }
    formInput.setResultCheck { result: Int ->
        indicateBad = result != FormEntryController.ANSWER_OK
    }
    TextField(
        questionSpec.textFieldState,
        Modifier.Companion
            .fillMaxWidth()
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.colors().copy(
            focusedIndicatorColor =
                if (indicateBad) Color.Companion.Red else myBlue
        ),
        labelPosition = TextFieldLabelPosition.Above(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Companion.Default, showKeyboardOnFocus = true
        ),
        textStyle = scaleStyle(MaterialTheme.typography.bodySmall, 1.5),
        onKeyboardAction = { formInput.onNext() },
        label = {
            Column() {
                Text(
                    questionSpec.questionDef.labelInnerText,
                    style = scaleStyle(MaterialTheme.typography.bodyMedium, 1.5),
                    fontWeight = FontWeight.Companion.Bold
                )
                Text(
                    questionSpec.questionDef.helpText,
                    style = scaleStyle(MaterialTheme.typography.bodySmall, 1.5)
                )
            }
        })
}