package com.coding.pulseart.navigation

import androidx.compose.ui.graphics.vector.ImageVector

data class BarItem(
    val title: String,
    val image: ImageVector,
    val route: Screen,
    val screen: Screen
)
