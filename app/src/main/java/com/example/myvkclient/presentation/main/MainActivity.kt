package com.example.myvkclient.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myvkclient.domain.entity.AuthState
import com.example.myvkclient.getApplicationComponent
import com.example.myvkclient.ui.theme.MyVkClientTheme
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKScope

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val component = getApplicationComponent()
            val mainViewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
            val authState = mainViewModel.authState.collectAsState()

            val launcher = rememberLauncherForActivityResult(
                contract = VK.getVKAuthActivityResultContract()
            ) {
                mainViewModel.performAuthResult()
            }
            MyVkClientTheme {
                when (authState.value) {
                    AuthState.Authorized -> {
                        VkNewsMainScreen()
                    }
                    AuthState.NotAuthorized -> {
                        LoginScreen {
                            launcher.launch(listOf(VKScope.WALL, VKScope.FRIENDS))
                        }
                    }
                    AuthState.Initial -> {

                    }
                }
            }
        }
    }


}
