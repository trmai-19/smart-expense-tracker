package com.smartexpense.android.presentation.navigation

/**
 * Tất cả các route trong ứng dụng.
 * Auth flow và Main flow tách biệt.
 */
sealed class Screen(val route: String) {
    // ─── Auth Flow ────────────────────────────────────────────────
    data object Login    : Screen("login")
    data object Register : Screen("register")

    // ─── Main Flow (Pager) ────────────────────────────────────────
    data object MainFlow   : Screen("main_flow")
    
    // ─── History / Timeline (Locket style) ────────────────────────
    data object Timeline   : Screen("timeline/{initialIndex}") {
        fun createRoute(index: Int) = "timeline/$index"
    }

    // ─── Full Screen ──────────────────────────────────────────────
    data object Profile : Screen("profile")
}
