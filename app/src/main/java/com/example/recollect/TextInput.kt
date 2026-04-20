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
import androidx.compose.ui.unit.dp
import org.javarosa.form.api.FormEntryController

@Composable
fun FocusingTextField(focusRequester: FocusRequester) {
    val form = LocalActivity.current as FormControl
    val questionSpec = form.questionSpec
    Column {
        Text(
            questionSpec.questionDef.labelInnerText,
            style = myMediumStyle(true),
            fontWeight = FontWeight.Bold,
        )
        Text(
            questionSpec.questionDef.helpText,
            style = mySmallStyle()
        )
        Spacer(Modifier.height(10.dp))
    }
    var indicateBad by remember { mutableStateOf(false) }
    form.setResultCheck { result: Int ->
        indicateBad = result != FormEntryController.ANSWER_OK
    }
    val containerColor = Color(242, 242, 242)
    TextField(
        questionSpec.textFieldState,
        Modifier.Companion
            .fillMaxWidth()
            .focusRequester(focusRequester),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            focusedIndicatorColor =
                if (indicateBad) Color.Companion.Red else myBlue
        ),
        labelPosition = TextFieldLabelPosition.Above(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Default,
            showKeyboardOnFocus = true
        ),
        textStyle = myMediumStyle(),
        onKeyboardAction = { form.onNext() },
        label = { }
    )


}