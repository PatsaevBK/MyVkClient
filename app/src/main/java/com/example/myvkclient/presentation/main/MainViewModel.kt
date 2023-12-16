package com.example.myvkclient.presentation.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKPreferencesKeyValueStorage
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthenticationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _authState: MutableStateFlow<AuthState> = MutableStateFlow(AuthState.Initial)
    val authState = _authState.asStateFlow()

    init {
        val storage = VKPreferencesKeyValueStorage(application)
        val token = VKAccessToken.restore(storage)
        Log.d("MainViewModel", "Token: ${token?.accessToken}")
        val loggedIn = token != null && token.isValid
        _authState.value = if (loggedIn) AuthState.Authorized else AuthState.NotAuthorized
    }

    fun performAuthResult(result: VKAuthenticationResult) {
        when (result) {
            is VKAuthenticationResult.Failed -> {
                _authState.value = AuthState.NotAuthorized
            }

            is VKAuthenticationResult.Success -> {
                _authState.value = AuthState.Authorized
            }
        }
    }
}