package com.adaptive.kit_flow.manualtesting.desktop

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.adaptive.kit_flow.Adaptive
import com.adaptive.kit_flow.AdaptiveKitProvider
import com.adaptive.kit_flow.accessibility.AdaptiveAccessibleLayout
import com.adaptive.kit_flow.accessibility.LocalAdaptiveAccessibilityInfo
import com.adaptive.kit_flow.layout.AdaptiveFlowGrid
import com.adaptive.kit_flow.rememberWindowInfo
import java.awt.Dimension
import kotlin.math.roundToInt

private val AppBackground = Color(0xFFF4F6FA)
private val PanelBackground = Color(0xFFFFFFFF)
private val PreviewBackground = Color(0xFFF7F8FC)
private val Ink = Color(0xFF172033)
private val MutedInk = Color(0xFF667085)
private val Brand = Color(0xFF5B4FDB)
private val BrandSoft = Color(0xFFECEAFF)
private val Border = Color(0xFFDDE2EA)
private val Success = Color(0xFF087E5B)

private data class ViewportPreset(
    val label: String,
    val width: Dp,
    val height: Dp
)

private data class PreviewConfiguration(
    val minColumnWidth: Dp,
    val maxColumns: Int,
    val itemCount: Int,
    val fontScaleAware: Boolean
)

private data class ShowcaseCard(
    val eyebrow: String,
    val title: String,
    val description: String,
    val accent: Color
)

private val viewportPresets = listOf(
    ViewportPreset("Small phone", 320.dp, 568.dp),
    ViewportPreset("Phone", 390.dp, 844.dp),
    ViewportPreset("Large phone", 480.dp, 854.dp),
    ViewportPreset("Tablet", 600.dp, 960.dp),
    ViewportPreset("Desktop", 840.dp, 900.dp),
    ViewportPreset("Landscape", 844.dp, 390.dp),
    ViewportPreset("Square", 600.dp, 600.dp)
)

private val showcaseCards = listOf(
    ShowcaseCard("ANALYTICS", "Live revenue", "A compact metric with a two-line explanation.", Color(0xFF5B4FDB)),
    ShowcaseCard("ORDERS", "Ready to ship", "Actions keep enough room as the viewport narrows.", Color(0xFF0B8A6A)),
    ShowcaseCard("INVENTORY", "Low-stock alerts", "Natural card heights are preserved in every row.", Color(0xFFE17B36)),
    ShowcaseCard("CUSTOMERS", "New members", "Large text makes the grid wrap before content crowds.", Color(0xFF1976D2)),
    ShowcaseCard("CAMPAIGNS", "Spring launch", "Column count follows constraints, not a device name.", Color(0xFFC1437A)),
    ShowcaseCard("SUPPORT", "Fast responses", "The same deterministic layout runs on every target.", Color(0xFF7A52B3)),
    ShowcaseCard("RETENTION", "Returning users", "Try each preset to see width-based reflow.", Color(0xFF267D87)),
    ShowcaseCard("PERFORMANCE", "Healthy store", "The optional local advisor never ships in the app.", Color(0xFF586174))
)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KitFlow Adaptive Layout Showcase",
        state = rememberWindowState(width = 1320.dp, height = 900.dp)
    ) {
        LaunchedEffect(window) {
            window.minimumSize = Dimension(900, 640)
        }
        MaterialTheme {
            AdaptiveKitProvider {
                WindowLab()
            }
        }
    }
}

