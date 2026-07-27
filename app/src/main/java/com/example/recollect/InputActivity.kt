package com.example.recollect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.example.recollect.ui.theme.RecollectTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import org.javarosa.core.model.FormDef
import org.javarosa.core.model.Constants
import org.javarosa.core.model.data.StringData
import org.javarosa.form.api.FormEntryCaption
import org.javarosa.form.api.FormEntryController
import org.javarosa.form.api.FormEntryController.EVENT_END_OF_FORM
import org.javarosa.form.api.FormEntryController.EVENT_QUESTION
import org.javarosa.form.api.FormEntryModel
import org.javarosa.form.api.FormEntryPrompt
import org.javarosa.xform.util.XFormUtils
import java.io.InputStream
import kotlin.time.TimeSource.Monotonic

var time_: Monotonic.ValueTimeMark? = null

fun getNumbers1_(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000)
    }
}

@Composable
fun TextStyle.scale(
    by: Double,
    bold: Boolean = false
): TextStyle {
    return if (bold)
        copy(
            fontSize = fontSize.times(by),
            lineHeight = lineHeight.times(by),
            fontWeight = FontWeight.Bold
        ) else
        copy(
            fontSize = fontSize.times(by),
            lineHeight = lineHeight.times(by),
        )
}

@Composable
fun mySmallStyle(): TextStyle =
    typography.bodySmall.scale(1.3).copy(Color.Gray)

@Composable
fun myMediumStyle(bold: Boolean = false): TextStyle =
    typography.bodyMedium.scale(1.45, bold)

data class QuestionSpec(
    val captions: Array<FormEntryCaption> = emptyArray(),
    val labelText: String = "",
    val helpText: String = "",
    val keyboard: KeyboardType = KeyboardType.Unspecified
) {
    override fun toString(): String {
        return this.run {
            "label: $labelText help: $helpText"
        }
    }
}

data class ScreenState(
    val textFieldState: TextFieldState = TextFieldState("[A string]"),
    val questionSpec: QuestionSpec = QuestionSpec(),
    val formTitle: String = "",
    val hasError: Boolean = false,
    val showBack: Boolean = false,
    val showNext: Boolean = false,
    val endOfForm: Boolean = false
){
    override fun toString(): String {
        return "showBack = $showBack showNext = $showNext "
    }
}

class InputActivity : ComponentActivity() {
    fun getNumbers4_(): Flow<Int> = flow {
        for (i in 4..6) {
            delay(1000)
            emit(i)
        }
    }

    private lateinit var controller: FormEntryController
    private var firstQuestionPrompt: FormEntryPrompt?=null
    var event: Int = -1
    var questionAt = -1
    private var hasError: Boolean = false
    private val _screenState = MutableStateFlow(ScreenState())
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()
    private fun traceEventAndQuestion(state: ScreenState? = null) {
        println("R1: questionAt = $questionAt")
        if (false)return
        println("R1: event = $event")
        if (state == null)return
        println("R1: state = $state firstQuestionPrompt = ${firstQuestionPrompt.hashCode()}} ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val formDef by lazy {
            try {
                val formId = resources.getIdentifier(
                    "end", "raw", packageName
                )
                val inputStream: InputStream = resources.openRawResource(formId)
                return@lazy XFormUtils.getFormFromInputStream(inputStream)
            } catch (e: Exception) {
                println("R1: = $e")
            }
        }
        controller = FormEntryController(FormEntryModel(formDef as FormDef?))
        event = controller.model.event
        while (questionAt < 2) nextQuestion()
        enableEdgeToEdge()
        setContent {
            RecollectTheme {
                Screens(this)
            }
        }
    }

    private fun updateScreenState(endOfForm: Boolean=false) {
        val model = controller.model
        val questionPrompt = model.questionPrompt
        val formElement = questionPrompt.formElement
        if (questionAt==0&&firstQuestionPrompt==null){
            firstQuestionPrompt=questionPrompt
        }
        val showBack = if (true)
            formElement != firstQuestionPrompt?.formElement
        else questionAt!=0
        val question = questionPrompt.question
        _screenState.update {
            it.copy(
                endOfForm = endOfForm,
                showBack = showBack,
                showNext = !endOfForm,
                formTitle = model.formTitle,
                questionSpec = QuestionSpec(
                    captions = model.captionHierarchy,
                    labelText = question.labelInnerText,
                    helpText = question.helpText,
                    keyboard = when (questionPrompt.dataType) {
//                        Constants.DATATYPE_DECIMAL -> KeyboardType.Decimal
                        Constants.DATATYPE_TEXT -> KeyboardType.Text
                        Constants.DATATYPE_INTEGER -> KeyboardType.Number
                        else -> KeyboardType.Unspecified
                    }
                )
            )
        }
        traceEventAndQuestion(_screenState.value)
    }
    private fun updateScreenState_(endOfForm: Boolean=true) {
        _screenState.update {
            it.copy(
                endOfForm = endOfForm,
                showBack =  endOfForm,
                showNext = !endOfForm,
                formTitle = controller.model.formTitle,
                questionSpec =  QuestionSpec()
            )
        }
        traceEventAndQuestion(_screenState.value)
    }

    private fun nextQuestion() {
        do {
            event = controller.stepToNextEvent()
            traceEventAndQuestion()
            if (atFormEnd()) {
                updateScreenState_()
                return
            }
        } while (event != EVENT_QUESTION)
        questionAt++
        traceEventAndQuestion()
        updateScreenState()
    }

    private fun atFormEnd(): Boolean = event == EVENT_END_OF_FORM

    private fun previousQuestion() {
        val atFormEnd = atFormEnd()
        do {
            event = controller.stepToPreviousEvent()
            traceEventAndQuestion()
        } while (event != EVENT_QUESTION)
        if(!atFormEnd) questionAt--
        updateScreenState()
    }

    fun onNext() {
        val answer = StringData(
            _screenState.value.textFieldState.text
                    as String
        )
        val result = controller.answerQuestion(answer, true)
        if (false) hasError = !hasError
        _screenState.update {
            it.copy(
                hasError = hasError,
                showBack = true
            )
        }
        if (!hasError) {
            nextQuestion()
        }
    }

    fun onBack() {
        previousQuestion()
    }
}












