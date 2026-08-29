package com.smartexpense.android.presentation.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.smartexpense.android.di.ViewModelFactory
import com.smartexpense.android.presentation.history.ExpenseViewModel
import com.smartexpense.android.ui.theme.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun WidgetGridScreen(
    expenses: List<com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto>,
    onExpenseClick: (Int) -> Unit
) {
    val accentColor = LocalAccentColor.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.Transparent)
    ) {
        Spacer(Modifier.height(12.dp))

        // ── Grid ───────────────────────────────────────────────────
        if (expenses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Chưa có chi tiêu", color = OnSurfaceMuted)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(expenses.size) { index ->
                    val expense = expenses[index]
                    ExpenseGridCard(
                        photoUrl = expense.photoUrl,
                        amount = expense.amount.toLong(),
                        caption = expense.caption ?: "",
                        accentColor = accentColor,
                        onClick = { onExpenseClick(index) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ExpenseGridCard(
    photoUrl: String,
    amount: Long,
    caption: String,
    accentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit
) {
    val imageUrl = if (photoUrl.startsWith("http")) photoUrl
                   else "${com.smartexpense.android.data.remote.RetrofitClient.BASE_URL.removeSuffix("/")}${if (photoUrl.startsWith("/")) photoUrl else "/$photoUrl"}"

    Box(
        modifier = Modifier
            .aspectRatio(3f / 4f)
            .clip(RoundedCornerShape(16.dp))
            .background(androidx.compose.ui.graphics.Color(0xFF1E243A)) // Placeholder dark blue
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = caption,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        
        // Amount pill top-right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(androidx.compose.ui.graphics.Color(0x80000000), RoundedCornerShape(12.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = formatVnd(amount),
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = accentColor
            )
        }

        // Overlay gradient for caption
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        listOf(
                            androidx.compose.ui.graphics.Color.Transparent,
                            androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
                .padding(10.dp)
        ) {
            Text(
                text = caption,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = androidx.compose.ui.graphics.Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private fun formatVnd(amount: Long): String {
    val symbols = DecimalFormatSymbols(Locale("vi", "VN")).apply { groupingSeparator = '.' }
    return "${DecimalFormat("#,###", symbols).format(amount)} đ"
}
