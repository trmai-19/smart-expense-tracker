package com.smartexpense.android.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.smartexpense.android.presentation.util.ThemeManager
import com.smartexpense.android.ui.theme.*
import androidx.compose.material.icons.filled.Check

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeColorSheet(
    currentAccent: ThemeManager.AccentColor,
    onColorSelected: (ThemeManager.AccentColor) -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = SurfaceVariant,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(OnSurfaceDim)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, bottom = 40.dp)
        ) {
            Text(
                text = "Chọn màu giao diện",
                style = MaterialTheme.typography.headlineMedium,
                color = OnBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Tất cả màu neon đều hỗ trợ Dark Mode",
                style = MaterialTheme.typography.bodySmall,
                color = OnSurfaceMuted
            )

            Spacer(Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(ThemeManager.AccentColor.entries.toList()) { accent ->
                    val isSelected = accent == currentAccent
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.clickable { onColorSelected(accent) }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(accent.composeColor)
                                .then(
                                    if (isSelected) Modifier.border(3.dp, OnBackground, CircleShape)
                                    else Modifier
                                )
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = androidx.compose.material.icons.Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Background,
                                    modifier = Modifier.align(Alignment.Center).size(24.dp)
                                )
                            }
                        }
                        Text(
                            accent.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) accent.composeColor else OnSurfaceMuted
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}
