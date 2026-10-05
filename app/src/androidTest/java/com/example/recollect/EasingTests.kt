package com.example.compose.rally

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.printToLog
import com.example.recollect.bits.EasingsViewer
import org.junit.Rule
import org.junit.Test

class MyTests {

    private fun setContent() {
        rule.setContent {
            EasingsViewer()
        }
        rule.onRoot(useUnmergedTree = true)
            .printToLog("Easings")
    }

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun scrollAndClick() {
        setContent()

        rule
            .onNode(hasText("2"),true)
            .assertIsDisplayed()
            .assertExists()
            .performScrollTo()
            .performClick()

        Thread.sleep(50000)
    }
    @Test
    fun printToLog() {
        setContent()
    }
}






















