package com.smartexpense.android.presentation.auth.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smartexpense.android.R
import com.smartexpense.android.di.ViewModelFactory
import com.smartexpense.android.presentation.auth.AuthViewModel
import com.smartexpense.android.presentation.util.ThemeManager
import com.smartexpense.android.presentation.util.UserManager
import com.smartexpense.android.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit,
    authViewModel: AuthViewModel = viewModel(factory = ViewModelFactory.getInstance())
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val accentColor = LocalAccentColor.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }

    val authSuccess by authViewModel.authSuccess.observeAsState()
    val authError   by authViewModel.authError.observeAsState()
    val isLoading   by authViewModel.isLoading.observeAsState(false)

    // Handle success
    LaunchedEffect(authSuccess) {
        authSuccess?.let { response ->
            UserManager.saveToken(context, response.token)
            UserManager.setUserName(context, response.displayName)
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Background, Surface)
                )
            )
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            // ── Logo ──────────────────────────────────────────────
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_logo_set),
                contentDescription = "SET Logo",
                tint = accentColor,
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "SET",
                style = MaterialTheme.typography.displayMedium,
                color = accentColor
            )
            Text(
                text = "Smart Expense Tracker",
                style = MaterialTheme.typography.bodyMedium,
                color = OnSurfaceMuted
            )

            Spacer(modifier = Modifier.height(52.dp))

            // ── Email field ───────────────────────────────────────
            TextField(
                value = email,
                onValueChange = { email = it; emailError = null },
                label = { Text("Tên đăng nhập / Email") },
                isError = emailError != null,
                supportingText = emailError?.let { { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                colors = setTextFieldColors(accentColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isEmailFocused = it.isFocused }
                    .border(
                        width = 1.dp,
                        color = if (isEmailFocused) accentColor else androidx.compose.ui.graphics.Color.Transparent,
                        shape = MaterialTheme.shapes.medium
                    ),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // ── Password field ────────────────────────────────────
            TextField(
                value = password,
                onValueChange = { password = it; passwordError = null },
                label = { Text("Mật khẩu") },
                isError = passwordError != null,
                supportingText = passwordError?.let { { Text(it) } },
                visualTransformation = if (passwordVisible) VisualTransformation.None
                                       else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_visibility),
                            contentDescription = "Toggle password",
                            tint = OnSurfaceDim
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                colors = setTextFieldColors(accentColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isPasswordFocused = it.isFocused }
                    .border(
                        width = 1.dp,
                        color = if (isPasswordFocused) accentColor else androidx.compose.ui.graphics.Color.Transparent,
                        shape = MaterialTheme.shapes.medium
                    ),
                singleLine = true,
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Auth error ────────────────────────────────────────
            AnimatedVisibility(visible = authError != null) {
                Text(
                    text = authError ?: "",
                    color = ErrorColor,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Login button ──────────────────────────────────────
            Button(
                onClick = {
                    focusManager.clearFocus()
                    var valid = true
                    if (email.isBlank()) { emailError = "Vui lòng nhập email"; valid = false }
                    if (password.isBlank()) { passwordError = "Vui lòng nhập mật khẩu"; valid = false }
                    if (valid) authViewModel.login(email.trim(), password)
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = accentColor),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Background,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "ĐĂNG NHẬP",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Background
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── Go to register ────────────────────────────────────
            Row(
                modifier = Modifier.clickable { onNavigateToRegister() },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Chưa có tài khoản?",
                    color = OnSurfaceMuted,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = " Đăng ký ngay",
                    color = accentColor,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun setTextFieldColors(accentColor: androidx.compose.ui.graphics.Color) =
    TextFieldDefaults.colors(
        focusedContainerColor = SurfaceCard,
        unfocusedContainerColor = SurfaceCard,
        disabledContainerColor = SurfaceCard,
        errorContainerColor = SurfaceCard,
        focusedIndicatorColor = accentColor,
        unfocusedIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        disabledIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        errorIndicatorColor = androidx.compose.ui.graphics.Color.Transparent,
        cursorColor = accentColor,
        focusedTextColor = OnBackground,
        unfocusedTextColor = OnSurface,
        focusedLabelColor = accentColor,
        unfocusedLabelColor = OnSurfaceMuted
    )
