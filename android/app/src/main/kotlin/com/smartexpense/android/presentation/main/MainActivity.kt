package com.smartexpense.android.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.smartexpense.android.R
import com.smartexpense.android.presentation.navigation.AppNavHost
import com.smartexpense.android.presentation.navigation.Screen
import com.smartexpense.android.presentation.navigation.SetBottomBar
import com.smartexpense.android.presentation.navigation.bottomNavItems
import com.smartexpense.android.presentation.notification.NotificationSheet
import com.smartexpense.android.presentation.util.ThemeManager
import com.smartexpense.android.presentation.util.UserManager
import com.smartexpense.android.ui.theme.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current

            var currentTheme by remember { mutableStateOf(ThemeManager.getAccentColor(context)) }
            var accentColor by remember { mutableStateOf(currentTheme.composeColor) }
            var accentBrush by remember { mutableStateOf(currentTheme.composeBrush) }

            SmartExpenseTheme(accentColor = accentColor, accentBrush = accentBrush) {
                val navController = rememberNavController()

                // Xác định start destination: nếu đã có token thì vào Camera, không thì Login
                val isLoggedIn = UserManager.getToken(context) != null
                val startDestination = if (isLoggedIn) Screen.MainFlow.route else Screen.Login.route

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background)
                ) {
                    // Blob 1 (Top-Center)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .align(Alignment.TopCenter)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        currentTheme.startColor.copy(alpha = 0.3f),
                                        androidx.compose.ui.graphics.Color.Transparent
                                    )
                                )
                            )
                    )
                    // Blob 2 (Bottom-Center)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(
                                        androidx.compose.ui.graphics.Color.Transparent,
                                        currentTheme.endColor.copy(alpha = 0.3f)
                                    )
                                )
                            )
                    )

                    AppNavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.fillMaxSize(),
                        onThemeUpdated = {
                            currentTheme = ThemeManager.getAccentColor(context)
                            accentColor = currentTheme.composeColor
                            accentBrush = currentTheme.composeBrush
                        }
                    )
                }
            }
        }
    }
}
