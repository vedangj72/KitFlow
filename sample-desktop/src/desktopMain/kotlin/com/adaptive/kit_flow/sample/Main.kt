package com.adaptive.kit_flow.sample

import androidx.compose.foundation.background
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
import com.adaptive.kit_flow.KitFlowAdaptiveProvider
import com.adaptive.kit_flow.rememberWindowInfo

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KitFlow Live Test",
        state = rememberWindowState(width = 420.dp, height = 760.dp)
    ) {
        MaterialTheme {
            KitFlowAdaptiveProvider {
                DemoScreen()
            }
        }
    }
}

@Composable
private fun DemoScreen() {
    val info = rememberWindowInfo()
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
        }
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