@Composable
private fun WindowLab() {
    var viewport by remember { mutableStateOf(viewportPresets[1]) }
    var simulatedDensity by remember { mutableFloatStateOf(1f) }
    var simulatedFontScale by remember { mutableFloatStateOf(1f) }
    var minColumnWidth by remember { mutableStateOf(160.dp) }
    var maxColumns by remember { mutableIntStateOf(4) }
    var itemCount by remember { mutableIntStateOf(6) }
    var fontScaleAware by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        Row(Modifier.fillMaxSize()) {
            ControlPanel(
                modifier = Modifier
                    .width(332.dp)
                    .fillMaxHeight(),
                viewport = viewport,
                density = simulatedDensity,
                fontScale = simulatedFontScale,
                configuration = PreviewConfiguration(
                    minColumnWidth = minColumnWidth,
                    maxColumns = maxColumns,
                    itemCount = itemCount,
                    fontScaleAware = fontScaleAware
                ),
                onViewportChange = { viewport = it },
                onDensityChange = { simulatedDensity = it },
                onFontScaleChange = { simulatedFontScale = it },
                onMinColumnWidthChange = { minColumnWidth = it },
                onMaxColumnsChange = { maxColumns = it },
                onItemCountChange = { itemCount = it },
                onFontScaleAwareChange = { fontScaleAware = it }
            )

            PreviewHost(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                viewport = viewport,
                density = simulatedDensity,
                fontScale = simulatedFontScale,
                configuration = PreviewConfiguration(
                    minColumnWidth = minColumnWidth,
                    maxColumns = maxColumns,
                    itemCount = itemCount,
                    fontScaleAware = fontScaleAware
                )
            )
        }
    }
}

@Composable
private fun ControlPanel(
    modifier: Modifier,
    viewport: ViewportPreset,
    density: Float,
    fontScale: Float,
    configuration: PreviewConfiguration,
    onViewportChange: (ViewportPreset) -> Unit,
    onDensityChange: (Float) -> Unit,
    onFontScaleChange: (Float) -> Unit,
    onMinColumnWidthChange: (Dp) -> Unit,
    onMaxColumnsChange: (Int) -> Unit,
    onItemCountChange: (Int) -> Unit,
    onFontScaleAwareChange: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .background(PanelBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "KitFlow Window Lab",
                color = Ink,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Change the viewport and grid inputs. Every update is deterministic and runs locally.",
                color = MutedInk,
                lineHeight = 20.sp
            )
        }

        ControlSection(
            title = "Viewport",
            supportingText = "Logical size in dp"
        ) {
            ChoiceGrid(
                options = viewportPresets,
                selected = viewport,
                label = { "${it.label}\n${it.width.value.roundToInt()} × ${it.height.value.roundToInt()}" },
                onSelected = onViewportChange
            )
        }

        ControlSection(
            title = "Display density",
            supportingText = "Changes physical pixels, not logical breakpoints"
        ) {
            ChoiceGrid(
                options = listOf(1f, 2f, 3f),
                selected = density,
                label = { "${it.cleanNumber()}×" },
                onSelected = onDensityChange
            )
        }

        ControlSection(
            title = "Font scale",
            supportingText = "Try 1.5× or 2× to force earlier wrapping"
        ) {
            ChoiceGrid(
                options = listOf(1f, 1.3f, 1.5f, 2f),
                selected = fontScale,
                label = { "${it.cleanNumber()}×" },
                onSelected = onFontScaleChange
            )
        }

        ControlSection(
            title = "Minimum card width",
            supportingText = "Passed directly to AdaptiveFlowGrid"
        ) {
            ChoiceGrid(
                options = listOf(120.dp, 160.dp, 200.dp, 240.dp),
                selected = configuration.minColumnWidth,
                label = { "${it.value.roundToInt()} dp" },
                onSelected = onMinColumnWidthChange
            )
        }

        ControlSection(
            title = "Column cap",
            supportingText = "The active layout breakpoint can lower this cap"
        ) {
            ChoiceGrid(
                options = listOf(2, 3, 4, 5),
                selected = configuration.maxColumns,
                label = { it.toString() },
                onSelected = onMaxColumnsChange
            )
        }

        ControlSection(
            title = "Cards",
            supportingText = "AdaptiveFlowGrid is eager and intended for small groups"
        ) {
            ChoiceGrid(
                options = listOf(4, 6, 8),
                selected = configuration.itemCount,
                label = { it.toString() },
                onSelected = onItemCountChange
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "Font-scale aware",
                    color = Ink,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Increase effective card width for large text",
                    color = MutedInk,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
            Switch(
                checked = configuration.fontScaleAware,
                onCheckedChange = onFontScaleAwareChange
            )
        }

        Surface(
            color = BrandSoft,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Optional local advisor",
                    color = Brand,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "The Python/Ollama helper can suggest these starting values, but no model or client is bundled with this app.",
                    color = Ink,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun <T> ChoiceGrid(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelected: (T) -> Unit
) {
    AdaptiveFlowGrid(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup(),
        minColumnWidth = 116.dp,
        maxColumns = 2,
        horizontalSpacing = 8.dp,
        verticalSpacing = 8.dp,
        fontScaleAware = false
    ) {
        options.forEach { option ->
            ChoiceButton(
                label = label(option),
                selected = option == selected,
                onClick = { onSelected(option) }
            )
        }
    }
}

@Composable
private fun ChoiceButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick
            ),
        color = if (selected) BrandSoft else Color(0xFFF8F9FB),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (selected) Brand else Border
        )
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 9.dp),
            color = if (selected) Brand else Ink,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            lineHeight = 16.sp
        )
    }
}

