package com.example.compose.rally

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import com.example.recollect.InputActivity
import com.example.recollect.bits.EasingsViewer
import org.junit.Rule
import org.junit.Test

class MyTests {

    private fun setContent() {
        rule.setContent {
            EasingsViewer()
        }
    }

    @Test
    fun printToLog() {
        rule.onRoot(useUnmergedTree = true).printToLog("")
    }

    val useAndroidRule = true

    @get:Rule
    val rule = if (useAndroidRule)
        createAndroidComposeRule<InputActivity>()
    else createComposeRule()

    @Test
    fun scrollAndClick() {
        if (!useAndroidRule) setContent()
        printToLog()

        nodeHasText("2")
            .assertExists()
            .performClick()

        nodeHasText("0+")
            .assertExists()

        Thread.sleep(1000)
    }

    private fun nodeHasText(text: String): SemanticsNodeInteraction =
        rule.onNode(hasText(text), true)
}






















