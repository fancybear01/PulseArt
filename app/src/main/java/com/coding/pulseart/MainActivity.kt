package com.coding.pulseart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.coding.pulseart.feature_main_screen.presentation.art_detail.ArtworkDetailScreenCore
import com.coding.pulseart.feature_main_screen.presentation.art_favorites.FavouriteScreenCore
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtworkListScreenCore
import com.coding.pulseart.feature_main_screen.presentation.art_search.SearchScreenCore
import com.coding.pulseart.navigation.BottomNavigationBar
import com.coding.pulseart.navigation.Screen
import com.coding.pulseart.ui.theme.PulseArtTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PulseArtTheme {
                MainScreen()
            }
        }
    }
}


//@Composable
//fun Main() {
//    val navController = rememberNavController()
//    Column(Modifier.padding(8.dp)) {
//        NavHost(
//            navController,
//            startDestination = NavRoutes.Home.route,
//            modifier = Modifier.weight(1f)
//        ) {
//            composable(NavRoutes.Home.route) { ArtworkListScreenCore {  } }
//            composable(NavRoutes.Favourite.route) { Contacts() }
//            composable(NavRoutes.Settings.route) { About() }
//        }
//        BottomNavigationBar(navController = navController)
//    }
//}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ArtworkList,
            modifier = Modifier.padding(padding)
        ) {
            composable<Screen.ArtworkList> {
                ArtworkListScreenCore {
                    navController.navigate(Screen.ArtworkDetails(it))
                }
            }
            composable<Screen.Favourite> {
                FavouriteScreenCore {
                    navController.navigate(Screen.ArtworkDetails(it))
                }
            }
            composable<Screen.Search> {
                SearchScreenCore {
                    navController.navigate(Screen.ArtworkDetails(it))
                }
            }
            composable<Screen.ArtworkDetails> { backStackEntry ->
                val artwork: Screen.ArtworkDetails = backStackEntry.toRoute()
                ArtworkDetailScreenCore(artworkId = artwork.artworkId)
            }
        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    MainScreen()
}