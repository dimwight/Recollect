package com.example.recollect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.recollect.ui.theme.RecollectTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import org.javarosa.core.model.FormDef
import org.javarosa.core.model.data.StringData
import org.javarosa.form.api.FormEntryCaption
import org.javarosa.form.api.FormEntryController
import org.javarosa.form.api.FormEntryController.EVENT_QUESTION
import org.javarosa.form.api.FormEntryModel
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
    val captions: Array<FormEntryCaption> =emptyArray(),
    val labelText: String="",
    val helpText: String="",
    val formTitle: String=""
) {
    override fun toString(): String {
        return this.run {
            "label: $labelText help: $helpText"
        }
    }
}
data class PageState(
    val textFieldState: TextFieldState = TextFieldState("[A string]"),
    val questionSpec: QuestionSpec= QuestionSpec(),
    val hasError: Boolean=false
)

class InputActivity : ComponentActivity() {
    fun getNumbers4_(): Flow<Int> = flow {
        for (i in 4..6) {
            delay(1000)
            emit(i)
        }
    }

    private lateinit var controller: FormEntryController
    var event: Int = -1
    var questionAt = -1
    var questionStop = 1
    private var emitBad: Boolean = false
    private val _pageState = MutableStateFlow(PageState())
    val pageState: StateFlow<PageState> = _pageState.asStateFlow()
    private fun traceEventAndQuestion(spec: QuestionSpec? = null) {
        println("R1: event = $event")
//        println("R1: questionAt = $questionAt")
        if (spec != null)
            println("R1: spec = $spec")
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val formDef by lazy {
            try {
                val formId = resources.getIdentifier(
                    "all", "raw", packageName
                )
                val inputStream: InputStream = resources.openRawResource(formId)
                return@lazy XFormUtils.getFormFromInputStream(inputStream)
            } catch (e: Exception) {
                println("R1: = $e")
            }
        }
        controller = FormEntryController(FormEntryModel(formDef as FormDef?))
       if (false)
        for (at in 0..3) {
            event = controller.stepToNextEvent()
                if (event == EVENT_QUESTION) {
                    buildQuestionDetails()
                    traceEventAndQuestion(_pageState.value.questionSpec)
                } else traceEventAndQuestion()
            }
        event = controller.model.event
        nextQuestion()
        setInputContent()
    }

    private fun buildQuestionDetails() {
        val model = controller.model
        val questionDef = model.questionPrompt.question
        _pageState.update {
            it.copy(
                questionSpec = QuestionSpec(
                    captions = model.captionHierarchy,
                    labelText = questionDef.labelInnerText,
                    helpText = questionDef.helpText,
                    formTitle = model.formTitle
                )
            )
        }
    }

    private fun setInputContent() {
        enableEdgeToEdge()
        setContent {
            RecollectTheme {
                ImePage()
            }
        }
    }

    private fun nextQuestion() {
        do {
            event = controller.stepToNextEvent()
            traceEventAndQuestion()
        } while (event != EVENT_QUESTION)
        questionAt++
        traceEventAndQuestion()
        if (false &&
            questionAt > questionStop
        ) return
        buildQuestionDetails()
        traceEventAndQuestion(_pageState.value.questionSpec)
    }

    private lateinit var resultNotice: (Int) -> Unit
    fun setResultNotice(notice: (Int) -> Unit) {
        resultNotice = notice
    }

    fun onNext() {
        val answer = StringData(_pageState.value.textFieldState.text
                as String)
        val result = controller.answerQuestion(answer, true)
        if (false) emitBad = !emitBad
        resultNotice.invoke(result - (if (emitBad) 1 else 0))
        if (!emitBad) nextQuestion()
    }

    fun onBack() {
        event = controller.stepToPreviousEvent()
    }
}












