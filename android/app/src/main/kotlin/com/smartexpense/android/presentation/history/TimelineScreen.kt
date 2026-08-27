package com.smartexpense.android.presentation.history

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.smartexpense.android.data.remote.RetrofitClient
import com.smartexpense.android.di.ViewModelFactory
import com.smartexpense.android.presentation.util.ThemeManager
import com.smartexpense.android.ui.theme.Background
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TimelineScreen(
    initialIndex: Int,
    onNavigateToCamera: () -> Unit,
    onNavigateBack: () -> Unit,
    expenseViewModel: ExpenseViewModel = viewModel(factory = ViewModelFactory.getInstance())
) {
    val expenses by expenseViewModel.expenses.observeAsState(emptyList())
    val safeExpenses = expenses ?: emptyList()

    LaunchedEffect(Unit) {
        if (safeExpenses.isEmpty()) {
            expenseViewModel.fetchExpenses()
        }
    }

    var selectedCategory by remember { mutableStateOf("Tất cả") }
    val categories = listOf("Tất cả") + safeExpenses.map { it.category }.distinct().sorted()
    val filtered = if (selectedCategory == "Tất cả") safeExpenses else safeExpenses.filter { it.category == selectedCategory }

    // 0 is Camera transition, 1..N are photos
    val pageCount = filtered.size + 1
    // Adjust initial page based on whether it's the initial load or a filter change
    var currentItem by remember { mutableStateOf(safeExpenses.getOrNull(initialIndex)) }
    val initialFilteredIndex = if (currentItem != null) filtered.indexOf(currentItem) else 0
    val pagerState = rememberPagerState(initialPage = if (filtered.isEmpty()) 0 else maxOf(1, initialFilteredIndex + 1)) { pageCount }

    LaunchedEffect(pagerState.currentPage) {
        if (pagerState.currentPage == 0) {
            onNavigateToCamera()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        VerticalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            if (page == 0) {
                // Placeholder for Camera transition
                Box(modifier = Modifier.fillMaxSize())
            } else {
                val expense = filtered.getOrNull(page - 1)
                if (expense != null) {
                    val imageUrl = if (expense.photoUrl.startsWith("http")) expense.photoUrl 
                                   else "${RetrofitClient.BASE_URL}${expense.photoUrl}"
                    
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Expense Photo",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(32.dp))
                        )

                        // Top Gradient
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .align(Alignment.TopCenter)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Black.copy(alpha = 0.6f), Color.Transparent)
                                    )
                                )
                        )

                        // Bottom Gradient
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                    )
                                )
                        )

                        // Content (Text overlay)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomStart)
                                .padding(24.dp)
                                .padding(bottom = 32.dp)
                        ) {
                            Text(
                                text = expense.category,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = expense.caption ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
                            Text(
                                text = formatter.format(expense.amount),
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                color = ThemeManager.getAccentComposeColor(androidx.compose.ui.platform.LocalContext.current)
                            )
                        }
                    }
                }
            }
        }

        // Top Bar in Timeline (Filters + Close)
        Row(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Filters
            androidx.compose.foundation.lazy.LazyRow(
                modifier = Modifier.weight(1f).padding(end = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                items(categories.size) { i ->
                    val cat = categories[i]
                    val selected = cat == selectedCategory
                    FilterChip(
                        selected = selected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, style = MaterialTheme.typography.labelSmall) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ThemeManager.getAccentComposeColor(androidx.compose.ui.platform.LocalContext.current),
                            selectedLabelColor = Color.Black,
                            containerColor = Color.Black.copy(alpha = 0.4f),
                            labelColor = Color.White.copy(alpha = 0.8f)
                        ),
                        border = null,
                        modifier = Modifier.padding(horizontal = 4.dp).height(32.dp)
                    )
                }
            }

            // Close button
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .background(Color.Black.copy(alpha = 0.4f), shape = RoundedCornerShape(50))
                    .size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
