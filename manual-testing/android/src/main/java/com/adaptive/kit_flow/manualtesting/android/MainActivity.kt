package com.adaptive.kit_flow.manualtesting.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adaptive.kit_flow.Adaptive
import com.adaptive.kit_flow.AdaptiveKitProvider
import com.adaptive.kit_flow.accessibility.AdaptiveAccessibleLayout
import com.adaptive.kit_flow.accessibility.LocalAdaptiveAccessibilityInfo
import com.adaptive.kit_flow.layout.AdaptiveFlowGrid
import com.adaptive.kit_flow.rememberWindowInfo
import kotlin.math.roundToInt

private val Canvas = Color(0xFFF7F8FC)
private val Ink = Color(0xFF172033)
private val MutedInk = Color(0xFF667085)
private val Brand = Color(0xFF5B4FDB)
private val BrandSoft = Color(0xFFECEAFF)
private val Border = Color(0xFFDDE2EA)
private val Success = Color(0xFF087E5B)

private data class ShowcaseCard(
    val eyebrow: String,
    val title: String,
    val description: String,
    val accent: Color
)

private val showcaseCards = listOf(
    ShowcaseCard("ANALYTICS", "Live revenue", "A compact metric that stays readable.", Color(0xFF5B4FDB)),
    ShowcaseCard("ORDERS", "Ready to ship", "Actions keep enough room on narrow screens.", Color(0xFF0B8A6A)),
    ShowcaseCard("INVENTORY", "Low-stock alerts", "Natural card heights are preserved.", Color(0xFFE17B36)),
    ShowcaseCard("CUSTOMERS", "New members", "Large text makes the grid wrap earlier.", Color(0xFF1976D2)),
    ShowcaseCard("CAMPAIGNS", "Spring launch", "Columns follow constraints, not device names.", Color(0xFFC1437A)),
    ShowcaseCard("SUPPORT", "Fast responses", "The layout stays deterministic and offline.", Color(0xFF7A52B3))
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                AdaptiveKitProvider {
                    AndroidShowcase()
                }
            }
        }
    }
}

@Composable
private fun AndroidShowcase() {
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
    val columnCap = Adaptive.layoutValue(
        sm = 1,
        md = 2,
        lg = 3,
        tab = 4,
        desktop = 4
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Canvas
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(pagePadding),
            verticalArrangement = Arrangement.spacedBy(sectionSpacing)
        ) {
            HeroCard(
                radius = stableRadius,
                screenClass = windowInfo.screenClass.name,
                layoutClass = windowInfo.layoutClass.name,
                columnCap = columnCap
            )

            SectionHeading(
                eyebrow = "LIVE CONTEXT",
                title = "What KitFlow sees",
                description = "Rotate the phone, enter split screen, or change display and font settings."
            )

            AdaptiveFlowGrid(
                modifier = Modifier.fillMaxWidth(),
                minColumnWidth = 120.dp,
                maxColumns = 3,
                horizontalSpacing = 8.dp,
                verticalSpacing = 8.dp,
                fontScaleAware = false
            ) {
                MetricTile("Viewport", "${windowInfo.widthDp} × ${windowInfo.heightDp} dp")
                MetricTile("Stable class", windowInfo.screenClass.name)
                MetricTile("Layout class", windowInfo.layoutClass.name)
                MetricTile("Orientation", windowInfo.orientation.name)
                MetricTile("Aspect ratio", windowInfo.aspectRatio.twoDecimals())
                MetricTile("Density", "${density.cleanNumber()}×")
                MetricTile("Font scale", "${accessibilityInfo.fontScale.cleanNumber()}×")
                MetricTile("Grid cap", columnCap.toString())
            }

            SectionHeading(
                eyebrow = "ADAPTIVE FLOW GRID",
                title = "Cards that find their own row",
                description = "A 160 dp minimum width and live breakpoint cap decide how many columns fit."
            )

            AdaptiveFlowGrid(
                modifier = Modifier.fillMaxWidth(),
                minColumnWidth = 160.dp,
                maxColumns = columnCap,
                horizontalSpacing = gridSpacing,
                verticalSpacing = gridSpacing
            ) {
                showcaseCards.forEachIndexed { index, card ->
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
                        description = "Stable style: ${windowInfo.screenClass}. Width reflow: ${windowInfo.layoutClass}."
                    )
                },
                landscape = {
                    BehaviorNotice(
                        title = "Landscape flow is active",
                        description = "The style class stays ${windowInfo.screenClass}, while the grid follows ${windowInfo.layoutClass}."
                    )
                },
                square = {
                    BehaviorNotice(
                        title = "Square flow is active",
                        description = "KitFlow handles this shape explicitly without device-name checks."
                    )
                }
            )

            SectionHeading(
                eyebrow = "ACCESSIBILITY",
                title = "Actions that survive large text",
                description = "AdaptiveAccessibleLayout changes this row into a column when system text grows."
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

            Surface(
                color = BrandSoft,
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Optional local advisor",
                        color = Brand,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Use it during development to suggest starting values. The app ships only deterministic Kotlin layout rules.",
                        color = Ink,
                        lineHeight = 19.sp
                    )
                    Text(
                        text = "tools/layout-advisor/layout_advisor.py",
                        color = Brand,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp
                    )
                }
            }

            Spacer(Modifier.height(4.dp))
        }
    }
}

@Composable
private fun HeroCard(
    radius: Dp,
    screenClass: String,
    layoutClass: String,
    columnCap: Int
) {
    val titleSize = Adaptive.value(
        sm = 26.sp,
        md = 30.sp,
        lg = 34.sp,
        tab = 40.sp,
        desktop = 46.sp
    )

    Card(
        shape = RoundedCornerShape(radius),
        colors = CardDefaults.cardColors(containerColor = Ink)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "LIVE RESPONSIVE SAMPLE",
                    color = Color(0xFFB9B2FF),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "One layout.\nEvery phone size.",
                    color = Color.White,
                    fontSize = titleSize,
                    lineHeight = titleSize * 1.04f,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Shortest-side styles stay stable while width-based layout decisions reflow.",
                    color = Color(0xFFC8CFDD),
                    lineHeight = 20.sp
                )
            }

            AdaptiveFlowGrid(
                modifier = Modifier.fillMaxWidth(),
                minColumnWidth = 118.dp,
                maxColumns = 3,
                horizontalSpacing = 8.dp,
                verticalSpacing = 8.dp,
                fontScaleAware = false
            ) {
                HeaderFact("Stable style", screenClass)
                HeaderFact("Width reflow", layoutClass)
                HeaderFact("Column cap", columnCap.toString())
            }
        }
    }
}

@Composable
private fun HeaderFact(label: String, value: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFF283247),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                color = Color(0xFFB8C0CF),
                fontSize = 10.sp
            )
            Text(
                text = value,
                color = Color.White,
                fontSize = 12.sp,
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
                fontSize = 10.sp
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
private fun BehaviorNotice(title: String, description: String) {
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

private fun Float.cleanNumber(): String =
    if (this == roundToInt().toFloat()) roundToInt().toString() else toString()

private fun Float.twoDecimals(): String =
    ((this * 100f).roundToInt() / 100f).toString()
