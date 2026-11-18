package com.adaptive.kit_flow.modules.kitcomponent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.adaptive.kit_flow.modules.kitcomponent.utils.KitNavItem

/**
 * Renders an extra navigation item at the end of the top toolbar (e.g. profile icon, search, settings).
 *
 * Supports single or multiple items with icon, badge, active selection highlight, and title fallback.
 *
 * @param item The navigation item model.
 * @param isSelected Whether this destination is currently active.
 * @param onClick Triggered when the toolbar item is tapped.
 * @param modifier Modifier applied to the toolbar item container.
 * @param selectedColor Accent color when selected.
 * @param unselectedColor Content color when unselected.
 */
@Composable
fun KitToolbarNavItem(
    item: KitNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = Color(0xFF4F46E5),
    unselectedColor: Color = Color(0xFF374151)
) {
    val tintColor = if (isSelected) selectedColor else unselectedColor
    val backgroundColor = if (isSelected) Color(0xFFEEF2FF) else Color.Transparent

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        if (item.icon != null) {
            IconButton(
                onClick = onClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(backgroundColor, CircleShape)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = tintColor,
                        modifier = Modifier.size(22.dp)
                    )

                    if (item.badgeCount != null && item.badgeCount > 0) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(Color(0xFFEF4444), CircleShape)
                                .padding(horizontal = 4.dp, vertical = 1.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.badgeCount.toString(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .background(backgroundColor, RoundedCornerShape(8.dp))
                    .clickable { onClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    item.title?.let {
                        Text(
                            text = it,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                            color = tintColor
                        )
                    }
                    if (item.badgeCount != null && item.badgeCount > 0) {
                        Box(
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .background(Color(0xFFEF4444), CircleShape)
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = item.badgeCount.toString(),
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
