package com.example.recollect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
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
import kotlin.time.Duration.Companion.seconds

@Preview
@Composable
fun UserProfileScreen() {
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }

    LaunchedEffect("") {
        delay(5.seconds)
        userProfile = fetchUserProfile()
    }

    val let = userProfile?.let {
        Text("User name: ${it.name}")
    }
    if (let != null) let else CircularProgressIndicator()
}

private fun fetchUserProfile(): UserProfile =
    UserProfile()

data class UserProfile(val name: String="Fred")


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
        setContent {
            RecollectTheme {
                ScaffoldSet_()
            }
        }
        if (true) return
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











