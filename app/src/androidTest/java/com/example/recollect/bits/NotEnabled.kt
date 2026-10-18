package com.example.recollect.bits

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.printToLog
import com.example.recollect.timeMillis_
import org.junit.Rule
import org.junit.Test

fun hasRole(role: Role): SemanticsMatcher =
    SemanticsMatcher.expectValue(SemanticsProperties.Role, role)


private enum class MatchStyle(val hasValue: SemanticsMatcher) {
    Bare(hasText("Down", true)),
    HasRole(hasText("Down", true) and hasRole(Role.Button)),
    CanClick(hasText("Down", true) and hasClickAction())
}

class MyTests {

    private fun setContentIf() {
        timeMillis_()
        if (!true) rule.setContent {
            EasingsViewer()
        }
        timeMillis_("setContent~")
    }

    @get:Rule
    val rule = createComposeRule()

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
}