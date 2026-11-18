package com.adaptive.kit_flow.modules.kitcomponent.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
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
 * An individual destination row inside [KitDrawerNavigator].
 * Displays an optional icon, title label, and optional badge count.
 *
 * @param item The navigation item model.
 * @param isSelected Whether this destination is currently active.
 * @param onClick Callback triggered when this destination is tapped.
 * @param modifier Modifier applied to the row container.
 * @param selectedBackground Background color when [isSelected] is true.
 * @param selectedContent Tint color for icon and text when [isSelected] is true.
 * @param unselectedContent Tint color for icon and text when [isSelected] is false.
 */
@Composable
fun KitDrawerItem(
    item: KitNavItem,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedBackground: Color = Color(0xFFEEF2FF),
    selectedContent: Color = Color(0xFF4F46E5),
    unselectedContent: Color = Color(0xFF374151)
) {
    val contentColor = if (isSelected) selectedContent else unselectedContent
    val backgroundColor = if (isSelected) selectedBackground else Color.Transparent

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (item.icon != null) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                modifier = Modifier.size(20.dp),
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(12.dp))
        }

        item.title?.let {
            Text(
                text = it,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 14.sp,
                color = contentColor,
                modifier = Modifier.weight(1f)
            )
        }

        if (item.badgeCount != null && item.badgeCount > 0) {
            Box(
                modifier = Modifier
                    .background(
                        if (isSelected) selectedContent else Color(0xFF9CA3AF),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 7.dp, vertical = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.badgeCount.toString(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
