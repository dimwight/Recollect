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
import org.javarosa.form.api.FormEntryController.EVENT_BEGINNING_OF_FORM
import org.javarosa.form.api.FormEntryController.EVENT_END_OF_FORM
import org.javarosa.form.api.FormEntryController.EVENT_GROUP
import org.javarosa.form.api.FormEntryController.EVENT_PROMPT_NEW_REPEAT
import org.javarosa.form.api.FormEntryController.EVENT_QUESTION
import org.javarosa.form.api.FormEntryController.EVENT_REPEAT
import org.javarosa.form.api.FormEntryController.EVENT_REPEAT_JUNCTURE
import org.javarosa.form.api.FormEntryModel
import org.javarosa.form.api.FormEntryPrompt
import org.javarosa.xform.util.XFormUtils
import java.io.InputStream
import java.lang.System.currentTimeMillis
import kotlin.time.TimeSource.Monotonic

var time_: Monotonic.ValueTimeMark? = null

var start=-1L
fun Times(msg : String="") {
    val elapsed = currentTimeMillis() - start
    if (start<0||elapsed>5000) {
        start= currentTimeMillis()
        println("R1: Times reset in $msg")
    }
    else
    println("R1: $msg=${elapsed / 10}")
}

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
    val keyboardType: KeyboardType = KeyboardType.Unspecified
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
    val endOfForm: Boolean = false,
    val newWidget_: Boolean = true,
    val questionAt: Int = -1,
    val thenState: ScreenState? = null,
    val forWipe: Boolean = false,
    val addRepeat: Boolean = false
) {
    override fun toString(): String {
        return if (false) "showBack = $showBack showNext = $showNext "
        else toString()
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
    private var firstQuestionPrompt: FormEntryPrompt? = null
    var event: Int = -1
    var questionAt = -1
    private var hasError: Boolean = false
    private val _screenState = MutableStateFlow(ScreenState())
    val screenState: StateFlow<ScreenState> = _screenState.asStateFlow()
    private fun traceEventAndQuestion(state: ScreenState? = null) {
        if (true) return
        println("R1: questionAt = $questionAt")
        println("R1: event = $event")
        if (state == null) return
        println("R1: state = $state firstQuestionPrompt = ${firstQuestionPrompt.hashCode()}} ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val formDef by lazy {
            try {
                val formId = resources.getIdentifier(
                    "repeats", "raw", packageName
                )
                val inputStream: InputStream = resources.openRawResource(formId)
                return@lazy XFormUtils.getFormFromInputStream(inputStream)
            } catch (e: Exception) {
                println("R1: = $e")
            }
        }
        controller = FormEntryController(FormEntryModel(formDef as FormDef?))
        event = controller.model.event
        while (questionAt < 4) doNext()
        enableEdgeToEdge()
        setContent {
            RecollectTheme {
                Screens(this)
            }
        }
    }

    private fun doNext(forward: Boolean = true) {
        if (true) {
            if (forward) nextQuestion()
            else previousQuestion()
        } else handleEvent(forward)
    }

    private fun handleEvent(forward: Boolean = true) {
        traceEventAndQuestion()
        if (forward) {
            /* EVENT_BEGINNING_OF_FORM = 0;
             EVENT_END_OF_FORM = 1;
             EVENT_PROMPT_NEW_REPEAT = 2;
             EVENT_QUESTION = 4;
             EVENT_GROUP = 8;
             EVENT_REPEAT = 16;
             EVENT_REPEAT_JUNCTURE = 32;*/
            when (event) {
                EVENT_QUESTION -> {
                    questionAt++
                    updateScreenState()
                }
                EVENT_END_OF_FORM ->
                    updateScreenState(endOfForm = true)
                EVENT_PROMPT_NEW_REPEAT -> {
                    _screenState.update {
                        it.copy(
                            addRepeat = true
                        )
                    }
                }
                EVENT_BEGINNING_OF_FORM,
                EVENT_GROUP,
                EVENT_REPEAT,
                EVENT_REPEAT_JUNCTURE -> {
                    event = controller.stepToNextEvent()
                    handleEvent()
                }
            }
            event = controller.stepToNextEvent()
        } else {
            traceEventAndQuestion()
            when (event) {
                EVENT_QUESTION -> {
                    questionAt--
                    updateScreenState()
                }
                EVENT_BEGINNING_OF_FORM,
                EVENT_PROMPT_NEW_REPEAT,
                EVENT_GROUP,
                EVENT_REPEAT,
                EVENT_REPEAT_JUNCTURE -> {
                    event = controller.stepToPreviousEvent()
                    handleEvent(forward = false)
                }
            }
            event = controller.stepToPreviousEvent()
        }
    }

    private fun updateScreenState(endOfForm: Boolean = false) {
        val thenState: ScreenState = screenState.value
        if (endOfForm) {
            _screenState.update {
                it.copy(
                    thenState = thenState,
                    endOfForm = true,
                    forWipe = true,
                    showBack = true,
                    showNext = false,
                    formTitle = controller.model.formTitle,
                    questionAt = ++questionAt
                )
            }
            traceEventAndQuestion(_screenState.value)
            return
        }
        val model = controller.model
        val questionPrompt = model.questionPrompt
        val formElement = questionPrompt.formElement
        if (questionAt == 0 && firstQuestionPrompt == null) {
            firstQuestionPrompt = questionPrompt
        }
        val showBack = if (true)
            formElement != firstQuestionPrompt?.formElement
        else questionAt != 0
        val question = questionPrompt.question
        _screenState.update {
            val labelText = question.labelInnerText
            if (false) Times("update")
            it.copy(
                thenState = thenState,
                questionAt = questionAt,
                newWidget_ = false,
                forWipe = questionAt>0||thenState.questionAt==1,
                textFieldState = TextFieldState("[$labelText]"),
                endOfForm = endOfForm,
                showBack = showBack,
                showNext = !endOfForm,
                formTitle = model.formTitle,
                questionSpec = QuestionSpec(
                    captions = model.captionHierarchy,
                    labelText = labelText,
                    helpText = question.helpText ?: "",
                    keyboardType = when (questionPrompt.dataType) {
                        Constants.DATATYPE_DECIMAL -> KeyboardType.Decimal
                        Constants.DATATYPE_TEXT -> KeyboardType.Text
                        Constants.DATATYPE_INTEGER -> KeyboardType.Number
                        else -> KeyboardType.Unspecified
                    }
                )
            )
        }
        val nowAt = screenState.value.questionAt
        val thenAt = screenState.value.thenState?.questionAt ?: -1
        if (false) println("R1: nowAt = $nowAt, thenAt = $thenAt")
        traceEventAndQuestion(_screenState.value)
    }

    fun clearNewWidget_() {
        _screenState.update {
            it.copy(
                newWidget_ = false
            )
        }
    }

    fun clearForWipe() {
        _screenState.update {
            it.copy(
                forWipe = false,
                newWidget_ = false
            )
        }
    }

    fun clearAddRepeat() {
        _screenState.update {
            it.copy(
                addRepeat = false
            )
        }
    }

    private fun nextQuestion() {
        do {
            event = controller.stepToNextEvent()
            traceEventAndQuestion()
            if (event == EVENT_END_OF_FORM) {
                updateScreenState(true)
                return
            }
        } while (event != EVENT_QUESTION)
        questionAt++
        updateScreenState()
    }

    private fun previousQuestion() {
        do {
            event = controller.stepToPreviousEvent()
            traceEventAndQuestion()
        } while (event != EVENT_QUESTION)
        questionAt--
        updateScreenState()
    }

    fun onNext() {
        if (event == EVENT_END_OF_FORM) return
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
        if (questionAt == 0) return
        previousQuestion()
    }

}












