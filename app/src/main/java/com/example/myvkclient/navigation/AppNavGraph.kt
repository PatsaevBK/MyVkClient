package com.example.myvkclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    homeScreenContent: @Composable () -> Unit,
    favouriteScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
    commentScreenContent: @Composable () -> Unit
) {
    NavHost(navController = navHostController, startDestination = Screen.NewsFeed.route) {
        composable(Screen.NewsFeed.route) { homeScreenContent() }
        composable(Screen.Profile.route) { profileScreenContent() }
        composable(Screen.Favourite.route) { favouriteScreenContent() }

        composable(
            Screen.Comments.route
//            arguments = listOf(navArgument("postId") { type = NavType.IntType })
        ) {
//            val postId = it.arguments?.getInt("postId")?:0
            commentScreenContent()
        }
    }
}