package com.adaptive.kit_flow.manualtesting.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.adaptive.kit_flow.modules.kitcomponent.scaffoldings.KitDrawerScaffold
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KitFlow Adaptive Navigation Manual Test",
        state = rememberWindowState(width = 1100.dp, height = 800.dp)
    ) {
        val navItems = remember {
            listOf(
                KitNavItem(id = "home", title = "Home"),
                KitNavItem(id = "explore", title = "Explore", badgeCount = 3),
                KitNavItem(id = "profile", title = "Profile"),
                KitNavItem(id = "settings", title = "Settings")
            )
        }

        val extraNavItems = remember {
            listOf(
                KitNavItem(id = "profile", title = "Profile", badgeCount = 1),
                KitNavItem(id = "help", title = "Help & Docs")
            )
        }

        MaterialTheme {
            AdaptiveKitProvider {
                KitDrawerScaffold(
                    navItems = navItems,
                    extraNavItems = extraNavItems,
                    header = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color(0xFF4F46E5), RoundedCornerShape(8.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("K", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "KitFlow",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = Color(0xFF111827)
                                )
                                Text(
                                    text = "Drawer Navigator",
                                    fontSize = 11.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }
                        }
                    },
                    toolbar = {
                        Text(
                            text = "KitFlow Dashboard",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = Color(0xFF111827)
                        )
                    },
                    drawerFooter = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(Color(0xFFE5E7EB), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("VJ", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Vedang Joshi",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1F2937)
                                )
                                Text(
                                    text = "v1.1.0 • SDK Active",
                                    fontSize = 11.sp,
                                    color = Color(0xFF9CA3AF)
                                )
                            }
                        }
                    }
                ) { currentItem ->
                    DestinationBody(currentItem)
                }
            }
        }
    }
}

@Composable
private fun DestinationBody(currentItem: KitNavItem) {
    when (currentItem.id) {
        "home" -> HomeScreen()
        "explore" -> ExploreScreen()
        "profile" -> ProfileScreen()
        "settings" -> SettingsScreen()
        "help" -> HelpScreen()
        else -> GenericScreen(currentItem.title ?: currentItem.id ?: "Destination")
    }
}

@Composable
private fun HomeScreen() {
    val scrollState = rememberScrollState()
    val screenPadding = Adaptive.value(
        sm = 16.dp,
        md = 24.dp,
        lg = 32.dp,
        tab = 40.dp,
        desktop = 48.dp
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9FAFB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(screenPadding),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "KitFlow Adaptive Dashboard",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Text(
                    text = "Responsive navigation & layout engine for Compose Multiplatform",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetricCard(
                    title = "Supported Targets",
                    value = "4 Platforms",
                    subtitle = "Desktop • Web • Android • iOS",
                    badgeColor = Color(0xFF4F46E5),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Active Navigation",
                    value = "KitDrawerScaffold",
                    subtitle = "Modal drawer + smooth scrim",
                    badgeColor = Color(0xFF059669),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "SDK Release",
                    value = "v1.1.0",
                    subtitle = "Production ready",
                    badgeColor = Color(0xFFD97706),
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                text = "Key Capabilities",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937)
            )

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                FeatureRow(
                    tag = "Drawer SPA",
                    title = "Fluid Slide-Over Navigation",
                    description = "Smooth animated drawer with scrim backdrop on wide screens. Automatically adapts to a bottom bar on mobile screens.",
                    tagColor = Color(0xFF4F46E5)
                )
                FeatureRow(
                    tag = "Adaptive",
                    title = "Breakpoint-Aware Layouts",
                    description = "Responsive values for spacing, typography, and grids that adjust dynamically without boilerplate.",
                    tagColor = Color(0xFF0284C7)
                )
                FeatureRow(
                    tag = "Multiplatform",
                    title = "Unified API Across All Targets",
                    description = "One codebase powers JVM Desktop, Wasm/JS Web, Android, and iOS with full Compose Multiplatform fidelity.",
                    tagColor = Color(0xFF059669)
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    badgeColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF6B7280))
            Text(text = value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = badgeColor)
            Text(text = subtitle, fontSize = 11.sp, color = Color(0xFF9CA3AF))
        }
    }
}

@Composable
private fun FeatureRow(
    tag: String,
    title: String,
    description: String,
    tagColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(tagColor.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = tag,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = tagColor
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
}

@Composable
private fun ExploreScreen() {
    val screenPadding = Adaptive.value(
        sm = 16.dp,
        md = 24.dp,
        lg = 32.dp,
        tab = 40.dp,
        desktop = 48.dp
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9FAFB)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(screenPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "Explore KitFlow Components",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = "Production-ready scaffolding, navigation, and adaptive layouts",
                        fontSize = 14.sp,
                        color = Color(0xFF6B7280)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            val components = listOf(
                Triple("KitDrawerScaffold", "Scaffolding", "Collapsible slide-over drawer with scrim backdrop and sticky toolbar."),
                Triple("KitNavScaffold", "Scaffolding", "Top navigation bar scaffold with adaptive spacing and dynamic header alignment."),
                Triple("AdaptiveFlowGrid", "Layout", "Wrapping grid layout with automatic column count calculation."),
                Triple("AdaptiveAccessibleLayout", "Accessibility", "Layout branching for normal and high font scale accessibility."),
                Triple("LocalAdaptiveAccessibilityInfo", "Accessibility", "CompositionLocal exposing system accessibility preferences.")
            )

            items(components) { (name, category, desc) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFEEF2FF), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = category, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4F46E5))
                            }
                        }
                        Text(text = desc, fontSize = 13.sp, color = Color(0xFF6B7280))
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9FAFB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                    .padding(28.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(Color(0xFF4F46E5), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("VJ", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Vedang Joshi", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                        Text("Author & Maintainer • KitFlow", fontSize = 13.sp, color = Color(0xFF6B7280))
                        Text("v1.1.0 • Multiplatform Navigation", fontSize = 12.sp, color = Color(0xFF059669), fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9FAFB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))

            listOf(
                "Navigation Scaffold" to "KitDrawerScaffold (Slide-over)",
                "Animation Duration" to "300ms Horizontally",
                "Scrim Backdrop" to "Enabled (35% Black)",
                "Target Framework" to "Compose Multiplatform 1.7.x",
                "SDK Version" to "1.1.0"
            ).forEach { (label, value) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF374151))
                        Text(text = value, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF4F46E5))
                    }
                }
            }
        }
    }
}

@Composable
private fun HelpScreen() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9FAFB)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Help & Documentation", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
            Text("Resources for integrating KitFlow into Compose Multiplatform apps.", fontSize = 14.sp, color = Color(0xFF6B7280))

            listOf(
                "Quick Start" to "Add implementation(project(\":shared\")) or Maven coordinate com.adaptive:kit-flow:1.1.0.",
                "Using KitDrawerScaffold" to "Wrap your destinations with KitDrawerScaffold to get automatic drawer & bottom bar adaptation.",
                "Customizing Headers & Footers" to "Pass your own composables into header, toolbar, and drawerFooter slots."
            ).forEach { (title, detail) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF111827))
                        Text(text = detail, fontSize = 13.sp, color = Color(0xFF6B7280))
                    }
                }
            }
        }
    }
}

@Composable
private fun GenericScreen(title: String = "Destination") {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9FAFB)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(48.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF374151))
        }
    }
}
