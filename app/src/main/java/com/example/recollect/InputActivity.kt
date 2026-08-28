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
import org.apache.commons.io.FileUtils
import org.javarosa.core.model.Constants
import org.javarosa.core.model.FormDef
import org.javarosa.core.model.data.StringData
import org.javarosa.core.model.instance.FormInstance
import org.javarosa.core.model.instance.TreeReference
import org.javarosa.core.model.instance.utils.DefaultAnswerResolver
import org.javarosa.core.services.transport.payload.ByteArrayPayload
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
import org.javarosa.model.xform.XFormSerializingVisitor
import org.javarosa.xform.parse.XFormParser
import org.javarosa.xform.util.XFormUtils
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.lang.System.currentTimeMillis
import kotlin.time.Duration.Companion.milliseconds

@Throws(IOException::class, RuntimeException::class)
fun importInstance(instanceFile: File, fec: FormEntryController) {
    val fileName = instanceFile.getName()
    // convert files into a byte array
    val fileBytes = FileUtils.readFileToByteArray(instanceFile)

    // get the root of the saved and template instances
    val savedRoot = XFormParser.restoreDataModel(fileBytes, null).getRoot()
    val saved = savedRoot.treeString()
    val templateRoot = fec.getModel().form.instance.getRoot().deepCopy(true)
    val template = templateRoot.treeString()

    // weak check for matching forms

    // populate the data model
    val tr = TreeReference.rootRef()
    tr.add(templateRoot.name, TreeReference.INDEX_UNBOUND)

    // Here we set the Collect's implementation of the IAnswerResolver.
    // We set it back to the default after select choices have been populated.
    val formDef = fec.getModel().form
    templateRoot.populate(savedRoot, formDef)
    XFormParser.setAnswerResolver(DefaultAnswerResolver())

    // FormInstanceParser.parseInstance is responsible for initial creation of instances. It explicitly sets the
    // main instance name to null so we force this again on deserialization because some code paths rely on the main
    // instance not having a name. Must be before the call on setRoot because setRoot also sets the root's name.
    fec.getModel().form.instance.name = null

    // populated model to current form
    fec.getModel().form.instance.setRoot(templateRoot)
}

const val DoWipe = true
const val ApplyQuestionFromBefore = true
const val QuestionFrom = 0

@Throws(IOException::class)
fun File.saveToFile(inputStream: InputStream) {
    if (exists() && !delete()) {
        throw IOException("Cannot overwrite $absolutePath. Perhaps the file is locked?")
    }
    inputStream.let { input ->
        outputStream().let { output ->
            input.copyTo(output)
            output.close()
        }
    }
}

class InputActivity : ComponentActivity() {
    fun getNumbers4_(): Flow<Int> = flow {
        for (i in 4..6) {
            delay(1000.milliseconds)
            emit(i)
        }
    }

