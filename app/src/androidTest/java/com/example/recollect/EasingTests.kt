package com.example.recollect

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.recollect.bits.EasingsViewer
import org.junit.Rule
import org.junit.Test

class EasingTests {

    val useAndroidRule = true

    @get:Rule
    val rule = if (useAndroidRule)
        createAndroidComposeRule<InputActivity>()
    else createComposeRule()

    private fun nodeHasText(text: String): SemanticsNodeInteraction =
        rule.onNode(hasText(text), true)

    private fun nodeAsButton(text: String): SemanticsNodeInteraction {
        val node = rule.onNode(
            hasText(text, true) and SemanticsMatcher.expectValue(
                SemanticsProperties.Role, Role.Button
            )
        )
        node.printToLog("")
        return node
    }

    private fun setContentIf() {
        timeMillis_()
        if (!useAndroidRule) rule.setContent {
            EasingsViewer()
        }
        timeMillis_("setContent~")
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

        val down = nodeAsButton("Down")

        down
            .performClick()
            .performClick()

        easings19.assertIsDisplayed()
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

        nodeHasText("W0").assertExists()

        nodeHasText("2").assertExists()
            .performClick()

        nodeHasText("2P").assertExists()

        val animationDurationMillis = 1000L
        rule.waitUntil(timeoutMillis = animationDurationMillis * 5 / 3) {
            rule.onAllNodes(hasText("W1", true))
                .fetchSemanticsNodes().isNotEmpty()
        }
        timeMillis_("scrollAndClick~")

//        Thread.sleep(1000)
    }
}




















