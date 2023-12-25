package com.example.myvkclient.presentation.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myvkclient.data.repository.RepositoryImpl
import com.example.myvkclient.domain.usecases.CheckAuthStateUseCase
import com.example.myvkclient.domain.usecases.GetAuthStateFlowUseCase
import com.vk.api.sdk.auth.VKAuthenticationResult
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = RepositoryImpl(application)
    private val getAuthStateFlowUseCase = GetAuthStateFlowUseCase(repository)
    private val checkAuthStateFlowUseCase = CheckAuthStateUseCase(repository)

    val authState = getAuthStateFlowUseCase()
    fun performAuthResult() {
        viewModelScope.launch { checkAuthStateFlowUseCase() }
    }
}