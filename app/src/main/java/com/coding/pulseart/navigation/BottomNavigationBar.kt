package com.coding.pulseart.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.coding.pulseart.ui.theme.PulseArtTheme
@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer
    ) {
        NavBarItems.BarItems.forEach { navItem ->
            NavigationBarItem(
                selected = currentRoute == navItem.route.toString(),
                onClick = {
                    // Используем Screen для навигации
                    when (navItem.screen) {
                        is Screen.ArtworkList -> navController.navigate(Screen.ArtworkList)
                        is Screen.Favourite -> navController.navigate(Screen.Favourite)
                        is Screen.Search -> navController.navigate(Screen.Search)
                        is Screen.ArtworkDetails -> navController.navigate(Screen.ArtworkDetails("1234"))
                    }
                },
                icon = {
                    Icon(
                        imageVector = navItem.image,
                        contentDescription = navItem.title
                    )
                },
                label = {
                    Text(text = navItem.title)
                }
            )
        }
    }
}