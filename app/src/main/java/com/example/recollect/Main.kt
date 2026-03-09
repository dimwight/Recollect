package com.example.recollect

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.recollect.ui.theme.RecollectTheme
import org.javarosa.xform.util.XFormUtils
import org.javarosa.core.model.FormDef

class Main : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val formName = "form6154"
        val identifier = resources.getIdentifier(formName, "raw", packageName)
       try {
           resources.openRawResource(identifier)
       }catch (e: Exception){
           println("Rec1: = ${e}")
       }
        if (false) {
            XFormUtils.getFormFromFormXml("","")
        }


        println("Rec1: formName = ${formName}")
        enableEdgeToEdge()
        setContent {
            RecollectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecollectTheme {
        Greeting("Android")
    }
}