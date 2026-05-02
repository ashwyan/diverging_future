package com.diverging.futures

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.diverging.futures.ui.screens.ConceptScreen
import com.diverging.futures.ui.screens.HomeScreen
import com.diverging.futures.ui.screens.IntroScreen
import com.diverging.futures.ui.screens.OnboardingScreen
import com.diverging.futures.ui.screens.CommunityVoiceScreen
import com.diverging.futures.ui.screens.PetitionFormScreen
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

    NavHost(
        navController = navController, 
        startDestination = "home",
        enterTransition = { fadeIn(animationSpec = tween(400)) + slideInHorizontally(animationSpec = tween(400)) { it } },
        exitTransition = { fadeOut(animationSpec = tween(400)) + slideOutHorizontally(animationSpec = tween(400)) { -it } },
        popEnterTransition = { fadeIn(animationSpec = tween(400)) + slideInHorizontally(animationSpec = tween(400)) { -it } },
        popExitTransition = { fadeOut(animationSpec = tween(400)) + slideOutHorizontally(animationSpec = tween(400)) { it } }
    ) {
        composable("home") {
            HomeScreen(onGetStarted = {
                navController.navigate("concept")
            })
        }
        composable("concept") {
            ConceptScreen(
                onContinue = { navController.navigate("onboarding") },
                onSkip = { navController.navigate("intro") }
            )
        }
        composable("onboarding") {
            OnboardingScreen(
                onFinish = { navController.navigate("intro") },
                onSkip = { navController.navigate("intro") }
            )
        }
        composable(
            "splash",
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) {
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
                }
            )
        }
        composable(
            "ar/{siteId}",
            enterTransition = { fadeIn(animationSpec = tween(500)) },
            exitTransition = { fadeOut(animationSpec = tween(500)) }
        ) { backStackEntry ->
            val siteId = backStackEntry.arguments?.getString("siteId")
            com.diverging.futures.ui.screens.ArViewContent(
                siteId = siteId,
                onNavigateToPetition = {
                    navController.navigate("community")
                },
                onBack = { 
                    navController.navigateUp()
                }
            )
        }
        composable("community") {
            CommunityVoiceScreen(
                onBack = { navController.navigateUp() },
                onSignClick = { navController.navigate("petition") }
            )
        }
        composable("petition") {
            PetitionFormScreen(
                onBack = { navController.navigateUp() },
                onSubmit = { 
                    navController.popBackStack("community", false)
                }
            )
        }
    }
}
