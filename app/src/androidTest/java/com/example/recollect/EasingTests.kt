package com.example.recollect

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.recollect.bits.WipeViewer
import org.junit.Rule
import org.junit.Test

class EasingTests {

    val useAndroidRule = false

    private fun setContentIf() {
        timeMillis_()
        if (!useAndroidRule) rule.setContent {
            WipeViewer()
        }
        timeMillis_("setContentIf~")
    }

    @get:Rule
    val rule = if (useAndroidRule)
        createAndroidComposeRule<InputActivity>()
    else createComposeRule()

    private fun nodeWithText(text: String): SemanticsNodeInteraction =
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


    @Test
    fun downAndUp() {
        setContentIf()

        val up = nodeAsButton("Up")
            .assertIsNotEnabled()
        val down = nodeAsButton("Down")
            .assertIsEnabled()

        val easing0 = nodeWithText("0")
            .assertIsDisplayed()
            .performClick()

        val pick0 = nodeWithText("0P")
            .assertIsDisplayed()

        val easing19 = nodeWithText("19")
            .assertIsNotDisplayed()

        down
            .performClick()
            .performClick()
            .assertIsNotEnabled()
        up.assertIsEnabled()

        easing19.assertIsDisplayed()
            .performClick()

        val pick19 = nodeWithText("19P")
            .assertIsDisplayed()

        pick0.performClick()
        easing0.assertIsDisplayed()
        up.assertIsNotEnabled()
        down.assertIsEnabled()

        pick19.performClick()
        easing19.assertIsDisplayed()
        up.assertIsEnabled()
        down.assertIsNotEnabled()
        Thread.sleep(20000)

    }

    @Test
    fun a_wipeIsTriggered() {
        setContentIf()

        nodeWithText("W0").assertExists()

        nodeWithText("2").assertExists()
            .performClick()

        nodeWithText("2P").assertExists()

        val animationDurationMillis = 1000L
        rule.waitUntil(timeoutMillis = animationDurationMillis * 4 / 3) {
            rule.onAllNodes(hasText("W1", true))
                .fetchSemanticsNodes().isNotEmpty()
        }
//        Thread.sleep(20000)
    }
}




















