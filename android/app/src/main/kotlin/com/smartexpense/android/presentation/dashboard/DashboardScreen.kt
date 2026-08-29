package com.smartexpense.android.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartexpense.android.data.remote.dto.response.BarEntryDto
import com.smartexpense.android.di.ViewModelFactory
import com.smartexpense.android.ui.theme.*
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private enum class PeriodTab(val id: String, val title: String) {
    WEEK("WEEK", "Tuần"),
    MONTH("MONTH", "Tháng"),
    YEAR("YEAR", "Năm"),
    CUSTOM("CUSTOM", "Tùy chọn")
}

// ── Date Range Picker ────────────────────────────────────────────────────────
// (Giữ nguyên logic DatePicker cũ)
private enum class DatePickerLevel { YEAR, MONTH, DAY }

@Composable
private fun DateRangePicker(
    onConfirm: (Date, Date) -> Unit,
    onDismiss: () -> Unit
) {
    val accentColor = LocalAccentColor.current
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)

    var fromDate by remember { mutableStateOf<Date?>(null) }
    var toDate by remember { mutableStateOf<Date?>(null) }
    var pickingFrom by remember { mutableStateOf(true) }

    var level by remember { mutableStateOf(DatePickerLevel.YEAR) }
    var selectedYear by remember { mutableIntStateOf(currentYear) }
    var selectedMonth by remember { mutableIntStateOf(0) }

    val monthNames = listOf("Tháng 1","Tháng 2","Tháng 3","Tháng 4","Tháng 5","Tháng 6",
                            "Tháng 7","Tháng 8","Tháng 9","Tháng 10","Tháng 11","Tháng 12")

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceCard,
        title = {
            Column {
                Text(
                    text = if (pickingFrom) "Chọn từ ngày" else "Chọn đến ngày",
                    style = MaterialTheme.typography.titleMedium,
                    color = OnBackground
                )
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    if (level != DatePickerLevel.YEAR) {
                        Text(
                            text = "$selectedYear",
                            color = accentColor,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.clickable { level = DatePickerLevel.YEAR }
                        )
                        Text("›", color = OnSurfaceMuted, style = MaterialTheme.typography.labelMedium)
                    }
                    if (level == DatePickerLevel.DAY) {
                        Text(
                            text = monthNames[selectedMonth],
                            color = accentColor,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.clickable { level = DatePickerLevel.MONTH }
                        )
                        Text("›", color = OnSurfaceMuted, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        },
        text = {
            when (level) {
                DatePickerLevel.YEAR -> {
                    val years = (currentYear downTo currentYear - 5).toList()
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        years.chunked(3).forEach { row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                row.forEach { year ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (year == selectedYear) accentColor else Surface)
                                            .clickable { selectedYear = year; level = DatePickerLevel.MONTH }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("$year", color = if (year == selectedYear) Background else OnSurface, fontSize = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                DatePickerLevel.MONTH -> {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        monthNames.chunked(3).forEachIndexed { rowIdx, row ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                row.forEachIndexed { colIdx, month ->
                                    val mIdx = rowIdx * 3 + colIdx
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (mIdx == selectedMonth) accentColor else Surface)
                                            .clickable { selectedMonth = mIdx; level = DatePickerLevel.DAY }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(month.replace("Tháng ", "T"), color = if (mIdx == selectedMonth) Background else OnSurface, fontSize = 13.sp)
                                    }
                                }
                            }
                        }
                    }
                }
                DatePickerLevel.DAY -> {
                    val cal = Calendar.getInstance().apply { set(selectedYear, selectedMonth, 1) }
                    val daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
                    val firstDayOfWeek = (cal.get(Calendar.DAY_OF_WEEK) - 2).let { if (it < 0) 6 else it }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row {
                            listOf("T2","T3","T4","T5","T6","T7","CN").forEach { d ->
                                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                    Text(d, color = OnSurfaceMuted, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        val cells = mutableListOf<Int?>()
                        repeat(firstDayOfWeek) { cells.add(null) }
                        (1..daysInMonth).forEach { cells.add(it) }
                        while (cells.size % 7 != 0) cells.add(null)
                        cells.chunked(7).forEach { week ->
                            Row {
                                week.forEach { day ->
                                    if (day == null) {
                                        Box(modifier = Modifier.weight(1f))
                                    } else {
                                        val date = Calendar.getInstance().apply { set(selectedYear, selectedMonth, day, 0, 0, 0); set(Calendar.MILLISECOND, 0) }.time
                                        val isSelected = (pickingFrom && fromDate?.let { sameDay(it, date) } == true) ||
                                                         (!pickingFrom && toDate?.let { sameDay(it, date) } == true)
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .aspectRatio(1f)
                                                .padding(2.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(if (isSelected) accentColor else androidx.compose.ui.graphics.Color.Transparent)
                                                .clickable {
                                                    if (pickingFrom) { fromDate = date; pickingFrom = false; level = DatePickerLevel.YEAR }
                                                    else { toDate = date }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("$day", color = if (isSelected) Background else OnSurface, fontSize = 13.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (fromDate != null && toDate != null) {
                TextButton(onClick = {
                    val from = minOf(fromDate!!, toDate!!)
                    val to = maxOf(fromDate!!, toDate!!)
                    onConfirm(from, to)
                }) {
                    Text("Xác nhận", color = accentColor, fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = OnSurfaceMuted)
            }
        }
    )
}

private fun sameDay(a: Date, b: Date): Boolean {
    val ca = Calendar.getInstance().apply { time = a }
    val cb = Calendar.getInstance().apply { time = b }
    return ca.get(Calendar.YEAR) == cb.get(Calendar.YEAR) &&
           ca.get(Calendar.DAY_OF_YEAR) == cb.get(Calendar.DAY_OF_YEAR)
}

// ── Bar Chart ──────────────────────────────────────────────────────────────────

@Composable
private fun BarChart(
    bars: List<BarEntryDto>,
    accentBrush: androidx.compose.ui.graphics.Brush
) {
    if (bars.isEmpty()) return
    val maxVal = bars.maxOf { it.amount }.takeIf { it > 0.0 } ?: 1.0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        bars.forEach { entry ->
            val fraction = (entry.amount / maxVal).toFloat().coerceIn(0f, 1f)
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .fillMaxHeight(fraction.coerceAtLeast(0.02f))
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .let { if (fraction > 0f) it.background(accentBrush) else it.background(Surface) }
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    text = entry.label,
                    color = OnSurfaceMuted,
                    fontSize = 10.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

// ── Main Screen ────────────────────────────────────────────────────────────────

@Composable
fun DashboardScreen(
    dashboardViewModel: DashboardViewModel = viewModel(factory = ViewModelFactory.getInstance())
) {
    val accentColor = LocalAccentColor.current
    val accentBrush = LocalAccentBrush.current

    val stats by dashboardViewModel.statistics.observeAsState()
    val isLoading by dashboardViewModel.isLoading.observeAsState(false)

    var selectedTab by remember { mutableStateOf(PeriodTab.MONTH) }
    var showDatePicker by remember { mutableStateOf(false) }
    var customFrom by remember { mutableStateOf<Date?>(null) }
    var customTo by remember { mutableStateOf<Date?>(null) }
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val isoFmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    // Lần đầu mở màn hình hoặc đổi tab (trừ CUSTOM), fetch dữ liệu
    LaunchedEffect(selectedTab) {
        if (selectedTab != PeriodTab.CUSTOM) {
            dashboardViewModel.fetchStatistics(selectedTab.id)
        }
    }

    if (showDatePicker) {
        DateRangePicker(
            onConfirm = { from, to -> 
                customFrom = from
                customTo = to
                showDatePicker = false
                dashboardViewModel.fetchStatistics(PeriodTab.CUSTOM.id, isoFmt.format(from), isoFmt.format(to))
            },
            onDismiss = { showDatePicker = false }
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Color.Transparent).padding(horizontal = 20.dp),
        contentPadding = PaddingValues(top = 20.dp, bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ── Tab row ─────────────────────────────────────────────────────
        item {
            Row(
                modifier = Modifier.fillMaxWidth().height(44.dp)
                    .background(SurfaceCard, RoundedCornerShape(50))
            ) {
                PeriodTab.values().forEach { tab ->
                    val selected = selectedTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f).fillMaxHeight()
                            .clip(RoundedCornerShape(50))
                            .let { if (selected) it.background(accentBrush) else it.background(androidx.compose.ui.graphics.Color.Transparent) }
                            .clickable {
                                selectedTab = tab
                                if (tab == PeriodTab.CUSTOM) showDatePicker = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.title,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (selected) Background else OnSurfaceMuted
                        )
                    }
                }
            }
        }

        // Custom date label
        if (selectedTab == PeriodTab.CUSTOM && customFrom != null && customTo != null) {
            item {
                val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    Text(
                        text = "${fmt.format(customFrom!!)} – ${fmt.format(customTo!!)}",
                        style = MaterialTheme.typography.bodySmall.copy(brush = accentBrush),
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
            }
        }

        if (isLoading) {
            item {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = accentColor)
                }
            }
            return@LazyColumn
        }

        val safeStats = stats
        if (safeStats == null) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("Chưa có dữ liệu", color = OnSurfaceMuted)
                }
            }
            return@LazyColumn
        }

        // ── Total card ───────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    val periodLabel = when (selectedTab) {
                        PeriodTab.WEEK -> "tuần này"
                        PeriodTab.MONTH -> "tháng này"
                        PeriodTab.YEAR -> "năm $currentYear"
                        PeriodTab.CUSTOM -> "khoảng chọn"
                    }
                    Text("Tổng chi tiêu $periodLabel", style = MaterialTheme.typography.bodyMedium, color = OnSurfaceMuted)
                    Spacer(Modifier.height(4.dp))
                    Text(formatVnd(safeStats.totalAmount.toLong()), style = MaterialTheme.typography.displayMedium.copy(brush = accentBrush))
                }
            }
        }

        // ── Bar chart ────────────────────────────────────────────────────
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceVariant)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    val chartTitle = when (selectedTab) {
                        PeriodTab.WEEK -> "7 ngày gần đây"
                        PeriodTab.MONTH -> "4 tuần gần đây"
                        PeriodTab.YEAR -> "12 tháng năm $currentYear"
                        PeriodTab.CUSTOM -> "Theo khoảng thời gian"
                    }
                    Text(chartTitle, style = MaterialTheme.typography.titleSmall, color = OnBackground)
                    Spacer(Modifier.height(16.dp))
                    if (safeStats.bars.isEmpty()) {
                        Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                            Text("Chưa có dữ liệu", color = OnSurfaceMuted)
                        }
                    } else {
                        BarChart(bars = safeStats.bars, accentBrush = accentBrush)
                    }
                }
            }
        }

        // ── Category breakdown ───────────────────────────────────────────
        if (safeStats.categories.isNotEmpty()) {
            item {
                Text("Phân loại chi tiêu", style = MaterialTheme.typography.titleMedium, color = OnBackground)
            }
            items(safeStats.categories.size) { index ->
                val catDto = safeStats.categories[index]
                val pct = if (safeStats.totalAmount > 0) (catDto.amount / safeStats.totalAmount).toFloat() else 0f
                Column(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp))
                        .background(SurfaceCard).padding(16.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("${catDto.category} (${(pct * 100).toInt()}%)", style = MaterialTheme.typography.bodyMedium, color = OnSurface)
                        Text(formatVnd(catDto.amount.toLong()), style = MaterialTheme.typography.bodyMedium, color = accentColor, fontWeight = FontWeight.SemiBold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(Surface)) {
                        Box(modifier = Modifier.fillMaxWidth(pct).fillMaxHeight().clip(RoundedCornerShape(3.dp)).background(accentColor))
                    }
                }
            }
        } else {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                    Text("Chưa có dữ liệu danh mục", color = OnSurfaceMuted)
                }
            }
        }
    }
}

private fun formatVnd(amount: Long): String {
    val symbols = DecimalFormatSymbols(Locale("vi", "VN")).apply { groupingSeparator = '.' }
    return "${DecimalFormat("#,###", symbols).format(amount)} đ"
}
