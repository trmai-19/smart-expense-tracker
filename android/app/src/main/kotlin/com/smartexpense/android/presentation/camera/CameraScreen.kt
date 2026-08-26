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
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import com.smartexpense.android.ui.theme.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executors

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CameraScreen(
    verticalPagerState: androidx.compose.foundation.pager.PagerState,
    expenses: List<com.smartexpense.android.data.remote.dto.response.ExpenseResponseDto>,
    onCaptureConfirm: (String) -> Unit,
    profileViewModel: ProfileViewModel = viewModel(factory = ViewModelFactory.getInstance())
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val accentColor = LocalAccentColor.current

    val userProfile by profileViewModel.userProfile.observeAsState(null)

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
        uri?.let { onCaptureConfirm(it.toString()) }
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    // Layout: page 0 = camera, page 1..N = history (newest first)
    // Swipe UP (finger bottom→top) goes from page 0 to page 1 = newest expense

    androidx.compose.foundation.pager.VerticalPager(
        state = verticalPagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) { page ->
        if (page == 0) {
            // ── Camera page ────────────────────────────────────────────
            if (hasCameraPermission) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Controls row (top)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { galleryLauncher.launch("image/*") },
                            modifier = Modifier.size(56.dp).clip(CircleShape).background(SurfaceVariant)
                        ) {
                            Icon(ImageVector.vectorResource(R.drawable.ic_gallery), "Gallery", tint = OnSurface, modifier = Modifier.size(28.dp))
                        }

                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .border(4.dp, accentColor, CircleShape)
                                .background(if (isCapturing) accentColor.copy(alpha = 0.5f) else Color.White)
                                .clickable(enabled = !isCapturing) {
                                    val capture = imageCapture ?: return@clickable
                                    isCapturing = true
                                    val outputDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES) ?: context.filesDir
                                    val photoFile = File(outputDir, "SET_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}.jpg")
                                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                                    val executor = Executors.newSingleThreadExecutor()
                                    capture.takePicture(outputOptions, executor, object : ImageCapture.OnImageSavedCallback {
                                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                            isCapturing = false
                                            onCaptureConfirm(Uri.fromFile(photoFile).toString())
                                        }
                                        override fun onError(e: ImageCaptureException) { isCapturing = false }
                                    })
                                }
                        ) {}

                        IconButton(
                            onClick = {
                                lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT else CameraSelector.LENS_FACING_BACK
                            },
                            modifier = Modifier.size(56.dp).clip(CircleShape).background(SurfaceVariant)
                        ) {
                            Icon(ImageVector.vectorResource(R.drawable.ic_flip_camera), "Flip camera", tint = OnSurface, modifier = Modifier.size(28.dp))
                        }
                    }

                    // Camera Preview (middle, fills remaining space)
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .clip(RoundedCornerShape(40.dp))
                    ) {
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
                    }

                    // "Lich su up" hint at bottom
                    if (expenses.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                        ) {
                            Text("Lịch sử", color = accentColor, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium))
                            Icon(Icons.Default.KeyboardArrowUp, "Up arrow", tint = accentColor, modifier = Modifier.size(16.dp))
                        }
                    } else {
                        Spacer(modifier = Modifier.height(32.dp))
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
                   else "${com.smartexpense.android.data.remote.RetrofitClient.BASE_URL}${expense.photoUrl}"

    val formattedAmount = java.text.DecimalFormat(
        "#,###",
        java.text.DecimalFormatSymbols(java.util.Locale("vi", "VN")).apply { groupingSeparator = '.' }
    ).format(expense.amount.toLong()) + " đ"

    // Parse expenseDate: ISO format "yyyy-MM-dd'T'HH:mm:ss" or "yyyy-MM-dd"
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
        // Photo Card
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
                contentDescription = expense.caption,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Caption Pill
            if (expense.caption.isNotEmpty()) {
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

        // User Info row
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
                    // Fallback: accent circle
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

        // Meta Info
        Text(
            text = androidx.compose.ui.text.buildAnnotatedString {
                append("${expense.category} · $dateText · ")
                withStyle(androidx.compose.ui.text.SpanStyle(color = accentColor)) {
                    append(formattedAmount)
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            color = OnSurfaceMuted,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}
