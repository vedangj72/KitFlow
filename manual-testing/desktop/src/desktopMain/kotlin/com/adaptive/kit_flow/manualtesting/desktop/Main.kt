package com.adaptive.kit_flow.manualtesting.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
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
import com.adaptive.kit_flow.modules.kitcomponent.scaffoldings.KitDrawerScaffold
import com.adaptive.kit_flow.modules.kitcomponent.scaffoldings.KitNavScaffold
import com.adaptive.kit_flow.modules.kitcomponent.utils.ExtraItemsPosition
import com.adaptive.kit_flow.modules.kitcomponent.utils.HeaderPosition
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem
import com.adaptive.kit_flow.modules.kitcomponent.utils.NavItemsPosition
import com.adaptive.kit_flow.rememberWindowInfo

enum class ScaffoldDemoMode {
    DRAWER,
    TOP_BAR
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KitFlow Adaptive Navigation Manual Test",
        state = rememberWindowState(width = 1100.dp, height = 800.dp)
    ) {
        // Primary Navigation Items
        val navItems = remember {
            listOf(
                KitNavItem(id = "home", title = "Home"),
                KitNavItem(id = "explore", title = "Explore", badgeCount = 3),
                KitNavItem(id = "profile", title = "Profile"),
                KitNavItem(id = "settings", title = "Settings")
            )
        }

        // Secondary / Extra Navigation Items (e.g. Profile, Help)
        val extraNavItems = remember {
            listOf(
                KitNavItem(id = "profile", title = "Profile", badgeCount = 1),
                KitNavItem(id = "help", title = "Help & Docs")
            )
        }

        // Controls state
        var demoMode by remember { mutableStateOf(ScaffoldDemoMode.DRAWER) }
        var isNonClosing by remember { mutableStateOf(false) }
        var isMobileDrawer by remember { mutableStateOf(false) }
        var extraItemsPos by remember { mutableStateOf(ExtraItemsPosition.TOOLBAR_END) }
        var useCustomHamburger by remember { mutableStateOf(false) }
        var topBarNavPosition by remember { mutableStateOf(NavItemsPosition.MID) }
        var topBarHeaderPosition by remember { mutableStateOf(HeaderPosition.START) }

        MaterialTheme {
            AdaptiveKitProvider {
                Column(modifier = Modifier.fillMaxSize()) {
                    // Control Ribbon at the very top of testing window
                    DemoControlBar(
                        demoMode = demoMode,
                        onModeChange = { demoMode = it },
                        isNonClosing = isNonClosing,
                        onNonClosingChange = { isNonClosing = it },
                        isMobileDrawer = isMobileDrawer,
                        onMobileDrawerChange = { isMobileDrawer = it },
                        extraItemsPos = extraItemsPos,
                        onExtraItemsPosChange = { extraItemsPos = it },
                        useCustomHamburger = useCustomHamburger,
                        onCustomHamburgerChange = { useCustomHamburger = it },
                        navPosition = topBarNavPosition,
                        onNavPositionChange = { topBarNavPosition = it },
                        headerPos = topBarHeaderPosition,
                        onHeaderPosChange = { topBarHeaderPosition = it }
                    )

                    Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                        when (demoMode) {
                            ScaffoldDemoMode.DRAWER -> {
                                KitDrawerScaffold(
                                    navItems = navItems,
                                    extraNavItems = extraNavItems,
                                    extraItemsPosition = extraItemsPos,
                                    nonClosing = isNonClosing,
                                    mobileDrawer = isMobileDrawer,
                                    hamburgerIcon = if (useCustomHamburger) {
                                        { toggleDrawer ->
                                            IconButton(
                                                onClick = toggleDrawer,
                                                modifier = Modifier
                                                    .size(36.dp)
                                                    .background(Color(0xFFEEF2FF), CircleShape)
                                            ) {
                                                Text("☰", fontSize = 18.sp, color = Color(0xFF4F46E5), fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    } else null,
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
                                                    text = "v1.0.4 • SDK Active",
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

                            ScaffoldDemoMode.TOP_BAR -> {
                                KitNavScaffold(
                                    navItems = navItems,
                                    extraNavItems = extraNavItems,
                                    navItemsPosition = topBarNavPosition,
                                    headerPosition = topBarHeaderPosition,
                                    navSpacing = Adaptive.value(
                                        sm = 8.dp,
                                        md = 12.dp,
                                        lg = 16.dp,
                                        tab = 20.dp,
                                        desktop = 24.dp
                                    ),
                                    header = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Box(
                                                modifier = Modifier
                                                    .size(28.dp)
                                                    .background(Color(0xFF4F46E5), RoundedCornerShape(6.dp)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text("K", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "KitFlow",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )
                                        }
                                    },
                                    toolbar = {
                                        Text(
                                            text = "KitFlow TopBar",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp
                                        )
                                    },
                                    footer = { onNavigate ->
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color(0xFFF3F4F6))
                                                .padding(horizontal = 24.dp, vertical = 14.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "© 2026 KitFlow Multiplatform", fontSize = 12.sp)

                                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                                Text(
                                                    text = "Help & Docs",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFF2563EB),
                                                    modifier = Modifier.clickable { onNavigate("help") }
                                                )
                                                Text(
                                                    text = "Privacy Policy",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = Color(0xFF2563EB),
                                                    modifier = Modifier.clickable { onNavigate("privacy") }
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
            }
        }
    }
}

@Composable
private fun DemoControlBar(
    demoMode: ScaffoldDemoMode,
    onModeChange: (ScaffoldDemoMode) -> Unit,
    isNonClosing: Boolean,
    onNonClosingChange: (Boolean) -> Unit,
    isMobileDrawer: Boolean,
    onMobileDrawerChange: (Boolean) -> Unit,
    extraItemsPos: ExtraItemsPosition,
    onExtraItemsPosChange: (ExtraItemsPosition) -> Unit,
    useCustomHamburger: Boolean,
    onCustomHamburgerChange: (Boolean) -> Unit,
    navPosition: NavItemsPosition,
    onNavPositionChange: (NavItemsPosition) -> Unit,
    headerPos: HeaderPosition,
    onHeaderPosChange: (HeaderPosition) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF1E293B),
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Mode:",
                color = Color(0xFF94A3B8),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )

            // Switcher: Drawer vs TopBar
            PillButton(
                text = "KitDrawerScaffold (Drawer)",
                selected = demoMode == ScaffoldDemoMode.DRAWER,
                onClick = { onModeChange(ScaffoldDemoMode.DRAWER) }
            )
            PillButton(
                text = "KitNavScaffold (Top Bar)",
                selected = demoMode == ScaffoldDemoMode.TOP_BAR,
                onClick = { onModeChange(ScaffoldDemoMode.TOP_BAR) }
            )

            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(1.dp)
                    .background(Color(0xFF475569))
            )

            // Mobile Navigation Toggle
            PillButton(
                text = if (isMobileDrawer) "Mobile: Drawer Enabled" else "Mobile: Bottom Bar",
                selected = isMobileDrawer,
                onClick = { onMobileDrawerChange(!isMobileDrawer) }
            )

            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(1.dp)
                    .background(Color(0xFF475569))
            )

            // Contextual controls depending on mode
            if (demoMode == ScaffoldDemoMode.DRAWER) {
                PillButton(
                    text = if (isNonClosing) "nonClosing: true (Permanent)" else "nonClosing: false (Collapsible)",
                    selected = isNonClosing,
                    onClick = { onNonClosingChange(!isNonClosing) }
                )
                PillButton(
                    text = if (extraItemsPos == ExtraItemsPosition.TOOLBAR_END) "Extra Items: Toolbar End" else "Extra Items: Inside Drawer",
                    selected = extraItemsPos == ExtraItemsPosition.TOOLBAR_END,
                    onClick = {
                        onExtraItemsPosChange(
                            if (extraItemsPos == ExtraItemsPosition.TOOLBAR_END) ExtraItemsPosition.INSIDE_DRAWER
                            else ExtraItemsPosition.TOOLBAR_END
                        )
                    }
                )
                PillButton(
                    text = if (useCustomHamburger) "Custom Hamburger" else "Default 3-Bar Icon",
                    selected = useCustomHamburger,
                    onClick = { onCustomHamburgerChange(!useCustomHamburger) }
                )
            } else {
                Text(
                    text = "Items Pos:",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
                listOf(NavItemsPosition.START, NavItemsPosition.MID, NavItemsPosition.END).forEach { pos ->
                    PillButton(
                        text = pos.name,
                        selected = navPosition == pos,
                        onClick = { onNavPositionChange(pos) }
                    )
                }
                Text(
                    text = "Header:",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp
                )
                listOf(HeaderPosition.START, HeaderPosition.FIXED).forEach { hp ->
                    PillButton(
                        text = if (hp == HeaderPosition.START) "START (Fixed)" else "FIXED (Attached)",
                        selected = headerPos == hp,
                        onClick = { onHeaderPosChange(hp) }
                    )
                }
            }
        }
    }
}

@Composable
private fun PillButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                if (selected) Color(0xFF4F46E5) else Color(0xFF334155),
                RoundedCornerShape(6.dp)
            )
            .border(
                1.dp,
                if (selected) Color(0xFF818CF8) else Color.Transparent,
                RoundedCornerShape(6.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else Color(0xFFE2E8F0),
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun DestinationBody(currentItem: KitNavItem) {
    when (currentItem.id) {
        "home" -> DemoScreen()
        "explore" -> DummyScreen(title = "Explore Trending Destinations")
        "profile" -> DummyScreen(title = "User Profile & Account Settings")
        "settings" -> DummyScreen(title = "KitFlow Configuration & Preferences")
        "help" -> DummyScreen(title = "Documentation & Help Center")
        "privacy" -> DummyScreen(title = "Privacy Policy (SPA Destination)")
        else -> DummyScreen(title = currentItem.title ?: "Destination ${currentItem.id}")
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