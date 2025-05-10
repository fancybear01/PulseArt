package com.coding.pulseart

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.coding.pulseart.feature_main_screen.presentation.art_detail.ArtworkDetailScreenCore
import com.coding.pulseart.feature_main_screen.presentation.art_favorites.FavouriteScreenCore
import com.coding.pulseart.feature_main_screen.presentation.art_list.ArtworkListScreenCore
import com.coding.pulseart.feature_main_screen.presentation.art_search.SearchScreenCore
import com.coding.pulseart.first_launch.OnboardingState
import com.coding.pulseart.navigation.BottomNavigationBar
import com.coding.pulseart.navigation.Screen
import com.coding.pulseart.ui.theme.PulseArtTheme
import com.coding.pulseart.first_launch.OnboardingScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val onboardingState = OnboardingState(this)

        setContent {
            PulseArtTheme {
                var showOnboarding by remember { mutableStateOf(!onboardingState.isOnboardingComplete()) }

                if (showOnboarding) {
                    OnboardingScreen {
                        onboardingState.completeOnboarding()
                        showOnboarding = false
                    }
                } else {
                    MainScreen()
                }
            }
        }
    }
}

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