@Composable
private fun ControlSection(
    title: String,
    supportingText: String,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                color = Ink,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = supportingText,
                color = MutedInk,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
        content()
    }
}

@Composable
private fun PreviewHost(
    modifier: Modifier,
    viewport: ViewportPreset,
    density: Float,
    fontScale: Float,
    configuration: PreviewConfiguration
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFE9ECF2))
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = viewport.label,
                    color = Ink,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${viewport.width.value.roundToInt()} × ${viewport.height.value.roundToInt()} dp",
                    color = MutedInk,
                    fontSize = 12.sp
                )
            }
            Text(
                text = "Scroll to inspect oversized previews",
                color = MutedInk,
                fontSize = 12.sp
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
                .verticalScroll(rememberScrollState())
                .padding(28.dp)
        ) {
            CompositionLocalProvider(
                LocalDensity provides Density(
                    density = density,
                    fontScale = fontScale
                )
            ) {
                Surface(
                    modifier = Modifier
                        .requiredSize(viewport.width, viewport.height)
                        .border(1.dp, Border, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp)),
                    color = PreviewBackground,
                    shadowElevation = 12.dp
                ) {
                    AdaptiveKitProvider {
                        AdaptiveStorePreview(configuration)
                    }
                }
            }
        }
    }
}

@Composable
private fun AdaptiveStorePreview(configuration: PreviewConfiguration) {
    val windowInfo = rememberWindowInfo()
    val accessibilityInfo = LocalAdaptiveAccessibilityInfo.current
    val density = LocalDensity.current.density
    var reportPreviewed by remember { mutableStateOf(false) }
    val pagePadding = Adaptive.layoutValue(
        sm = 12.dp,
        md = 16.dp,
        lg = 20.dp,
        tab = 28.dp,
        desktop = 36.dp
    )
    val sectionSpacing = Adaptive.layoutValue(
        sm = 18.dp,
        md = 20.dp,
        lg = 24.dp,
        tab = 28.dp,
        desktop = 32.dp
    )
    val gridSpacing = Adaptive.layoutValue(
        sm = 8.dp,
        md = 10.dp,
        lg = 12.dp,
        tab = 16.dp,
        desktop = 18.dp
    )
    val stableRadius = Adaptive.value(
        sm = 12.dp,
        md = 16.dp,
        lg = 20.dp,
        tab = 24.dp,
        desktop = 28.dp
    )
    val breakpointColumnCap = Adaptive.layoutValue(
        sm = 1,
        md = 2,
        lg = 3,
        tab = 4,
        desktop = 5
    )
    val activeColumnCap = minOf(configuration.maxColumns, breakpointColumnCap)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = PreviewBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(pagePadding),
            verticalArrangement = Arrangement.spacedBy(sectionSpacing)
        ) {
            PreviewHero(
                stableRadius = stableRadius,
                screenClass = windowInfo.screenClass.name,
                layoutClass = windowInfo.layoutClass.name,
                activeColumnCap = activeColumnCap
            )

            SectionHeading(
                eyebrow = "LIVE CONTEXT",
                title = "What KitFlow sees",
                description = "Screen class stays tied to the shortest side; layout class follows the available width."
            )

            AdaptiveFlowGrid(
                modifier = Modifier.fillMaxWidth(),
                minColumnWidth = 120.dp,
                maxColumns = 4,
                horizontalSpacing = 8.dp,
                verticalSpacing = 8.dp,
                fontScaleAware = false
            ) {
                MetricTile("Viewport", "${windowInfo.widthDp} × ${windowInfo.heightDp} dp")
                MetricTile("Classes", "${windowInfo.screenClass} / ${windowInfo.layoutClass}")
                MetricTile("Orientation", windowInfo.orientation.name)
                MetricTile("Aspect ratio", windowInfo.aspectRatio.twoDecimals())
                MetricTile("Density", "${density.cleanNumber()}×")
                MetricTile(
                    "Physical estimate",
                    "${(windowInfo.widthDp * density).roundToInt()} × ${(windowInfo.heightDp * density).roundToInt()} px"
                )
                MetricTile("Font scale", "${accessibilityInfo.fontScale.cleanNumber()}×")
                MetricTile("Grid cap", activeColumnCap.toString())
            }

            SectionHeading(
                eyebrow = "ADAPTIVE FLOW GRID",
                title = "Cards that find their own row",
                description = "The parent width, minimum card width, breakpoint cap, and font scale determine the result."
            )

            AdaptiveFlowGrid(
                modifier = Modifier.fillMaxWidth(),
                minColumnWidth = configuration.minColumnWidth,
                maxColumns = activeColumnCap,
                horizontalSpacing = gridSpacing,
                verticalSpacing = gridSpacing,
                fontScaleAware = configuration.fontScaleAware
            ) {
                showcaseCards.take(configuration.itemCount).forEachIndexed { index, card ->
                    ProductCard(
                        index = index,
                        card = card,
                        radius = stableRadius
                    )
                }
            }

            Adaptive.onOrientationChange(
                portrait = {
                    BehaviorNotice(
                        title = "Portrait flow is active",
                        description = "The stable style token remains ${windowInfo.screenClass}; the grid reflows from the current width."
                    )
                },
                landscape = {
                    BehaviorNotice(
                        title = "Landscape flow is active",
                        description = "The screen class remains ${windowInfo.screenClass}, while layout class is ${windowInfo.layoutClass}."
                    )
                },
                square = {
                    BehaviorNotice(
                        title = "Square flow is active",
                        description = "A square window is handled explicitly without device-name checks."
                    )
                }
            )

            SectionHeading(
                eyebrow = "ACCESSIBILITY",
                title = "Actions that survive large text",
                description = "AdaptiveAccessibleLayout switches this row to a column at large font scales."
            )

            AdaptiveAccessibleLayout(
                normal = {
                    AccessibleActionCard(
                        stacked = false,
                        fontScaleLabel = accessibilityInfo.fontScaleClass.name,
                        previewed = reportPreviewed,
                        onPreviewClick = { reportPreviewed = !reportPreviewed }
                    )
                },
                largeText = {
                    AccessibleActionCard(
                        stacked = true,
                        fontScaleLabel = accessibilityInfo.fontScaleClass.name,
                        previewed = reportPreviewed,
                        onPreviewClick = { reportPreviewed = !reportPreviewed }
                    )
                }
            )

            AdvisorNotice()
            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun PreviewHero(
    stableRadius: Dp,
    screenClass: String,
    layoutClass: String,
    activeColumnCap: Int
) {
    val titleSize = Adaptive.value(
        sm = 26.sp,
        md = 30.sp,
        lg = 34.sp,
        tab = 40.sp,
        desktop = 46.sp
    )
    val horizontalHeader = Adaptive.layoutValue(
        sm = false,
        md = false,
        lg = true,
        tab = true,
        desktop = true
    )

    Card(
        shape = RoundedCornerShape(stableRadius),
        colors = CardDefaults.cardColors(containerColor = Ink)
    ) {
        if (horizontalHeader) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeroCopy(
                    modifier = Modifier.weight(1f),
                    titleSize = titleSize
                )
                HeaderFacts(screenClass, layoutClass, activeColumnCap)
            }
        } else {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HeroCopy(
                    modifier = Modifier.fillMaxWidth(),
                    titleSize = titleSize
                )
                HeaderFacts(screenClass, layoutClass, activeColumnCap)
            }
        }
    }
}

