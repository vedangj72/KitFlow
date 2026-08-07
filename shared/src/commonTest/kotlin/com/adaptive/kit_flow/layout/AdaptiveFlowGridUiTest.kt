package com.adaptive.kit_flow.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.v2.runComposeUiTest
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertTrue

class AdaptiveFlowGridUiTest {
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun boundedParentPreservesUnevenRowsWhenContentFits() = runComposeUiTest {
        setContent {
            Box(Modifier.requiredSize(100.dp)) {
                AdaptiveFlowGrid(
                    modifier = Modifier.testTag("grid"),
                    minColumnWidth = 100.dp,
                    maxColumns = 1,
                    verticalSpacing = 12.dp,
                    fontScaleAware = false
                ) {
                    Box(Modifier.height(70.dp).testTag("tall"))
                    Box(Modifier.height(10.dp).testTag("short"))
                }
            }
        }

        val grid = onNodeWithTag("grid").fetchSemanticsNode().boundsInRoot
        val tall = onNodeWithTag("tall").fetchSemanticsNode().boundsInRoot
        val short = onNodeWithTag("short").fetchSemanticsNode().boundsInRoot

        assertClose(92f, grid.height)
        assertClose(70f, tall.height)
        assertClose(10f, short.height)
        assertClose(82f, short.top - grid.top)
        assertTrue(short.bottom <= grid.bottom)
    }

    private fun assertClose(expected: Float, actual: Float) {
        assertTrue(
            abs(expected - actual) < 0.5f,
            "expected $expected but was $actual"
        )
    }
}
