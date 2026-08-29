package com.smartexpense.android.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartexpense.android.R
import com.smartexpense.android.di.ViewModelFactory
import com.smartexpense.android.presentation.util.ThemeManager
import com.smartexpense.android.presentation.util.UserManager
import com.smartexpense.android.ui.theme.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    profileViewModel: ProfileViewModel = viewModel(factory = ViewModelFactory.getInstance())
) {
    val context = LocalContext.current
    val accentColor = LocalAccentColor.current

    val userProfile by profileViewModel.userProfile.observeAsState()

    var showThemeSheet by remember { mutableStateOf(false) }
    var showEditSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { profileViewModel.fetchProfile() }

    // Local cached values
    val displayName = userProfile?.displayName ?: UserManager.getUserName(context)
    val email = userProfile?.email ?: UserManager.getUserEmail(context)
    val streakDays = userProfile?.streakDays ?: UserManager.getStreakDays(context)
    val monthlyBudget = userProfile?.monthlyBudget?.toLong() ?: UserManager.getMonthlyBudget(context)
    val avatarInitial = displayName.firstOrNull()?.uppercaseChar()?.toString() ?: "U"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.Transparent)
            .verticalScroll(rememberScrollState())
    ) {
        // ── Top Bar ────────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_back),
                    contentDescription = "Back",
                    tint = OnSurface
                )
            }
            Text("Hồ sơ", style = MaterialTheme.typography.headlineMedium, color = OnBackground)
        }

        // ── Avatar & Name ─────────────────────────────────────────
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            ) {
                Text(
                    text = avatarInitial,
                    style = MaterialTheme.typography.displayMedium,
                    color = Background,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(displayName, style = MaterialTheme.typography.headlineMedium, color = OnBackground)
            Text(email, style = MaterialTheme.typography.bodyMedium, color = OnSurfaceMuted)
        }

        Spacer(Modifier.height(28.dp))

        // ── Stats row ─────────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                label = "Streak",
                value = "$streakDays ngày",
                accentColor = accentColor,
                modifier = Modifier.weight(1f)
            )
            StatCard(
                label = "Ngân sách",
                value = formatVnd(monthlyBudget),
                accentColor = accentColor,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(Modifier.height(28.dp))

        // ── Action buttons ────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ProfileActionButton(
                label = "Chỉnh sửa hồ sơ",
                iconResId = R.drawable.ic_profile_edit,
                accentColor = accentColor,
                onClick = { showEditSheet = true }
            )
            ProfileActionButton(
                label = "Đổi màu giao diện",
                iconResId = R.drawable.ic_palette,
                accentColor = accentColor,
                onClick = { showThemeSheet = true }
            )
            ProfileActionButton(
                label = "Đăng xuất",
                iconResId = R.drawable.ic_logout,
                accentColor = ErrorColor,
                onClick = { showLogoutDialog = true }
            )
        }

        Spacer(Modifier.height(40.dp))
    }

    // ── Theme color sheet ──────────────────────────────────────────
    if (showThemeSheet) {
        ThemeColorSheet(
            currentAccent = ThemeManager.getAccentColor(context),
            onColorSelected = { color ->
                ThemeManager.setAccentColor(context, color)
                showThemeSheet = false
            },
            onDismiss = { showThemeSheet = false }
        )
    }

    // ── Edit profile sheet ─────────────────────────────────────────
    if (showEditSheet) {
        EditProfileSheet(
            onDismiss = { showEditSheet = false },
            onProfileUpdated = {
                profileViewModel.fetchProfile()
                showEditSheet = false
            }
        )
    }

    // ── Logout dialog ──────────────────────────────────────────────
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            containerColor = SurfaceCard,
            title = { Text("Đăng xuất?", color = OnBackground) },
            text = { Text("Bạn có chắc muốn đăng xuất không?", color = OnSurfaceMuted) },
            confirmButton = {
                TextButton(onClick = {
                    UserManager.clearToken(context)
                    showLogoutDialog = false
                    onLogout()
                }) { Text("Đăng xuất", color = ErrorColor) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Hủy", color = accentColor)
                }
            }
        )
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    accentColor: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(label, style = MaterialTheme.typography.bodySmall, color = OnSurfaceMuted)
            Spacer(Modifier.height(4.dp))
            Text(value, style = MaterialTheme.typography.titleMedium, color = accentColor, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ProfileActionButton(
    label: String,
    iconResId: Int,
    accentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = SurfaceCard,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(iconResId),
                contentDescription = label,
                tint = accentColor,
                modifier = Modifier.size(22.dp)
            )
            Text(label, style = MaterialTheme.typography.bodyMedium, color = OnSurface)
        }
    }
}

private fun formatVnd(amount: Long): String {
    val symbols = DecimalFormatSymbols(Locale("vi", "VN")).apply { groupingSeparator = '.' }
    return "${DecimalFormat("#,###", symbols).format(amount)} đ"
}
