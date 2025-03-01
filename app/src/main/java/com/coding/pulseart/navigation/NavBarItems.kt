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
            route = Screen.ArtworkList,
            screen = Screen.ArtworkList
        ),
        BarItem(
            title = "Favourite",
            image = Icons.Filled.Favorite,
            route = Screen.Favourite,
            screen = Screen.Favourite
        ),
        BarItem(
            title = "Settings",
            image = Icons.Filled.Settings,
            route = Screen.Settings,
            screen = Screen.Settings
        )
    )
}