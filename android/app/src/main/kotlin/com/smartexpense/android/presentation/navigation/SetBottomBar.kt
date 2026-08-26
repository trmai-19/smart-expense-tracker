package com.smartexpense.android.presentation.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
    BottomNavItem(2, R.drawable.ic_camera_tab, "Camera"),
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
            .background(Surface)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // First 2 icons (Grid, Stats)
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                NavIcon(bottomNavItems[0], currentPage, accentColor, onPageSelected)
                NavIcon(bottomNavItems[1], currentPage, accentColor, onPageSelected)
            }

            // Center FAB (Camera) – hidden when already on camera page
            val fabScale by animateFloatAsState(
                targetValue = if (isOnCameraPage) 0f else 1f,
                animationSpec = tween(durationMillis = 200),
                label = "camScale"
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .offset(y = (-8).dp)
                    .scale(fabScale)
                    .clip(CircleShape)
                    .background(accentColor)
                    .clickable(enabled = !isOnCameraPage) { onPageSelected(2) }
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_camera_tab),
                    contentDescription = "Camera",
                    tint = Background,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Right 2 icons (Chat, Palette)
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceEvenly) {
                NavIcon(bottomNavItems[3], currentPage, accentColor, onPageSelected)

                // Palette
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .clickable { onPaletteClick() }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_palette),
                        contentDescription = "Theme",
                        tint = OnSurfaceDim,
                        modifier = Modifier.size(24.dp)
                    )
                }
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
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .clickable { onPageSelected(item.pageIndex) }
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(item.iconResId),
            contentDescription = item.label,
            tint = if (isSelected) accentColor else OnSurfaceDim,
            modifier = Modifier.size(24.dp)
        )
    }
}
