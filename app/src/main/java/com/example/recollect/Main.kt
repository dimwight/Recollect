package com.example.recollect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.recollect.ui.theme.RecollectTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.javarosa.core.model.FormDef
import org.javarosa.core.model.QuestionDef
import org.javarosa.form.api.FormEntryController
import org.javarosa.form.api.FormEntryModel
import org.javarosa.xform.util.XFormUtils
import java.io.InputStream

fun getNumbers1(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000)
    }
}

fun QuestionDef.toString(): String {
    return "labelInnerText = ${labelInnerText}" +
            "helpText = ${helpText}"
}

data class QuestionSpec(
    val textFieldState: TextFieldState = TextFieldState("[A string]"),
    val questionDef: QuestionDef
){
    override fun toString(): String {
        return questionDef.run {
            "label: ${labelInnerText} hint: ${helpText}"
        }
    }
}



class Main : ComponentActivity() {
    lateinit var questionSpec: QuestionSpec

    fun getNumbers4(): Flow<Int> = flow {
        for (i in 4..6) {
            delay(1000)
            emit(i)
        }
    }

    private lateinit var controller: FormEntryController
    var event: Int = -1

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
        setContent()

    }

    private fun update() {
        val questionPrompt = controller.model.questionPrompt
        val questionDef = questionPrompt.question
        questionSpec = QuestionSpec(questionDef = questionDef)
        traceEventOrQuestion(questionSpec)
    }

    fun onNext() {
        event = controller.stepToNextEvent()
        if (event == FormEntryController.EVENT_QUESTION){
            update()
        }
        traceEventOrQuestion()
    }

    fun onBack() {
        event = controller.stepToPreviousEvent()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    private fun setContent() {
        setContent {
            RecollectTheme {
                FormPage()
            }
        }
    }

}











