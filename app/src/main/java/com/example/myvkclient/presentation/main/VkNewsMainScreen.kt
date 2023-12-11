package com.example.myvkclient.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myvkclient.navigation.AppNavGraph
import com.example.myvkclient.navigation.rememberNavigationState
import com.example.myvkclient.presentation.comments.CommentsScreen
import com.example.myvkclient.presentation.news.NewsFeedScreen

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun VkNewsMainScreen() {
    val navigationState = rememberNavigationState()

    Scaffold(
        bottomBar = {
            BottomBar(navigationState)
        }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            newsFeedScreenContent = {
                NewsFeedScreen(paddingValues) {
                    navigationState.navigateToComments(it)
                }
            },
            commentsScreenContent = {feedPost ->
                CommentsScreen(feedPost) {
                    navigationState.navHostController.popBackStack()
                }
            },
            favouriteScreenContent = { TextCounter(text = "Favourite") },
            profileScreenContent = { TextCounter(text = "Profile") }
        )
    }
}

@Composable
private fun TextCounter(text: String) {
    var count by rememberSaveable {
        mutableIntStateOf(0)
    }
    Text(text = "$text $count", color = Color.Red, modifier = Modifier.clickable { count++ })
}






