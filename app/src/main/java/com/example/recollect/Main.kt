package com.example.recollect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
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
            println("R1: = ${e}")
        }
        println("R1: formDef = $formDef")

        setContent {
            RecollectTheme {
                ScaffoldSet()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BackNext() {
    Row (Modifier.height(IntrinsicSize.Min)){
        Button(
            onClick = {}) {
            Text("Back")
        }

        Button(
            onClick={},
   //         modifier = Modifier.fillMaxHeight()
        ) {
            Text("Next")
        }
    }
}

