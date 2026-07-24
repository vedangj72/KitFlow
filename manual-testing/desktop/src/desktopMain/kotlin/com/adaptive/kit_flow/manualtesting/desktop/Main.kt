package com.adaptive.kit_flow.manualtesting.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.adaptive.kit_flow.Adaptive
import com.adaptive.kit_flow.AdaptiveKitProvider
import com.adaptive.kit_flow.accessibility.AdaptiveAccessibleLayout
import com.adaptive.kit_flow.accessibility.LocalAdaptiveAccessibilityInfo
import com.adaptive.kit_flow.accessibility.adaptiveSemantics
import com.adaptive.kit_flow.accessibility.adaptiveTouchTarget
import com.adaptive.kit_flow.rememberWindowInfo

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KitFlow Live Test",
        state = rememberWindowState(width = 420.dp, height = 760.dp)
    ) {
        MaterialTheme {
            AdaptiveKitProvider {
                DemoScreen()
            }
        }
    }
}

@Composable
private fun DemoScreen() {
    val info = rememberWindowInfo()
    val accessibilityInfo = LocalAdaptiveAccessibilityInfo.current
    val screenPadding = Adaptive.value(
        sm = 12.dp,
        md = 16.dp,
        lg = 20.dp,
        tab = 32.dp,
        desktop = 48.dp
    )
    val titleSize = Adaptive.value(
        sm = 20.sp,
        md = 22.sp,
        lg = 24.sp,
        tab = 28.sp,
        desktop = 32.sp
    )
    val cardRadius = Adaptive.value(
        sm = 10.dp,
        md = 14.dp,
        lg = 18.dp,
        tab = 22.dp,
        desktop = 26.dp
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "KitFlow live test",
                fontSize = titleSize
            )

            InfoRow("widthDp", info.widthDp.toString())
            InfoRow("heightDp", info.heightDp.toString())
            InfoRow("shortestSideDp", info.shortestSideDp.toString())
            InfoRow("screenClass", info.screenClass.name)
            InfoRow("layoutClass", info.layoutClass.name)
            InfoRow("orientation", info.orientation.name)
            InfoRow("adaptive padding", screenPadding.toString())
            InfoRow("fontScale", accessibilityInfo.fontScale.toString())
            InfoRow("fontScaleClass", accessibilityInfo.fontScaleClass.name)
            InfoRow("minimumTouchTarget", accessibilityInfo.minimumTouchTarget.toString())
            InfoRow("reducedMotion", accessibilityInfo.reducedMotion.toString())
            InfoRow("highContrast", accessibilityInfo.highContrast.toString())

            Card(shape = RoundedCornerShape(cardRadius)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFFE6F3FF))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Resize the window to test KitFlow")
                }
            }

            Adaptive.onOrientationChange(
                portrait = {
                    Text("Portrait branch is active")
                },
                landscape = {
                    Text("Landscape branch is active")
                },
                square = {
                    Text("Square branch is active")
                }
            )

            AdaptiveAccessibleLayout(
                normal = {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Normal font layout")
                        AccessibleActionBox("Open")
                    }
                },
                largeText = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Large text-safe layout")
                        AccessibleActionBox("Open")
                    }
                }
            )
        }
    }
}

@Composable
private fun AccessibleActionBox(label: String) {
    Box(
        modifier = Modifier
            .adaptiveTouchTarget()
            .background(Color(0xFFDEF7E5), RoundedCornerShape(12.dp))
            .adaptiveSemantics(label = "$label demo action")
            .clickable { }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label)
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String
) {
    Row {
        Text(label)
        Spacer(Modifier.width(12.dp))
        Text(value)
    }
}
