package com.example.recollect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import com.example.recollect.ui.theme.RecollectTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.javarosa.core.model.FormDef
import org.javarosa.core.model.QuestionDef
import org.javarosa.core.model.data.StringData
import org.javarosa.form.api.FormEntryCaption
import org.javarosa.form.api.FormEntryController
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
fun TextStyle.scale(by: Double): TextStyle =
    copy(fontSize = fontSize.times(by),
        lineHeight = lineHeight.times(by))

data class QuestionSpec(
    val textFieldState: TextFieldState = TextFieldState("[A string]"),
    val formDef: FormDef,
    val questionDef: QuestionDef,
    val captions: Array<FormEntryCaption>
) {
    override fun toString(): String {
        return questionDef.run {
            "label: ${labelInnerText} hint: ${helpText}"
        }
    }
}

class FormControl : ComponentActivity() {
    fun getNumbers4_(): Flow<Int> = flow {
        for (i in 4..6) {
            delay(1000)
            emit(i)
        }
    }
    private lateinit var controller: FormEntryController
    var event: Int = -1
    private var emitBad: Boolean=false
    private lateinit var checkResult: (Int) -> Unit
    lateinit var questionSpec: QuestionSpec
    private fun traceEventOrQuestion(spec: QuestionSpec? = null) {
        println("R1: event = $event")
        if (spec == null) return
        println("R1: spec = ${spec.toString()}")
    }
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val formDef by lazy {
            try {
                val formId = resources.getIdentifier(
                    if (false) "form6154" else "all",
                    "raw", packageName
                )
                val inputStream: InputStream = resources.openRawResource(formId)
                return@lazy XFormUtils.getFormFromInputStream(inputStream)
            } catch (e: Exception) {
                println("R1: = $e")
            }
        }
        if (false) println("R1: formDef = $formDef")
        controller = FormEntryController(FormEntryModel(formDef as FormDef?))
        event = controller.model.event
        while (event != FormEntryController.EVENT_QUESTION)
            event = controller.stepToNextEvent()

        update()
        setInputContent()
    }
    private fun setInputContent() {
        setContent {
            RecollectTheme {
                ImePage()
            }
        }
    }
    fun setResultCheck(check: (Int) -> Unit) {
        checkResult = check
    }
    private fun update() {
        val model = controller.model
        questionSpec = QuestionSpec(
            formDef = model.form,
            questionDef = model.questionPrompt.question,
            captions = model.captionHierarchy
        )
        traceEventOrQuestion(questionSpec)
    }
    fun onNext() {
        val answer = StringData(questionSpec.textFieldState.text as String)
        val result = controller.answerQuestion(answer, true)
        emitBad=!emitBad
        checkResult(result-(if (emitBad)1 else 0))
        if (false) return

        event = controller.stepToNextEvent()
        if (event == FormEntryController.EVENT_QUESTION) {
            update()
        }
        traceEventOrQuestion()
    }
    fun onBack() {
        event = controller.stepToPreviousEvent()
    }
}

