    private lateinit var formName: String
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
        formName = arrayOf(
            "simple",
            "groups",
            "repeats",
            "all",
            "end"
        )[2]
        var formDef = FormDef()
        try {
            val file = File(getExternalFilesDir(null), "$formName.xml")
            val inputStream = FileInputStream(file)
            formDef = XFormUtils.getFormFromInputStream(inputStream)
        } catch (e: Exception) {
            println("R1: = $e")
        }
        controller = FormEntryController(FormEntryModel(formDef))
        val instanceFile = fetchInstanceFile(formName)
        if (instanceFile.exists())
            importInstance(
                instanceFile = instanceFile,
                fec = controller
            )
        event = controller.model.event
        if (false) event = controller.stepToNextEvent()
        if (ApplyQuestionFromBefore)
            while (questionAt < QuestionFrom) {
                nextEvent()
            }
        enableEdgeToEdge()
        setContent {
            RecollectTheme {
                Screens(this)
            }
        }
    }

    private fun nextEvent(forward: Boolean = true) {
        if (forward) {
            event = controller.stepToNextEvent()
            traceEventAndQuestion()
            when (event) {
                EVENT_QUESTION -> {
//                    questionAt++
                    updateScreenState()
                }

                EVENT_END_OF_FORM ->
                    updateScreenState(endOfForm = true)

                EVENT_PROMPT_NEW_REPEAT -> {
                    if (false) _screenState.update {
                        it.copy(
                            addRepeat = false
                        )
                    }
                    else nextEvent()
                }

                EVENT_BEGINNING_OF_FORM,
                EVENT_GROUP,
                EVENT_REPEAT,
                EVENT_REPEAT_JUNCTURE -> {
                    nextEvent()
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
//                    questionAt--
                    updateScreenState(false)
                }

                EVENT_BEGINNING_OF_FORM,
                EVENT_PROMPT_NEW_REPEAT,
                EVENT_GROUP,
                EVENT_REPEAT,
                EVENT_REPEAT_JUNCTURE -> {
                    nextEvent(false)
                }
            }
        }
    }

    fun onNext() {
        traceEventAndQuestion("onNext")
        if (event == EVENT_QUESTION) {
            val answer = StringData(
                _screenState.value.textFieldState.text as String
            )
            val result = controller.answerQuestion(answer, true)
            hasError = if (false) !hasError
            else result != FormEntryController.ANSWER_OK
            _screenState.update {
                it.copy(
                    hasError = hasError,
                )
            }
        }
        if (!hasError && event != EVENT_END_OF_FORM) {
            nextEvent()
        }
    }

    fun saveAsDraft() {
        val formInstance: FormInstance? = controller.model.form.instance
        val serializer = XFormSerializingVisitor()
        val payload = serializer.createSerializedPayload(formInstance)
                as ByteArrayPayload
        fetchInstanceFile(formName).saveToFile(payload.payloadStream)
    }

    private fun fetchInstanceFile(formName: String): File {
        return File(getExternalFilesDir(null), "${formName}Latest.xml")
    }

    fun addRepeat() {
        clearAddRepeat()
    }

    fun onBack() {
        if (questionAt == 0) return
        _screenState.update {
            hasError = false
            it.copy(
                hasError = hasError,
            )
        }
        nextEvent(false)
    }

    private fun updateScreenState(forward: Boolean = true, endOfForm: Boolean = false) {
        val thenState = if (false) _screenState.value else
            _screenState.value.copy(
                wipeTo = if (forward) 0 else 1
            )
        val model = controller.model
        if (endOfForm) {
            _screenState.update {
                it.copy(
                    thenState = thenState,
                    endOfForm = true,
                    forWipe = DoWipe,
                    showBack = true,
                    showNext = false,
                    formTitle = model.formTitle,
                    wipeTo = ++questionAt
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
                wipeTo = if (forward) 1 else 0,
                forWipe = DoWipe &&
                        (questionAt > 0 || thenState.wipeTo == 1),
                textFieldState = TextFieldState(questionPrompt.answerText ?: ""),
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
//        val nowAt = screenState.value.wipeTo
//        val thenAt = screenState.value.thenState?.wipeTo ?: -1
//        if (false) println("R1: nowAt = $nowAt, thenAt = $thenAt")
        traceEventAndQuestion(_screenState.value)
    }

    fun clearForWipe() {
        _screenState.update {
            it.copy(
                forWipe = false,
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

data class ScreenState(
    val textFieldState: TextFieldState = TextFieldState("[A string]"),
    val questionSpec: QuestionSpec = QuestionSpec(),
    val formTitle: String = "",
    val hasError: Boolean = false,
    val showBack: Boolean = false,
    val showNext: Boolean = false,
    val endOfForm: Boolean = false,
    val wipeTo: Int = -1,
    val thenState: ScreenState? = null,
    val forWipe: Boolean = false,
    val addRepeat: Boolean = false
) {
    override fun toString(): String {
        return if (true) "${questionSpec.labelText} $wipeTo"
//            "showBack = $showBack showNext = $showNext "
        else ("${hashCode()}")
    }
}

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

private var timeThen = -1L
fun times(msg: String = "") {
    val timeSince = currentTimeMillis() - timeThen
    if (timeThen < 0 || timeSince > 5000) {
        timeThen = currentTimeMillis()
        println("R1: Times reset in $msg")
    } else println("R1: $msg=${timeSince / 10}")
}

fun getNumbers1_(): Flow<Any> = flow {
    (1..3).forEach { delay(1000.milliseconds) }
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






















