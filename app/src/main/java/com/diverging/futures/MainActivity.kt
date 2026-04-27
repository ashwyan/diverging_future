package com.diverging.futures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.diverging.futures.ui.screens.ConceptScreen
import com.diverging.futures.ui.screens.HomeScreen
import com.diverging.futures.ui.screens.IntroScreen
import com.diverging.futures.ui.screens.PetitionScreen
import com.diverging.futures.ui.screens.SplashScreen
import com.diverging.futures.ui.theme.DivergingFuturesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DivergingFuturesTheme {
                MainNavigation()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onGetStarted = {
                navController.navigate("concept")
            })
        }
        composable("concept") {
            ConceptScreen(
                onContinue = { navController.navigate("intro") },
                onSkip = { navController.navigate("intro") },
                onBack = { 
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    }
                }
            )
        }
        composable("splash") {
            SplashScreen(onTimeout = {
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            })
        }
        composable("intro") {
            IntroScreen(
                onSiteSelected = { site ->
                    navController.navigate("ar/${site.id}")
                },
                onBack = { 
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    }
                }
            )
        }
        composable("ar/{siteId}") { backStackEntry ->
            val siteId = backStackEntry.arguments?.getString("siteId")
            ArScreen(
                siteId = siteId,
                onNavigateToPetition = {
                    navController.navigate("petition")
                },
                onBack = { 
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    }
                }
            )
        }
        composable("petition") {
            PetitionScreen(
                onBack = { 
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    }
                }
            )
        }
    }
}

@Composable
fun ArScreen(siteId: String?, onNavigateToPetition: () -> Unit, onBack: () -> Unit) {
    // This is a placeholder for ArViewActivity logic integrated into Compose
    // In a full implementation, this would use AndroidView to host an ARCore SurfaceView
    // and overlay the LensOverlayView and LensTabBar.
    com.diverging.futures.ui.screens.ArViewContent(siteId, onNavigateToPetition, onBack)
}
