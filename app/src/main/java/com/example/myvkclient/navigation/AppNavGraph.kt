package com.example.myvkclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myvkclient.domain.entity.FeedPost

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    newsFeedScreenContent: @Composable () -> Unit,
    favouriteScreenContent: @Composable () -> Unit,
    profileScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (FeedPost) -> Unit
) {
    NavHost(navController = navHostController, startDestination = Screen.Home.route) {
        homeScreenNavGraph(newsFeedScreenContent, commentsScreenContent)
        composable(Screen.Profile.route) { profileScreenContent() }
        composable(Screen.Favourite.route) { favouriteScreenContent() }
    }
}