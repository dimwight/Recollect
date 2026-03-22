package com.example.recollect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.recollect.ui.theme.RecollectTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.javarosa.core.model.FormDef
import org.javarosa.form.api.FormEntryController
import org.javarosa.form.api.FormEntryModel
import org.javarosa.form.api.FormEntryPrompt
import org.javarosa.xform.util.XFormUtils
import java.io.InputStream

fun getNumbers1(): Flow<Int> = flow {
    for (i in 1..3) {
        delay(1000)
        emit(i)
    }
}

class Main : ComponentActivity() {
    fun getNumbers4(): Flow<Int> = flow {
        for (i in 4..6) {
            delay(1000)
            emit(i)
        }
    }

    private lateinit var controller: FormEntryController
    var event: Int = -1

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
        println("R1: formDef = $formDef")
        controller = FormEntryController(FormEntryModel(formDef as FormDef?))
        event = controller.model.event
        while (event != FormEntryController.EVENT_QUESTION)
            event = controller.stepToNextEvent()
        val index = controller.model.formIndex
        traceQuestionOrPrompt()

        setContent {
            RecollectTheme {
                ScaffoldSet()
            }
        }
    }

    private fun traceQuestionOrPrompt() {
        println("R1: event = $event")
        if (event != FormEntryController.EVENT_QUESTION) return
        val prompt: FormEntryPrompt? = controller.model.questionPrompt
        println("R1: prompt = ${prompt?.questionText}")
    }

    fun onNext() {
        event = controller.stepToNextEvent()
        traceQuestionOrPrompt()
    }

    fun onBack() {
        event = controller.stepToPreviousEvent()
        traceQuestionOrPrompt()
    }
}











