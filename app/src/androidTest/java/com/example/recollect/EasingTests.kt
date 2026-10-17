package com.example.compose.rally

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
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

fun hasRole(role: Role): SemanticsMatcher =
    SemanticsMatcher.expectValue(SemanticsProperties.Role, role)

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
        println("+Hi")
        val next = rule.onNode(MatchStyle.Bare.hasValue)
        next.assertIsDisplayed()
            .assertIsNotEnabled()
            .printToLog("")
        println("+Ho")

    }

    enum class MatchStyle(val hasValue: SemanticsMatcher) {
        Bare(hasText("Down", true)),
        HasRole(hasText("Down", true) and hasRole(Role.Button)),
        CanClick(hasText("Down", true) and hasClickAction())
    }

    @Test
    fun clickEasings() {
        setContentIf()

        val easings2 = nodeHasText("2")
        easings2.assertIsDisplayed()
            .performClick()

        val picks2 = nodeHasText("2P")
        picks2.assertIsDisplayed()

        val easings19 = nodeHasText("19")
        easings19
            .assertIsNotDisplayed()

        val text = "Down"
        val down = if (false) rule.onNode(hasText(text), true)
        else nodeIsButton(text)

        down
            .performClick()
            .performClick()

        easings19.assertIsDisplayed()
        down.printToLog("")
        down.assertIsNotEnabled()
        easings19.performClick()
        val picks19 = nodeHasText("19P")
        picks19.assertIsDisplayed()

        picks2.performClick()
        easings2.assertIsDisplayed()

        picks19.performClick()
        easings19.assertIsDisplayed()

//        Thread.sleep(10000)
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

    private fun nodeHasText(text: String): SemanticsNodeInteraction =
        rule.onNode(hasText(text), true)
}

private fun MyTests.nodeIsButton(text: String): SemanticsNodeInteraction = rule.onNode(
    hasText(text, true) and SemanticsMatcher.expectValue(
        SemanticsProperties.Role, Role.Button
    )
)






















