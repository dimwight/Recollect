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
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.TimeSource.Monotonic

var time_: Monotonic.ValueTimeMark? = null

var start = -1L
fun times(msg: String = "") {
    val elapsed = currentTimeMillis() - start
    if (start < 0 || elapsed > 5000) {
        start = currentTimeMillis()
        println("R1: Times reset in $msg")
    } else println("R1: $msg=${elapsed / 10}")
}

fun getNumbers1_(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000.milliseconds)
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
        return if (true) "${questionSpec.labelText} $questionAt"
//            "showBack = $showBack showNext = $showNext "
        else ("${hashCode()}")
    }
}


class InputActivity : ComponentActivity() {
    fun getNumbers4_(): Flow<Int> = flow {
        for (i in 4..6) {
            delay(1000.milliseconds)
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
    private fun traceEventAndQuestion(msg: Any? = null) {
        val top = if (msg != null && msg !is ScreenState) " msg = $msg" else ""
        val tail = " event = $event questionAt = $questionAt"
        println("R1:$top$tail")
        val state: ScreenState? =
            if (msg != null && msg is ScreenState) msg else null
        if (state != null)
            println("R1: state = $state ")
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
        if (false) event = controller.stepToNextEvent()
        if (ApplyQuestionFromBefore) while (questionAt < QuestionFrom) doNext()
        enableEdgeToEdge()
        setContent {
            RecollectTheme {
                Screens(this)
            }
        }
    }

    private fun doNext(forward: Boolean = true) {
        if (false) {
            if (forward) nextQuestion()
            else previousQuestion()
        } else handleNextEvent(forward)
    }

    companion object {
        const val QuestionFrom = 0
        const val ApplyQuestionFromBefore = true
    }

    private fun handleNextEvent(forward: Boolean = true) {
        if (forward) {
            event = controller.stepToNextEvent()
            traceEventAndQuestion()
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
                            addRepeat = false
                        )
                    }
                }

                EVENT_BEGINNING_OF_FORM,
                EVENT_GROUP,
                EVENT_REPEAT,
                EVENT_REPEAT_JUNCTURE -> {
                    handleNextEvent()
                }
            }
            /* EVENT_BEGINNING_OF_FORM = 0;
                  EVENT_END_OF_FORM = 1;
                  EVENT_PROMPT_NEW_REPEAT = 2;
                  EVENT_QUESTION = 4;
                  EVENT_GROUP = 8;
                  EVENT_REPEAT = 16;
                  EVENT_REPEAT_JUNCTURE = 32;*/
        } else {
            event = controller.stepToPreviousEvent()
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
                    handleNextEvent(forward = false)
                }
            }
        }
    }

    val doWipe: Boolean = true

    private fun updateScreenState(endOfForm: Boolean = false) {
        val thenState: ScreenState = _screenState.value
        val model = controller.model
        if (endOfForm) {
            _screenState.update {
                it.copy(
                    thenState = thenState,
                    endOfForm = true,
                    forWipe = doWipe,
                    showBack = true,
                    showNext = false,
                    formTitle = model.formTitle,
                    questionAt = ++questionAt
                )
            }
            traceEventAndQuestion(_screenState.value)
            return
        }
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
            if (false) times("update")
            it.copy(
                thenState = thenState,
                questionAt = questionAt,
                newWidget_ = false,
                forWipe = doWipe &&
                        (questionAt > 0 || thenState.questionAt == 1),
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


    fun onNext() {
        traceEventAndQuestion("onNext")
        if (false|| event != EVENT_QUESTION) {
            doNext()
            return
        }
        val answer = StringData(
            _screenState.value.textFieldState.text as String
        )
        val result = controller.answerQuestion(answer, true)
        if (true) hasError = !hasError
        _screenState.update {
            it.copy(
                hasError = hasError,
            )
        }
        if (!hasError) doNext()
    }

    fun onBack() {
        if (true&& questionAt == 0) return
        doNext(false)
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
}












