package com.smartexpense.android.presentation.camera

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.smartexpense.android.R
import com.smartexpense.android.di.ViewModelFactory
import com.smartexpense.android.presentation.profile.ProfileViewModel
import com.smartexpense.android.presentation.history.ExpenseViewModel
import com.smartexpense.android.ui.theme.*
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    verticalPagerState: androidx.compose.foundation.pager.PagerState,
    expenses: List<com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto>,
    expenseViewModel: ExpenseViewModel,
    profileViewModel: ProfileViewModel = viewModel(factory = ViewModelFactory.getInstance())
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val accentColor = LocalAccentColor.current

    val userProfile by profileViewModel.userProfile.observeAsState(null)
    
    // States for Confirm Flow
    // 0: Camera, 1: Caption input, 2: Loading AI, 3: Confirm Form
    var captureState by remember { mutableIntStateOf(0) }
    var capturedImagePath by remember { mutableStateOf<String?>(null) }
    
    var amount by remember { mutableStateOf("") }
    var caption by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Khác") }
    var amountError by remember { mutableStateOf<String?>(null) }
    var serverPhotoUrl by remember { mutableStateOf("") }

    val baseCategories = remember { mutableStateListOf("Ăn uống", "Di chuyển", "Mua sắm", "Giải trí", "Sức khỏe", "Hóa đơn", "Giáo dục", "Khác") }

    val createSuccess by expenseViewModel.createSuccess.observeAsState()
    val isLoading by expenseViewModel.isLoading.observeAsState(false)
    val analyzeResult by expenseViewModel.analyzeResult.observeAsState()

    LaunchedEffect(analyzeResult) {
        if (captureState == 2 && analyzeResult != null) {
            val res = analyzeResult!!
            if (res.amount > 0) amount = res.amount.toString()
            if (res.category.isNotBlank()) {
                val cat = res.category.take(20)
                if (!baseCategories.contains(cat)) {
                    baseCategories.add(0, cat)
                }
                selectedCategory = cat
            }
            if (res.photoUrl.isNotBlank()) serverPhotoUrl = res.photoUrl
            captureState = 3 // move to form
        }
    }

    val error by expenseViewModel.error.observeAsState()
    LaunchedEffect(error) {
        if (error != null) {
            android.widget.Toast.makeText(context, error, android.widget.Toast.LENGTH_LONG).show()
            if (captureState == 2 || captureState == 3) {
                captureState = 1 // Quay lại màn hình nhập caption
            }
            expenseViewModel.clearError()
        }
    }

    LaunchedEffect(createSuccess) {
        if (createSuccess == true && captureState == 3) {
            captureState = 0 // return to camera
            capturedImagePath = null
            caption = ""
            amount = ""
            // UI will automatically update history because ExpenseViewModel fetches on success
        }
    }

    LaunchedEffect(Unit) {
        profileViewModel.fetchProfile()
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasCameraPermission = granted }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var isCapturing by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { 
            capturedImagePath = it.toString()
            serverPhotoUrl = it.toString()
            amount = ""
            selectedCategory = "Khác"
            caption = ""
            captureState = 1
        }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    androidx.compose.foundation.pager.VerticalPager(
        state = verticalPagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.Transparent)
    ) { page ->
        if (page == 0) {
            // ── Camera page ────────────────────────────────────────────
            if (hasCameraPermission) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Camera Preview or Captured Image (top, fills most of the screen)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .clip(RoundedCornerShape(40.dp))
                    ) {
                        if (captureState == 0) {
                            // Live Camera Preview
                            key(lensFacing) {
                                AndroidView(
                                    factory = { ctx ->
                                        val previewView = PreviewView(ctx)
                                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                                        cameraProviderFuture.addListener({
                                            val cameraProvider = cameraProviderFuture.get()
                                            val preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
                                            val capture = ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()
                                            imageCapture = capture
                                            val selector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
                                            try {
                                                cameraProvider.unbindAll()
                                                cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview, capture)
                                            } catch (e: Exception) { e.printStackTrace() }
                                        }, ContextCompat.getMainExecutor(ctx))
                                        previewView
                                    },
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        } else {
                            // Captured Image Overlay
                            AsyncImage(
                                model = Uri.parse(capturedImagePath),
                                contentDescription = "Captured preview",
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            
                            // Close Button (Cancel)
                            IconButton(
                                onClick = { 
                                    captureState = 0
                                    
                                    // Delete server file if exists
                                    analyzeResult?.photoUrl?.let { url ->
                                        expenseViewModel.deleteFile(url)
                                    }
                                    
                                    // Delete local file
                                    capturedImagePath?.let { path ->
                                        try {
                                            val uri = android.net.Uri.parse(path)
                                            if (uri.scheme == "file") {
                                                java.io.File(uri.path!!).delete()
                                            }
                                        } catch (e: Exception) { e.printStackTrace() }
                                    }
                                    
                                    capturedImagePath = null
                                    
                                    // Clear states
                                    caption = ""
                                    amount = ""
                                    selectedCategory = "Khác"
                                },
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.TopStart)
                                    .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, "Cancel", tint = Color.White)
                            }

                            // State 2: Loading Overlay (Phải đặt trước Caption để Caption không bị làm mờ)
                            if (captureState == 2) {
                                Box(
                                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator(color = accentColor)
                                        Spacer(Modifier.height(8.dp))
                                        Text("AI đang xử lý...", color = Color.White, style = MaterialTheme.typography.labelMedium)
                                    }
                                }
                            }

                            // State 1, 2, 3: Caption Display (Đè lên ảnh)
                            if (captureState in 1..3 && (captureState == 1 || caption.isNotEmpty())) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(bottom = 24.dp, start = 24.dp, end = 24.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(32.dp))
                                        .background(Color.Black.copy(alpha = 0.5f))
                                        .padding(horizontal = 20.dp, vertical = 14.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (captureState == 1) {
                                        androidx.compose.foundation.text.BasicTextField(
                                            value = caption,
                                            onValueChange = { if (it.length <= 60) caption = it },
                                            textStyle = androidx.compose.ui.text.TextStyle(
                                                color = Color.White,
                                                fontSize = androidx.compose.ui.unit.TextUnit(16f, androidx.compose.ui.unit.TextUnitType.Sp),
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                fontWeight = FontWeight.Medium
                                            ),
                                            modifier = Modifier.fillMaxWidth(),
                                            decorationBox = { innerTextField ->
                                                if (caption.isEmpty()) {
                                                    Text(
                                                        text = "caption ...",
                                                        color = Color.White.copy(alpha = 0.6f),
                                                        modifier = Modifier.fillMaxWidth(),
                                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                                                    )
                                                }
                                                innerTextField()
                                            },
                                            maxLines = 2,
                                            cursorBrush = androidx.compose.ui.graphics.SolidColor(Color.White)
                                        )
                                    } else {
                                        Text(
                                            text = caption,
                                            color = Color.White,
                                            fontSize = androidx.compose.ui.unit.TextUnit(16f, androidx.compose.ui.unit.TextUnitType.Sp),
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Form Container when State is 3 (Appears below the image)
                    if (captureState == 3) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedTextField(
                                    value = amount,
                                    onValueChange = { amount = it; amountError = null },
                                    label = { Text("Số tiền (VNĐ)") },
                                    isError = amountError != null,
                                    supportingText = amountError?.let { { Text(it) } },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = accentColor, unfocusedBorderColor = SurfaceCard,
                                        focusedLabelColor = accentColor, cursorColor = accentColor
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )

                                var expanded by remember { mutableStateOf(false) }
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = it }
                                ) {
                                    OutlinedTextField(
                                        value = selectedCategory,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Danh mục") },
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = accentColor, unfocusedBorderColor = SurfaceCard,
                                            focusedLabelColor = accentColor, cursorColor = accentColor
                                        ),
                                        modifier = Modifier.menuAnchor().fillMaxWidth()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                    ) {
                                        baseCategories.forEach { cat ->
                                            DropdownMenuItem(
                                                text = { Text(cat) },
                                                onClick = {
                                                    selectedCategory = cat
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            // Circular Save Button
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(accentColor)
                                    .clickable(enabled = !isLoading) {
                                        val amtLong = amount.replace(",", "").replace(".", "").toLongOrNull()
                                        if (amtLong == null || amtLong <= 0) {
                                            amountError = "Vui lòng nhập số hợp lệ"
                                            return@clickable
                                        }
                                        expenseViewModel.createExpense(
                                            amount = amtLong.toDouble(),
                                            category = selectedCategory,
                                            photoUrl = serverPhotoUrl,
                                            caption = caption.ifBlank { "" },
                                            expenseDate = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                        )
                                    }
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(color = Background, modifier = Modifier.size(24.dp))
                                } else {
                                    Icon(Icons.Default.Check, "Lưu", tint = Background, modifier = Modifier.size(32.dp))
                                }
                            }
                        }
                    } else if (captureState in 0..2) {
                        // Controls row (gallery | capture | flip)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 40.dp, vertical = 16.dp),
                            horizontalArrangement = if (captureState in 1..2) Arrangement.Center else Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (captureState == 0) {
                                // Gallery
                                IconButton(
                                    onClick = { galleryLauncher.launch("image/*") },
                                    modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.1f))
                                ) {
                                    Icon(ImageVector.vectorResource(R.drawable.ic_gallery), "Gallery", tint = Color.White, modifier = Modifier.size(24.dp))
                                }
                            }

                            // Capture or Send button
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (captureState == 1) accentColor
                                        else if (captureState == 2) accentColor.copy(alpha = 0.5f)
                                        else Color.White.copy(alpha = 0.1f)
                                    )
                                    .border(2.dp, if (captureState in 1..2) Color.Transparent else accentColor.copy(alpha = 0.6f), CircleShape)
                                    .clickable(enabled = !isCapturing && captureState in 0..1) {
                                        if (captureState == 1) {
                                            captureState = 2
                                            expenseViewModel.analyzeExpense(capturedImagePath!!, caption)
                                        } else {
                                            val capture = imageCapture ?: return@clickable
                                            isCapturing = true
                                            val outputDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES) ?: context.filesDir
                                            val photoFile = File(outputDir, "SET_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg")
                                            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                                            val executor = ContextCompat.getMainExecutor(context)
                                            capture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
                                                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                                    isCapturing = false
                                                    capturedImagePath = Uri.fromFile(photoFile).toString()
                                                    serverPhotoUrl = capturedImagePath!!
                                                    captureState = 1 // enter caption state
                                                }
                                                override fun onError(e: ImageCaptureException) { isCapturing = false }
                                            })
                                        }
                                    }
                            ) {
                                if (captureState in 1..2) {
                                    if (captureState == 2) {
                                        CircularProgressIndicator(color = Background, modifier = Modifier.size(28.dp), strokeWidth = 3.dp)
                                    } else {
                                        Icon(Icons.Default.Send, "Gửi", tint = Background, modifier = Modifier.size(32.dp))
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(if (isCapturing) Color.White.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.8f))
                                    )
                                }
                            }

                            if (captureState == 0) {
                                // Flip camera
                                IconButton(
                                    onClick = {
                                        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
                                    },
                                    modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.1f))
                                ) {
                                    Icon(ImageVector.vectorResource(R.drawable.ic_flip_camera), "Flip camera", tint = Color.White, modifier = Modifier.size(24.dp))
                                }
                            }
                        }

                        // "Lịch sử ↑" hint at the very bottom
                        if (expenses.isNotEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(bottom = 16.dp)
                            ) {
                                Text("Lịch sử", color = Color.White, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold))
                                Icon(Icons.Default.KeyboardArrowUp, "Up arrow", tint = Color.White, modifier = Modifier.size(20.dp))
                            }
                        } else {
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            } else {
                // Permission denied
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Cần quyền camera", style = MaterialTheme.typography.headlineMedium, color = OnBackground)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                        ) {
                            Text("Cấp quyền", color = Background)
                        }
                    }
                }
            }
        } else {
            // ── History pages: page 1 = newest, page N = oldest ────────
            val expense = expenses.getOrNull(page - 1)
            if (expense != null) {
                HistoryCard(
                    expense = expense,
                    accentColor = accentColor,
                    displayName = userProfile?.displayName ?: "bạn",
                    avatarUrl = userProfile?.avatarUrl
                )
            }
        }
    }
}

