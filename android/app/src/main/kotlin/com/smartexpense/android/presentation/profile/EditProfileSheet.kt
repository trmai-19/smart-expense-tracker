package com.smartexpense.android.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.smartexpense.android.presentation.util.UserManager
import com.smartexpense.android.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileSheet(
    onDismiss: () -> Unit,
    onProfileUpdated: () -> Unit
) {
    val context = LocalContext.current
    val accentColor = LocalAccentColor.current

    var name by remember { mutableStateOf(UserManager.getUserName(context)) }
    var email by remember { mutableStateOf(UserManager.getUserEmail(context)) }
    var phone by remember { mutableStateOf(UserManager.getUserPhone(context)) }
    var budget by remember { mutableStateOf(UserManager.getMonthlyBudget(context).toString()) }
    var selectedEmoji by remember { mutableStateOf(UserManager.getAvatarEmoji(context)) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var budgetError by remember { mutableStateOf<String?>(null) }

    val emojis = listOf("✨", "👑", "🚀", "🐱", "🔥", "🎯", "💰", "🍔")

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
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text("Chỉnh sửa hồ sơ", style = MaterialTheme.typography.headlineMedium, color = OnBackground)
            Spacer(Modifier.height(24.dp))

            // Emoji Picker
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f))
            ) {
                Text(selectedEmoji, style = MaterialTheme.typography.displayMedium)
            }
            Spacer(Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(8),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                items(emojis) { emoji ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (emoji == selectedEmoji) accentColor else SurfaceCard)
                            .clickable { selectedEmoji = emoji }
                    ) {
                        Text(emoji)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Form Fields
            OutlinedTextField(
                value = name,
                onValueChange = { name = it; nameError = null },
                label = { Text("Tên hiển thị") },
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors(accentColor)
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors(accentColor)
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Số điện thoại") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors(accentColor)
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = budget,
                onValueChange = { budget = it; budgetError = null },
                label = { Text("Ngân sách tháng (VNĐ)") },
                isError = budgetError != null,
                supportingText = budgetError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = textFieldColors(accentColor)
            )

            Spacer(Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = accentColor)
                ) {
                    Text("Hủy")
                }

                Button(
                    onClick = {
                        if (name.isBlank()) { nameError = "Vui lòng nhập tên"; return@Button }
                        val budgetLong = budget.toLongOrNull()
                        if (budgetLong == null || budgetLong <= 0) {
                            budgetError = "Ngân sách không hợp lệ"; return@Button
                        }

                        UserManager.setUserName(context, name.trim())
                        UserManager.setUserEmail(context, email.trim())
                        UserManager.setUserPhone(context, phone.trim())
                        UserManager.setAvatarEmoji(context, selectedEmoji)
                        UserManager.setMonthlyBudget(context, budgetLong)

                        onProfileUpdated()
                    },
                    modifier = Modifier.weight(1f).height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                ) {
                    Text("Lưu", color = Background)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun textFieldColors(accentColor: androidx.compose.ui.graphics.Color) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = accentColor,
    unfocusedBorderColor = SurfaceCard,
    focusedLabelColor = accentColor,
    focusedTextColor = OnBackground,
    unfocusedTextColor = OnSurface
)
