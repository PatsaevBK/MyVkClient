package com.example.myvkclient.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myvkclient.navigation.AppNavGraph
import com.example.myvkclient.navigation.rememberNavigationState

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun MainScreen(viewModel: ViewModel) {
    val navigationState = rememberNavigationState()
    Scaffold(
        topBar = {

        },
        bottomBar = {
            BottomBar(navigationState)
        }
    ) { paddingValues ->
        AppNavGraph(
            navHostController = navigationState.navHostController,
            { HomeScreen(viewModel = viewModel, paddingValues) },
            { TextCounter(text = "Favourite") },
            { TextCounter(text = "Profile") },
            { /*CommentsScreen()*/ }
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






