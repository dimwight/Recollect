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
import com.example.recollect.EasingTestBase.ButtonLabel.Clear
import com.example.recollect.EasingTestBase.ButtonLabel.Down
import com.example.recollect.EasingTestBase.ButtonLabel.Up
import com.example.recollect.bits.Easings
import com.example.recollect.bits.WipeViewer
import com.example.recollect.bits.RandomListBuilder
import org.junit.Rule
import org.junit.Test
import kotlin.collections.map
import kotlin.random.Random

class EasingTests : EasingTestBase() {


    @Test
    fun topToTail() {
        setContentIf()

        val getter =
            RandomListBuilder(
                random = Random(1),
                src = Easings.map() { it.listAt } as MutableList<Int>)
                .build().iterator()
        var passes=0
        while (getter.hasNext()) {
            val top = getter.next()
            val tail = getter.next()
            println("R1: " +
                    "\r passes = ${++passes}" +
                    "\r top = $top" +
                    "\r tail = $tail"
            )
            if (false&& top==6)continue
            if (!getter.hasNext())
                return
            easing(top)
                .performClick()
            easing(tail)
                .performScrollTo()
                .performClick()

            pick(top)
                .performClick()
//            rule.waitForIdle()
            easing(top)
                .assertIsDisplayed()

            pick(tail)
                .performClick()
//            rule.waitForIdle()
            easing(tail)
                .assertIsDisplayed()

//            Thread.sleep(2000)

            button(Clear)
                .performClick()
                .performClick()

        }
    }

    @Test
    fun scrollTo() {
        setContentIf()

        val up = button(Up)
            .assertIsNotEnabled()
        val down = button(Down)
            .assertIsEnabled()

        easing(0)
            .assertIsDisplayed()

        down.performClick()
        easing(0).assertIsNotDisplayed()
        up.assertIsEnabled()

        easing(29)//.assertExists()
            .performScrollTo()
        down.assertIsNotEnabled()

//        Thread.sleep(20000)
    }

    @Test
    fun downAndUp() {
        setContentIf()

        val up = button(Up)
            .assertIsNotEnabled()
        val down = button(Down)
            .assertIsEnabled()

        easing(0)
            .assertIsDisplayed()
            .performClick()

        down.performClick()
        easing(0).assertIsNotDisplayed()
        up.assertIsEnabled()

        easing(29)
            .assertIsNotDisplayed()
            .performScrollTo()
        down.assertIsNotEnabled()

        pick(0)
            .assertIsDisplayed()
            .performClick()
        easing(0).assertIsDisplayed()

        down
            .performClick()
            .performClick()
            .performClick()
            .performClick()
            .assertIsNotEnabled()
        up.assertIsEnabled()

        easing(29).assertIsDisplayed()
            .performClick()

        pick(29)
            .assertIsDisplayed()

        pick(0).performClick()
        easing(0).assertIsDisplayed()
        up.assertIsNotEnabled()
        down.assertIsEnabled()

        pick(29).performClick()
        easing(29).assertIsDisplayed()
        up.assertIsEnabled()
        down.assertIsNotEnabled()
//        Thread.sleep(20000)
    }

    @Test
    fun a_wipeIsTriggered() {
        setContentIf()

        nodeWithText("W0").assertExists()

        easing(2)//.assertExists()
            .performClick()

        pick(2).assertExists()

        val animationDurationMillis = 1000L
        rule.waitUntil(timeoutMillis = animationDurationMillis * 4 / 3) {
            rule.onAllNodes(hasText("W1", true))
                .fetchSemanticsNodes().isNotEmpty()
        }
//        Thread.sleep(20000)
    }

}

abstract class EasingTestBase {

    private val useAndroidRule = false

    @get:Rule
    val rule = if (useAndroidRule)
        createAndroidComposeRule<InputActivity>()
    else createComposeRule()

    fun setContentIf() {
        if (!useAndroidRule) rule.setContent {
            WipeViewer()
        }
    }

    fun nodeWithText(text: String): SemanticsNodeInteraction =
        rule.onNode(hasText(text), true)

    enum class ButtonLabel {
        Down, Up, Clear
    }

    fun button(label: ButtonLabel): SemanticsNodeInteraction {
        val node = rule.onNode(
            hasText(label.name, true) and SemanticsMatcher.expectValue(
                SemanticsProperties.Role, Role.Button
            )
        )
        node.printToLog("")
        return node
    }

    fun easing(n: Int): SemanticsNodeInteraction = nodeWithText("$n")
    fun pick(n: Int): SemanticsNodeInteraction = nodeWithText("${n}P")

}




















