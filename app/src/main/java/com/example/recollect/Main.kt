package com.example.recollect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recollect.ui.theme.RecollectTheme
import org.javarosa.xform.util.XFormUtils
import org.javarosa.core.model.FormDef
import java.io.InputStream

class Main : ComponentActivity() {
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

        setContent {
            RecollectTheme {
                ScaffoldSet()
            }
        }
    }
}


@Composable
fun BackNext() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {println("R1: = back")}) {
            Text("Back")
        }

        Button(
            onClick={println("R1: = next")},
        ) {
            Text("Next")
        }
    }


}











