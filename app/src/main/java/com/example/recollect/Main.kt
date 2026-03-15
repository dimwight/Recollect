package com.example.recollect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import com.example.recollect.ui.theme.RecollectTheme
import org.javarosa.core.model.FormDef
import org.javarosa.form.api.FormEntryController
import org.javarosa.form.api.FormEntryModel
import org.javarosa.xform.util.XFormUtils
import java.io.InputStream

class Main : ComponentActivity() {
   private lateinit var controller: FormEntryController
   var event: Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var formDef: FormDef
        try {
            val formId = resources.getIdentifier("form6154", "raw", packageName)
            val inputStream: InputStream = resources.openRawResource(formId)
            formDef = XFormUtils.getFormFromInputStream(inputStream)
        } catch (e: Exception) {
            println("R1: = $e")
        }
        println("R1: formDef = $formDef")
        controller = FormEntryController(FormEntryModel(formDef))
        event = controller.model.event
        println("R1: event = $event")

        setContent {
            RecollectTheme {
                ScaffoldSet()
            }
        }
    }

    fun onNext() {
        event = controller.stepToNextEvent()
        println("R1: event = $event")
    }
    fun onBack() {
        event = controller.stepToPreviousEvent()
        println("R1: event = $event")
    }
}











