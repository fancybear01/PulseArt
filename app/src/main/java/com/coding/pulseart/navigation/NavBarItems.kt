package com.coding.pulseart.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            title = "Art",
            image = Icons.Filled.Home,
            route = "art"
        ),
        BarItem(
            title = "Favourite",
            image = Icons.Filled.Favorite,
            route = "favourite"
        ),
        BarItem(
            title = "Settings",
            image = Icons.Filled.Settings,
            route = "settings"
        )
    )
}