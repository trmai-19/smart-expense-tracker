package com.smartexpense.android.presentation.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.smartexpense.android.R
import com.smartexpense.android.ui.theme.*

data class BottomNavItem(
    val pageIndex: Int,
    val iconResId: Int,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(0, R.drawable.ic_grid_widget, "Grid"),
    BottomNavItem(1, R.drawable.ic_dashboard_tab, "Stats"),
    BottomNavItem(2, R.drawable.ic_home, "Home"),
    BottomNavItem(3, R.drawable.ic_chat, "Chat")
)

@Composable
fun SetBottomBar(
    currentPage: Int,
    isOnCameraPage: Boolean,
    accentColor: Color,
    onPageSelected: (Int) -> Unit,
    onPaletteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            NavIcon(bottomNavItems[0], currentPage, accentColor, onPageSelected)
            NavIcon(bottomNavItems[1], currentPage, accentColor, onPageSelected)
            NavIcon(bottomNavItems[2], currentPage, accentColor, onPageSelected)
            NavIcon(bottomNavItems[3], currentPage, accentColor, onPageSelected)

            // Palette
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.08f))
                    .clickable { onPaletteClick() }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_palette),
                    contentDescription = "Theme",
                    tint = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun NavIcon(
    item: BottomNavItem,
    currentPage: Int,
    accentColor: Color,
    onPageSelected: (Int) -> Unit
) {
    val isSelected = currentPage == item.pageIndex
    val bgColor = if (isSelected) accentColor.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.08f)
    val borderColor = if (isSelected) accentColor.copy(alpha = 0.5f) else Color.Transparent
    val iconColor = if (isSelected) accentColor else Color.White.copy(alpha = 0.7f)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(bgColor)
            .border(1.dp, borderColor, CircleShape)
            .clickable { onPageSelected(item.pageIndex) }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(item.iconResId),
            contentDescription = item.label,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
    }
}
