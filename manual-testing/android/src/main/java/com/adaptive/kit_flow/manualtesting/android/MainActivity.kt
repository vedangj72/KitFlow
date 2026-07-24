package com.adaptive.kit_flow.manualtesting.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.adaptive.kit_flow.Adaptive
import com.adaptive.kit_flow.AdaptiveKitProvider
import com.adaptive.kit_flow.accessibility.AdaptiveAccessibleLayout
import com.adaptive.kit_flow.accessibility.LocalAdaptiveAccessibilityInfo
import com.adaptive.kit_flow.accessibility.adaptiveSemantics
import com.adaptive.kit_flow.accessibility.adaptiveTouchTarget
import com.adaptive.kit_flow.rememberWindowInfo

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                AdaptiveKitProvider {
                    AndroidDemoScreen()
                }
            }
        }
    }
}

@Composable
private fun AndroidDemoScreen() {
    val windowInfo = rememberWindowInfo()
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
                .verticalScroll(rememberScrollState())
                .padding(screenPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "KitFlow Android test",
                fontSize = titleSize
            )

            InfoRow("widthDp", windowInfo.widthDp.toString())
            InfoRow("heightDp", windowInfo.heightDp.toString())
            InfoRow("screenClass", windowInfo.screenClass.name)
            InfoRow("layoutClass", windowInfo.layoutClass.name)
            InfoRow("orientation", windowInfo.orientation.name)
            InfoRow("fontScale", accessibilityInfo.fontScale.toString())
            InfoRow("fontScaleClass", accessibilityInfo.fontScaleClass.name)
            InfoRow("minimumTouchTarget", accessibilityInfo.minimumTouchTarget.toString())
            InfoRow("reducedMotion", accessibilityInfo.reducedMotion.toString())

            Card(shape = RoundedCornerShape(cardRadius)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFE6F3FF))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Rotate device and change Android font size to test")
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
                    AdaptiveRowsDemo(layoutName = "Row layout")
                },
                largeText = {
                    AdaptiveColumnsDemo(layoutName = "Column layout for large text")
                }
            )
        }
    }
}

@Composable
private fun AdaptiveRowsDemo(layoutName: String) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(layoutName)
        repeat(8) { index ->
            DemoRowItem(index = index + 1)
        }
    }
}

@Composable
private fun AdaptiveColumnsDemo(layoutName: String) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(layoutName)
        repeat(8) { index ->
            DemoColumnItem(index = index + 1)
        }
    }
}

@Composable
private fun DemoRowItem(index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7FA), RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Setting $index",
            modifier = Modifier.weight(1f)
        )
        AccessibleActionBox("Primary")
        AccessibleActionBox("Secondary")
    }
}

@Composable
private fun DemoColumnItem(index: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7FA), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Setting $index")
        AccessibleActionBox("Primary")
        AccessibleActionBox("Secondary")
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
