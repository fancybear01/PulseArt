package com.coding.pulseart.navigation

sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("art")
    data object Favourite : NavRoutes("favourite")
    data object Settings : NavRoutes("settings")
}