@Composable
private fun HeroCopy(
    modifier: Modifier,
    titleSize: androidx.compose.ui.unit.TextUnit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "LIVE RESPONSIVE SAMPLE",
            color = Color(0xFFB9B2FF),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "One layout.\nEvery window.",
            color = Color.White,
            fontSize = titleSize,
            lineHeight = titleSize * 1.04f,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Resize, rotate, increase text, or change density. KitFlow keeps the UI readable without a runtime model.",
            color = Color(0xFFC8CFDD),
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun HeaderFacts(
    screenClass: String,
    layoutClass: String,
    activeColumnCap: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        HeaderFact("Stable style", screenClass)
        HeaderFact("Width reflow", layoutClass)
        HeaderFact("Column cap", activeColumnCap.toString())
    }
}

@Composable
private fun HeaderFact(label: String, value: String) {
    Surface(
        color = Color(0xFF283247),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = label,
                color = Color(0xFFB8C0CF),
                fontSize = 11.sp
            )
            Text(
                text = value,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun SectionHeading(
    eyebrow: String,
    title: String,
    description: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
        Text(
            text = eyebrow,
            color = Brand,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = title,
            color = Ink,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = description,
            color = MutedInk,
            lineHeight = 19.sp
        )
    }
}

@Composable
private fun MetricTile(label: String, value: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                color = MutedInk,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                color = Ink,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ProductCard(
    index: Int,
    card: ShowcaseCard,
    radius: Dp
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(radius),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(card.accent.copy(alpha = 0.14f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = (index + 1).toString().padStart(2, '0'),
                    color = card.accent,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = card.eyebrow,
                    color = card.accent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = card.title,
                    color = Ink,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = card.description,
                    color = MutedInk,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
private fun BehaviorNotice(
    title: String,
    description: String
) {
    Surface(
        color = Color(0xFFE7F6F1),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = title,
                color = Success,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                color = Ink,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
private fun AccessibleActionCard(
    stacked: Boolean,
    fontScaleLabel: String,
    previewed: Boolean,
    onPreviewClick: () -> Unit
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Border)
    ) {
        if (stacked) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ActionCopy(
                    fontScaleLabel = fontScaleLabel,
                    previewed = previewed
                )
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onPreviewClick
                ) {
                    Text(if (previewed) "Close preview" else "Preview report")
                }
            }
        } else {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionCopy(
                    fontScaleLabel = fontScaleLabel,
                    previewed = previewed,
                    modifier = Modifier.weight(1f)
                )
                OutlinedButton(onClick = onPreviewClick) {
                    Text(if (previewed) "Close preview" else "Preview report")
                }
            }
        }
    }
}

@Composable
private fun ActionCopy(
    fontScaleLabel: String,
    previewed: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = "Weekly performance report",
            color = Ink,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = if (previewed) {
                "Preview opened • Font class: $fontScaleLabel"
            } else {
                "Font class: $fontScaleLabel"
            },
            color = if (previewed) Success else MutedInk,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun AdvisorNotice() {
    Surface(
        color = BrandSoft,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Want a starting configuration?",
                color = Brand,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Use the optional local advisor during development, then commit the deterministic values it suggests.",
                color = Ink,
                lineHeight = 19.sp
            )
            Text(
                text = "python tools/layout-advisor/layout_advisor.py \"Dashboard cards\"",
                color = Brand,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp
            )
        }
    }
}

private fun Float.cleanNumber(): String =
    if (this == roundToInt().toFloat()) roundToInt().toString() else toString()

private fun Float.twoDecimals(): String =
    ((this * 100f).roundToInt() / 100f).toString()
