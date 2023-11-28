package com.example.myvkclient.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.myvkclient.ui.theme.MyVkClientTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<ViewModel> { ViewModel.Factory }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyVkClientTheme {
                // A surface container using the 'background' color from the theme
                MainScreen(viewModel = viewModel)
            }
        }
    }


}
