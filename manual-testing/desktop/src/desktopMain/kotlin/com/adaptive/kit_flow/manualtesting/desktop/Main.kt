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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
import com.adaptive.kit_flow.layout.AdaptiveFlowGrid
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem
import com.adaptive.kit_flow.modules.kitcomponent.scaffoldings.KitNavScaffold
import com.adaptive.kit_flow.modules.kitcomponent.utils.NavItemsPosition
import com.adaptive.kit_flow.rememberWindowInfo

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KitFlow Live Test",
        state = rememberWindowState(width = 1024.dp, height = 760.dp)
    ) {
        val navItems = remember {
            listOf(
                KitNavItem(id = "home", title = "Home"),
                KitNavItem(id = "explore", title = "Explore"),
                KitNavItem(id = "profile", title = "Profile"),
                KitNavItem(id = "settings", title = "Settings")
            )
        }
        var navPosition by remember { mutableStateOf(NavItemsPosition.MID) }

        MaterialTheme {
            AdaptiveKitProvider {
                KitNavScaffold(
                    navItemsPosition = navPosition,
                    navItems = navItems,
                    header = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "KitFlow App",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Row(
                                modifier = Modifier
                                    .background(Color(0xFFE5E7EB), RoundedCornerShape(8.dp))
                                    .padding(2.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf(
                                    NavItemsPosition.START to "Start",
                                    NavItemsPosition.MID to "Mid",
                                    NavItemsPosition.END to "End"
                                ).forEach { (pos, label) ->
                                    val active = navPosition == pos
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                if (active) Color.White else Color.Transparent,
                                                RoundedCornerShape(6.dp)
                                            )
                                            .clickable { navPosition = pos }
                                            .padding(horizontal = 10.dp, vertical = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 12.sp,
                                            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
                                            color = if (active) Color(0xFF1F2937) else Color(0xFF6B7280)
                                        )
                                    }
                                }
                            }
                        }
                    },
                    toolbar = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "KitFlow",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    },
                    footer = { onNavigate ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF3F4F6))
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "© 2026 KitFlow Multiplatform", fontSize = 12.sp)

                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                Text(
                                    text = "Settings",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2563EB),
                                    modifier = Modifier.clickable { onNavigate("settings") }
                                )
                                Text(
                                    text = "Profile",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2563EB),
                                    modifier = Modifier.clickable { onNavigate("profile") }
                                )
                                Text(
                                    text = "Privacy Policy",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2563EB),
                                    modifier = Modifier.clickable { onNavigate("privacy") }
                                )
                                Text(
                                    text = "Terms",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF2563EB),
                                    modifier = Modifier.clickable { onNavigate("terms") }
                                )
                            }
                        }
                    }
                ) { currentItem ->
                    when (currentItem.id) {
                        "home" -> DemoScreen()
                        "explore" -> DummyScreen(title = "Explore Trending")
                        "profile" -> DummyScreen(title = "User Profile")
                        "settings" -> DummyScreen(title = "App Settings")
                        // Footer-only routes rendered inside the same SPA body
                        "privacy" -> DummyScreen(title = "Privacy Policy (Opened from Footer)")
                        "terms" -> DummyScreen(title = "Terms of Service (Opened from Footer)")
                    }
                }
            }
        }
    }
}

@Composable
private fun DummyScreen(title: String) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
        items(20) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(text = "$title — Sample Card #${index + 1}")
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
                .verticalScroll(rememberScrollState())
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

            AdaptiveFlowGrid(
                minColumnWidth = 160.dp,
                maxColumns = 3
            ) {
                repeat(4) { index ->
                    DemoPanel(index + 1)
                }
            }

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
private fun DemoPanel(index: Int) {
    Box(
        modifier = Modifier
            .background(Color(0xFFF2EDFF), RoundedCornerShape(12.dp))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Adaptive panel $index")
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