package com.smartexpense.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.smartexpense.android.presentation.auth.login.LoginScreen
import com.smartexpense.android.presentation.auth.register.RegisterScreen
import com.smartexpense.android.presentation.camera.CameraScreen
import com.smartexpense.android.presentation.camera.confirm.ConfirmScreen
import com.smartexpense.android.presentation.chat.ChatScreen
import com.smartexpense.android.presentation.dashboard.DashboardScreen
import com.smartexpense.android.presentation.history.TimelineScreen
import com.smartexpense.android.presentation.main.MainScreen
import com.smartexpense.android.presentation.profile.ProfileScreen
import com.smartexpense.android.presentation.widget.WidgetGridScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    onThemeUpdated: () -> Unit = {}
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // ─── Auth ─────────────────────────────────────────────────
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.MainFlow.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Screen.MainFlow.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // ─── Main Flow (Pager) ────────────────────────────────────
        composable(Screen.MainFlow.route) {
            MainScreen(
                navController = navController,
                onAccentColorChanged = { onThemeUpdated() }
            )
        }

        // ─── Full Screen ──────────────────────────────────────────
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Confirm.route,
            arguments = listOf(navArgument("imagePath") { type = NavType.StringType })
        ) { backStackEntry ->
            val imagePath = backStackEntry.arguments?.getString("imagePath") ?: ""
            ConfirmScreen(
                imagePath = imagePath,
                onConfirmSuccess = {
                    navController.navigate(Screen.MainFlow.route) {
                        popUpTo(Screen.MainFlow.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
