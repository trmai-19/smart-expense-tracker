package com.smartexpense.android.presentation.camera.confirm

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.smartexpense.android.R
import com.smartexpense.android.di.ViewModelFactory
import com.smartexpense.android.presentation.history.ExpenseViewModel
import com.smartexpense.android.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ConfirmScreen(
    imagePath: String,
    onConfirmSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    expenseViewModel: ExpenseViewModel = viewModel(factory = ViewModelFactory.getInstance())
) {
    val accentColor = LocalAccentColor.current

    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Ăn uống") }
    var caption by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf<String?>(null) }

    val createSuccess by expenseViewModel.createSuccess.observeAsState()
    val isLoading by expenseViewModel.isLoading.observeAsState(false)

    val categories = listOf("Ăn uống", "Di chuyển", "Mua sắm", "Giải trí", "Sức khỏe", "Hóa đơn", "Giáo dục", "Khác")

    LaunchedEffect(createSuccess) {
        if (createSuccess == true) onConfirmSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
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
            Text("Xác nhận chi tiêu", style = MaterialTheme.typography.headlineMedium, color = OnBackground)
        }

        // ── Preview image (3:4 Locket style) ──────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp)
                .aspectRatio(3f / 4f)
                .clip(RoundedCornerShape(28.dp))
        ) {
            AsyncImage(
                model = Uri.parse(imagePath),
                contentDescription = "Expense photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(Modifier.height(24.dp))

        // ── Form ───────────────────────────────────────────────────
        Column(
            modifier = Modifier.padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Amount
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it; amountError = null },
                label = { Text("Số tiền (VNĐ)") },
                isError = amountError != null,
                supportingText = amountError?.let { { Text(it) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = SurfaceCard,
                    focusedLabelColor = accentColor,
                    focusedTextColor = OnBackground,
                    unfocusedTextColor = OnSurface,
                    cursorColor = accentColor
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Caption
            OutlinedTextField(
                value = caption,
                onValueChange = { caption = it },
                label = { Text("Ghi chú (tuỳ chọn)") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = SurfaceCard,
                    focusedLabelColor = accentColor,
                    focusedTextColor = OnBackground,
                    unfocusedTextColor = OnSurface,
                    cursorColor = accentColor
                ),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )

            // Category
            Text("Danh mục", style = MaterialTheme.typography.labelLarge, color = OnSurfaceMuted)
            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.height(100.dp)
            ) {
                items(categories.size) { i ->
                    val cat = categories[i]
                    val sel = cat == selectedCategory
                    Surface(
                        onClick = { selectedCategory = cat },
                        shape = RoundedCornerShape(12.dp),
                        color = if (sel) accentColor else SurfaceCard
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(8.dp)) {
                            Text(
                                cat,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (sel) Background else OnSurfaceMuted
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Confirm button ─────────────────────────────────────────
        Button(
            onClick = {
                val amtLong = amount.replace(",", "").replace(".", "").toLongOrNull()
                if (amtLong == null || amtLong <= 0) {
                    amountError = "Vui lòng nhập số tiền hợp lệ"
                    return@Button
                }
                expenseViewModel.createExpense(
                    amount = amtLong.toDouble(),
                    category = selectedCategory,
                    photoUrl = imagePath,
                    caption = caption.ifBlank { "" },
                    expenseDate = LocalDateTime.now()
                        .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                )
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor),
            shape = MaterialTheme.shapes.medium
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Background, strokeWidth = 2.dp)
            } else {
                Text("Xác nhận", style = MaterialTheme.typography.labelLarge, color = Background)
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}