@Composable
private fun HistoryCard(
    expense: com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto,
    accentColor: Color,
    displayName: String,
    avatarUrl: String?
) {
    val imageUrl = if (expense.photoUrl.startsWith("http")) expense.photoUrl
                   else "${com.smartexpense.android.data.remote.RetrofitClient.BASE_URL.removeSuffix("/")}${if (expense.photoUrl.startsWith("/")) expense.photoUrl else "/${expense.photoUrl}"}"

    val formattedAmount = java.text.DecimalFormat(
        "#,###",
        java.text.DecimalFormatSymbols(java.util.Locale("vi", "VN")).apply { groupingSeparator = '.' }
    ).format(expense.amount.toLong()) + " đ"

    val dateText = remember(expense.expenseDate) {
        runCatching {
            val formats = listOf(
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()),
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault()),
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            )
            val parsed = formats.firstNotNullOfOrNull { fmt ->
                runCatching { fmt.parse(expense.expenseDate) }.getOrNull()
            }
            if (parsed != null) {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val expDay = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(parsed)
                val timeStr = SimpleDateFormat("HH:mm", Locale.getDefault()).format(parsed)
                if (today == expDay) "Hôm nay lúc $timeStr"
                else SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(parsed)
            } else expense.expenseDate.take(10)
        }.getOrDefault(expense.expenseDate.take(10))
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(Color(0xFF1E243A))
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = expense.caption ?: "Không có chú thích",
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            if (!expense.caption.isNullOrEmpty()) {
                Text(
                    text = expense.caption,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                        .background(Color(0x99000000), CircleShape)
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(2.dp, accentColor, CircleShape)
                    .background(Color(0xFF1E243A)),
                contentAlignment = Alignment.Center
            ) {
                if (!avatarUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = if (avatarUrl.startsWith("http")) avatarUrl
                                else "${com.smartexpense.android.data.remote.RetrofitClient.BASE_URL}$avatarUrl",
                        contentDescription = "Avatar",
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .border(2.dp, accentColor, CircleShape)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = displayName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = OnBackground
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = androidx.compose.ui.text.buildAnnotatedString {
                append("${expense.category} · $dateText · ")
                withStyle(androidx.compose.ui.text.SpanStyle(color = accentColor, fontWeight = FontWeight.Bold)) {
                    append(formattedAmount)
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceMuted,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}
