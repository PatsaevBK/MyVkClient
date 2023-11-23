package com.example.myvkclient.ui.theme

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.myvkclient.ViewModel

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun MainScreen(viewModel: ViewModel) {
    val bottomState = viewModel.bottomStateFlow.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Top app bar")
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Menu, contentDescription = null)
                    }
                })
        },
        bottomBar = {
            BottomPanel(viewModel = viewModel, bottomState)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (bottomState.value) {
                NavigationItem.HOME -> HomeScreen(viewModel)
                NavigationItem.FAVORITE -> TextCounter("Favorite")
                NavigationItem.PROFILE -> TextCounter(text = "Profile")
            }
        }
    }
}

@Composable
private fun TextCounter(text: String) {
    var count by remember {
        mutableStateOf(0)
    }
    Text(text = "$text $count", color = Color.Red, modifier = Modifier.clickable { count++ })
}





