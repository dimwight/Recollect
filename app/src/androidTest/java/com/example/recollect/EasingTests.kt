package com.example.compose.rally

import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.recollect.InputActivity
import com.example.recollect.bits.EasingsViewer
import com.example.recollect.timeMillis_
import org.junit.Rule
import org.junit.Test

class MyTests {

    private fun setContentIf() {
        timeMillis_()
        if (!useAndroidRule) rule.setContent {
            EasingsViewer()
        }
        timeMillis_("setContent~")
    }

    @Test
    fun printToLog() {
        rule.onRoot(useUnmergedTree = true)
            .printToLog("")
        timeMillis_("printToLog~")
    }

    val useAndroidRule = true

    @get:Rule
    val rule = if (useAndroidRule)
        createAndroidComposeRule<InputActivity>()
    else createComposeRule()

    @Test
    fun assertIsNotEnabled() {
        setContentIf()
        printToLog()
        println("+Hi")
        rule.onNode(hasText("Down"), true).assertIsNotEnabled()
        println("+Ho")

        Thread.sleep(10000)
    }
    @Test
    fun clickEasings() {
        setContentIf()
        printToLog()

        val easings2 = nodeHasText("2")
        easings2.assertIsDisplayed()
            .performClick()

        val picks2 = nodeHasText("2P")
        picks2.assertIsDisplayed()

        val easings19 = nodeHasText("19")
        easings19
            .assertIsNotDisplayed()

        val down = nodeHasText("Down")
        down
            .performClick()
            .performClick()

        easings19.assertIsDisplayed()
        printToLog()
        println("+Hi")
        val node = rule.onNode(hasText("Down"), true)
        node.assertIsNotEnabled()
        println("+Ho")
        easings19.performClick()
        val picks19 = nodeHasText("19P")
        picks19.assertIsDisplayed()

        picks2.performClick()
        easings2.assertIsDisplayed()

        picks19.performClick()
        easings19.assertIsDisplayed()

        Thread.sleep(10000)
    }
    @Test
    fun wipeIsTriggered() {
        setContentIf()
        printToLog()

        nodeHasText("W0").assertExists()

        nodeHasText("2").assertExists()
            .performClick()

        nodeHasText("2P").assertExists()

        val animationDurationMillis = 1000L
        rule.waitUntil(timeoutMillis = animationDurationMillis * 4 / 3) {
            rule.onAllNodes(hasText("W1", true))
                .fetchSemanticsNodes().isNotEmpty()
        }
        timeMillis_("scrollAndClick~")

        Thread.sleep(1000)
    }

    private fun nodeHasText(text: String): SemanticsNodeInteraction {
//        timeMillis_()
        val node = rule.onNode(hasText(text), true)
//        timeMillis_("$text~")
        return node
    }
}






















