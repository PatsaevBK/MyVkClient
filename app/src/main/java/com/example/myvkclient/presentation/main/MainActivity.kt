package com.example.myvkclient.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myvkclient.ui.theme.MyVkClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyVkClientTheme {
                val mainViewModel: MainViewModel = viewModel()
                val authState = mainViewModel.authState.collectAsState()

                val launcher = rememberLauncherForActivityResult(
                    contract = VK.getVKAuthActivityResultContract()
                ) {
                    mainViewModel.performAuthResult(it)
                }

                when (authState.value) {
                    AuthState.Authorized -> {
                        Log.d("MainActivity", "Success auth")
                        VkNewsMainScreen()
                    }
                    AuthState.NotAuthorized -> {
                        LoginScreen {
                            launcher.launch(listOf(VKScope.WALL))
                        }
                    }
                    AuthState.Initial -> {

                    }
                }
            }
        }
    }


}
