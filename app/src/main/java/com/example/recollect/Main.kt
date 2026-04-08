package com.example.recollect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.recollect.bits.Ime
import com.example.recollect.bits.Pad
import com.example.recollect.ui.theme.RecollectTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.javarosa.core.model.FormDef
import org.javarosa.form.api.FormEntryController
import org.javarosa.form.api.FormEntryModel
import org.javarosa.xform.util.XFormUtils
import java.io.InputStream

fun getNumbers1(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000)
    }
}

data class QuestionDetails(
    val textFieldState: TextFieldState = TextFieldState("[A string]"),
    val questionText: String
)

class Main : ComponentActivity() {
    lateinit var questionDetails: QuestionDetails

    fun getNumbers4(): Flow<Int> = flow {
        for (i in 4..6) {
            delay(1000)
            emit(i)
        }
    }

    private lateinit var controller: FormEntryController
    var event: Int = -1

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

        val questionPrompt = controller.model.questionPrompt
        val questionText = questionPrompt.questionText
        questionDetails = QuestionDetails(questionText = questionText)
        if (false) traceQuestionOrPrompt(questionText)

        enableEdgeToEdge()

        setContent {
            RecollectTheme {
                if (false) FormPage(questionDetails)
                else Pad()
            }
        }

    }

    private fun traceQuestionOrPrompt(prompt: String? = null) {
        println("R1: event = $event")
        if (prompt == null) return
        println("R1: prompt = $prompt")
    }

    fun onNext() {
        println("R1: textFieldState = ${questionDetails.
            textFieldState.text}")
        event = controller.stepToNextEvent()
        traceQuestionOrPrompt()
    }

    fun onBack() {
        event = controller.stepToPreviousEvent()
        traceQuestionOrPrompt()
    }
}











