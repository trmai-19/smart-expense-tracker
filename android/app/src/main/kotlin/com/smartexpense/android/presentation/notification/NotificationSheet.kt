package com.smartexpense.android.presentation.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartexpense.android.R
import com.smartexpense.android.data.model.NotificationItem
import com.smartexpense.android.di.ViewModelFactory
import com.smartexpense.android.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSheet(
    onDismiss: () -> Unit,
    notificationViewModel: NotificationViewModel = viewModel(factory = ViewModelFactory.getInstance())
) {
    val accentColor = LocalAccentColor.current
    val notificationsState by notificationViewModel.notifications.observeAsState(emptyList())
    val notifications = notificationsState
    val unreadCount = notifications.count { !it.isRead }

    LaunchedEffect(Unit) {
        notificationViewModel.fetchNotifications()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Background,
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
                .fillMaxHeight(0.8f) // Occupy 80% of screen height
        ) {
            // ── Header ─────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_notifications),
                            contentDescription = "Notifications",
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Thông báo",
                            style = MaterialTheme.typography.headlineMedium,
                            color = OnBackground
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = if (unreadCount > 0) "$unreadCount thông báo chưa đọc" else "Tất cả thông báo đã được đọc",
                        style = MaterialTheme.typography.bodySmall,
                        color = OnSurfaceMuted
                    )
                }

                if (unreadCount > 0) {
                    TextButton(onClick = { notificationViewModel.markAllAsRead() }) {
                        Text("Đánh dấu đã đọc", color = accentColor, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Notification List ──────────────────────────────────────
            if (notifications.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_notifications),
                            contentDescription = "Empty",
                            tint = OnSurfaceDim,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(Modifier.height(16.dp))
                        Text("Không có thông báo nào", color = OnSurfaceMuted, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(notifications) { dto ->
                        NotificationRowItem(
                            content = dto.content,
                            isRead = dto.isRead,
                            dateStr = try {
                                LocalDateTime.parse(dto.createdAt)
                                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                            } catch (e: Exception) {
                                dto.createdAt.take(16)
                            },
                            accentColor = accentColor,
                            onClick = { notificationViewModel.markAsRead(dto.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationRowItem(
    content: String,
    isRead: Boolean,
    dateStr: String,
    accentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (isRead) Background else SurfaceCard.copy(alpha = 0.5f))
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(if (isRead) SurfaceCard else accentColor.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_notifications),
                contentDescription = null,
                tint = if (isRead) OnSurfaceDim else accentColor,
                modifier = Modifier.size(20.dp)
            )
        }
        
        Spacer(Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Hệ thống SET",
                style = MaterialTheme.typography.labelMedium,
                color = if (isRead) OnSurfaceMuted else OnBackground
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isRead) OnSurface else OnBackground
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = dateStr,
                style = MaterialTheme.typography.labelSmall,
                color = OnSurfaceDim
            )
        }
        
        if (!isRead) {
            Spacer(Modifier.width(8.dp))
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(accentColor).align(Alignment.CenterVertically))
        }
    }
}
