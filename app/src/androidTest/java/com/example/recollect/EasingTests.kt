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
import androidx.compose.ui.test.performScrollTo
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
    fun scrollTo() {
        setContentIf()

        val up = nodeAsButton("Up")
            .assertIsNotEnabled()
        val down = nodeAsButton("Down")
            .assertIsEnabled()

        val easing0 = nodeWithText("0")
            .assertIsDisplayed()

        down.performClick()
        easing0.assertIsNotDisplayed()
        up.assertIsEnabled()

        nodeWithText("29").assertExists()
            .performScrollTo()
        down.assertIsNotEnabled()

//        Thread.sleep(20000)
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

        down.performClick()
        easing0.assertIsNotDisplayed()
        up.assertIsEnabled()

        val easing29 = nodeWithText("29")
            .assertIsNotDisplayed()
            .performScrollTo()
        down.assertIsNotEnabled()

        val pick0 = nodeWithText("0P")
            .assertIsDisplayed()
            .performClick()
        easing0.assertIsDisplayed()

        down
            .performClick()
            .performClick()
            .performClick()
            .performClick()
            .assertIsNotEnabled()
        up.assertIsEnabled()

        easing29.assertIsDisplayed()
            .performClick()

        val pick29 = nodeWithText("29P")
            .assertIsDisplayed()

        pick0.performClick()
        easing0.assertIsDisplayed()
        up.assertIsNotEnabled()
        down.assertIsEnabled()

        pick29.performClick()
//        easing29.assertIsDisplayed()
        up.assertIsEnabled()
        down.assertIsNotEnabled()
//        Thread.sleep(20000)

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




















