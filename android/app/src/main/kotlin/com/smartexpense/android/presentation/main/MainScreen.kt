package com.smartexpense.android.presentation.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Check
import com.smartexpense.android.ui.theme.SurfaceCard
import androidx.navigation.NavController
import com.smartexpense.android.R
import com.smartexpense.android.presentation.camera.CameraScreen
import com.smartexpense.android.presentation.chat.ChatScreen
import com.smartexpense.android.presentation.dashboard.DashboardScreen
import com.smartexpense.android.presentation.navigation.Screen
import com.smartexpense.android.presentation.navigation.SetBottomBar
import com.smartexpense.android.presentation.notification.NotificationSheet
import com.smartexpense.android.presentation.util.ThemeManager
import com.smartexpense.android.presentation.history.ExpenseViewModel
import com.smartexpense.android.presentation.widget.WidgetGridScreen
import com.smartexpense.android.ui.theme.Background
import com.smartexpense.android.ui.theme.OnSurface
import com.smartexpense.android.ui.theme.OnSurfaceMuted
import com.smartexpense.android.ui.theme.LocalAccentBrush
import androidx.compose.runtime.livedata.observeAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController,
    onAccentColorChanged: () -> Unit
) {
    val context = LocalContext.current
    val accentColor = ThemeManager.getAccentComposeColor(context)
    // 4 tabs: Widget(0), Stats(1), Camera(2), Chat(3)
    val pagerState = rememberPagerState(initialPage = 2) { 4 }
    val coroutineScope = rememberCoroutineScope()

    var showNotifications by remember { mutableStateOf(false) }
    var showThemePicker by remember { mutableStateOf(false) }

    val expenseViewModel: ExpenseViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = com.smartexpense.android.di.ViewModelFactory.getInstance())

    LaunchedEffect(Unit) {
        expenseViewModel.fetchExpenses()
    }

    val expensesState by expenseViewModel.expenses.observeAsState(emptyList())
    val expenses = expensesState ?: emptyList()
    var selectedCategory by remember { mutableStateOf("Tất cả") }
    val categories = listOf("Tất cả") + expenses.map { it.category }.distinct().sorted()
    val filtered = if (selectedCategory == "Tất cả") expenses else expenses.filter { it.category == selectedCategory }

    // Camera = vertical page 0; history = pages 1..N (newest first)
    // derivedStateOf ensures pageCount lambda always reflects current filtered.size
    val verticalPagerState = androidx.compose.foundation.pager.rememberPagerState(
        initialPage = 0,
        pageCount = { filtered.size + 1 }
    )

    // When data loads, filtered.size > 0 but pager is still on page 0 (camera) – OK, no scroll needed
    // When widget navigates to history, it calls verticalPagerState.animateScrollToPage(index+1)

    // FAB hidden only when on the actual camera page (vertical page 0) in the Camera tab
    val isOnCameraPage = pagerState.currentPage == 2 && verticalPagerState.currentPage == 0

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background.copy(alpha = 0.9f))
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left icon (Star / Profile)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                        .clickable { navController.navigate(Screen.Profile.route) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_star_avatar),
                        contentDescription = "Profile",
                        tint = Background,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Title / Category filter
                val onCameraPageExact = pagerState.currentPage == 2 && verticalPagerState.currentPage == 0
                when {
                    onCameraPageExact -> {
                        // Camera page: show "SET" gradient title
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                text = "SET",
                                style = MaterialTheme.typography.displaySmall.copy(
                                    fontWeight = FontWeight.Black,
                                    brush = LocalAccentBrush.current
                                )
                            )
                        }
                    }
                    pagerState.currentPage == 0 || pagerState.currentPage == 2 -> {
                        // Widget or history: show category dropdown
                        var expanded by remember { mutableStateOf(false) }
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Box {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .clip(androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                                        .background(SurfaceCard)
                                        .border(1.dp, LocalAccentBrush.current, androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                                        .clickable { expanded = true }
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = selectedCategory,
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = androidx.compose.ui.graphics.Color.White
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Dropdown",
                                        tint = androidx.compose.ui.graphics.Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier
                                        .background(SurfaceCard)
                                        .width(160.dp)
                                        .heightIn(max = (5 * 48).dp)
                                ) {
                                    categories.forEach { cat ->
                                        val isSelected = selectedCategory == cat
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = cat,
                                                    color = if (isSelected) accentColor else OnSurface,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            trailingIcon = {
                                                if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = accentColor,
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            },
                                            onClick = {
                                                selectedCategory = cat
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                    else -> {
                        Text(
                            text = if (pagerState.currentPage == 1) "Thống kê" else "Trợ lý SET AI",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                            color = accentColor,
                            modifier = Modifier.weight(1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }

                // Bell
                IconButton(onClick = { showNotifications = true }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_notifications),
                        contentDescription = "Thông báo",
                        tint = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        bottomBar = {
            SetBottomBar(
                currentPage = pagerState.currentPage,
                isOnCameraPage = isOnCameraPage,
                accentColor = accentColor,
                onPageSelected = { page ->
                    coroutineScope.launch {
                        if (page == 2) {
                            // Go to camera tab and ensure camera page (vertical page 0)
                            pagerState.animateScrollToPage(2)
                            verticalPagerState.animateScrollToPage(0)
                        } else {
                            pagerState.animateScrollToPage(page)
                        }
                    }
                },
                onPaletteClick = { showThemePicker = true }
            )
        }
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            userScrollEnabled = true
        ) { page ->
            Box(modifier = Modifier.fillMaxSize()) {
                when (page) {
                    0 -> WidgetGridScreen(
                        expenses = filtered,
                        onExpenseClick = { index ->
                            coroutineScope.launch {
                                // Camera tab, then scroll to history page (index+1 because page 0 = camera)
                                pagerState.animateScrollToPage(2)
                                verticalPagerState.animateScrollToPage(index + 1)
                            }
                        }
                    )
                    1 -> DashboardScreen()
                    2 -> Box(modifier = Modifier.fillMaxSize()) {
                        CameraScreen(
                            verticalPagerState = verticalPagerState,
                            expenses = filtered,
                            onCaptureConfirm = { path ->
                                navController.navigate(Screen.Confirm.createRoute(path))
                            }
                        )
                    }
                    3 -> ChatScreen()
                }
            }
        }
    }

    if (showNotifications) {
        NotificationSheet(onDismiss = { showNotifications = false })
    }

    if (showThemePicker) {
        com.smartexpense.android.presentation.theme.ThemePickerSheet(
            onDismiss = { showThemePicker = false },
            onColorSelected = { onAccentColorChanged() }
        )
    }
}
