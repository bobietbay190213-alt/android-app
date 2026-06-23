package com.modernapp.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.modernapp.app.ui.screens.DetailScreen
import com.modernapp.app.ui.screens.HomeScreen
import com.modernapp.app.ui.screens.SettingsScreen
import com.modernapp.app.ui.screens.SplashScreen
import com.modernapp.app.utils.Constants

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Detail : Screen("detail/{itemId}") {
        fun createRoute(itemId: Long) = "detail/$itemId"
    }
    data object Settings : Screen("settings")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(
            route = Screen.Splash.route,
            exitTransition = { fadeOut(tween(Constants.ANIMATION_SHORT)) }
        ) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = Screen.Home.route,
            enterTransition = { fadeIn(tween(Constants.ANIMATION_SHORT)) },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(Constants.ANIMATION_SHORT)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(Constants.ANIMATION_SHORT)
                )
            }
        ) {
            HomeScreen(
                onNavigateToDetail = { itemId ->
                    navController.navigate(Screen.Detail.createRoute(itemId))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.LongType }),
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left,
                    tween(Constants.ANIMATION_SHORT)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right,
                    tween(Constants.ANIMATION_SHORT)
                )
            }
        ) {
            DetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Screen.Settings.route,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Up,
                    tween(Constants.ANIMATION_SHORT)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Down,
                    tween(Constants.ANIMATION_SHORT)
                )
            }
        ) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
